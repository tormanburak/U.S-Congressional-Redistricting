package webapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class NeighborPrecinct implements Serializable {
    private int precinctID;
    private int neighborID;
    private int joinability;
    private String stateName;




    @Id
    @Column(name = "precinctID")
    public int getPrecinctID() {
        return precinctID;
    }

    public void setPrecinctID(int precinctID) {
        this.precinctID = precinctID;
    }


    @Id
    @Column(name = "neighborID")
    public int getNeighborID() {
        return neighborID;
    }

    public void setNeighborID(int neighborID) {
        this.neighborID = neighborID;
    }
    @Column(name = "joinability")
    public int getJoinability() {
        return joinability;
    }

    public void setJoinability(int joinability) {
        this.joinability = joinability;
    }

    @Column(name = "stateName")
    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
