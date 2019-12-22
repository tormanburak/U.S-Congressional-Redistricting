package webapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "Demographic")
public class Demographic implements Cloneable {
    private int demID;
    private String stateName;
    private int totalPopulation;
    private String highestRacePop;
    private int whitePop;
    private int africanAmericanPop;
    private int asianPop;
    private Integer hispanicPop;
    private int othersPop;
    private int americanIndianPop;

    @Transient
    private HashMap<Race, Integer> populations;
    @Transient
    private HashMap<Race, Integer> copyOfpopulations;
    @Transient
    private ArrayList<Race> eligibleRaces;
    private Race highestEligibleRace;
    private double highestEligibleRacePercent = 0.0;

    //    public Demographic(int demID, int totalPopulation,int whitePop,int africanAmericanPop, int asianPop, Integer hispanicPop, int othersPop,int americanIndianPop){
//    this.demID = demID;
//    this.totalPopulation = totalPopulation;
//    this.whitePop = whitePop;
//    this.africanAmericanPop = africanAmericanPop;
//    this.asianPop = asianPop;
//    this.hispanicPop = hispanicPop;
//    this.othersPop = othersPop;
//    this.americanIndianPop = americanIndianPop;
//    }
    public Demographic() {

    }

    @Id
    @Column(name = "demID")
    public int getDemID() {
        return demID;
    }

    public void setDemID(int demID) {
        this.demID = demID;
    }

    @Column(name = "stateName")
    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @Column(name = "totalPopulation")
    public int getTotalPopulation() {
        return totalPopulation;
    }

    public void setTotalPopulation(int totalPopulation) {
        this.totalPopulation = totalPopulation;
    }

    @Column(name = "highestRacePop")
    public String getHighestRacePop() {
        return highestRacePop;
    }

    public void setHighestRacePop(String highestRacePop) {
        this.highestRacePop = highestRacePop;
    }

    @Column(name = "whitePop")
    public int getWhitePop() {
        return whitePop;
    }

    public void setWhitePop(int whitePop) {
        this.whitePop = whitePop;
    }

    @Column(name = "africanAmericanPop")
    public int getAfricanAmericanPop() {
        return africanAmericanPop;
    }

    public void setAfricanAmericanPop(int africanAmericanPop) {
        this.africanAmericanPop = africanAmericanPop;
    }

    @Column(name = "asianPop")
    public int getAsianPop() {
        return asianPop;
    }

    public void setAsianPop(int asianPop) {
        this.asianPop = asianPop;
    }

    @Column(name = "hispanicPop")
    public Integer getHispanicPop() {
        return hispanicPop;
    }

    public void setHispanicPop(Integer hispanicPop) {
        this.hispanicPop = hispanicPop;
    }

    @Column(name = "othersPop")
    public int getOthersPop() {
        return othersPop;
    }

    public void setOthersPop(int othersPop) {
        this.othersPop = othersPop;
    }

    @Column(name = "americanIndianPop")
    public int getAmericanIndianPop() {
        return americanIndianPop;
    }

    public void setAmericanIndianPop(int americanIndianPop) {
        this.americanIndianPop = americanIndianPop;
    }

    public HashMap<Race, Integer> getPopulations() {
        return populations;
    }

    public void setPopulations(HashMap<Race, Integer> populations) {
        this.populations = populations;
    }

    public HashMap<Race, Integer> getCopyOfpopulations() {
        return copyOfpopulations;
    }

    public void setCopyOfpopulations(HashMap<Race, Integer> copyOfpopulations) {
        this.copyOfpopulations = copyOfpopulations;
    }

    public void setAllPopulationRaces() {
        populations = new HashMap<>();
        eligibleRaces = new ArrayList<>();
        //populations.clear();
        populations.put(Race.WHITE, whitePop);
        populations.put(Race.AFRICAN_AMERICANS, africanAmericanPop);
        populations.put(Race.ASIAN, asianPop);
        populations.put(Race.AMERICAN_INDIAN, americanIndianPop);
        populations.put(Race.OTHERS, othersPop);
        populations.put(Race.HISPANIC, hispanicPop);
        populations.put(Race.NATIVE_AMERICAN, americanIndianPop);
        if(Algorithm.getInstance().phase0call==0){
            copyOfpopulations = new HashMap<>();
            for (Map.Entry<Race, Integer> entry : populations.entrySet()) {
                try {
                    int copy = entry.getValue();
                    copyOfpopulations.put(entry.getKey(), copy);
                }catch(Exception e){
                    System.out.println("Error in"+ entry.getKey());
                }
            }
        }
    }

