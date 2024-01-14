import java.util.*;
import java.util.stream.*;
import java.io.*;

public class MinesweeperModel {
    private boolean[][] mineArray;
    private boolean[][] showArray;

    public enum Status {
        WIN_GAME,
        LOSE_GAME,
        GAME_CONTINUE;
    }

    int numMines;

    public MinesweeperModel(int numMines, int boardSize) {
        this.mineArray = new boolean[boardSize][boardSize];
        this.numMines = numMines;
        this.showArray = new boolean[boardSize][boardSize];
    }

    public void resizeArrays(int size) {
        this.mineArray = new boolean[size][size];
        this.showArray = new boolean[size][size];
    }

    public void resetModel() {
        for (int row = 0; row < mineArray.length; row++) {
            for (int column = 0; column < mineArray.length; column++) {
                mineArray[row][column] = false;
                showArray[row][column] = false;
            }
        }
    }

    public void fillMineArray(int numMines) {
        int boardSize = (int) Math.pow(mineArray.length,2.0);
        IntStream randomStream = new Random().ints(0, boardSize).distinct();
        int[] minePositions = randomStream.limit(numMines).toArray();
        for (int pos : minePositions) {
            int xPos = pos / mineArray.length;
            int yPos = pos % mineArray.length;
            mineArray[xPos][yPos] = true;
        }
    }

    public Status uncoverSpot(int x, int y) {
        if (arrayAllFalse(showArray)) {
            while (countNeighborMines(x,y) > 0) {
                resetModel();
                fillMineArray(numMines);
            }
        }
        showArray[x][y] = true;
        if (mineArray[x][y]) {
            return Status.LOSE_GAME;
        }
        showArray[x][y] = true;
        if (countNeighborMines(x,y) == 0) {
            for (int r = Math.max(0,x-1); r <= Math.min(mineArray.length-1,x+1); r++) {
                for (int c = Math.max(0,y-1); c <= Math.min(mineArray.length-1,y+1); c++) {
                    if (!showArray[r][c]){
                        uncoverSpot(r, c);
                    }
                }
            }
        }
        if (countTrues(showArray) == showArray.length * showArray.length - numMines) {
            return Status.WIN_GAME;
        } else {
            return Status.GAME_CONTINUE;
        }

    }

    public int countNeighborMines(int x, int y) {
        int neighbors = 0;
        for (int r = Math.max(0,x-1); r <= Math.min(mineArray.length-1,x+1); r++) {
            for (int c = Math.max(0,y-1); c <= Math.min(mineArray.length-1,y+1); c++) {
                if (mineArray[r][c]) {
                    neighbors++;
                }
            }
        }
        return neighbors;
    }

    private int countTrues (boolean[][] array) {
        int trueCount = 0;
        for (boolean[] arr : array) {
            for (boolean bool : arr) {
                if (bool) {
                    trueCount++;
                }
            }
        }
        return trueCount;
    }

    private boolean arrayAllFalse (boolean[][] array) {
        return countTrues(array) == 0;
    }

    public boolean isShown(int x, int y) {
        return showArray[x][y];
    }

    public boolean isMine(int x, int y) {
        return mineArray[x][y];
    }

    public void saveToFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("./src/save.txt",false));
            bw.write(Integer.toString(mineArray.length));
            bw.write("\n");
            writeArrayToFile(bw, mineArray);
            writeArrayToFile(bw, showArray);
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException("Exception thrown when writing to file");
        }
    }

    private void writeArrayToFile(BufferedWriter bw, boolean[][] mineArray) throws IOException {
        for (boolean[] row : mineArray) {
            StringBuilder rowText = new StringBuilder();
            for (boolean show : row) {
                rowText.append(show ? 1 : 0);
            }
            bw.write(String.valueOf(rowText));
            bw.write("\n");
        }
    }

    public int loadFromFile() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("./src/save.txt"));
            // First line is number of rows
            int numRows = Integer.parseInt(br.readLine());

            resizeArrays(numRows);

            for (int row = 0; row < numRows; row++) {
                String rowText = br.readLine();
                for (int column = 0; column < numRows; column++) {
                    mineArray[row][column] = (rowText.charAt(column)) != '0';
                }
            }
            for (int row = 0; row < numRows; row++) {
                String rowText = br.readLine();
                for (int column = 0; column < numRows; column++) {
                    showArray[row][column] = (rowText.charAt(column)) != '0';
                }
            }
            br.close();
            this.numMines = countTrues(mineArray);
            return numRows;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("No such file 'save.txt'");
        } catch (IOException e) {
            throw new RuntimeException("Exception thrown while reading file");
        }
    }
}