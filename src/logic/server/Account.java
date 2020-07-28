package logic.server;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public int getScore() {
        return wins - losses;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

}
