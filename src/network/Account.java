package network;

public class Account {
    private String name;
    private String password;
    private int score;
    private boolean isOnline;
    private int wins;
    private int losses;

    Account(String userName, String password) {
        this.name = userName;
        this.password = password;
        score = 0;
        wins = 0;
        losses = 0;
        isOnline = false;
    }

    void saveAccount() {

    }

    void getScore() {

    }
}
