package modules;

import java.io.Serializable;
import java.util.Objects;

public class Location implements Serializable {
    private long x;
    private double y;
    private String name; // Строка не может быть пустой, Поле может быть null

    public Location(long x, double y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                ", name='" + name + '\'' +
                '}';
    }
}