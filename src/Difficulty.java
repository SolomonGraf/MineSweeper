public enum Difficulty {
    EASY(8,12),
    MEDIUM(10,16),
    HARD(12,20);

    private final int length;
    private final int numMines;

    Difficulty(int length,int numMines) {
        this.length = length;
        this.numMines = numMines;
    }

    /** @return the code associated with this response. */
    public int getLength() {
        return this.length;
    }

    public int getNumMines() {
        return  this.numMines;
    }
}