package logic.server;

import logic.ResourceManager;
import logic.XOException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.*;
import java.security.SecureRandom;
import java.util.*;

public class XOSever {
    private String serverIP = "localhost";
    private int serverPort = 8000;
    private int maxLength = 1000;
    private DatagramSocket datagramSocket;

    private AccountController accountController;
    private HashMap<Account, String> onlineAccounts;
    private SecureRandom secureRandom;
    private Account player1, player2;

    public static void main(String[] args) throws IOException {
        XOSever server = new XOSever();
        server.run();
    }

    /////////////
    /////////////

    private XOSever() throws IOException {
        System.out.println("server is starting...");
        accountController = AccountController.getInstance();
        onlineAccounts = new HashMap<>();
        secureRandom = new SecureRandom();
        updateServerPort();
        datagramSocket = new DatagramSocket(serverPort);
//        DatagramSocket datagramSocket = new DatagramSocket(new InetSocketAddress(serverIP , defaultPort));
        System.out.println("server is running\n------------\n");
    }

    private void updateServerPort() {
        System.out.println("checking server port configuration...");
        Integer port = ResourceManager.getInstance().getServerPort();
        if (port != null && port != serverPort) {
            serverPort = port;
            System.out.println("port: " + serverPort);
        } else System.out.println("port: default - " + serverPort);
    }

    private void run() throws IOException {
        while (true) {
            DatagramPacket packet = readPacket();
            String result = handleCommand(packet);
            writePacket(result, packet.getSocketAddress()); //todo send to all
            System.out.println("Server responded: " + result + "\n + + + + +");
        }
    }

