package github.com.jakubDoka.directions.game;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import github.com.jakubDoka.directions.ui.CanvasObject;

import java.awt.Color;

public class Player extends CanvasObject {
    private final Rectangle drawer;
    //private final int size;
    
    public Player(int size) {
        this.setColor(Color.BLUE);
        this.drawer = new Rectangle(
            Directions.HEIGHT / 2 - size, 
            Directions.WIDTH / 2 - size, 
            size * 2, 
            size * 2
        );
        //this.size = size;
    }

    @Override
    public void drawImpl(Graphics2D g) {
        g.fill(this.drawer);
    }
}
