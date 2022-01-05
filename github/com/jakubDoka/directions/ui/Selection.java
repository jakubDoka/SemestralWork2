package github.com.jakubDoka.directions.ui;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.awt.Font;

/**
 * Selection allows selecting from fixed amount of choices.
 * Only one choice can be selected at the time and each choice has its own
 * color and text. Colors aof choices can be changed after creation.
 */
public class Selection extends CanvasObject {
    private final Point mouse;
    private final Item[] items;
    private final Rectangle bounds;
    private final Rectangle drawer;
    private final TextHandle text;

    private int selected;

    /**
     * Creates a new selection instance. Selection is immutable but items are not.
     * @param bounds - bounding box of the selection.
     * @param font - font of the selection text.
     * @param items - items of the selection.
     */
    public Selection(Rectangle bounds, Font font, Item... items) {
        this.mouse = new Point();
        this.items = items;
        this.bounds = bounds;
        this.drawer = new Rectangle();
        this.text = new TextHandle(font, 0, 0);
        this.text.setMargin(TextHandle.Margin.CENTER);
    }
    
    @Override
    public void drawImpl(Graphics2D g) {
        
        for (int i = 0; i < this.items.length; i++) {
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

    /**
     * Selects an item. Previously selected items are deselected.
     * @param index - index of the item to be selected.
     */
    public void setSelected(int selected) {
        this.selected = selected;

        for (Item i : this.items) {
            i.deselect();
        }

        this.items[this.selected].select();
    }

    /**
     * Sets color but not the element color but singular item color.
     * @param i - index of the item.
     * @param color - new color of the item.
     */
    public void setColor(int i, Color color) {
        this.items[i].setColor(color);
    }

    /**
     * Makes scroll responsive to user input. Needs to be called every frame to feel natural.
     * @param canvas - source of mouse information
     * @return - -1 if selection did not change otherwise the index of selection
     */
    public int changed(Canvas canvas) {
        Point localMouse = canvas.getMousePos(this.mouse);
        for (int i = 0; i < this.items.length; i++) {
            this.setBounds(i);
            if (
                this.drawer.contains(localMouse) && 
                canvas.isMouseJustPressed(MouseEvent.BUTTON1) && 
                    this.selected != i
            ) {
                for (Item item : this.items) {
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

    /**
     * Item groups information to be store din array.
     */
    public static class Item {
        private final String text;
        private final Color idle;
        
        private Color selection;
        private boolean selected;

        /**
         * Creates a new item.
         * @param text - text of the item that will be displayed.
         * @param idle - color of the item when not selected.
         * @param selection - color of the item when selected.
         */
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
            return this.text;
        }

        public Color getColor() {
            return this.selected ? this.selection : this.idle;
        }

        public void setColor(Color color) {
            this.selection = color;
        }
    }
}