    @Transient
    public Race getEligibleRace() {
        setAllPopulationRaces();
        compareAgainstThresholds();
        if (highestEligibleRace != null) {
            setNewHighestEligibleRacePercent(highestEligibleRace);
            return highestEligibleRace;
        }
        if (eligibleRaces.size() == 0) {
            return null;
        }
        highestEligibleRace = eligibleRaces.get(0);
        for (int i = 1; i < eligibleRaces.size(); i++) {
            if (populations.get(highestEligibleRace) < populations.get(eligibleRaces.get(i))) {
                highestEligibleRace = eligibleRaces.get(i);
            }
        }
        setNewHighestEligibleRacePercent(highestEligibleRace);
        return highestEligibleRace;
    }

    private void compareAgainstThresholds() {
        int highestPopulation = populations.get(Race.valueOf(highestRacePop));
        double demoPercent = (Double.valueOf(highestPopulation) / Double.valueOf(totalPopulation)) * 100;
        UserInput input = UserInput.getInstance();
        HashMap<ThresholdType, Double> thresholds = input.getThresholds();
        Double popMax = thresholds.get(ThresholdType.POPULATION_PERCENT_MAX);
        Double popMin = thresholds.get(ThresholdType.POPULATION_PERCENT_MIN);
        if (popMin <= demoPercent && popMax >= demoPercent) {
            this.highestEligibleRace = (Race.valueOf(highestRacePop));
        } else {
            eligibleRaces.clear();
            for (Race r : Race.values()) {
                Integer population = populations.get(r);
                if (population != null) {
                    double popPercent = (Double.valueOf(populations.get(r)) / Double.valueOf(totalPopulation)) * 100;
                    if (popMin <= popPercent && popMax >= popPercent) {
                        eligibleRaces.add(r);
                    }
                }
            }
        }
    }

    public double getHighestEligibleRacePercent() {
        return highestEligibleRacePercent;
    }

    public void setHighestEligibleRacePercent(double highestEligibleRacePercent) {
        this.highestEligibleRacePercent = highestEligibleRacePercent;
    }

    public Race getHighestEligibleRace() {
        return highestEligibleRace;
    }

    public void setHighestEligibleRace(Race highestEligibleRace) {
        this.highestEligibleRace = highestEligibleRace;
    }

    public void setNewHighestEligibleRacePercent(Race highestEligibleRace) {
        int highestPopulation = populations.get(highestEligibleRace);
        this.highestEligibleRacePercent = Double.valueOf((Double.valueOf(highestPopulation) / Double.valueOf(totalPopulation)) * 100);
    }

    public int getPopulationByRace(Race race) {
        for (Race r : Race.values()) {
            if (race == r) {
                return populations.get(r);
            }
        }
        return 0;
    }

    public void setPopulationByRace(Race race, int pop) {
        for (Race r : Race.values()) {
            if (race == r) {
                populations.put(r, pop);
                if (r == Race.WHITE) {
                    setWhitePop(pop);
                }
                if (r == Race.AFRICAN_AMERICANS) {
                    setAfricanAmericanPop(pop);
                }
                if (r == Race.ASIAN) {
                    setAsianPop(pop);
                }
                try {
                    if (r == Race.NATIVE_AMERICAN) {
                        setAmericanIndianPop(pop);
                    }
                } catch (Exception exception) {
                }
                try {
                    if (r == Race.HISPANIC) {
                        setHispanicPop(pop);
                    }
                } catch (Exception exception) {
                }
                try {
                    if (r == Race.AMERICAN_INDIAN) {
                        setAmericanIndianPop(pop);
                    }
                } catch (Exception exception) {
                }

            }
        }
    }

    protected Object clone() throws CloneNotSupportedException {
        Demographic ed = (Demographic) super.clone();
        return ed;
    }
}
