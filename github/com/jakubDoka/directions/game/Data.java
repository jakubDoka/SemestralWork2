package github.com.jakubDoka.directions.game;

import java.nio.file.Paths;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;


public class Data {
    private static final String FILE = Paths.get(
        System.getenv("LOCALAPPDATA"), "directions", "data.txt").toString();

    int bestScore;
    Difficulty difficulty;
    
    int backgroundColor;
    int pathColor;
    int playerColor;
    
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

    public void save() {
        try {
            File file = new File(FILE);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file);

            writer.write(
                this.bestScore + " " + 
                this.difficulty.getIndex() + " " + 
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
