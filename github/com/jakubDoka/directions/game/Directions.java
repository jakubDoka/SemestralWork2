package github.com.jakubDoka.directions.game;

import java.awt.Color;

import github.com.jakubDoka.directions.ui.Canvas;
import github.com.jakubDoka.directions.ui.Colors;
import github.com.jakubDoka.directions.ui.FrameManager;

/**
 * Directions is a main game object that handles all the game logic.
 * It contains sub-classes for each of the game screens.
 */
public class Directions implements Runnable {
    public static final int SIZE = 4;
    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;
    public static final Color UI_COLOR = new Color(255, 255, 255, 100);
    
    private final MainMenu mainMenu;
    private final GamePlay gamePlay;
    private final Settings settings;
    private final ScoreScreen scoreScreen;
    private final Data data;

    private Canvas canvas;
    private FrameManager frameManager;
    private State state;

    /**
     * Constructs the Directions object but does not run the game loop.
     */
    public Directions() {
        this.mainMenu = new MainMenu();
        this.gamePlay = new GamePlay();
        this.settings = new Settings();
        this.scoreScreen = new ScoreScreen();

        this.frameManager = new FrameManager();
        this.canvas = new Canvas("Directions", WIDTH, HEIGHT);
        
        this.data = new Data();
        this.data.load(this);
        
        this.canvas.addObject(this.mainMenu);
        this.canvas.addObject(this.gamePlay);
        this.canvas.addObject(this.settings);
        this.canvas.addObject(this.scoreScreen);

        this.state = State.MAIN_MENU;
    }

    /**
     * Runs the game loop.
     */
    @Override
    public void run() {
        while (true) {
            switch (this.state) {
                case MAIN_MENU:
                    this.mainMenu.update(this);
                    break;
                case PLAYING:
                    this.gamePlay.update(this);
                    break;
                case SCORE_SCREEN:
                    this.scoreScreen.update(this);
                    break;
                case SETTINGS:
                    this.settings.update(this);
            }

            
            this.frameManager.update();
            this.canvas.update();
        }
    }

    /**
     * Starts the game and lets player make his moves.
     * If this is tutorial mode, no score is increased 
     * and player cant lose.
     * @param isTutorial - if true, game is in tutorial mode
     */
    public void startGame(boolean isTutorial) {
        this.gamePlay.start(
            isTutorial,    
            this.data.getDifficulty(),
            Colors.create(this.data.getPlayerColor()),
            Colors.create(this.data.getPathColor())
        );
        this.mainMenu.setVisible(false);
        this.state = State.PLAYING;
    }

    public void showSettings() {
        this.mainMenu.setVisible(false);
        this.settings.start(this);
        this.state = State.SETTINGS;
    }

    public void showScore(int score) {
        this.data.setBestScore(score);
        this.scoreScreen.start(score, this.data.getBestScore());
        this.state = State.SCORE_SCREEN;
    }

    public void showMainMenu() {
        this.mainMenu.setVisible(true);
        this.state = State.MAIN_MENU;
    }

    /**
     * Restarts the game configuration to defaults.
     */
    public void resetData() {
        Data cleanData = new Data();
        this.setDifficulty(Difficulty.EASY);
        this.setPathColor(cleanData.getPathColor());
        this.setBackgroundColor(cleanData.getBackgroundColor());
        this.setPlayerColor(cleanData.getPlayerColor());
    }

    /**
     * Sets path color and makes sure everything changes properly.
     * @param colorValue
     */
    public void setPathColor(int colorValue) {
        Color color = Colors.create(colorValue);
        this.mainMenu.setPathColor(color);
        this.data.setPathColor(colorValue);
        this.settings.setPathColor(color);
    }

    /**
     * Sets background color and makes sure everything changes properly.
     * @param colorValue
     */
    public void setBackgroundColor(int colorValue) {
        this.canvas.setBackground(Colors.create(colorValue));
        this.data.setBackgroundColor(colorValue);
    }

    /**
     * Sets player color and makes sure everything changes properly.
     * @param colorValue
     */
    public void setPlayerColor(int colorValue) {
        Color color = Colors.create(colorValue);
        this.gamePlay.setColor(color);
        this.data.setPlayerColor(colorValue);
        this.settings.setPlayerColor(color);
    }

    /**
     * Sets difficulty color and makes sure everything changes properly.
     * @param colorValue
     */
    public void setDifficulty(Difficulty difficulty) {
        this.data.setDifficulty(difficulty);
        this.settings.setDifficulty(this);
    }

    /**
     * Represents variants of state game can be in.
     */
    public enum State {
        MAIN_MENU,
        PLAYING,
        SETTINGS,
        SCORE_SCREEN,
    }

    public Canvas getCanvas() {
        return this.canvas;
    }
    
    public int getPlayerColor() {
        return this.data.getPlayerColor();
    }
    
    public int getPathColor() {
        return this.data.getPathColor();
    }
    
    public int getBackgroundColor() {
        return this.data.getBackgroundColor();
    }
    
    public void saveData() {
        this.data.save();
    }

    public Difficulty getDifficulty() {
        return this.data.getDifficulty();
    }
}