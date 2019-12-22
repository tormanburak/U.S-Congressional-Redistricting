package webapp.model;



import java.net.Inet4Address;
import java.util.*;

public class BaseDistrict {
    private String ID;
    private BaseState state;
    private int population;
    private int internalEdges = 0;
    private int externalEdges = 0;
    private Map<Party, Integer> votes;

    private HashMap<String, BasePrecinct> precincts;

    public String getID() {
        return ID;
    }

    public Map<Party, Integer> getVotes() {
        return votes;
    }

    public BaseState getState() {
        return state;
    }

    public int getPopulation() {
        return population;
    }

    public int getInternalEdges() {
        return internalEdges;
    }

    public int getExternalEdges() {
        return externalEdges;
    }

    public Set<BasePrecinct> getPrecincts() {
        return precincts == null ? new HashSet<>() : (Set<BasePrecinct>) precincts.values();
    }
}