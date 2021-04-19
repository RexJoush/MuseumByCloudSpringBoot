package com.nwu.entity.cluster.graph;

/**
 * @author Rex Joush
 * @time 2021.04.16
 */

public class Coordinate {
    double x;
    double y;
    double spanLow;
    double spanHigh;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getSpanLow() {
        return spanLow;
    }

    public void setSpanLow(double spanLow) {
        this.spanLow = spanLow;
    }

    public double getSpanHigh() {
        return spanHigh;
    }

    public void setSpanHigh(double spanHigh) {
        this.spanHigh = spanHigh;
    }

    public Coordinate() {
    }

    public Coordinate(double x, double y, double spanLow, double spanHigh) {
        this.x = x;
        this.y = y;
        this.spanLow = spanLow;
        this.spanHigh = spanHigh;
    }
}
