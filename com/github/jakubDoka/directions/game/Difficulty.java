package com.github.jakubDoka.directions.game;

/**
 * Difficulty stores some constant data that GamePlay uses to alter 
 * the game Difficulty and score accumulation.
 */
public enum Difficulty {
    EASY(10, 10),
    MEDIUM(20, 6),
    HARD(40, 4);

    
    private int pathLength;
    private int pathSize;
    

    Difficulty(int pathLength, int pathSize) {
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