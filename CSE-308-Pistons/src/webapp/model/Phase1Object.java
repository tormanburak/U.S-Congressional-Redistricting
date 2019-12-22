
package webapp.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Phase1Object implements Serializable {

    private int precinctID;
    private String coordinates;
    private Demographic demographic;
    private ElectionData electionData;
    private Set<Integer> precinctsInCluster = new HashSet<>();
    private boolean isPrecinctDestroyed = false;
    private boolean isPhase1Finished = false;
    private Set<Integer> neighbors = new HashSet<>();
    private boolean isMajMin = false;

    public Set<Integer> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(Set<Integer> neighbors) {
        this.neighbors = neighbors;
    }

    public int getPrecinctID() {
        return precinctID;
    }

    public void setPrecinctID(int precinctID) {
        this.precinctID = precinctID;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }


    public Demographic getDemographic() {
        return demographic;
    }

    public void setDemographic(Demographic demographic) {
        this.demographic = demographic;
    }

    public ElectionData getElectionData() {
        return electionData;
    }

    public void setElectionData(ElectionData electionData) {
        this.electionData = electionData;
    }

    public Set<Integer> getPrecinctsInCluster() {
        return precinctsInCluster;
    }

    public void setPrecinctsInCluster(Set<Integer> precinctsInCluster) {
        this.precinctsInCluster = precinctsInCluster;
    }

    public boolean isPrecinctDestroyed() {
        return isPrecinctDestroyed;
    }

    public void setPrecinctDestroyed(boolean precinctDestroyed) {
        isPrecinctDestroyed = precinctDestroyed;
    }

    public boolean isPhase1Finshed() {
        return isPhase1Finished;
    }

    public void setPhase1Finshed(boolean phase1Finshed) {
        isPhase1Finished = phase1Finshed;
    }

    public boolean isMajMin() {
        return isMajMin;
    }

    public void setMajMin(boolean majMin) {
        isMajMin = majMin;
    }
}
