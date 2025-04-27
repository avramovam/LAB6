import java.io.Serializable;
import java.util.Objects;

public class Person implements Serializable {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return name.equals(person.name) && passportID.equals(person.passportID) && Objects.equals(location, person.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, passportID, location);
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