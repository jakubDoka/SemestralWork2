package github.com.jakubDoka.directions.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import github.com.jakubDoka.directions.ui.Button;
import github.com.jakubDoka.directions.ui.Canvas;
import github.com.jakubDoka.directions.ui.FrameManager;
import github.com.jakubDoka.directions.ui.Panel;
import github.com.jakubDoka.directions.ui.TextHandle;

public class GamePlay extends Panel {
    private final Button end;
    private final TextHandle scoreText;
    private final FrameManager frameManager;
    
    private Path path;
    private Player player;
    private int score;

    public GamePlay() {
        super(new Rectangle(), new Color(0, true));
        
        this.end = new Button(
            Color.GRAY,
            Color.DARK_GRAY,
            Fonts.SMALL,
            Color.BLACK,
            "END",
            new Rectangle(10, 10, 50, 30)
        );

        this.scoreText = new TextHandle(Fonts.BIG, Directions.WIDTH / 2, 30);
        this.scoreText.setText("0");
        this.scoreText.setMargin(TextHandle.Margin.CENTER);

        this.frameManager = new FrameManager();
        

        this.score = 0;

        this.addItem(this.end);
        this.addItem(this.scoreText);
        
        this.setVisible(false);
    }

    @Override
    public void drawImpl(Graphics2D g) {
        this.path.drawImpl(g);
        g.setColor(this.player.getColor());
        this.player.drawImpl(g);
        super.drawImpl(g);
    }

    public void start(Difficulty difficulty, Color playerColor, Color pathColor) {
        this.setVisible(true);

        this.frameManager.setDelta(1);
        
        this.scoreText.setText("0");
        this.score = 0;

        this.player = new Player(difficulty.getPathSize() / 3 * 2);
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

    public void update(Directions directions) {
        Canvas canvas = directions.getCanvas();
        boolean success = true;
        boolean moved = true;
        if (canvas.isJustPressed(KeyEvent.VK_LEFT)) {
            success = this.path.move(Path.Direction.LEFT);
        } else if (canvas.isJustPressed(KeyEvent.VK_RIGHT)) {
            success = this.path.move(Path.Direction.RIGHT);
        } else if (canvas.isJustPressed(KeyEvent.VK_UP)) {
            success = this.path.move(Path.Direction.UP);
        } else if (canvas.isJustPressed(KeyEvent.VK_DOWN)) {
            success = this.path.move(Path.Direction.DOWN);
        } else {
            moved = false;
        }

        if (!success) {
            this.setVisible(false);
            directions.showScore(this.score);
        } else if (moved) {
            double delta = this.frameManager.getDelta();

            int addition = (int)((1 - Math.min(Math.pow(delta, 1/30.0), 1)) * 1000) * 
                (directions.getDifficulty().getIndex() + 1);

            this.score += addition;
            this.scoreText.setText(String.valueOf(this.score));

            this.frameManager.update();
        }

        if (this.end.pressed(canvas)) {
            this.setVisible(false);
            directions.showScore(score);
        }
    }
}
