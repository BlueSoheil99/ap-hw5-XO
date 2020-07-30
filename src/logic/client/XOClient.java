package logic.client;

import gui.*;
import logic.BoardListener;
import logic.ResourceManager;
import logic.XOException;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class XOClient {
    private String serverIP = "localhost";
    private int serverPort = 8000;
    //    private int clientPort = 9000;
    private int maxLength = 1000;
    private SocketAddress serverAddress;
    private DatagramSocket datagramSocket;

    private String userName;
    private String token;
    private boolean isPlayerTurn;
    private String playerSign, opponentSign;
    private String opponentName;
    private String[] board;

    private GameFrame frame;
    private MenuPanel menu;


    public static void main(String[] args) throws IOException {
        XOClient client = new XOClient();
    }

    private XOClient() throws IOException {
        System.out.println("client is starting...");
        updateServerPort();
        serverAddress = new InetSocketAddress(serverIP, serverPort);
//        datagramSocket = new DatagramSocket(clientPort);
        datagramSocket = new DatagramSocket();

        frame = new GameFrame();
        frame.initFrame(new LoginAndRegisterPanel(this));
        System.out.println("client is started\n------------\n");
    }

    private void updateServerPort() {
        System.out.println("checking client port configuration...");
        Integer port = ResourceManager.getInstance().getServerPort();
        if (port != null && port != serverPort) {
            serverPort = port;
            System.out.println("serverPort: " + serverPort);
        } else System.out.println("serverPort: default - " + serverPort);
    }

    private String requestServerAndGetResponse(String[] standardMessage) throws IOException {
        StringBuilder message = new StringBuilder(standardMessage[0]);
        for (int i = 1; i < standardMessage.length; i++) {
            message.append("_").append(standardMessage[i]);
        }
        System.out.println(message.substring(2));

        byte[] data = message.toString().getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress);

        datagramSocket.send(packet);
        return receiveTheResponse();

    }

    private String receiveTheResponse() throws IOException {
        DatagramPacket packet = readPacket();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet.getData());
        Scanner socketScanner = new Scanner(byteArrayInputStream);
        String response = socketScanner.nextLine();
        response = response.replaceAll("\u0000", "");

        return response.substring(2);
    }

    private DatagramPacket readPacket() throws IOException {
        DatagramPacket datagramPacket = new DatagramPacket(new byte[maxLength], maxLength);
        datagramSocket.receive(datagramPacket);
        return datagramPacket;
    }

    //////////////////////
    //////////////////////
    //////////////////////

    public void register(String userName, String password) throws XOException {
        System.out.print("client registering  ");
        try {
            String response = requestServerAndGetResponse(new String[]{"1", userName, password});
            System.out.println("register response: " + response);
            switch (response.substring(0, 1)) {
                case "0":
                    break;
                case "1":
                    throw new XOException(response.substring(2));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void login(String userName, String password) throws XOException {
        System.out.print("client logging in  ");
        try {
            String response = requestServerAndGetResponse(new String[]{"2", userName, password});
            System.out.println("login response: " + response);

            switch (response.substring(0, 1)) {
                case "0":
                    String[] info = response.substring(2).split("_");
                    this.userName = info[0];
                    token = info[1];
                    runMenu();
                    break;
                case "1":
                    throw new XOException(response.substring(2));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void quitGame() {
        System.out.print("quiting the game   ");
        try {
            String response = requestServerAndGetResponse(new String[]{"8", userName, token});
            System.out.println("quit response: " + response);

            switch (response.substring(0, 1)) {
                case "0":
                    stopMenu();
                    System.exit(0);
                    break;
                case "1":
                    throw new XOException(response.substring(2));
            }
        } catch (IOException | XOException e) {
            e.printStackTrace();
        }
    }

    public void endMatch(Boolean hasPlayerWon) {
        String endCode;
        if (hasPlayerWon == null) endCode = "2";
        else if (hasPlayerWon) endCode = "0";
        else endCode = "1";
        System.out.print("match ended:  ");
        try {
            String response = requestServerAndGetResponse(new String[]{"7", userName, token, endCode});
            System.out.println("endMatch response: " + response);
            switch (response.substring(0, 1)) {
                case "0":
                    break;
                case "1":
                    throw new XOException(response.substring(2));
            }
        } catch (IOException | XOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void runMenu() {
        System.out.println("menu running");
        menu = new MenuPanel(this);
        frame.initFrame(menu);
    }

    private void stopMenu() {
        menu.stopUpdating();
        System.out.println("menu stopped running");
        menu = null;
    }

    public String[] getStates() {
        String[] stats = new String[]{userName, "0", "0", "0"};
        System.out.print("requesting for account states:  ");
        try {
            String response = requestServerAndGetResponse(new String[]{"3", userName, token});
            System.out.println("stateRequest response: " + response);
            switch (response.substring(0, 1)) {
                case "0":
                    String[] info = response.substring(2).split("_");
                    stats[1] = info[0];
                    stats[2] = info[1];
                    stats[3] = info[2];
                    break;
                case "1":
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stats;
    }

    public String[][] getBoardUpdates() {
        System.out.print("requesting for board states:  ");
        try {
            String response = requestServerAndGetResponse(new String[]{"4", userName, token});
            System.out.println("boardRequest response: " + response);
            switch (response.substring(0, 1)) {
                case "0":
                    String[] info = response.substring(2).split("_");
                    String[][] stats = new String[info.length][3];
                    for (int i = 0; i < info.length; i++) {
                        stats[i] = info[i].split(",");
                    }
                    return stats;
                case "1":
                    throw new XOException(response.substring(2));
            }
        } catch (IOException | XOException e) {
            e.printStackTrace();
        }
        return new String[100][3];
    }

    public void playMulti() {
        System.out.println("initializing multiPlayer");
        try {
            String response = requestServerAndGetResponse(new String[]{"5", userName, token});
            System.out.println("playRequest response: " + response);
            switch (response.substring(0, 1)) {
                case "0":
                    String[] info = response.substring(2).split("_");
                    prepareTheGame(info);
                    runPlay(opponentName, playerSign, opponentSign, isPlayerTurn);
                    break;
                case "1":
                    throw new XOException(response.substring(2));
            }
        } catch (IOException | XOException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }

    private void prepareTheGame(String[] info) {
        if (info[0].equals(userName)) {
            isPlayerTurn = true;
            opponentName = info[1];
            playerSign = info[2];
            opponentSign = info[3];
        } else {
            isPlayerTurn = false;
            opponentName = info[0];
            playerSign = info[3];
            opponentSign = info[2];
        }
    }

    private void runPlay(String opponentName, String playerSign, String opponentSign, boolean isPlayerTurn) {
        stopMenu();
        board = new String[49];
        GameLogic gameLogic = new GameLogic(board, playerSign);
        PlayPanel playPanel = new PlayPanel(this, opponentName, playerSign, opponentSign, isPlayerTurn);
        playPanel.setBoardListener(new BoardListener() {
            @Override
            public void selectPlayer(int tileNumber) {
                System.out.println(tileNumber + " selected");
                board[tileNumber] = playerSign;
                playPanel.changeTurn();
                Integer[] winningTiles = gameLogic.checkForWin(true);
                if (winningTiles != null) playPanel.playerWon(winningTiles);
                else if (gameLogic.checkForTie()) playPanel.tie();
            }

            @Override
            public void selectOpponent(int tileNumber) {
                System.out.println(tileNumber + " selected");
                board[tileNumber] = opponentSign;
                playPanel.changeTurn();
                Integer[] winningTiles = gameLogic.checkForWin(false);
                if (winningTiles != null) playPanel.playerLost(winningTiles);
                else if (gameLogic.checkForTie()) playPanel.tie();

            }
        });
        frame.initFrame(playPanel);
    }


}
