package com.github.jakubDoka.directions.ui;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * CanvasObject is what every class that needs to be drawn on Canvas.
 * It already offers some basic features all canvas elements should have
 * like visibility switch and color.
 */
public abstract class CanvasObject {
    private Color color;
    private boolean isVisible;

    public CanvasObject() {
        this.color = Color.BLACK;
        this.isVisible = true;
    }
    
    /**
     * Place your drawing logic here.
     */
    public abstract void drawImpl(Graphics2D g);

    /**
     * @return returns false if this object is not on the screen
     * and should be skipped.
     */
    public boolean intersects(Rectangle bounds) {
        return true;
    }

    /**
     * Performs one drawing cycle. This method ca skip drawing of the 
     * object if either of
     *  - {@link #isVisible()} returns false.
     *  - {@link #intersects(Rect)} returns false.
     */
    public void draw(Graphics2D g, Rectangle bounds) {
        if (this.isVisible && this.intersects(bounds)) {
            g.setColor(this.color);
            this.drawImpl(g);
        }
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public boolean isVisible() {
        return this.isVisible;
    }

    public Color getColor() {
        return this.color;
    }
}
