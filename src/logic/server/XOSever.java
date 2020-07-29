package logic.server;

import logic.ResourceManager;
import logic.XOException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.*;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class XOSever {
    private String serverIP = "localhost";
    private int serverPort = 8000;
    private int maxLength = 1000;
    private DatagramSocket datagramSocket;

    private AccountController accountController;
    private HashMap<Account, String> accounts;
    private SecureRandom secureRandom;

    public static void main(String[] args) throws IOException {
        XOSever server = new XOSever();
        server.run();
    }

    /////////////
    /////////////

    private XOSever() throws IOException {
        System.out.println("server is starting...");
        accountController = AccountController.getInstance();
        accounts = new HashMap<>();
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
                System.out.println("endGame request: " + message);
                result = endGame(message);
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
    //////////////////
    private String register(String message) {
        String[] info = message.split("-");
        try {
            accountController.register(info[0], info[1]);
            return "1-0-";
        } catch (XOException e) {
            String error = e.getMessage();
            return "1-1-" + error;
        }
    }

    private String login(String message) {
        String[] info = message.split("-");
        try {
            Account client = accountController.login(info[0], info[1]);
            String token = addNewClient(client);
            return "2-0-" + info[0] + "-" + token;
        } catch (XOException e) {
            String error = e.getMessage();
            return "2-1-" + error;
        }
    }

    private String addNewClient(Account account) {
        int randomInt = secureRandom.nextInt(100000000);
        String token = Integer.toString(randomInt);
        accounts.put(account, token);
        return token;
    }

    private String getAccountStates(String message) {
        try {
            String[] info = message.split("-");
            Account account = getAccount(info[0], info[1]);
            return "3-0-" + account.getWins() + "-" + account.getLosses() + "-" + account.getScore();
        } catch (XOException e) {
            return "3-1-" + e.getMessage();

        }
    }

    private Account getAccount(String userName, String token) throws XOException {
        for (Account account : accounts.keySet()) {
            if (account.getName().equals(userName) && accounts.get(account).equals(token)) {
                return account;
            }
        }
        throw new XOException("account is offline");
    }

    private String getBoardStates(String message) {
        return message;
    }

    private String requestPlay(String message) {
        return message;
    }

    private String selectTile(String message) {
        return message;
    }

    private String endGame(String message) {
        return message;
    }
}
