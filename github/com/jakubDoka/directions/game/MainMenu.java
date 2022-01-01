package github.com.jakubDoka.directions.game;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Graphics2D;

import github.com.jakubDoka.directions.ui.Button;
import github.com.jakubDoka.directions.ui.Canvas;
import github.com.jakubDoka.directions.ui.Panel;

/**
 * Class mainly for organization purposes, it holds the main menu 
 * interface and communicates with Directions.
 */
public class MainMenu extends Panel {
    private final Button play;
    private final Button settings;
    private final Button exit;

    private final Path path;

    public MainMenu() {
        super(
            new Rectangle(
                Directions.WIDTH / 4, 
                Directions.HEIGHT / 4, 
                Directions.WIDTH / 2, 
                Directions.HEIGHT / 2
            ), 
            Directions.UI_COLOR
        );

        this.path = new Path(100, 6, Directions.WIDTH / 2, Directions.HEIGHT / 2);

        final int padding = 10;
        final int width = Directions.WIDTH / 2 - padding * 2;
        final int height = (Directions.HEIGHT / 2 - padding * 4) / 3;

        final String[] names = {"PLAY", "SETTINGS", "EXIT"};

        final Button[] buttons = new Button[3];

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new Button(
                Color.GRAY, 
                Color.DARK_GRAY, 
                Fonts.BIG,
                Color.BLACK,
                names[i], 
                new Rectangle(
                    Directions.WIDTH / 4 + padding,
                    Directions.HEIGHT / 4 + padding * (i + 1) + height * i,
                    width,
                    height
                )
            );
            this.addItem(buttons[i]);
        }

        this.play = buttons[0];
        this.settings = buttons[1];
        this.exit = buttons[2];            
    }

    @Override
    public void drawImpl(Graphics2D g) {
        this.path.drawImpl(g);
        super.drawImpl(g);
    }

    /**
     * Handles the button events and updates the background path.
     */
    public void update(Directions directions) {
        Canvas canvas = directions.getCanvas();
        if (this.play.pressed(canvas)) {
            directions.startGame();
        } else if (this.settings.pressed(canvas)) {
            directions.showSettings();
        } else if (this.exit.pressed(canvas)) {
            System.exit(0);
        }

        this.path.expand();

        // So that animation wont cause epileptic seizures
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setPathColor(Color color) {
        this.path.setColor(color);
    }
}