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
    private String[] stats = new String[4];
    private String userName;
    private String token ;
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
        datagramSocket = new DatagramSocket(clientPort);

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
        String message = standardMessage[0];
        for (int i = 1; i < standardMessage.length; i++) {
            message = message + "-" + standardMessage[i];
        }
        System.out.println(message.substring(2));

        byte[] data = message.getBytes();
        String response = "";
        DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress);

        datagramSocket.send(packet);
        response = receiveTheResponse();
        return response;
    }

    private String receiveTheResponse() throws IOException {
        DatagramPacket packet = readPacket();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet.getData());
        Scanner socketScanner = new Scanner(byteArrayInputStream);
        String response = socketScanner.nextLine();
        response = response.replaceAll("\u0000", "");

        return handleResponse(response);
    }

    private DatagramPacket readPacket() throws IOException {
        DatagramPacket datagramPacket = new DatagramPacket(new byte[maxLength], maxLength);
        datagramSocket.receive(datagramPacket);
        return datagramPacket;
    }

    private String handleResponse(String response) {
//        String responseCode = response.substring(0, 1);
        response = response.substring(2);
        return response;
    }//todo delete this method

    //////////////////////
    //////////////////////
    //////////////////////

    public void register(String userName, String password) throws XOException {
        System.out.print("client registering  ");
        try {
            String response = requestServerAndGetResponse(new String[]{"1", userName, password});
            System.out.println("register response: " + response);
            switch (response.substring(0,1)){
                case "0":
                    break;
                case "1":
                    throw new XOException(response.substring(2));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void login (String userName, String password) throws XOException {
        System.out.print("client logging in  ");
        try {
            String response = requestServerAndGetResponse(new String[]{"2", userName, password});
            System.out.println("login response: " + response);

            switch (response.substring(0,1)){
                case "0":
                    String[] info = response.substring(2).split("-");
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
        String[] stats = new String[]{userName, "0", "0", "0"};
        //todo requestServerAndGetResponse({3,token})
        return stats;
    }

    public String[][] getBoardUpdates() {
        //todo requestServerAndGetResponse({4,token})
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
