package github.com.jakubDoka.directions.game;

import java.awt.Color;
import java.awt.Rectangle;

import github.com.jakubDoka.directions.ui.Button;
import github.com.jakubDoka.directions.ui.Canvas;
import github.com.jakubDoka.directions.ui.Colors;
import github.com.jakubDoka.directions.ui.Panel;
import github.com.jakubDoka.directions.ui.Scroll;
import github.com.jakubDoka.directions.ui.Selection;

/**
 * Manages state of game settings screen.
 */
public class Settings extends Panel {
    private static final int PADDING = 10;
    private static final int PLAYER = 0;
    private static final int PATH = 1;
    private static final int BACKGROUND = 2;

    private final Button back;
    private final Selection difficulty;
    private final Panel colorPanel; 
    private final Selection colorSelection;
    private final Scroll red;
    private final Scroll green;
    private final Scroll blue;

    /**
     * Creates new settings screen. Initially hidden when added to canvas.
     */
    public Settings() {
        super(
            new Rectangle(
                PADDING,
                PADDING,
                Directions.WIDTH - PADDING * 2,
                Directions.HEIGHT - PADDING * 2
            ), 
            Directions.UI_COLOR
        );

        // Back button
        final int backButtonHeight = 50;
        final Rectangle backButtonRect = new Rectangle(
            PADDING * 2,
            Directions.HEIGHT - PADDING * 2 - backButtonHeight,
            Directions.WIDTH - PADDING * 4,
            backButtonHeight
        );

        this.back = new Button(
            Color.GRAY,
            Color.DARK_GRAY,
            Fonts.BIG,
            Color.BLACK,
            "BACK",
            backButtonRect
        );

        // difficulty selection
        final Color nonSelectedDifficulty = Colors.create(0xFF777777);
        final int[] difficultyColors = {0xFF7aeb34, 0xFFe5eb34, 0xFFeb4034};
        final String[] difficultyNames = {"EASY", "MEDIUM", "HARD"};
        final Selection.Item[] difficultyItems = new Selection.Item[difficultyColors.length];

        for (int i = 0; i < difficultyItems.length; i++) {
            difficultyItems[i] = new Selection.Item(
                difficultyNames[i],
                nonSelectedDifficulty,
                Colors.create(difficultyColors[i])
            );
        }

        final int difficultyHeight = 100;
        final Rectangle difficultyRect = new Rectangle(
            PADDING * 2,
            PADDING * 2,
            Directions.WIDTH - PADDING * 4,
            difficultyHeight
        );

        this.difficulty = new Selection(
            difficultyRect,
            Fonts.BIG,
            difficultyItems
        );

        // panel with color picker
        final Rectangle colorPanelRect = new Rectangle(
            PADDING * 2,
            PADDING * 3 + difficultyHeight,
            Directions.WIDTH - PADDING * 4,
            Directions.HEIGHT - PADDING * 6 - difficultyHeight - backButtonHeight
        );
        this.colorPanel = new Panel(
            colorPanelRect,
            Colors.create(0xFF666666)
        );
        
        // the color picker target selection
        final String[] names = {"PLAYER", "PATH", "BACKGROUND"};
        final Selection.Item[] items = new Selection.Item[names.length];
        
        final Color idle = Colors.create(0xFF555555);
        final Color selected = Colors.create(0xFFAAAAAA);
        
        for(int i = 0; i < items.length; i++) {
            items[i] = new Selection.Item(names[i], idle, selected);
        }
        
        final Rectangle colorSelectionRect = new Rectangle(
            PADDING * 3,
            colorPanelRect.y + PADDING,
            Directions.WIDTH - PADDING * 6,
            colorPanelRect.height / 3 - PADDING * 2
        );
        this.colorSelection = new Selection(
            colorSelectionRect,
            Fonts.BIG,
            items
        );
        this.colorSelection.setSelected(0);

        // the color picker scrolls
        final Color[] colors = {Color.RED, Color.GREEN, Color.BLUE};
        final Scroll[] scrolls = new Scroll[colors.length];
        final int scrollHeight = 
            (colorPanelRect.height - PADDING * 5 - colorSelectionRect.height) / colors.length;

        for (int i = 0; i < scrolls.length; i++) {
            scrolls[i] = new Scroll(
                new Rectangle(
                    PADDING * 3,
                    colorPanelRect.y + PADDING * (2 + i) + 
                    scrollHeight * i + colorSelectionRect.height,
                    Directions.WIDTH - PADDING * 6,
                    scrollHeight
                ),
                idle,
                colors[i]
            );
        }

        this.red = scrolls[0];
        this.green = scrolls[1];
        this.blue = scrolls[2];
        
        this.colorPanel.addItem(this.colorSelection);
        this.colorPanel.addItem(this.red);
        this.colorPanel.addItem(this.green);
        this.colorPanel.addItem(this.blue);

        this.addItem(this.difficulty);
        this.addItem(this.colorPanel);
        this.addItem(this.back);

        this.setVisible(false);
    }

    /**
     * Makes the settings screen visible and intractable.
     */
    public void start(Directions directions) {
        this.setVisible(true);
        this.colorSelection.setColor(0, Colors.create(directions.getPlayerColor()));
        this.colorSelection.setColor(1, Colors.create(directions.getPathColor()));
        this.colorSelection.setColor(2, Colors.create(directions.getBackgroundColor()));
        this.selectScrolls(this.colorSelection.getSelected(), directions);
    }

    /**
     * Makes the setting screen responsive to user input.
     * Needs to be called every frame.
     */
    public void update(Directions directions) {
        Canvas canvas = directions.getCanvas();

        int newDifficulty = this.difficulty.changed(canvas);
        if (newDifficulty != -1) {
            directions.setDifficulty(Difficulty.values()[newDifficulty]);;
        }

        int newColor = this.colorSelection.changed(canvas);

        if (newColor != -1) {
            this.selectScrolls(newColor, directions);
        }

        if (
            this.red.changed(canvas) ||
            this.green.changed(canvas) ||
            this.blue.changed(canvas)
        ) {
            int color = Colors.color(
                (int)(this.red.getValue() * 255), 
                (int)(this.green.getValue() * 255), 
                (int)(this.blue.getValue() * 255),                     
                255
            );

            int selected = this.colorSelection.getSelected();

            this.colorSelection.setColor(selected, Colors.create(color));

            switch (this.colorSelection.getSelected()) {
                case PLAYER:
                    directions.setPlayerColor(color);
                    break;
                case PATH:
                    directions.setPathColor(color);
                    break;
                case BACKGROUND:
                    directions.setBackgroundColor(color);
            }
            
        }

        if (this.back.pressed(canvas)) {
            directions.saveData();
            this.setVisible(false);
            directions.showMainMenu();
        }
    }

    /**
     * Readjusts the color scrolls to match selected item.
     * @param selected - the selected item index (0-2)
     * @param directions
     */
    private void selectScrolls(int selected, Directions directions) {
        int color;
            
        switch (selected) {
            case PLAYER:
                color = directions.getPlayerColor();
                break;
            case PATH:
                color = directions.getPathColor();
                break;
            default:
                color = directions.getBackgroundColor();
        }

        int red = Colors.r(color);
        int green = Colors.g(color);
        int blue = Colors.b(color);

        this.red.setValue(red / 255.0);
        this.green.setValue(green / 255.0);
        this.blue.setValue(blue / 255.0);
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty.setSelected(difficulty.ordinal());
    }
}