package logic.server;

import logic.ResourceManager;
import logic.XOException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class XOSever {
    private String serverIP = "localhost";
    private int serverPort = 8000;
    private int maxLength = 1000;
    private DatagramSocket datagramSocket;

    private AccountController accountController;
    private ArrayList<Account> accounts;

    public static void main(String[] args) throws IOException {
        XOSever server = new XOSever();
        server.run();
    }

    /////////////
    /////////////
    /////////////

    private XOSever() throws IOException {
        System.out.println("server is starting...");
        accountController = AccountController.getInstance();
        accounts = new ArrayList<>();
        updateServerPort();
        datagramSocket = new DatagramSocket(serverPort);
//        DatagramSocket datagramSocket = new DatagramSocket(new InetSocketAddress(serverIP , defaultPort));
        System.out.println("server is running\n------------");
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
            System.out.println("Server responded: " + result);
        }
    }

    private String handleCommand(DatagramPacket packet) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet.getData());
        Scanner scanner = new Scanner(byteArrayInputStream);
        String message = scanner.nextLine();
        message=message.replaceAll("\u0000","");

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
                break;
            case "4":
                System.out.println("board states request: " + message);
                break;
            case "5":
                System.out.println("play multi request: " + message);
                break;
            case "6":
                System.out.println("select tile request: " + message);
                break;
            case "7":
                System.out.println("endGame request: " + message);
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
            return "1-0-" + message;
        } catch (XOException e) {
            String error = e.getMessage();
            return "1-1-" + error;
        }
    }

    private String login(String message) {
        String[] info = message.split("-");
        try {
            accountController.login(info[0], info[1]);
            return "2-0-" + message;
        } catch (XOException e) {
            String error = e.getMessage();
            return "2-1-" + error;
        }
    }


}
