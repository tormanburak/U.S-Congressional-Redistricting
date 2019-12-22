package webapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class NeighborDistrict implements Serializable {
    private int districtID;
    private int neighborID;
    private String stateName;

    @Id
    @Column(name = "districtID")
    public int getDistrictID() {
        return districtID;
    }


    public void setDistrictID(int districtID) {
        this.districtID = districtID;
    }
    @Id
    @Column(name = "neighborID")
    public int getNeighborID() {
        return neighborID;
    }

    public void setNeighborID(int neighborID) {
        this.neighborID = neighborID;
    }

    @Column(name = "stateName")
    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
