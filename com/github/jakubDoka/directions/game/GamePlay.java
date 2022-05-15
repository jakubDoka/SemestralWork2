package com.github.jakubDoka.directions.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import com.github.jakubDoka.directions.ui.Button;
import com.github.jakubDoka.directions.ui.Canvas;
import com.github.jakubDoka.directions.ui.FrameManager;
import com.github.jakubDoka.directions.ui.Panel;
import com.github.jakubDoka.directions.ui.TextHandle;
import com.github.jakubDoka.directions.ui.Vec;

/**
 * GamePlay manages the game state when player is actually playing the game.
 * It contains the bail button Score display and counts the score.
 */
public class GamePlay extends Panel {
    private final Vec temp;
    private final Button end;
    private final TextHandle scoreText;
    private final TextHandle tutorialHint;
    private final FrameManager frameManager;
    
    private Path path;
    private Player player;
    private int score;
    private boolean isTutorial;

    /**
     * Constructs the GamePlay object. It will be initially hidden after
     * its added to canvas.
     */
    public GamePlay() {
        super(new Rectangle(), new Color(0, true));
        
        this.temp = new Vec();

        this.end = new Button(
            Color.GRAY,
            Color.DARK_GRAY,
            Fonts.SMALL,
            Color.BLACK,
            "END",
            new Rectangle(10, 10, 50, 30)
        );

        this.scoreText = new TextHandle(Fonts.BIG, Directions.WIDTH / 2, 30);
        this.scoreText.setMargin(TextHandle.Margin.CENTER);

        this.tutorialHint = new TextHandle(Fonts.SMALL, Directions.WIDTH / 2, 40);
        this.tutorialHint.setMargin(TextHandle.Margin.CENTER);

        this.frameManager = new FrameManager();
        

        this.score = 0;

        this.addItem(this.end);
        this.addItem(this.scoreText);
        this.addItem(this.tutorialHint);
        
        this.setVisible(false);
    }

    @Override
    public void drawImpl(Graphics2D g) {
        this.path.drawImpl(g);
        g.setColor(this.player.getColor());
        this.player.drawImpl(g);
        super.drawImpl(g);
    }

    /**
     * Starts the game and allows player to make moves.
     * @param isTutorial
     */
    public void start(boolean isTutorial, Difficulty difficulty, Color playerColor, Color pathColor) {
        this.isTutorial = isTutorial;
        this.setVisible(true);

        this.frameManager.setDelta(1);
        
        if (this.isTutorial) {
            this.tutorialHint.setText(
                "Goal of the game is to get to Green square.\n" + 
                "You have to move as fast as possible to get\n" + 
                "more score per move. Once you press wrong key\n" +
                "you are out. This tutorial will guide you when\n" + 
                "you make a mistake. Good luck! (You are the small\n" + 
                "square in the middle)"
            );
        } else {
            this.scoreText.setText("0");
            this.score = 0;
        }

        this.player = new Player(difficulty.getPathSize(), Directions.WIDTH / 2, Directions.HEIGHT / 2);
        this.player.setColor(playerColor);
        this.path = new Path(
            difficulty.getPathLength(), 
            difficulty.getPathSize(), 
            Directions.WIDTH / 2, 
            Directions.HEIGHT / 2
        );
        this.path.setColor(pathColor);
        for (int i = 0; i < 3; i++) {
            this.path.expand();
        }
    }

    /**
     * Makes game responsive to player input. Needs to be called every frame.
     * Method can switch the state to score screen. Score determination is also 
     * handled here.
     */
    public void update(Directions directions) {
        Canvas canvas = directions.getCanvas();
        Vec shift = this.temp;
        boolean moved = true;
        if (canvas.isJustPressed(KeyEvent.VK_LEFT)) {
            shift = this.path.move(Path.Direction.LEFT);
        } else if (canvas.isJustPressed(KeyEvent.VK_RIGHT)) {
            shift = this.path.move(Path.Direction.RIGHT);
        } else if (canvas.isJustPressed(KeyEvent.VK_UP)) {
            shift = this.path.move(Path.Direction.UP);
        } else if (canvas.isJustPressed(KeyEvent.VK_DOWN)) {
            shift = this.path.move(Path.Direction.DOWN);
        } else {
            moved = false;
        }

        if (shift == null) {
            if (this.isTutorial) {
                this.tutorialHint.setText("press " + this.path.getCorrectDirection() + " key");
            } else {
                this.setVisible(false);
                directions.showScore(this.score);
            }
        } else if (moved) {
            if (this.isTutorial) {
                this.tutorialHint.setText("");
            } else {
                // faster you move more score you get per move, difficulty also multiplies 
                // the score.
                double delta = this.frameManager.getDelta();

                int addition = (int)((1 - Math.min(Math.pow(delta, 1 / 30.0), 1)) * 1000) * 
                    (directions.getDifficulty().ordinal() + 1);
    
                this.score += addition;
                this.scoreText.setText(String.valueOf(this.score));
    
                this.frameManager.update();
            }

            Vec pathShift = this.player.move(shift);
            this.path.shift(-pathShift.getX(), -pathShift.getY());
        }

        if (this.end.pressed(canvas)) {
            if (this.isTutorial) {
                directions.showMainMenu();
            } else {
                directions.showScore(this.score);
            }
            this.setVisible(false);
        }
    }
}
