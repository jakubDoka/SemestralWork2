package github.com.jakubDoka.directions.ui;

import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.HashSet;

public class Canvas extends JFrame implements KeyListener, MouseInputListener {
    private final java.awt.Canvas canvas;
    private Color background;

    private final Rect bounds;
    private final Rectangle tBounds;
    private final Point mouse;

    
    private final ArrayList<CanvasObject> objects;
    
    private final HashSet<Integer> pressed;
    private final HashSet<Integer> justPressed;
    private final HashSet<Integer> justReleased;
    
    private final HashSet<Integer> mousePressed;
    private final HashSet<Integer> mouseJustPressed;
    private final HashSet<Integer> mouseJustReleased;

    public Canvas(String title, int width, int height) {
        super(title);
        
        this.canvas = new java.awt.Canvas();
        this.canvas.setPreferredSize(new Dimension(width, height));
        this.canvas.setFocusable(true);
        this.canvas.addKeyListener(this);
        this.canvas.addMouseListener(this);
        this.canvas.addMouseMotionListener(this);

        this.add(this.canvas);
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.background = Color.WHITE;
        
        this.objects = new ArrayList<CanvasObject>();
        
        this.pressed = new HashSet<>();
        this.justPressed = new HashSet<>();
        this.justReleased = new HashSet<>();
        
        this.mousePressed = new HashSet<>();
        this.mouseJustPressed = new HashSet<>();
        this.mouseJustReleased = new HashSet<>();

        this.bounds = new Rect();
        this.tBounds = new Rectangle();
        this.mouse = new Point();
    }

    public void update() {
        this.updateInput();
        this.redraw();
    }

    public void updateInput() {
        this.justPressed.clear();
        this.justReleased.clear();
        this.mouseJustPressed.clear();
        this.mouseJustReleased.clear();
    }

    public void addObject(CanvasObject canvasObject) {
        this.objects.add(canvasObject);
    }

    public void removeObject(CanvasObject canvasObject) {
        this.objects.remove(canvasObject);
    }

    public void redraw() {
        BufferStrategy bs = this.canvas.getBufferStrategy();
        
        if (bs == null) {
            this.canvas.createBufferStrategy(3);
            return;
        }

        Graphics2D g = (Graphics2D)bs.getDrawGraphics();

        this.erase(g);

        Rect b = this.getRect();
        for (CanvasObject c : this.objects) {
            c.draw(g, b);
        }

        g.dispose();
        bs.show();
    }

    public void setBackground(Color background) {
        this.background = background;
    }
       
    public void erase(Graphics2D g) {
        g.setColor(this.background);
        g.fill(this.getTBounds());
    }

    public Rect getRect() {
        return this.bounds.set(this.getTBounds());
    }
    
    public Rectangle getTBounds() {
        return this.canvas.getBounds(this.tBounds);
    }

    public boolean isPressed(int keyCode) {
        return this.pressed.contains(keyCode);
    }

    public boolean isJustPressed(int keyCode) {
        return this.justPressed.contains(keyCode);
    }
    
    public boolean isJustReleased(int keyCode) {
        return this.justReleased.contains(keyCode);
    }

    public boolean isMouseJustReleased(int button) {
        return this.mouseJustReleased.contains(button);
    }

    public boolean isMouseJustPressed(int button) {
        return this.mouseJustPressed.contains(button);
    }
    
    public boolean isMousePressed(int button1) {
        return this.mousePressed.contains(button1);
    }

    public Point getMousePos(Point target) {
        target.setLocation(this.mouse);
        return target;
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        this.justPressed.add(e.getKeyCode());
        this.pressed.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        this.pressed.remove(e.getKeyCode());
        this.justReleased.add(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void mousePressed(MouseEvent e) {
        this.mouseJustPressed.add(e.getButton());
        this.mousePressed.add(e.getButton());
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        this.mousePressed.remove(e.getButton());
        this.mouseJustReleased.add(e.getButton());
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
        this.mouse.setLocation(e.getPoint());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.mouse.setLocation(e.getPoint());
    }

}
