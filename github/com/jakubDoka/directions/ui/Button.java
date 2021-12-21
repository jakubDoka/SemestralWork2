package github.com.jakubDoka.directions.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import github.com.jakubDoka.directions.ui.TextHandle.Margin;

/**
 * Button is basic ui element with some text and solid color bounding box.
 * Bounding box can change color when button is pressed. Button state is controlled
 * trough {@link #pressed()} method.
 */
public class Button extends CanvasObject {
    public static final Font FONT = new Font("Monospaced", Font.BOLD, 30);

    private final Point mouse;
    private final Color idle;
    private final Color pressed;
    private final Color fontColor;
    private final TextHandle text;
    private final Rectangle bounds;

    private boolean isPressed;

    public Button(Color idle, Color pressed, Color fontColor, String text, Rectangle bounds) {
        this.mouse = new Point();
        this.idle = idle;
        this.pressed = pressed;
        this.fontColor = fontColor;
        this.isPressed = false;
        
        this.text = new TextHandle(Button.FONT, (int)bounds.getCenterX(), (int)bounds.getCenterY());
        this.text.setText(text);
        this.text.setMargin(Margin.CENTER);

        this.bounds = bounds;
        this.isPressed = false;
    }

    /**
     * Maintains button state, has to be called each frame for button to be responsive.
     * @param canvas - source of mouse information
     * @return - true if press event happened
     */
    public boolean pressed(Canvas canvas) {
        Point mouse = canvas.getMousePos(this.mouse);

        this.isPressed = this.isPressed || (
            this.bounds.contains(mouse) && 
            canvas.isMousePressed(MouseEvent.BUTTON1)
        );

        if (this.isPressed) {
            if (!canvas.isMousePressed(MouseEvent.BUTTON1)) {                    
                this.isPressed = false;
                return this.bounds.contains(mouse);
            }
            return false;
        }
        
        return false;
    }

    @Override
    public void drawImpl(Graphics2D g) {
        g.setColor(this.isPressed ? this.pressed : this.idle);
        g.fill(this.bounds);
        g.setColor(this.fontColor);
        this.text.drawImpl(g);        
    }
}
