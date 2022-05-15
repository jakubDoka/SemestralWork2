package com.github.jakubDoka.directions.ui;

/**
 * Vec is a simple vector class that is used to store
 * coordinates.
 */
public class Vec {
    private double x;
    private double y;

    /**
     * Creates a new Vec instance. Zero initialized.
     */
    public Vec() {
        this.x = 0d;
        this.y = 0d;
    }

    /**
     * Creates a new Vec instance.
     * @param x - x coordinate.
     * @param y - y coordinate.
     */
    public Vec(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
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

    /**
     * Adds a vector to this vector.
     * @param v - vector to add.
     * @return - this vector.
     */
    public Vec add(Vec v) {
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    /**
     * Subtracts a vector from this vector.
     * @param v - vector to subtract.
     * @return - this vector.
     */
    public Vec sub(Vec v) {
        this.x -= v.x;
        this.y -= v.y;
        return this;
    }

    /**
     * Sets this to be component wise minimum of both vectors
     */
    public Vec min(Vec v) {
        this.x = Math.min(this.x, v.x);
        this.y = Math.min(this.y, v.y);
        return this;
    }
}
