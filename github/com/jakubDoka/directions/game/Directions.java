package github.com.jakubDoka.directions.game;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.Graphics2D;
import java.awt.Font;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

import github.com.jakubDoka.directions.ui.Button;
import github.com.jakubDoka.directions.ui.Canvas;
import github.com.jakubDoka.directions.ui.Colors;
import github.com.jakubDoka.directions.ui.FrameManager;
import github.com.jakubDoka.directions.ui.Panel;
import github.com.jakubDoka.directions.ui.Scroll;
import github.com.jakubDoka.directions.ui.Selection;
import github.com.jakubDoka.directions.ui.TextHandle;

public class Directions implements Runnable {
    public static final int SIZE = 4;
    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;
    public static final Color UI_COLOR = new Color(255, 255, 255, 100);
    
    private final MainMenu mainMenu;
    private final GamePlay gamePlay;
    private final Settings settings;
    private final Data data;

    private Canvas canvas;
    private FrameManager frameManager;
    private State state;

    public Directions() {
        this.mainMenu = new MainMenu();
        this.gamePlay = new GamePlay();
        this.settings = new Settings();
        
        this.frameManager = new FrameManager();
        this.canvas = new Canvas("Directions", WIDTH, HEIGHT);
        this.canvas.setBackground(Color.WHITE.darker());
        
        this.data = new Data();
        this.data.load();
        
        this.canvas.addObject(this.mainMenu);
        this.canvas.addObject(this.gamePlay);
        this.canvas.addObject(this.settings);

        this.state = State.MAIN_MENU;
    }

    @Override
    public void run() {
        while (true) {
            
            switch (this.state) {
                case MAIN_MENU:
                    this.mainMenu.update();
                    break;
                case PLAYING:
                    this.gamePlay.update();
                    break;
                case SCORE_SCREEN:
                    
                    break;
                case SETTINGS:
                    this.settings.update();
            }

            
            this.frameManager.update();
            this.canvas.update();
        }
    }

    public void startGame() {
        this.state = State.PLAYING;
        this.gamePlay.start();
        this.mainMenu.setVisible(false);
    }

    public void showSettings() {
        this.mainMenu.setVisible(false);
        this.settings.start();
        this.state = State.SETTINGS;
    }

    public void showScore(int score) {
        throw new RuntimeException("Not implemented");
    }

