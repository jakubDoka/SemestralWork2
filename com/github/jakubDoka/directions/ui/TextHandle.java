package com.github.jakubDoka.directions.ui;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.regex.Pattern;

/**
 * TextHandle offers object oriented interface to drawing text.
 * It handles the text margin.
 */
public class TextHandle extends CanvasObject {
    /**
     * regex that matches the line ending
     */
    private static final Pattern NLINE_PATTERN = Pattern.compile("(\n\r|\n|\r)");

    private String text;
    private Font font;
    private int x;
    private int y;
    private Margin margin;

    /**
     * Creates a new TextHandle instance.
     * 
     * @param font - font of the text.
     * @param x - x coordinate of the text.
     * @param y - y coordinate of the text.
     */
    public TextHandle(Font font, int x, int y) {
        this.text = "";
        this.font = font;
        this.x = x;
        this.y = y;
        this.margin = Margin.BOTTOM_LEFT;
    }
    

    public void setMargin(Margin margin) {
        this.margin = margin;
    }

    public void setText(String text) {
        this.text = text;
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
        int localX = 0;
        int localY = 0;
        switch (this.margin) {
            case BOTTOM_LEFT:
                localX = this.x;
                localY = this.y;
                break;     
            case CENTER:
                int width = 0;
                int lines = 0;
                for (String line : NLINE_PATTERN.split(this.text)) {
                    width = Math.max(width, g.getFontMetrics().stringWidth(line));
                    lines++;
                }
                int height = g.getFontMetrics().getHeight() * lines;
                localX = this.x - width / 2;
                localY = this.y + height / 2 - g.getFontMetrics().getDescent();
        }

        int currentLine = 0;
        for (String line : NLINE_PATTERN.split(this.text)) {
            g.drawString(line, localX, localY + currentLine * g.getFontMetrics().getHeight());
            currentLine++;
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
