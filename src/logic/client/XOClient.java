package logic.client;

import gui.*;
import logic.BoardListener;
import logic.XOException;
import logic.server.Account;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class XOClient {
    private String serverIP = "localhost";
    private int serverPort = 8000;
    private int clientPort = 8001; //todo is it necessary?
    private int maxLength = 1000;
    private SocketAddress serverAddress;
    private DatagramSocket datagramSocket;
    private Scanner scanner;

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
        frame = new GameFrame();
        frame.initFrame(new LoginAndRegisterPanel(this));
        //todo read config for server port
        serverAddress = new InetSocketAddress(serverIP, serverPort);
        datagramSocket = new DatagramSocket(clientPort);
        scanner = new Scanner(System.in); //todo system in is kazaie
    }

    private void run() throws IOException {
        while (true) {
            String message = scanner.nextLine();
            writePacket(datagramSocket, message, serverAddress);
            System.out.println("Client Sent: " + message);

            DatagramPacket packet = readPacket(datagramSocket);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet.getData());
            Scanner socketScanner = new Scanner(byteArrayInputStream);
            String response = socketScanner.nextLine();

            System.out.println("Client Received: " + response);

            if (message.equals("exit")) {
                break;
            }
        }
    }

    private void writePacket(DatagramSocket datagramSocket, String message, SocketAddress socketAddress) throws IOException {
        byte[] data = message.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, socketAddress);
        datagramSocket.send(packet);
    }

    private DatagramPacket readPacket(DatagramSocket datagramSocket) throws IOException {
        DatagramPacket datagramPacket = new DatagramPacket(new byte[maxLength], maxLength);
        datagramSocket.receive(datagramPacket);
        return datagramPacket;
    }

    //////////////////////
    //////////////////////
    //////////////////////

    public void register(String userName, String password) throws XOException {
        //todo
        System.out.println("registered");
    }

    public void login(String userName, String password) throws XOException {
        //todo
        System.out.println("logged in");
//        player = get player from server;
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
