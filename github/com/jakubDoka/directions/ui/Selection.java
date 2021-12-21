package github.com.jakubDoka.directions.ui;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.Point;

public class Selection extends CanvasObject {
    private final Point mouse;
    private final Item[] items;
    private final Rectangle bounds;
    private final Rectangle drawer;
    private final TextHandle text;

    private int selected;

    public Selection(Rectangle bounds, Item... items) {
        this.mouse = new Point();
        this.items = items;
        this.bounds = bounds;
        this.drawer = new Rectangle();
        this.text = new TextHandle(Button.FONT, 0, 0);
        this.text.setMargin(TextHandle.Margin.CENTER);
    }
    
    @Override
    public void drawImpl(Graphics2D g) {
        
        for(int i = 0; i < this.items.length; i++) {
            this.setBounds(i);
            g.setColor(this.items[i].getColor());
            g.fill(this.drawer);
            g.setColor(Color.BLACK);
            this.text.setText(this.items[i].getText());
            this.text.setX((int)this.drawer.getCenterX());
            this.text.setY((int)this.drawer.getCenterY());
            this.text.drawImpl(g);
        }
    }

    public int getSelected() {
        return this.selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;

        for(Item i : this.items) {
            i.deselect();
        }

        this.items[this.selected].select();
    }

    public void setColor(int i, Color color) {
        this.items[i].setColor(color);
    }

    public int changed(Canvas canvas) {
        Point mouse = canvas.getMousePos(this.mouse);
        for(int i = 0; i < this.items.length; i++) {
            this.setBounds(i);
            if(
                this.drawer.contains(mouse) && 
                canvas.isMouseJustPressed(MouseEvent.BUTTON1) && 
                this.selected != i
            ) {
                for(Item item : this.items) {
                    item.deselect();
                }

                this.items[i].select();

                this.selected = i;

                return i;
            }
        }

        return -1;
    }

    private void setBounds(int i) {
        final int reminder = this.bounds.width % this.items.length;
        final int step = this.bounds.width / this.items.length;
        this.drawer.setBounds(
            this.bounds.x + i * step,
            this.bounds.y,
            step + (i - 1 == this.items.length ? reminder : 0),
            this.bounds.height
        );
    } 

    public static class Item {
        private final String text;
        private final Color idle;
        
        private Color selection;
        private boolean selected;

        public Item(String text, Color idle, Color selection) {
            this.text = text;
            this.idle = idle;
            this.selection = selection;
            this.selected = false;
        }

        public void select() {
            this.selected = true;
        }

        public void deselect() {
            this.selected = false;
        }

        public String getText() {
            return text;
        }

        public Color getColor() {
            return selected ? selection : idle;
        }

        public void setColor(Color color) {
            this.selection = color;
        }
    }
}
