package github.com.jakubDoka.directions.game;

import java.awt.Rectangle;
import java.awt.Color;

import github.com.jakubDoka.directions.ui.Button;
import github.com.jakubDoka.directions.ui.Canvas;
import github.com.jakubDoka.directions.ui.Colors;
import github.com.jakubDoka.directions.ui.Panel;
import github.com.jakubDoka.directions.ui.TextHandle;

/**
 * Score screen displays achieved score and also the best 
 * score of all time for comparison. It also offers three buttons:
 *  - Back to main menu
 *  - Play again
 *  - Exit
 */
public class ScoreScreen extends Panel {
    private final TextHandle scoreText;
    private final TextHandle bestScoreText;
    private final Button rageQuit;
    private final Button back;
    private final Button restart;

    /**
     * Creates new score screen. Initially hidden when added to canvas.
     */
    public ScoreScreen() {
        super(new Rectangle(10, 10, Directions.WIDTH - 20, Directions.HEIGHT - 20), Colors.create(0x55FFFFFF));

        this.bestScoreText = new TextHandle(Fonts.BIG, Directions.WIDTH / 2, 100);
        this.bestScoreText.setMargin(TextHandle.Margin.CENTER);

        this.scoreText = new TextHandle(Fonts.BIG, Directions.WIDTH / 2, 200);
        this.scoreText.setMargin(TextHandle.Margin.CENTER);

        final int padding = 10;
        final int buttonHeight = 100;
        final Rectangle buttonRect = new Rectangle(
            padding * 2,
            Directions.HEIGHT - padding * 2 - buttonHeight,
            Directions.WIDTH - 4 * padding,
            buttonHeight
        );

        final String[] buttonNames = { "RAGE QUIT", "BACK", "RESTART" };
        final int buttonWidth = (buttonRect.width - padding * 2) / buttonNames.length;

        final Button[] buttons = new Button[buttonNames.length];
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new Button(
                Color.GRAY,
                Color.DARK_GRAY,
                Fonts.BIG,
                Color.BLACK,
                buttonNames[i],
                new Rectangle(
                    buttonRect.x + i * (buttonWidth + padding),
                    buttonRect.y,
                    buttonWidth,
                    buttonHeight
                )
            );
            this.addItem(buttons[i]);
        }

        this.rageQuit = buttons[0];
        this.back = buttons[1];
        this.restart = buttons[2];

        this.addItem(this.scoreText);
        this.addItem(this.bestScoreText);
        
        this.setVisible(false);
    }

    /**
     * Sets score to be displayed. and intractable by user.
     * @param score - current score to display
     * @param bestScore - best score to display
     */
    public void start(int score, int bestScore) {
        this.bestScoreText.setText("BEST: " + bestScore);
        this.scoreText.setText(String.valueOf(score));
        this.setVisible(true);
    }

    /**
     * Makes score screen responsive to user input. Needs
     * to be called every fame.
     */
    public void update(Directions directions) {
        Canvas canvas = directions.getCanvas();
        if (this.rageQuit.pressed(canvas)) {
            System.exit(0);
        } else if (this.back.pressed(canvas)) {
            this.setVisible(false);
            directions.showMainMenu();
        } else if (this.restart.pressed(canvas)) {
            this.setVisible(false);
            directions.startGame(false);
        }
    }
}