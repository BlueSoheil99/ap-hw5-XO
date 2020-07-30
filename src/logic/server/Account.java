package logic.server;

public class Account {
    private String name;
    private String password;
    private int wins;
    private int losses;

    Account(String userName, String password) {
        this.name = userName;
        this.password = password;
        wins = 0;
        losses = 0;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return wins - losses;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public void increaseWins() {
        wins++;
    }

    public void increaseLosses() {
        losses++;
    }

}
