package network;

import gui.*;

public class XOClient {
    private Account player;
    private GameFrame frame;
    private MenuPanel menu;


    public static void main(String[] args) {
        XOClient client = new XOClient();
    }

    public XOClient() {
        frame = new GameFrame();
        frame.initFrame(new LoginAndRegisterPanel(this));
    }

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
        stopMenu();
        frame.initFrame(new PlayPanel(this,"test" , "X","O",true));

    }

}
