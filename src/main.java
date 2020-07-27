import gui.GameBoard;
import gui.GameFrame;

public class main {
    public static void main(String[] args) {

        GameFrame frame = new GameFrame();
        frame.initFrame(new GameBoard());
    }
}
