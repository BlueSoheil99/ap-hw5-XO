package network;

import gui.GameBoard;
import gui.GameFrame;
import gui.LoginAndRegisterPanel;
import gui.MenuPanel;

public class XOClient {
    private Account player;
    private GameFrame frame;


    public static void main(String[] args) {
        XOClient client = new XOClient();
    }

    public XOClient() {
        frame = new GameFrame();
        frame.initFrame(new LoginAndRegisterPanel(this));
    }

    public void register(String userName, String password) throws XOException {

        System.out.println("registered");

    }

    public void login(String userName, String password) throws XOException {
        System.out.println("logged in");
//        player = get player from server;
        runMenu();
    }

    private void runMenu() { //todo combine with login method
        System.out.println("menu ran");
        frame.initFrame(new MenuPanel(this));
    }

    public String getPlayerName() {
        if (player != null) return player.getName();
        return "Guest";
    }

    public void playSolo() {
        frame.initFrame(new GameBoard());
    }

    public void playMulti() {
        System.out.println("initializing multiPlayer");
    }

}
