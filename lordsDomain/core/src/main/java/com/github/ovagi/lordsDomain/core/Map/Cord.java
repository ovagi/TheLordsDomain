package com.github.ovagi.lordsDomain.core.Map;

import playn.core.Event;
import pythagoras.f.XY;

import java.awt.geom.Point2D;

public class Cord implements XY, Comparable {
    public final int x, y;

    public Cord(int x, int y) {
        assert x >= 0 && y >= 0;
        this.x = x;
        this.y = y;
    }

    public static double getDistance(Cord cord1, Cord cord2) {
        return Point2D.distance(cord1.x, cord1.y, cord2.x, cord2.y);

    }

    public boolean equals(Cord other) {
        return other.x == x && other.y == y;
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof Cord) && equals((Cord) other);
    }

    @Override
    public int hashCode() {
        return x ^ y;
    }

    @Override
    public String toString() {
        return "+" + x + "+" + y;
    }

    @Override
    public float x() {
        return x;
    }

    @Override
    public float y() {
        return y;
    }

    @Override
    public int compareTo(Object o) {
        if(this.equals(o))
            return 0;

        if(o instanceof Cord) {
            Cord other = (Cord) o;
            return other.x - this.x + other.y - this.y;
        } else {
            return -1;
        }
    }
}
