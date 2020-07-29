package logic.client;

import gui.*;
import logic.BoardListener;
import logic.ResourceManager;
import logic.XOException;
import logic.server.Account;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class XOClient {
    private String serverIP = "localhost";
    private int serverPort = 8000;
    private int clientPort = 9000; //todo is it necessary?
    private int maxLength = 1000;
    private SocketAddress serverAddress;
    private DatagramSocket datagramSocket;

    private Account player;
    private String playerSign, opponentSign;
    private String opponentName;
    private String[] board;

    private GameFrame frame;
    private MenuPanel menu;


    public static void main(String[] args) throws IOException {
        XOClient client = new XOClient();
        client.run();
    }

    private XOClient() throws IOException {
        System.out.println("client is starting...");
        updateServerPort();
        serverAddress = new InetSocketAddress(serverIP, serverPort);
        datagramSocket = new DatagramSocket(clientPort);

        frame = new GameFrame();
        frame.initFrame(new LoginAndRegisterPanel(this));
        System.out.println("client is started\n------------");
    }

    private void updateServerPort() {
        System.out.println("checking client port configuration...");
        Integer port = ResourceManager.getInstance().getServerPort();
        if (port != null && port != serverPort) {
            serverPort = port;
            System.out.println("serverPort: " + serverPort);
        } else System.out.println("serverPort: default - " + serverPort);
    }

    private void run() throws IOException {
        while (true) {
            //lines below execute after writePacket method is called
            DatagramPacket packet = readPacket();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet.getData());
            Scanner socketScanner = new Scanner(byteArrayInputStream);
            String response = socketScanner.nextLine();
            handleResponse(response);
        }
    }

    private void writePacket(String[] standardMessage) {
        String message = standardMessage[0];
        for (int i = 1; i < standardMessage.length; i++) {
            message = message + "-" + standardMessage[i];
        }
        System.out.println(message.substring(2));

        byte[] data = message.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress);
        try { // todo or throw in method signature
            datagramSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private DatagramPacket readPacket() throws IOException {
        DatagramPacket datagramPacket = new DatagramPacket(new byte[maxLength], maxLength);
        datagramSocket.receive(datagramPacket);
        return datagramPacket;
    }

    private void handleResponse(String response) {
        String responseCode = response.substring(0, 1);
        response = response.substring(2);
        switch (responseCode) {
            case "1":
                register(response);
                break;
            case "2":
                System.out.println("login response: " + response);
                break;
            case "3":
                System.out.println("player states response: " + response);
                break;
            case "4":
                System.out.println("board states response: " + response);
                break;
            case "5":
                System.out.println("play multi response: " + response);
                break;
            case "6":
                System.out.println("select tile response: " + response);
                break;
            case "7":
                System.out.println("endGame response: " + response);
                break;
        }
    }

    //////////////////////
    //////////////////////
    //////////////////////

    public void tryToRegister(String userName, String password) throws XOException {
        System.out.print("client registering... ");
        writePacket(new String[]{"1", userName, password});
//        register();
    }

    private void register(String response) {
        System.out.println("register response: " + response);

    }

    public void tryToLogin(String userName, String password) throws XOException {
        System.out.print("client logging in... ");
        writePacket(new String[]{"2", userName, password});
        runMenu();
    }


    public void runMenu() {
        System.out.println("menu ran");
        menu = new MenuPanel(this);
        frame.initFrame(menu);
    }

    private void stopMenu() {
        menu.stopUpdating();
        menu = null;
    }

    public String[] getStates() {
        String[] stats = new String[]{"Guest", "0", "0", "0"};
        if (player != null) {
            stats[0] = player.getName();
            stats[1] = Integer.toString(player.getWins());
            stats[2] = Integer.toString(player.getLosses());
            stats[3] = Integer.toString(player.getScore());
        }
        return stats;
    }

    public String[][] getBoardUpdates() {
        //todo
        return new String[100][3];
    }

    public void playMulti() {
        System.out.println("initializing multiPlayer");

        //todo get signs, first turn and opponent name from server
        playerSign = "X";
        opponentSign = "O";
        opponentName = "test";
        boolean isPlayerTurn = true;

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
