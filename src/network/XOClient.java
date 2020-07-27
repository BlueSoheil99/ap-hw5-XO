package network;

import gui.GameFrame;
import gui.LoginAndRegisterPanel;
import gui.MenuPanel;

public class XOClient {
    private GameFrame frame;

    public static void main(String[] args) {
        XOClient client = new XOClient();
    }

    public XOClient() {
        frame = new GameFrame();
        frame.initFrame(new LoginAndRegisterPanel(this));
    }

    private void runMenu() {
        System.out.println("menu ran");
//        frame.initFrame(new MenuPanel(this));
    }

    public void register(String userName, String password) throws XOException {

        System.out.println("registered");

    }

    public void login(String userName, String password) throws XOException{
        System.out.println("logged in");
        runMenu();
    }

}
