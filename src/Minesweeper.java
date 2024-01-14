import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Minesweeper implements Runnable{

    Difficulty difficulty;

    public void run() {
        final JFrame frame = new JFrame("Minesweeper");
        frame.setLocation(0, 0);

        difficulty = Difficulty.MEDIUM;

        final MinesweeperBoard mb = new MinesweeperBoard(difficulty);

        frame.add(mb, BorderLayout.CENTER);
        frame.add(createButtonBar(mb),BorderLayout.NORTH);

        final JFrame instructions = new JFrame("Instructions");
        instructions.setLocation(100, 200);


        instructions.add(new InstructionsPopup());

        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);

        instructions.pack();
        instructions.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        instructions.setVisible(true);
        instructions.setResizable(false);
    }

    private JPanel createButtonBar(MinesweeperBoard mb) {
        JButton saveButton = new JButton("Save Game");
        JButton loadButton = new JButton("Load Game");
        JButton resetButton = new JButton("Reset");

        ButtonGroup difficultyButtons = new ButtonGroup();
        JRadioButton easyButton = new JRadioButton("Easy",false);
        JRadioButton mediumButton = new JRadioButton("Medium",true);
        JRadioButton hardButton = new JRadioButton("Hard",false);

        difficultyButtons.add(easyButton);
        difficultyButtons.add(mediumButton);
        difficultyButtons.add(hardButton);

        JPanel toolbar = new JPanel(new FlowLayout());
        toolbar.add(saveButton);
        toolbar.add(loadButton);
        toolbar.add(resetButton);
        toolbar.add(easyButton);
        toolbar.add(mediumButton);
        toolbar.add(hardButton);

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mb.resizeArrays();
                mb.resetSpots();
                mb.resetBoard();
                mb.repaint();
            }
        });

        easyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mb.setDifficulty(Difficulty.EASY);
            }
        });

        mediumButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mb.setDifficulty(Difficulty.MEDIUM);
            }
        });

        hardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mb.setDifficulty(Difficulty.HARD);
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                difficulty = mb.loadFromFile();
                mb.repaint();
                switch(difficulty) {
                    case EASY -> easyButton.setSelected(true);
                    case MEDIUM -> mediumButton.setSelected(true);
                    case HARD -> hardButton.setSelected(true);
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mb.saveToFile();
                mb.repaint();
            }
        });

        return toolbar;
    }
}
