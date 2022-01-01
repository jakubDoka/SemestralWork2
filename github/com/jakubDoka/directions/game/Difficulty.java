package github.com.jakubDoka.directions.game;

/**
 * Difficulty stores some constant data that GamePlay uses to alter 
 * the game Difficulty and score accumulation.
 */
public enum Difficulty {
    EASY(10, 10),
    MEDIUM(40, 6),
    HARD(100, 4);

    
    private int pathLength;
    private int pathSize;
    

    private Difficulty(int pathLength, int pathSize) {
        this.pathLength = pathLength;
        this.pathSize = pathSize;
    }

    public int getPathLength() {
        return this.pathLength;
    }

    public int getPathSize() {
        return this.pathSize;
    }
}