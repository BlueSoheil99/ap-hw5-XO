package network;

import gui.*;

public class XOClient {
    private Account player;
    private String playerSign,opponentSign;
    private String opponentName;
    private String[] board;

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
                Integer[] winningTiles = gameLogic.checkForWin(true);
                if (winningTiles != null) playPanel.playerWon(winningTiles);
            }

            @Override
            public void selectOpponent(int tileNumber) {
                Integer[] winningTiles = gameLogic.checkForWin(false);
                if (winningTiles != null) playPanel.playerLost(winningTiles);
            }
        });
        frame.initFrame(playPanel);
    }


}
