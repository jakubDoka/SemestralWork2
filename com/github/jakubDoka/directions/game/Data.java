package com.github.jakubDoka.directions.game;

import java.nio.file.Paths;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

/**
 * Data stores game data that has to be preserved 
 * between application executions. It stores all data 
 * in simple text format inside the users app-data directory.
 */
public class Data {
    private static final String FILE = Paths.get(
        System.getenv("LOCALAPPDATA"), "directions", "data.txt").toString();

    private int bestScore;
    private Difficulty difficulty;
    
    private int backgroundColor;
    private int pathColor;
    private int playerColor;
    
    /**
     * Constructs data with default settings.
     */
    public Data() {
        this.bestScore = 0;
        this.difficulty = Difficulty.EASY;
        
        this.backgroundColor = 0xFFCCCCCC;
        this.pathColor = 0xFF000000;
        this.playerColor = 0xFF0000FF;
    }

    public int getBestScore() {
        return this.bestScore;
    }

    /**
     * Sets the best score, only if bestScore is higher then current value.
     * Also saves.
     * @param bestScore - used if higher then current bestScore
     */
    public void setBestScore(int bestScore) {
        if (bestScore > this.bestScore) {
            this.bestScore = bestScore;
            this.save();
        }
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public int getBackgroundColor() {
        return this.backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getPathColor() {
        return this.pathColor;
    }

    public void setPathColor(int pathColor) {
        this.pathColor = pathColor;
    }

    public int getPlayerColor() {
        return this.playerColor;
    }

    public void setPlayerColor(int playerColor) {
        this.playerColor = playerColor;
    }

    /**
     * Loads saved data and if non is found it saves the current one.
     */
    public void load(Directions directions) {
        try {
            Scanner scanner = new Scanner(new File(FILE));

            this.bestScore = scanner.nextInt();

            this.difficulty = Difficulty.values()[scanner.nextInt()];
            directions.setDifficulty(this.difficulty);
            
            directions.setBackgroundColor(scanner.nextInt());
            directions.setPathColor(scanner.nextInt());
            directions.setPlayerColor(scanner.nextInt());

            scanner.close();
        } catch (FileNotFoundException e) {
            this.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves current data to file in AppData directory.
     */
    public void save() {
        try {
            File file = new File(FILE);
            
            // create if does not exists
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            // write the data
            FileWriter writer = new FileWriter(file);

            writer.write(
                this.bestScore + " " + 
                this.difficulty.ordinal() + " " + 
                this.backgroundColor + " " + 
                this.pathColor + " " + 
                this.playerColor
            );

            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
