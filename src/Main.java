import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Set the game you want to run here
        Runnable game = new Minesweeper();

        SwingUtilities.invokeLater(game);
    }
}