    private String handleCommand(DatagramPacket packet) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet.getData());
        Scanner scanner = new Scanner(byteArrayInputStream);
        String message = scanner.nextLine();
        message = message.replaceAll("\u0000", "");
        String result = "";
        String operationCode = message.substring(0, 1);
        message = message.substring(2);

        switch (operationCode) {
            case "1":
                System.out.println("register request: " + message);
                result = register(message);
                break;
            case "2":
                System.out.println("login request: " + message);
                result = login(message);
                break;
            case "3":
                System.out.println("player states request: " + message);
                result = getAccountStates(message);
                break;
            case "4":
                System.out.println("board states request: " + message);
                result = getBoardStates(message);
                break;
            case "5":
                System.out.println("play multi request: " + message);
                result = requestPlay(message);
                break;
            case "6":
                System.out.println("select tile request: " + message);
                result = selectTile(message);
                break;
            case "7":
                System.out.println("endMatch request: " + message);
                result = endMatch(message);
                break;
            case "8":
                System.out.println("quitGame request: " + message);
                result = quitGame(message);
                break;
        }
        return result;
    }

    private DatagramPacket readPacket() throws IOException {
        DatagramPacket datagramPacket = new DatagramPacket(new byte[maxLength], maxLength);
        datagramSocket.receive(datagramPacket);
        return datagramPacket;
    }

    private void writePacket(String message, SocketAddress socketAddress) throws IOException {
        byte[] data = message.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, socketAddress);
        datagramSocket.send(packet);
    }

    //////////////////
    //////////////////

    private String register(String message) {
        String[] info = message.split("_");
        try {
            accountController.register(info[0], info[1]);
            return "1_0_";
        } catch (XOException e) {
            String error = e.getMessage();
            return "1_1_" + error;
        }
    }

    private String login(String message) {
        String[] info = message.split("_");
        try {
            Account client = accountController.login(info[0], info[1]);
            if (accountIsOnline(info[0])) throw new XOException("this user is already online");
            String token = addNewClient(client);
            return "2_0_" + info[0] + "_" + token;
        } catch (XOException e) {
            String error = e.getMessage();
            return "2_1_" + error;
        }
    }

    private String getAccountStates(String message) {
        try {
            String[] info = message.split("_");
            Account account = getAccount(info[0], info[1]);
            return "3_0_" + account.getWins() + "_" + account.getLosses() + "_" + account.getScore();
        } catch (XOException e) {
            return "3_1_" + e.getMessage();
        }
    }

    private String getBoardStates(String message) {
        try {
            String[] info = message.split("_");
            getAccount(info[0], info[1]);
            ArrayList<Account> allAccounts = accountController.getSortedAccounts();
            String[][] stats = new String[allAccounts.size()][3];
            Account account;
            for (int i = 0; i < allAccounts.size(); i++) {
                String status;
                account = allAccounts.get(i);
                if (accountIsOnline(account.getName())) status = "ON";
                else status = "OFF";
                stats[i] = new String[]{account.getName(), status, Integer.toString(account.getScore())};
            }
            return "4_0_" + getString(stats);
        } catch (XOException e) {
            return "4_1_" + e.getMessage();
        }
    }

    private String requestPlay(String message) {
        try {
            String[] info = message.split("_");
            Account account = getAccount(info[0], info[1]);
            addToMatch(account);
            addToMatch(accountController.getSortedAccounts().get(accountController.getSortedAccounts().size() - 1));// todo this part is temporary

            if (player2 != null && player1 != null) {
                String[] preparationStuff = drawAndStart();
                notifyWaitingPlayerToStartMatch(account, preparationStuff);
                return "5_0_" + getString(preparationStuff);
            } else throw new XOException("wait for another player to request");

        } catch (XOException e) {
            return "5_1_" + e.getMessage();
        }
    }

    private String selectTile(String message) {
//       todo try {
//            String[] info = message.split("_");
//            Account account = getAccount(info[0], info[1]);
//            String[] selectionDetails = {info[0] , info[2]};
//            notifyWaitingPlayerToPlay(account, selectionDetails);
//            return "6_0_" + getString(selectionDetails);
//        } catch (XOException e) {
//            return "6_1_" + e.getMessage();
//        }
        return message;
    }

    private String endMatch(String message) {
        try {
            String[] info = message.split("_");
            Account account = getAccount(info[0], info[1]);
            if (account.getName().equals(player1.getName())) {
                if (info[2].equals("0")) {
                    accountController.increaseWins(player1);
                    accountController.increaseLosses(player2);
                    notifyWaitingPlayerTheResult(account , false);
                } else if (info[2].equals("1")) {
                    accountController.increaseWins(player2);
                    accountController.increaseLosses(player1);
                    notifyWaitingPlayerTheResult(account , true);
                }
            } else if (account.getName().equals(player2.getName())) {
                if (info[2].equals("0")) {
                    player1.increaseLosses();
                    player2.increaseWins();
                    notifyWaitingPlayerTheResult(account , true);
                } else if (info[2].equals("1")) {
                    player1.increaseLosses();
                    player2.increaseWins();
                    notifyWaitingPlayerTheResult(account , false);
                }
            } else throw new XOException("there is no match anymore"); // for when the 2nd request from players is sent

            player1 = null;
            player2 = null;
            return "7_0_";
        } catch (XOException e) {
            return "7_1_" + e.getMessage();
        }
    }

    private String quitGame(String message) {
        String[] info = message.split("_");
        if (accountIsOnline(info[0], info[1])) {
            for (Account account : onlineAccounts.keySet()) {
                if (account.getName().equals(info[0])) {
                    onlineAccounts.remove(account);
                    break;
                }
            }
            return "8_0_";
        }
        return "8_1_" + "failed to quit";
    }


    ////////////////////
    ////////////////////

    private void addToMatch(Account account) throws XOException {
        if (player2 != null && player1 != null)
            throw new XOException("server can't handle a new match. try again later");
        else if (player1 == null) player1 = account;
        else player2 = account;
    }

    private String[] drawAndStart() {
        String[] signs = {"X", "O"};
        List<String> signsList = Arrays.asList(signs);
        Collections.shuffle(signsList);
        signsList.toArray(signs);

        Account[] players = {player1, player2};
        List<Account> playersList = Arrays.asList(players);
        Collections.shuffle(playersList);
        playersList.toArray(players);

        return new String[]{players[0].getName(), players[1].getName(), signs[0], signs[1]};
    }

    private void notifyWaitingPlayerToStartMatch(Account operatingPlayer, String[] preparationStuff) {
        Account otherPlayer = getOtherPlayer(operatingPlayer);
//         "5_0_" + getString(preparationStuff);
    }

    private void notifyWaitingPlayerToPlay(Account operatingPlayer, String[] selectionDetails) {
        Account otherPlayer = getOtherPlayer(operatingPlayer);
//        "6_0_" + getString(selectionDetails);
    }

    private void notifyWaitingPlayerTheResult(Account operatingPlayer, boolean otherPlayerWon) {
        Account otherPlayer = getOtherPlayer(operatingPlayer);
//        "6_0_" + getString(selectionDetails);
    }

    private Account getOtherPlayer(Account currentPlayer) {
        if (currentPlayer.getName().equals(player1)) return player2;
        return player1;
    }

    private String addNewClient(Account account) {
        int randomInt = secureRandom.nextInt(100000000);
        String token = Integer.toString(randomInt);
        onlineAccounts.put(account, token);
        return token;
    }

    private Account getAccount(String userName, String token) throws XOException {
        if (accountIsOnline(userName, token)) {
            for (Account account : accountController.getSortedAccounts()) {
                if (account.getName().equals(userName)) {
                    return account;
                }
            }
        }
        throw new XOException("account is offline");
    }

    private boolean accountIsOnline(String userName, String token) {
        for (Account account : onlineAccounts.keySet()) {
            if (account.getName().equals(userName) && onlineAccounts.get(account).equals(token)) {
                return true;
            }
        }
        return false;
    }

    private boolean accountIsOnline(String userName) {
        for (Account account : onlineAccounts.keySet()) {
            if (account.getName().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    private String getString(String[][] twoD_Array) {
        String ans = "";
        for (int i = 0; i < twoD_Array.length; i++) {
            for (int j = 0; j < 3; j++) {
                ans = ans + twoD_Array[i][j] + ",";
            }
            ans = ans.substring(0, ans.length() - 1);//to remove extra "," thing at the end
            ans = ans + "_";
        }
        ans = ans.substring(0, ans.length() - 1);//to remove extra "_" thing at the end
        return ans;
    }

    private String getString(String[] oneD_Array) {
        String ans = "";
        for (String s : oneD_Array) {
            ans = ans + s + "_";
        }
        ans = ans.substring(0, ans.length() - 1);//to remove extra "_" thing at the end
        return ans;
    }

}
