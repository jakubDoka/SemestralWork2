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
    private static final int PREVIEW_WIDTH = 300;
    
    private final Button back;
    private final Button reset;
    private final Selection difficulty;
    private final Selection colorSelection;
    private final Panel controls;
    private final Panel colors;
    private final Panel preview;
    private final Scroll red;
    private final Scroll green;
    private final Scroll blue;
    
    private Player previewPlayer;
    private Path previewPath;

    /**
     * Creates new settings screen. Initially hidden when added to canvas.
     */
    public Settings() {
        super(
            new Rectangle(),
            Color.BLACK
        );

        this.controls = new Panel(
            new Rectangle(
                PADDING,
                PADDING + PREVIEW_WIDTH,
                Directions.WIDTH - PADDING * 2,
                Directions.HEIGHT - PADDING * 2 - PREVIEW_WIDTH
            ), 
            Directions.UI_COLOR
        );

        // not actually visible, only for layering of preview
        this.preview = new Panel(new Rectangle(), Color.BLACK);

        // Back button
        final int buttonHeight = 40;
        
        final Rectangle backButtonRect = new Rectangle(
            PADDING * 2,
            Directions.HEIGHT - PADDING * 2 - buttonHeight,
            Directions.WIDTH / 2 - PADDING / 2 * 5,
            buttonHeight
        );
        this.back = new Button(
            Color.GRAY,
            Color.DARK_GRAY,
            Fonts.BIG,
            Color.BLACK,
            "BACK",
            backButtonRect
        );

        final Rectangle resetButtonRect = new Rectangle(
            Directions.WIDTH / 2 + PADDING / 2,
            Directions.HEIGHT - PADDING * 2 - buttonHeight,
            Directions.WIDTH / 2 - PADDING / 2 * 5,
            buttonHeight
        );
        this.reset = new Button(
            Color.GRAY,
            Color.DARK_GRAY,
            Fonts.BIG,
            Color.BLACK,
            "RESET",
            resetButtonRect
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

        final int difficultyHeight = 40;
        final Rectangle difficultyRect = new Rectangle(
            PADDING * 2,
            PADDING * 2 + PREVIEW_WIDTH,
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
            PADDING * 3 + difficultyHeight + PREVIEW_WIDTH,
            Directions.WIDTH - PADDING * 4,
            Directions.HEIGHT - PADDING * 6 - difficultyHeight - buttonHeight - PREVIEW_WIDTH
        );
        this.colors = new Panel(
            colorPanelRect,
            Colors.create(0xFF666666)
        );
        
        // the color picker target selection
        final String[] names = {"PLAYER", "PATH", "BACKGROUND"};
        final Selection.Item[] items = new Selection.Item[names.length];
        
        final Color idle = Colors.create(0xFF555555);
        final Color selected = Colors.create(0xFFAAAAAA);
        
        for (int i = 0; i < items.length; i++) {
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
        final Color[] localColors = {Color.RED, Color.GREEN, Color.BLUE};
        final Scroll[] scrolls = new Scroll[localColors.length];
        final int scrollHeight = 
            (colorPanelRect.height - PADDING * 5 - colorSelectionRect.height) / localColors.length;

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
                localColors[i]
            );
        }

        this.red = scrolls[0];
        this.green = scrolls[1];
        this.blue = scrolls[2];
        
        
        this.colors.addItem(this.colorSelection);
        this.colors.addItem(this.red);
        this.colors.addItem(this.green);
        this.colors.addItem(this.blue);
        
        this.controls.addItem(this.difficulty);
        this.controls.addItem(this.colors);
        this.controls.addItem(this.back);
        this.controls.addItem(this.reset);
        
        this.addItem(this.preview);
        this.addItem(this.controls);
        

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
            directions.setDifficulty(Difficulty.values()[newDifficulty]);
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

        // buttons
        if (this.back.pressed(canvas)) {
            directions.saveData();
            this.setVisible(false);
            directions.showMainMenu();
        } else if (this.reset.pressed(canvas)) {
            directions.resetData();
            this.start(directions);
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

        this.red.setValue(Colors.r(color) / 255.0);
        this.green.setValue(Colors.g(color) / 255.0);
        this.blue.setValue(Colors.b(color) / 255.0);
    }

    public void setDifficulty(Directions directions) {
        Difficulty localDifficulty = directions.getDifficulty();

        this.difficulty.setSelected(localDifficulty.ordinal());
        
        this.preview.removeItem(this.previewPath);
        this.preview.removeItem(this.previewPlayer);
        
        this.previewPath = new Path(localDifficulty.getPathLength(), localDifficulty.getPathSize(), Directions.WIDTH / 2, PREVIEW_WIDTH / 2);
        this.previewPlayer = new Player(localDifficulty.getPathSize(), Directions.WIDTH / 2, PREVIEW_WIDTH / 2);
        
        this.preview.addItem(this.previewPath);
        this.preview.addItem(this.previewPlayer);

        this.previewPath.setColor(Colors.create(directions.getPathColor()));
        this.previewPlayer.setColor(Colors.create(directions.getPlayerColor()));

        for (int i = 0; i < localDifficulty.getPathLength() - 1; i++) {
            this.previewPath.expand();
        }
    }

    public void setPlayerColor(Color color) {
        this.previewPlayer.setColor(color);
    }

    public void setPathColor(Color color) {
        this.previewPath.setColor(color);
    }
}