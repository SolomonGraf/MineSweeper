import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MinesweeperBoard extends JComponent {

    private MinesweeperSpot[][] board;
    private final MinesweeperModel model;
    private MinesweeperModel.Status status;
    private Difficulty difficulty;
    public MinesweeperBoard(Difficulty difficulty) {
        setFocusable(true);

        this.status = MinesweeperModel.Status.GAME_CONTINUE;
        this.difficulty = difficulty;
        this.model = new MinesweeperModel(difficulty.getNumMines(),difficulty.getLength());

        this.resetBoard();
        createSpots();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e);
            }
        });

        repaint();
    }

    // Drawing

    @Override
    public void paintComponent(Graphics gc) {
        for (MinesweeperSpot[] minesweeperSpots : board) {
            for (int column = 0; column < board.length; column++) {
                minesweeperSpots[column].draw(gc);
            }
        }
        if (status != MinesweeperModel.Status.GAME_CONTINUE) {
            gc.setColor(Constants.ENDGAME_PANEL_COLOR);
            gc.fillRect(100,100,400,400);
            gc.setColor(Constants.ENDGAME_CONTENT_COLOR);
            gc.drawRect(100,100,400,400);
            Font font = new Font("Arial", Font.PLAIN, 60);
            gc.setFont(font);
            if (status == MinesweeperModel.Status.WIN_GAME) {
                gc.drawString("You Win!",200,300);
            } else {
                gc.drawString("You Lose :(", 200,300);
            }
        }
    }
    
    // Handling Events

    // Sends down mouseClick event to appropriate MinesweeperSpot
    public void handleClick(MouseEvent e) {
        if (status == MinesweeperModel.Status.GAME_CONTINUE){
            for (MinesweeperSpot[] minesweeperSpots : board) {
                for (int column = 0; column < board.length; column++) {
                    if (minesweeperSpots[column].contains(e.getX(), e.getY())) {
                        status = minesweeperSpots[column].handle();
                    }
                }
            }
            repaint();
        }
    }

    // Resets board on RESET click
    public void resetBoard() {
        status = MinesweeperModel.Status.GAME_CONTINUE;
        resizeArrays();
        model.resetModel();
        model.fillMineArray(difficulty.getNumMines());
    }

    // Changes difficulty on EASY/MEDIUM/HARD click
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    // Drawing Aux

    // Creates a new MinesweeperSpot given row and column
    private MinesweeperSpot createSpot(int row, int column) {
        int size = Constants.BOARD_LENGTH / board.length;
        return new MinesweeperSpot(row,column,size*row,size*column,
                size,this.model);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Constants.BOARD_LENGTH,Constants.BOARD_LENGTH);
    }

    // Aux

    public void resizeArrays() {
        this.board = new MinesweeperSpot[difficulty.getLength()][difficulty.getLength()];
        createSpots();
        this.model.resizeArrays(difficulty.getLength());
    }

    public void createSpots() {
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board.length; column++) {
                this.board[row][column] = createSpot(row,column);
            }
        }
    }

    public void resetSpots() {
        this.model.resetModel();
        repaint();
    }

    public Difficulty loadFromFile() {
        int numRows = this.model.loadFromFile();
        this.difficulty = switch(numRows) {
            case 8 -> Difficulty.EASY;
            case 10 -> Difficulty.MEDIUM;
            case 12 -> Difficulty.HARD;
            default -> throw new IllegalStateException("Unexpected value: " + numRows);
        };
        resetBoard();
        this.model.loadFromFile();
        return difficulty;
    }

    public void saveToFile() {
        if (this.status == MinesweeperModel.Status.GAME_CONTINUE){
            this.model.saveToFile();
        }
    }
}
