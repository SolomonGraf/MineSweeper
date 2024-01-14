import java.awt.*;

public class MinesweeperSpot {
    int row;
    int col;
    int size;
    int x;
    int y;
    MinesweeperModel model;

    public MinesweeperSpot(int row, int col, int x, int y,int size, MinesweeperModel model) {
        this.row = row;
        this.col = col;
        this.x = x;
        this.y = y;
        this.size = size;
        this.model = model;
    }

    // Drawing

    public void draw(Graphics gc) {
        gc.setColor(Color.BLACK);
        gc.drawRect(x,y,size,size);
        if (model.isShown(row, col)) {
            gc.setColor(Constants.SHOW_COLOR);
        } else {
            gc.setColor(Constants.HIDDEN_COLOR);
        }
        gc.fillRect(x,y,size,size);
        gc.translate(x,y);
        if (model.isShown(row, col)) {
            if (model.isMine(row,col)) {
                gc.setColor(Color.RED);
                gc.fillOval(0,0,size,size);
            } else {
                String neighbors = Integer.toString(model.countNeighborMines(row, col));
                Font font = new Font("Arial", Font.PLAIN, 30);
                gc.setFont(font);
                int width = gc.getFontMetrics().stringWidth(neighbors);
                int height = (int) font.getLineMetrics(neighbors, gc.getFontMetrics().getFontRenderContext()).getAscent();
                gc.setColor(Color.BLACK);
                gc.drawString(neighbors, this.size / 2 - width / 2, this.size/2 + height/2);
            }
        }
        gc.translate(-x,-y);
    }

    // Event Handling

    public boolean contains(int x, int y) {
        return (x >= this.x && x < this.x + size) && (y >= this.y && y < this.y + size);
    }

    public MinesweeperModel.Status handle() {
        if (!model.isShown(row, col)) {
            return model.uncoverSpot(row, col);
        } else {
            return MinesweeperModel.Status.GAME_CONTINUE;
        }
    }
}
