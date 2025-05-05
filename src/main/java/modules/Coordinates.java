package modules;

import java.io.Serializable;
import java.util.Objects;

public class Coordinates implements Serializable, Comparable<Coordinates> {
    private Double x; // Поле не может быть null
    private long y; // Значение поля должно быть больше -177

    public Coordinates(Double x, long y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public void setY(long y) {
        this.y = y;
    }

    @Override
    public int compareTo(Coordinates other) {
        int xComparison = Double.compare(this.x, other.x);
        if (xComparison != 0) {
            return xComparison;
        }
        return Long.compare(this.y, other.y);
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}