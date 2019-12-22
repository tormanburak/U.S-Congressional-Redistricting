package webapp.model;

import java.util.Map;
import java.util.Set;

public class Phase2Result {
    private Set<Phase2Object> set;
    private Map<Integer,Double> oldDistricts;
    private Map<Integer,Double> newDistricts;

    public Set<Phase2Object> getSet() {
        return set;
    }

    public void setSet(Set<Phase2Object> set) {
        this.set = set;
    }

    public Map<Integer, Double> getOldDistricts() {
        return oldDistricts;
    }

    public void setOldDistricts(Map<Integer, Double> oldDistrict) {
        this.oldDistricts = oldDistrict;
    }

    public Map<Integer, Double> getNewDistricts() {
        return newDistricts;
    }

    public void setNewDistricts(Map<Integer, Double> newDistricts) {
        this.newDistricts = newDistricts;
    }
}
