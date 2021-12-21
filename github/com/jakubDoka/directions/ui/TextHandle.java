package github.com.jakubDoka.directions.ui;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class TextHandle extends CanvasObject {
    /**
     * regex that matches the line ending
     */
    private static final Pattern pattern = Pattern.compile("(\n\r|\n|\r)");

    private String text;
    private Font font;
    private int x;
    private int y;
    private Margin margin;
    private int lines;

    public TextHandle(Font font, int x, int y) {
        this.text = "";
        this.font = font;
        this.x = x;
        this.y = y;
        this.margin = Margin.BOTTOM_LEFT;
        this.lines = 1;
    }
    

    public void setMargin(Margin margin) {
        this.margin = margin;
    }

    /**
     * Sets text and determines number of lines
     * @param text
     */
    public void setText(String text) {
        this.text = text;
        this.lines = 1;
        
        Matcher mather = pattern.matcher(text);
        while (mather.find()) {
            this.lines++;
        }
    }
    
    public void setFont(Font font) {
        this.font = font;
    }

    public void setX(int x) {
        this.x = x;
    }
    
    public void setY(int y) {
        this.y = y;
    }

    public String getText() {
        return this.text;
    }

    public Font getFont() {
        return this.font;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    @Override
    public void drawImpl(Graphics2D g) {
        g.setFont(this.font);
        switch (this.margin) {
            case BOTTOM_LEFT:
                g.drawString(this.text, this.x, this.y); 
                break;     
            case CENTER:
                int width = g.getFontMetrics().stringWidth(this.text);
                int height = g.getFontMetrics().getHeight() * this.lines;
                g.drawString(this.text, this.x - width / 2, this.y + height / 2 - g.getFontMetrics().getDescent());
        }

    }

    /**
     * Determines how the position data is used.
     */
    public enum Margin {
        CENTER,
        BOTTOM_LEFT,
    }
}
