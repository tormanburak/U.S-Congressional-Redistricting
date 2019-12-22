package webapp.model;

import java.io.Serializable;
import java.util.Set;

public class FinalResult implements Serializable {

    private State state;
    private Set<District> districts;
    private Set<Precinct> precincts;

    public FinalResult() {}

    public State getState() { return state; }

    public void setState(State state) {
        this.state = state;
    }

    public Set<District> getDistricts() {
        return districts;
    }

    public void setDistricts(Set<District> districts) {
        this.districts = districts;
    }

    public Set<Precinct> getPrecincts() {
        return precincts;
    }

    public void setPrecincts(Set<Precinct> precincts) {
        this.precincts = precincts;
    }
}
