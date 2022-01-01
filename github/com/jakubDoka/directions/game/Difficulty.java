package github.com.jakubDoka.directions.game;

public enum Difficulty {
    EASY(10, 10),
    MEDIUM(40, 6),
    HARD(100, 4);

    private static int counter;
    
    private int index;
    private int pathLength;
    private int pathSize;
    
    private static int next() {
        return counter++;
    }

    private Difficulty(int pathLength, int pathSize) {
        this.index = Difficulty.next();
        this.pathLength = pathLength;
        this.pathSize = pathSize;
    }

    public int getIndex() {
        return this.index;
    }

    public int getPathLength() {
        return this.pathLength;
    }

    public int getPathSize() {
        return this.pathSize;
    }
}