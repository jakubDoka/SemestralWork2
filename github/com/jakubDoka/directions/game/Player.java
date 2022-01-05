package github.com.jakubDoka.directions.game;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import github.com.jakubDoka.directions.ui.CanvasObject;
import github.com.jakubDoka.directions.ui.Vec;

/**
 * Player is merely draws it self on the screen.
 * Its isolated class to make things more organized.
 */
public class Player extends CanvasObject {
    private static final double MARGIN = 0.25; 
    
    private final Vec temp;
    private final Rectangle drawer;

    private final Vec position;

    /**
     * Player can be added to canvas and it will appear in the 
     * middle of the screen.
     * @param size - size of the rectangle, it acts more like an radius of a square.
     */
    public Player(int size, double x, double y) {
        size = size / 3 * 2;
        this.temp = new Vec();
        this.position = new Vec(x - size, y - size);
        this.drawer = new Rectangle(
            0, 
            0, 
            size * 2, 
            size * 2
        );
    }

    /**
     * Move moves the player by vec.
     * @param shift - the shift
     */
    public Vec move(Vec shift) {
        Vec nextPosition = this.temp.set(this.position).sub(shift);
        double realMoveX = Math.min(
            Math.max(
                nextPosition.getX(), 
                Directions.WIDTH * MARGIN
            ), 
            Directions.WIDTH - Directions.WIDTH * MARGIN
        );
        double realMoveY = Math.min(
            Math.max(
                nextPosition.getY(), 
                Directions.HEIGHT * MARGIN
            ), 
            Directions.HEIGHT - Directions.HEIGHT * MARGIN
        );
        this.position.set(realMoveX, realMoveY);
        this.temp.sub(this.position);

        return this.temp;
    }

    /**
     * Draws the player on the screen.
     */
    @Override
    public void drawImpl(Graphics2D g) {
        this.drawer.setLocation((int)this.position.getX(), (int)this.position.getY());
        g.setColor(this.getColor());
        g.fill(this.drawer);
    }
}
