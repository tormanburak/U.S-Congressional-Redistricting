package webapp.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Coordinates implements Serializable {

    private Set<String> coordinates;

    public Coordinates() {}

    public Set<String> getCoordinates() { return this.coordinates; }

    public void setCoordinatesSet(Set<String> newCoordinates) { this.coordinates = newCoordinates; }

    public void addToSet(String coordinate){
        if(coordinates == null){
            coordinates = new HashSet<>();
        }
        coordinates.add(coordinate);
    }
}
