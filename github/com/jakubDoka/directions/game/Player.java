package github.com.jakubDoka.directions.game;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import github.com.jakubDoka.directions.ui.CanvasObject;

/**
 * Player is merely draws it self on the screen.
 * Its isolated class to make things more organized.
 */
public class Player extends CanvasObject {
    private final Rectangle drawer;
    
    /**
     * Player can be added to canvas and it will appear in the 
     * middle of the screen.
     * @param size - size of the rectangle, it acts more like an radius of a square.
     */
    public Player(int size) {
        this.drawer = new Rectangle(
            Directions.HEIGHT / 2 - size, 
            Directions.WIDTH / 2 - size, 
            size * 2, 
            size * 2
        );
    }

    /**
     * Draws the player on the screen.
     */
    @Override
    public void drawImpl(Graphics2D g) {
        g.setColor(this.getColor());
        g.fill(this.drawer);
    }
}
