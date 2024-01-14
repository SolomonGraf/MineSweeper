import javax.swing.*;
import java.awt.*;

public class InstructionsPopup extends JComponent {

    @Override
    public void paintComponent(Graphics gc) {
        String welcome = "Welcome to Minesweeper!";
        String[] instructions = {"Change difficulty with Easy/Medium/Hard buttons",
                "Reset game at selected difficulty with Reset button",
                "Save an ongoing game with Save button",
                "Load a saved game with Load button",
                "Click on every square without a mine on it to win!"
        };
        Font font = new Font("Arial",Font.BOLD,30);
        gc.setFont(font);
        gc.drawString(welcome,Constants.POPUP_LENGTH/2 - gc.getFontMetrics(font).stringWidth(welcome)/2,50);
        font = new Font("Arial",Font.BOLD,16);
        gc.setFont(font);
        int y = 150;
        for (String line : instructions) {
            gc.drawString(line,
                    Constants.POPUP_LENGTH / 2 - gc.getFontMetrics(font).stringWidth(line) / 2, y);
            y += 30;
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Constants.POPUP_LENGTH,Constants.POPUP_LENGTH);
    }

}
