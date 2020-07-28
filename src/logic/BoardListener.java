package logic;

import java.util.EventListener;

public interface BoardListener extends EventListener {

    void selectPlayer(int tileNumber);
    void selectOpponent(int tileNumber);
}
