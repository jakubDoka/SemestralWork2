package github.com.jakubDoka.directions.ui;
public class Vec {
    private double x;
    private double y;

    public static Vec rad(double angle, double mag) {
        return new Vec(Math.cos(angle) * mag, Math.sin(angle) * mag);
    }

    public Vec() {
        this.x = 0d;
        this.y = 0d;
    }

    public Vec(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec(Vec v) {
        this.x = v.x;
        this.y = v.y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public Vec setX(double x) {
        this.x = x;
        return this;
    }

    public Vec setY(double y) {
        this.y = y;
        return this;
    }

    public Vec negX() {
        this.x = -this.x;
        return this;
    }

    public Vec negY() {
        this.y = -this.y;
        return this;
    }

    public Vec neg() {
        this.x = -this.x;
        this.y = -this.y;
        return this;
    }

    public Vec set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vec set(Vec v) {
        this.x = v.x;
        this.y = v.y;
        return this;
    }

    public Vec add(Vec v) {
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    public Vec add(double a) {
        this.x += a;
        this.y += a;
        return this;
    }

    public Vec sub(Vec v) {
        this.x -= v.x;
        this.y -= v.y;
        return this;
    }

    public Vec sub(double a) {
        this.x -= a;
        this.y -= a;
        return this;
    }

    public Vec mul(double d) {
        this.x *= d;
        this.y *= d;
        return this;
    }

    public Vec mul(Vec v) {
        this.x *= v.x;
        this.y *= v.y;
        return this;
    }

    public Vec div(double d) {
        this.x /= d;
        this.y /= d;
        return this;
    }

    public Vec div(Vec v) {
        this.x /= v.x;
        this.y /= v.y;
        return this;
    }

    public Vec min(Vec v) {
        this.x = Math.min(this.x, v.x);
        this.y = Math.min(this.y, v.y);
        return this;
    }

    public Vec max(Vec v) {
        this.x = Math.max(this.x, v.x);
        this.y = Math.max(this.y, v.y);
        return this;
    }

    public double dot(Vec v) {
        return this.x * v.x + this.y * v.y;
    }

    public double cross(Vec v) {
        return this.x * v.y - this.y * v.x;
    }

    public double mag() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public double mag2() {
        return this.x * this.x + this.y * this.y;
    }

    public Vec normal() {
        double temp = this.x;
        this.x = -this.y;
        this.y = temp;
        return this;
    }

    public Vec normalize() {
        double m = this.mag();
        if (m != 0) {
            this.x /= m;
            this.y /= m;
        }
        return this;
    }

    public Vec rotate(double angle) {
        this.x = this.x * Math.cos(angle) - this.y * Math.sin(angle);
        this.y = this.x * Math.sin(angle) + this.y * Math.cos(angle);
        return this;
    }

    public double angleTo(Vec normal) {
        return Math.acos(this.dot(normal) / (this.mag() * normal.mag()));
    }

    public double angle() {
        return Math.atan2(this.y, this.x);
    }
}