    public void showMainMenu() {
        this.mainMenu.setVisible(true);
        this.state = State.MAIN_MENU;
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

    /**
     * Class mainly for organization purposes. Holds the game state
     * preserved during game-play.
     */
    public class GamePlay extends Panel {
        private final Player player;
        private final Path path;
        private final Button end;
        private final TextHandle scoreText;
        private final FrameManager frameManager;

        private int score;

        public GamePlay() {
            super(new Rectangle(), new Color(0, true));

            this.path = new Path(20, SIZE, WIDTH / 2, HEIGHT / 2);
            this.player = new Player(SIZE / 3 * 2);
            
            this.end = new Button(
                Color.GRAY,
                Color.DARK_GRAY,
                Color.BLACK,
                "END",
                new Rectangle(10, 10, 50, 30)
            );

            this.scoreText = new TextHandle(Button.FONT, WIDTH / 2, 30);
            this.scoreText.setText("0");
            this.scoreText.setMargin(TextHandle.Margin.CENTER);

            this.frameManager = new FrameManager();

            this.score = 0;

            this.addItem(this.end);
            
            this.setVisible(false);
        }

        @Override
        public void drawImpl(Graphics2D g) {
            this.path.drawImpl(g);
            g.setColor(this.player.getColor());
            this.player.drawImpl(g);
            super.drawImpl(g);
        }


        public void start() {
            this.setVisible(true);
            
            this.scoreText.setText("0");
            this.score = 0;

            this.path.restart();
            for (int i = 0; i < 3; i++) {
                this.path.expand();
            }
        }

        public void update() {
            boolean success = true;
            if (Directions.this.canvas.isJustPressed(KeyEvent.VK_LEFT)) {
                success = this.path.move(Path.Direction.LEFT);
            } else if (Directions.this.canvas.isJustPressed(KeyEvent.VK_RIGHT)) {
                success = this.path.move(Path.Direction.RIGHT);
            } else if (Directions.this.canvas.isJustPressed(KeyEvent.VK_UP)) {
                success = this.path.move(Path.Direction.UP);
            } else if (Directions.this.canvas.isJustPressed(KeyEvent.VK_DOWN)) {
                success = this.path.move(Path.Direction.DOWN);
            }
    
            if (!success) {
                this.setVisible(false);
                Directions.this.showScore(this.score);
            } else {
                double delta = this.frameManager.getDelta();

                int addition = (int)((1 - Math.min(Math.pow(delta, 10) * 100, 1)) * 10000);

                this.score += addition;
                this.scoreText.setText(String.valueOf(this.score));

                this.frameManager.update();
            }
        }
    }

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
                    WIDTH / 4, 
                    HEIGHT / 4, 
                    WIDTH / 2, 
                    HEIGHT / 2
                ), 
                UI_COLOR
            );

            this.path = new Path(100, 6, WIDTH / 2, HEIGHT / 2);

            final int padding = 10;
            final int width = WIDTH / 2 - padding * 2;
            final int height = (HEIGHT / 2 - padding * 4) / 3;

            final String[] names = {"PLAY", "SETTINGS", "EXIT"};

            final Button[] buttons = new Button[3];

            for (int i = 0; i < buttons.length; i++) {
                buttons[i] = new Button(
                    Color.GRAY, 
                    Color.DARK_GRAY, 
                    Color.BLACK,
                    names[i], 
                    new Rectangle(
                        WIDTH / 4 + padding,
                        HEIGHT / 4 + padding * (i + 1) + height * i,
                        width,
                        height
                    )
                );
            }

            this.play = buttons[0];
            this.settings = buttons[1];
            this.exit = buttons[2];
            
            this.addItem(this.play);
            this.addItem(this.settings);
            this.addItem(this.exit);
        }

        @Override
        public void drawImpl(Graphics2D g) {
            this.path.drawImpl(g);
            super.drawImpl(g);
        }

        /**
         * Handles the button events and updates the background path.
         */
        public void update() {
            if (this.play.pressed(Directions.this.canvas)) {
                Directions.this.startGame();
            } else if (this.settings.pressed(Directions.this.canvas)) {
                Directions.this.showSettings();
            } else if (this.exit.pressed(Directions.this.canvas)) {
                System.exit(0);
            }

            this.path.expand();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class Settings extends Panel {
        public static final Font FONT = new Font("Monospaced", Font.BOLD, 10);
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

        public Settings() {
            super(
                new Rectangle(
                    PADDING,
                    PADDING,
                    WIDTH - PADDING * 2,
                    HEIGHT - PADDING * 2
                ), 
                UI_COLOR
            );

            final int backButtonHeight = 50;
            final Rectangle backButtonRect = new Rectangle(
                PADDING * 2,
                HEIGHT - PADDING * 2 - backButtonHeight,
                WIDTH - PADDING * 4,
                backButtonHeight
            );

            this.back = new Button(
                Color.GRAY,
                Color.DARK_GRAY,
                Color.BLACK,
                "BACK",
                backButtonRect
            );

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
                WIDTH - PADDING * 4,
                difficultyHeight
            );

            this.difficulty = new Selection(
                difficultyRect,
                difficultyItems
            );

            final Rectangle colorPanelRect = new Rectangle(
                PADDING * 2,
                PADDING * 3 + difficultyHeight,
                WIDTH - PADDING * 4,
                HEIGHT - PADDING * 6 - difficultyHeight - backButtonHeight
            );
            this.colorPanel = new Panel(
                colorPanelRect,
                Colors.create(0xFF666666)
            );
            
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
                WIDTH - PADDING * 6,
                colorPanelRect.height / 3 - PADDING * 2
            );
            this.colorSelection = new Selection(
                colorSelectionRect,
                items
            );
            this.colorSelection.setSelected(0);

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
                        WIDTH - PADDING * 6,
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

        public void start() {
            this.setVisible(true);
            this.colorSelection.setColor(0, Colors.create(Directions.this.data.getPlayerColor()));
            this.colorSelection.setColor(1, Colors.create(Directions.this.data.getPathColor()));
            this.colorSelection.setColor(2, Colors.create(Directions.this.data.getBackgroundColor()));
            this.selectScrolls(this.colorSelection.getSelected());
        }

        public void update() {
            int newDifficulty = this.difficulty.changed(Directions.this.canvas);
            if (newDifficulty != -1) {
                Directions.this.data.setDifficulty(Difficulty.values()[newDifficulty]);;
            }

            int newColor = this.colorSelection.changed(Directions.this.canvas);

            if (newColor != -1) {
                this.selectScrolls(newColor);
            }

            if (
                this.red.changed(Directions.this.canvas) ||
                this.green.changed(Directions.this.canvas) ||
                this.blue.changed(Directions.this.canvas)
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
                        Directions.this.data.setPlayerColor(color);
                        break;
                    case PATH:
                        Directions.this.data.setPathColor(color);
                        break;
                    case BACKGROUND:
                        Directions.this.data.setBackgroundColor(color);
                }
                
            }

            if (this.back.pressed(Directions.this.canvas)) {
                Directions.this.data.save();
                this.setVisible(false);
                Directions.this.showMainMenu();
            }
        }

        private void selectScrolls(int selected) {
            int color;
                
            switch (selected) {
                case PLAYER:
                    color = Directions.this.data.getPlayerColor();
                    break;
                case PATH:
                    color = Directions.this.data.getPathColor();
                    break;
                default:
                    color = Directions.this.data.getBackgroundColor();
            }

            int red = Colors.r(color);
            int green = Colors.g(color);
            int blue = Colors.b(color);

            this.red.setValue(red / 255.0);
            this.green.setValue(green / 255.0);
            this.blue.setValue(blue / 255.0);
        }
    }

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
            this.bestScore = bestScore;
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
            Directions.this.canvas.setBackground(Colors.create(backgroundColor));
            this.backgroundColor = backgroundColor;
        }

        public int getPathColor() {
            return this.pathColor;
        }

        public void setPathColor(int pathColor) {
            Color color = Colors.create(pathColor);
            Directions.this.mainMenu.path.setColor(color);
            Directions.this.gamePlay.path.setColor(color);
            this.pathColor = pathColor;
        }

        public int getPlayerColor() {
            return this.playerColor;
        }

        public void setPlayerColor(int playerColor) {
            Directions.this.gamePlay.setColor(Colors.create(playerColor));
            this.playerColor = playerColor;
        }

        public void load() {
            try {
                Scanner scanner = new Scanner(new File(FILE));

                this.bestScore = scanner.nextInt();

                this.difficulty = Difficulty.values()[scanner.nextInt()];
                Directions.this.settings.difficulty.setSelected(this.difficulty.getIndex());
                
                this.setBackgroundColor(scanner.nextInt());
                this.setPathColor(scanner.nextInt());
                this.setPlayerColor(scanner.nextInt());

                scanner.close();

            } catch (Exception e) {
                e.printStackTrace();
                this.save();
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

    public enum Difficulty {
        EASY(10, 10),
        MEDIUM(20, 6),
        HARD(30, 4);

        private static int counter;
        
        private int index;
        private int pathLength;
        private int pathSize;
        
        private static int next() {
            return counter++;
        }

        private Difficulty(int pathLength, int pathSize) {
            this.index = Difficulty.next();
            this.pathLength = pathLength;
            this.pathSize = pathSize;
        }

        public int getIndex() {
            return this.index;
        }

        public int getPathLength() {
            return this.pathLength;
        }

        public int getPathSize() {
            return this.pathSize;
        }
    }
}