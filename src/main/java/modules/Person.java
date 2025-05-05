package modules;

import java.io.Serializable;
import java.util.Objects;

public class Person implements Serializable, Comparable<Person> {
    private String name; // Поле не может быть null, Строка не может быть пустой
    private String passportID; // Поле не может быть null
    private Location location; // Поле может быть null

    public Person(String name, String passportID, Location location) {
        this.name = name;
        this.passportID = passportID;
        this.location = location;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassportID() {
        return passportID;
    }

    public void setPassportID(String passportID) {
        this.passportID = passportID;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public int compareTo(Person other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", passportID='" + passportID + '\'' +
                ", location=" + location +
                '}';
    }
}