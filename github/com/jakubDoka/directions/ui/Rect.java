package github.com.jakubDoka.directions.ui;
import java.awt.Rectangle;

public class Rect {
    private Vec min;
    private Vec max;


    public Rect() {
        this.min = new Vec();
        this.max = new Vec();
    }

    public Rect(double minX, double minY, double maxX, double maxY) {
        this.min = new Vec(minX, minY);
        this.max = new Vec(maxX, maxY);
    }

    public Rect(Vec min, Vec max) {
        this.min = new Vec(min);
        this.max = new Vec(max);
    }

    public Rect(Rect rect) {
        this.min = new Vec(rect.min);
        this.max = new Vec(rect.max);
    }

    public Rect set(double minX, double minY, double maxX, double maxY) {
        this.min.set(minX, minY);
        this.max.set(maxX, maxY);
        return this;
    }

    public Rect set(Rect r) {
        this.min.set(r.min);
        this.max.set(r.max);
        return this;
    }

    public Rect set(Rectangle r) {
        this.min.set(r.x, r.y);
        this.max.set(r.x + r.width, r.y + r.height);
        return this;
    }

    public Rect set(Vec min, Vec max) {
        this.min.set(min);
        this.max.set(max);
        return this;
    }

    public Rect setPos(Vec pos) {
        this.setX(pos.getX());
        this.setY(pos.getY());
        return this;
    }

    public void setX(double x) {
        this.max.setX(x + this.getWidth());
        this.min.setX(x);
    }

    public void setY(double y) {
        this.max.setY(y + this.getHeight());
        this.min.setY(y);
    }

    public double getX() {
        return this.min.getX();
    }

    public double getY() {
        return this.min.getY();
    }

    public double getMinX() {
        return this.min.getX();
    }

    public double getMinY() {
        return this.min.getX();
    }
    
    public double getMaxX() {
        return this.max.getX();
    }

    public double getMaxY() {
        return this.max.getY();
    }

    public Rect setSize(Vec size) {
        this.setSize(size.getX(), size.getY());
        return this;
    }

    public Rect setSize(double width, double height) {
        this.setWidth(width);
        this.setHeight(height);
        return this;
    }

    public Vec getSize() {
        return this.getSize(new Vec());
    }

    public Vec getSize(Vec temp) {
        temp.set(this.getWidth(), this.getHeight());
        return temp;
    }

    public void setWidth(double width) {
        this.max.setX(this.min.getX() + width);
    }

    public void setHeight(double height) {
        this.max.setY(this.min.getY() + height);
    }

    public double getWidth() {
        return this.max.getX() - this.min.getX();
    }

    public double getHeight() {
        return this.max.getY() - this.min.getY();
    }

    public double getArea() {
        return this.getWidth() * this.getHeight();
    }

    public Rect setCenter(Vec center) {
        this.setCenter(center.getX(), center.getY());
        return this;
    }

    public Rect setCenter(double x, double y) {
        double halfWidth = this.getWidth() * 0.5d;
        double halfHeight = this.getHeight() * 0.5d;
        this.min.set(x - halfWidth, y - halfHeight);
        this.max.set(x + halfWidth, y + halfHeight);
        return this;
    }

    public Vec getCenter() {
        return this.getCenter(new Vec());
    }

    public Vec getCenter(Vec temp) {
        temp.set(this.getCenterX(), this.getCenterY());
        return temp;
    }

    public double getCenterX() {
        return (this.min.getX() + this.max.getX()) * 0.5d;
    }

    public double getCenterY() {
        return (this.min.getY() + this.max.getY()) * 0.5d;
    }
    
    public Rect add(Vec v) {
        this.min.add(v);
        this.max.add(v);
        return this;
    }

    public Rect sub(Vec v) {
        this.min.sub(v);
        this.max.sub(v);
        return this;
    }

    public boolean intersects(Rect r) {
        return 
            !(
                this.min.getX() > r.max.getX() ||
                this.max.getX() < r.min.getX() ||
                this.min.getY() > r.max.getY() ||
                this.max.getY() < r.min.getY()
            );
    }

    public boolean contains(Vec v) {
        return 
            v.getX() >= this.min.getX() &&
            v.getX() <= this.max.getX() &&
            v.getY() >= this.min.getY() &&
            v.getY() <= this.max.getY();
    }

    public Rect intersect(Rect o) {
        return this.intersect(o, new Rect());
    }

    public Rect intersect(Rect o, Rect t) {
        t.min.set(o.min).max(this.min);
        t.max.set(o.max).min(this.max);
        if (t.getWidth() <= 0 || t.getHeight() <= 0) {
            return null;
        }
        return t;
    }

    public Rectangle toRectangle() {
        return this.toRectangle(new Rectangle());
    }

    public Rectangle toRectangle(Rectangle t) {
        t.x = (int)this.min.getX();
        t.y = (int)this.min.getY();
        t.width = (int)this.getWidth();
        t.height = (int)this.getHeight();
        return t;
    }
}