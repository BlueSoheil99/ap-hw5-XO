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
        if (port != null && port!= serverPort)  {
            serverPort = port;
            System.out.println("port: "+serverPort);
        }else System.out.println("port: default - "+serverPort);
    }

    private void run() throws IOException {
        while (true) {
            DatagramPacket packet = readPacket();

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet.getData());
            Scanner scanner = new Scanner(byteArrayInputStream);
            String message = scanner.nextLine();
            System.out.println("Server Received: " + message);

            String result = message.toUpperCase();
            writePacket( result, packet.getSocketAddress());
            System.out.println("Server Sent: " + result);

            if (message.equals("exit")) {
                break;
            }
        }
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

    private void register(String username, String pass) throws XOException {
        accountController.register(username, pass);
    }

    private void login(String username, String pass) throws XOException {
        accounts.add(accountController.login(username, pass));
    }


}
