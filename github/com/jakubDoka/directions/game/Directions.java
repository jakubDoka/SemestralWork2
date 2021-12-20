package github.com.jakubDoka.directions.game;
import java.awt.Color;
import java.awt.event.KeyEvent;

import github.com.jakubDoka.directions.ui.Canvas;
import github.com.jakubDoka.directions.ui.FrameManager;

public class Directions implements Runnable {

    public static final int SIZE = 4;
    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;

    private Path path;
    private Canvas canvas;
    private Player player;
    private FrameManager frameManager;

    public Directions() {
        this.frameManager = new FrameManager();
        this.canvas = new Canvas("Directions", WIDTH, HEIGHT, Color.WHITE);
        this.path = new Path(100, SIZE, WIDTH / 2, HEIGHT / 2);
        this.player = new Player(SIZE / 3 * 2);

        for(int i = 0; i < 3; i++) {
            this.path.expand();
        }

        this.canvas.addObject(this.path);
        this.canvas.addObject(this.player);
    }

    @Override
    public void run() {
        while (true) {
            
            boolean success = true;
            if (this.canvas.isJustPressed(KeyEvent.VK_LEFT)) {
                success = this.path.move(Path.Direction.LEFT);
            } else if (this.canvas.isJustPressed(KeyEvent.VK_RIGHT)) {
                success = this.path.move(Path.Direction.RIGHT);
            } else if (this.canvas.isJustPressed(KeyEvent.VK_UP)) {
                success = this.path.move(Path.Direction.UP);
            } else if (this.canvas.isJustPressed(KeyEvent.VK_DOWN)) {
                success = this.path.move(Path.Direction.DOWN);
            }

            if (!success) {
                throw new RuntimeException("You lost!");
            }

            this.canvas.update();
            this.frameManager.update();
        }
    }
}