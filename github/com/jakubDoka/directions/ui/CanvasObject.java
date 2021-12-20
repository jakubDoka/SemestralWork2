package github.com.jakubDoka.directions.ui;
import java.awt.Color;
import java.awt.Graphics2D;

public abstract class CanvasObject {
    private Color color;
    private boolean isVisible;

    public CanvasObject() {
        this.color = Color.BLACK;
        this.isVisible = true;
    }
    
    public abstract void drawImpl(Graphics2D g);

    public boolean intersects(Rect bounds) {
        return true;
    }

    public void draw(Graphics2D g, Rect bounds) {
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
