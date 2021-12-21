package github.com.jakubDoka.directions.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.event.MouseEvent;

public class Scroll extends CanvasObject {
    private final Point mouse;
    private final Rectangle bounds;
    private final Rectangle drawer;
    private final Color background;
    private final Color foreground;

    private int value;
    private boolean selected;

    public Scroll(Rectangle bounds, Color background, Color foreground) {
        this.mouse = new Point();
        this.bounds = bounds;
        this.drawer = new Rectangle(bounds.x, bounds.y, 0, bounds.height);
        this.background = background;
        this.foreground = foreground;
        this.value = bounds.width / 2;
        this.selected = false;
    }

    public double getValue() {
        return (double)this.value / this.bounds.width;
    }

    public void setValue(double value) {
        this.value = (int)(value * this.bounds.width);
    }

    public boolean changed(Canvas canvas) {
        Point mouse = canvas.getMousePos(this.mouse);

        if (
            (this.selected || this.bounds.contains(mouse)) &&
            canvas.isMousePressed(MouseEvent.BUTTON1)
        ) {
            this.selected = true;
            int value = Math.min(Math.max(mouse.x - this.bounds.x, 0), this.bounds.width); 
            if (value != this.value) {
                this.value = value;
                return true;
            }
            return false;
        }

        this.selected = false;

        return false;
    }

    @Override
    public void drawImpl(Graphics2D g) {
        g.setColor(this.background);
        g.fill(this.bounds);
        g.setColor(this.foreground);
        this.drawer.width = value;
        g.fill(this.drawer);   
    }
}