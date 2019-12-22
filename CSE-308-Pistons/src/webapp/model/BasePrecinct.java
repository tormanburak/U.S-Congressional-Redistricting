package webapp.model;

import java.util.*;

public class BasePrecinct {
    private String ID;
    private int population;
    private Map<Party, Integer> votes;

    public int getPopulation() {
        return population;
    }

    public Map<Party, Integer> getVotes() {
        return votes;
    }

    public void setVotes(Map<Party, Integer> votes) {
        this.votes = votes;
    }

    public String getID() {
        return ID;
    }
}