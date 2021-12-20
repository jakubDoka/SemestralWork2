package github.com.jakubDoka.directions.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import github.com.jakubDoka.directions.ui.TextHandle.Margin;

public class Button extends CanvasObject {
    private static final Font font = new Font("Arial", Font.PLAIN, 20);

    private final Color idle;
    private final Color pressed;
    private final TextHandle text;
    private final Rectangle bounds;

    private boolean isPressed;

    public Button(Color idle, Color pressed, String text, Rectangle bounds) {
        this.idle = idle;
        this.pressed = pressed;
        this.isPressed = false;
        
        this.text = new TextHandle(Button.font, (int)bounds.getCenterX(), (int)bounds.getCenterY());
        this.text.setText(text);
        this.text.setMargin(Margin.BOTTOM_LEFT);

        this.bounds = bounds;
        this.isPressed = false;
    }

    public boolean pressed(Canvas canvas) {
        if (this.isPressed) {
            if (canvas.isMouseJustReleased(MouseEvent.BUTTON1)) {                    
                this.isPressed = false;
                return this.bounds.contains(canvas.getMousePosition());
            }
            return false;
        }
       
        this.isPressed =  
            this.bounds.contains(canvas.getMousePosition()) && 
            canvas.isMouseJustPressed(MouseEvent.BUTTON1);
        
        return false;
    }

    @Override
    public void drawImpl(Graphics2D g) {
        g.setColor(this.isPressed ? this.pressed : this.idle);
        g.fill(this.bounds);
        this.text.drawImpl(g);        
    }
}
