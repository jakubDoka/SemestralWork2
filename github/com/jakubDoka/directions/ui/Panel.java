package github.com.jakubDoka.directions.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * Panels role is to craw all its children on to oh himself.
 * This allows some nice ui trees.
 */
public class Panel extends CanvasObject {
    private final Color color;
    private final Rectangle bounds;
    private final ArrayList<CanvasObject> items;

    /**
     * Creates a new panel instance. Panel is immutable.
     * @param bounds - bounding box of the panel.
     * @param color - color of the panel.
     */
    public Panel(Rectangle bounds, Color color) {
        this.color = color;
        this.bounds = bounds;
        this.items = new ArrayList<>();
    }

    public void addItem(CanvasObject item) {
        this.items.add(item);
    }

    public void removeItem(CanvasObject item) {
        this.items.remove(item);
    }

    @Override
    public void drawImpl(Graphics2D g) {
        g.setColor(this.color);
        g.fill(this.bounds);
        for (CanvasObject item : this.items) {
            item.drawImpl(g);
        }        
    }
}
