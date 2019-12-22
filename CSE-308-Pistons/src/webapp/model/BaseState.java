package webapp.model;

import java.util.*;

public class BaseState {

    private String name;
    private Map<String, BaseDistrict> districts;
    private Map<String, BasePrecinct> precincts;
    private int population;


    public String getName() {
        return name;
    }

    public Map<String, BasePrecinct> getPrecincts() {
        return precincts;
    }

    public Set<BaseDistrict> getDistricts() {
        return districts == null ? new HashSet<>() : (Set<BaseDistrict>) districts.values();
    }

    public int getPopulation() {
        return population;
    }
}