package webapp.model;


import com.google.gson.Gson;
import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
public class State {


    private String stateName;
    private int demographicID;
    private int electionDataID;
    private Set<District> districts;
    private Set<Precinct> precincts;
    private Set<VotingBlocResultObject> votingBlocResultObjects;
    private Coordinates districtCoordinates;
    private Demographic demographic;
    private ElectionData electionData;
    private HashMap<Precinct, Race> eligiblePrecincts;
    private Set<Precinct> phase0EligiblePrecincts;
    public int precinctCounter = 0;
    public int totalPrecinctsPop = 0;
    public int destroyedCounter = 0;
    public int totalPrecincts = 0;
    public double efficiency_gap = 0;
    public double efficiency_gapDistrict = 0;
    public HashMap<Integer, Demographic> copyOfPrecinctsDemo;
    public HashMap<Integer, ElectionData> copyOfPrecinctsElectionData;
    public int districtCtr;
    private Set<Precinct> newDistricts;

    public State() {
    }

    public void addToDistrictSet(District district) {
        districts.add(district);
    }

    @Id
    @Column(name = "stateName")
    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @Column(name = "demographicID")
    public int getDemographicID() {
        return demographicID;
    }

    public void setDemographicID(int demographicID) {
        this.demographicID = demographicID;
    }

    @Column(name = "electionDataID")
    public int getElectionDataID() {
        return electionDataID;
    }

    public void setElectionDataID(int electionDataID) {
        this.electionDataID = electionDataID;
    }

    @Transient
    public Coordinates getDistrictCoordinates() {
        return districtCoordinates;
    }

    public void setDistrictCoordinates(Coordinates districtCoordinates) {
        this.districtCoordinates = districtCoordinates;
    }

    @Transient
    public Demographic getDemographic() {
        return demographic;
    }

    public void setDemographic(Demographic demographic) {
        this.demographic = demographic;
    }

    @Transient
    public ElectionData getElectionData() {
        return electionData;
    }

    public void setElectionData(ElectionData electionData) {
        this.electionData = electionData;
    }

    public void setPrecincts(Set<Precinct> precincts) {
        this.precincts = precincts;
    }

    @Transient
    public Set<Precinct> getPrecincts() {
        return precincts;
    }

    public void setDistricts(Set<District> districts) {
        this.districts = districts;
    }

    @Transient
    public Set<District> getDistricts(){return districts;}

    public void identifyPrecinctsDemoBloc() {
        eligiblePrecincts = new HashMap<>();
        for (Precinct p : precincts) {
            Race eligibleRace = p.retrieveEligibleRace();
            if (eligibleRace != null) {
                eligiblePrecincts.put(p, eligibleRace);
            }
        }
        System.out.println("Done Use case 23");
    }

    public int totalPrecinctsPop() {

        for (Precinct p : precincts) {
            totalPrecinctsPop = totalPrecinctsPop + p.getDemographic().getTotalPopulation();
        }
        return totalPrecinctsPop;
    }

    public void setTotalPrecincts() {
        for (Precinct p : precincts) {
            totalPrecincts++;
        }
    }

    @Transient
    public Set<VotingBlocResultObject> getDemoVoteBlocPrecincts() {
        Iterator it = eligiblePrecincts.entrySet().iterator();
        votingBlocResultObjects = new HashSet<>();
        phase0EligiblePrecincts = new HashSet<>();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            VotingBlocResultObject votingBlock = ((Precinct) pair.getKey()).getEligibleVotingBlocResultObject();
            if (votingBlock != null) {
                votingBlocResultObjects.add(votingBlock);
                phase0EligiblePrecincts.add((Precinct) pair.getKey());
            }
        }
        System.out.println("Done Use case 24");
        return votingBlocResultObjects;
    }

    @Transient
    public Set<Precinct> getNewDistricts() {
        return newDistricts;
    }

    public void setNewDistricts(Set<Precinct> newDistricts) {
        this.newDistricts = newDistricts;
    }

    public void finalIteration() {
        for (Precinct precinct : precincts) {
            if (totalPrecincts - destroyedCounter == UserInput.getInstance().getDesiredDistricts()) {
                return;
            }
            if (!precinct.hasClusterPaired() && !precinct.isPrecinctDestroyed()) {
                precinct.calculateFinalBestNeighbor();
            }
        }
    }

    public void findMajMinPairs() {
        for (Precinct precinct : precincts) {
            if (!precinct.hasClusterPaired() && !precinct.isPrecinctDestroyed()) {
                precinct.findMajMinPrecincts();
            }
        }
    }
    public void resetForNextIteration() {
        for (Precinct p : precincts) {
            if (!p.isPrecinctDestroyed()) {
                p.setHasClusterPaired(false);
                p.setBestJoinablePrecinct(null);
                p.setCurrentBestJoinability(Integer.MIN_VALUE);
                p.setBestNonMajMinJoinability(Integer.MIN_VALUE);
                p.setBestFinalIterationCalc(Integer.MIN_VALUE);
            }
        }
    }

    public void findNonMajMinPairs() {
        for (Precinct precinct : precincts) {
            if (!precinct.hasClusterPaired() && !precinct.isPrecinctDestroyed()) {
                precinct.findNonMajMinPrecincts();
            }
        }
    }

    public void joinCandidatePairs() {
        for (Precinct precinct : precincts) {
            if (totalPrecincts - destroyedCounter == UserInput.getInstance().getDesiredDistricts()) {
                return;
            }
            if (precinct.getBestJoinablePrecinct() != null) {
                precinct.combineBestNeighbor();
            }
        }
    }

    public void efficiency_gapOne() {
        int iv_g = 0;
        int iv_d = 0;
        int tv = 0;
        int gv = 0;
        int dv = 0;
        UserInput userInput = UserInput.getInstance();
        String votingType = userInput.getVotingType();
        int votingYear = userInput.getVotingYear();
        for (Precinct sd : precincts) {
            if (votingType.equals("Presidential") && votingYear == 2016) {
                gv = sd.getElectionData().getPresRep2016();
                dv = sd.getElectionData().getPresDem2016();
            } else if (votingType.equals("Congressional") && votingYear == 2016) {
                gv = sd.getElectionData().getCongressionalRep2016();
                dv = sd.getElectionData().getCongressionalDem2016();
            } else if (votingType.equals("Congressional") && votingYear == 2018) {
                gv = sd.getElectionData().getCongressionalRep2018();
                dv = sd.getElectionData().getCongressionalDem2018();
            }
            if (gv > dv) {
                iv_d += dv;
                iv_g += (gv - dv);
            } else if (dv > gv) {
                iv_g += gv;
                iv_d += (dv - gv);
            }
            tv += gv;
            tv += dv;
        }
        efficiency_gap = 1.0 - ((Math.abs(iv_g - iv_d) * 1.0) / tv);
    }

    public void efficiency_gapDistrict() {
        int iv_g = 0;
        int iv_d = 0;
        int tv = 0;
        int gv = 0;
        int dv = 0;
        UserInput userInput = UserInput.getInstance();
        String votingType = userInput.getVotingType();
        int votingYear = userInput.getVotingYear();
        for (District sd : districts) {
            if (votingType.equals("Presidential") && votingYear == 2016) {
                gv = sd.getElectionData().getPresRep2016();
                dv = sd.getElectionData().getPresDem2016();
            } else if (votingType.equals("Congressional") && votingYear == 2016) {
                gv = sd.getElectionData().getCongressionalRep2016();
                dv = sd.getElectionData().getCongressionalDem2016();
            } else if (votingType.equals("Congressional") && votingYear == 2018) {
                gv = sd.getElectionData().getCongressionalRep2018();
                dv = sd.getElectionData().getCongressionalDem2018();
            }
            if (gv > dv) {
                iv_d += dv;
                iv_g += (gv - dv);
            } else if (dv > gv) {
                iv_g += gv;
                iv_d += (dv - gv);
            }
            tv += gv;
            tv += dv;
        }
        efficiency_gapDistrict = 1.0 - ((Math.abs(iv_g - iv_d) * 1.0) / tv);
    }

    public void loadNeighbors() {

        EntityManager em = HibernateUtil.getEntityManager();
        Set<NeighborPrecinct> neighborPrecincts = new HashSet(em.createQuery("SELECT x FROM NeighborPrecinct x WHERE x.stateName=:name", NeighborPrecinct.class)
                .setParameter("name", this.stateName)
                .getResultList());
        // store in haspmap
        Map<Integer, Precinct> map = new HashMap<Integer, Precinct>();
        //fill in map
        for (Precinct p : precincts) {
            map.put(p.getPrecinctID(), p);
        }
        for (NeighborPrecinct precinct : neighborPrecincts) {
            Precinct p = map.get(precinct.getPrecinctID());
            Precinct neighbor = map.get(precinct.getNeighborID());
            p.getNeighbors().add(neighbor);
            p.getCountyJoinability().put(precinct.getNeighborID(), precinct.getJoinability());
        }
    }


    public String returnClusters() {
        Set<Phase1Object> returnPrecincts = new HashSet<>();
        precinctCounter = 0;
        districtCtr = 0;
        UserInput ui = UserInput.getInstance();
        for (Precinct p : precincts) {
            if (p.isPrecinctDestroyed()) {
                continue;
            }
            if (p.getPrecinctsInCluster().size() == 0) {
                districtCtr++;
            }
            Phase1Object object = new Phase1Object();
            object.setPrecinctID(p.getPrecinctID());
            //object.setCoordinates(p.getCoordinates());
            //object.setDistrictID(p.getDistrictID());
            object.setDemographic(p.getDemographic());
            object.setElectionData(p.getElectionData());
            object.setPrecinctDestroyed(p.isPrecinctDestroyed());
            object.setPrecinctsInCluster(p.getPrecinctsInCluster());
            for (Precinct neigh : p.getNeighbors()) {
                object.getNeighbors().add(neigh.getPrecinctID());
            }

            returnPrecincts.add(object);
            precinctCounter++;
            if (precinctCounter != ui.getDesiredDistricts()) {
                object.setPhase1Finshed(false);
            } else {
                object.setPhase1Finshed(true);
            }
        }
        if (precinctCounter== UserInput.getInstance().getDesiredDistricts()&& !Algorithm.getInstance().getCurrentStateName().equals("TX")) {
            Map<Integer, Phase1Object> sortedPhase1 = new TreeMap<Integer, Phase1Object>();
            for (Phase1Object ob : returnPrecincts) {
                int totalMinority = 0;
                for (int x = 0; x < (UserInput.getInstance().getMinorityRace().size()); x++) {
                    totalMinority = totalMinority + ob.getDemographic().getPopulationByRace(UserInput.getInstance().getMinorityRace().get(x));
                }
                sortedPhase1.put(totalMinority, ob);
            }
            for (int x = 0; x < UserInput.getInstance().getDesiredMajMinDistricts(); x++) {
                ((TreeMap<Integer, Phase1Object>) sortedPhase1).lastEntry().getValue().setMajMin(true);
                ((TreeMap<Integer, Phase1Object>) sortedPhase1).pollLastEntry();
            }
        }
        Gson g = new Gson();
        return g.toJson(returnPrecincts);
        //+" CurrentDistricts "+ String.valueOf(precinctCounter);
    }

    public void resetEverything() throws CloneNotSupportedException {
        precinctCounter = 0;
        //totalPrecinctsPop = 0;
        destroyedCounter = 0;
        totalPrecincts = 0;
        for (Precinct p : precincts) {
            p.setHasClusterPaired(false);
            p.setDemographic((Demographic) copyOfPrecinctsDemo.get(p.getPrecinctID()).clone());
            p.setElectionData((ElectionData) copyOfPrecinctsElectionData.get(p.getPrecinctID()).clone());
            p.getDemographic().setPopulations((HashMap<Race, Integer>) p.getDemographic().getCopyOfpopulations().clone());
            p.setBestFinalIterationCalc(Integer.MIN_VALUE);
            p.setBestNonMajMinJoinability(Integer.MIN_VALUE);
            p.setCurrentBestJoinability(Integer.MIN_VALUE);
            p.getNeighbors().clear();
            p.setBestJoinablePrecinct(null);
            p.setPrecinctDestroyed(false);
            p.getPrecinctsInCluster().clear();
        }
    }

    public void saveCopy() throws CloneNotSupportedException {
        copyOfPrecinctsElectionData = new HashMap<>();
        copyOfPrecinctsDemo = new HashMap<>();
        for (Precinct p : precincts) {
            copyOfPrecinctsElectionData.put(p.getPrecinctID(), (ElectionData) p.getElectionData().clone());
            copyOfPrecinctsDemo.put(p.getPrecinctID(), (Demographic) p.getDemographic().clone());
        }
    }

    public void resetPhaseZero() {
        votingBlocResultObjects.clear();
        phase0EligiblePrecincts.clear();
        eligiblePrecincts.clear();
    }

    public void calculateObjFunction() {
        double politicalFairness = 0;
        double compactness = 0;
        double equalPop = 0;
        efficiency_gapOne();
        for (Precinct p : newDistricts) {
            compactness = p.calculateCompactness();
            politicalFairness = (p.rateEfficiencyGap(p) + efficiency_gap + p.gerryDemo(p) + p.gerryRep(p)) / 4.0;
            equalPop = p.calculateEquPopulationDistrict(p);
            double total = Math.abs((compactness + equalPop + politicalFairness)) / 3.0;
            total = (total > 1) ? 1 : total;
            total = Math.round(total * 100.0) / 100.0;
            p.setObjectiveFunction(total);
        }
    }

    public void calculateNewObjectiveFunction(Precinct pickedPrecinct) {
        double politicalFairness = 0;
        double compactness = 0;
        double equalPop = 0;
        efficiency_gapOne();
        Algorithm al = Algorithm.getInstance();
        if(pickedPrecinct.getNewDistrict() != null) {
            for (Precinct p : pickedPrecinct.getNewDistrict().getNeighbors()) {
                if (!p.isPrecinctDestroyed() && this.getNewDistricts().contains(p)) {
                    //for each neighbor cluster add the picked precinct and calc new obj function
                    Phase2Object p2object = new Phase2Object();
                    Precinct temp = combineTwoPrecinct(p, pickedPrecinct);
                    compactness = temp.calculateCompactness();
                    politicalFairness = (temp.rateEfficiencyGap(temp) + efficiency_gap + temp.gerryDemo(temp) + temp.gerryRep(temp)) / 4.0;
                    equalPop = temp.calculateEquPopulationDistrict(temp);
                    double total = Math.abs((compactness + equalPop + politicalFairness)) / 3.0;
                    total = (total > 1) ? 1 : total;
                    total = Math.round(total * 100.0) / 100.0;
                    p2object.setPickedPrecinctID(pickedPrecinct.getPrecinctID());
                    p2object.setSourceDistrictID(pickedPrecinct.getNewDistrict().getPrecinctID());
                    p2object.setTargetDistrictID(p.getPrecinctID());
                    p2object.setTargetOldObjFunc(p.getObjectiveFunction());
                    p2object.setTargetNewObjFunc(total);
                    boolean makeMove = (total > p.getObjectiveFunction()) ? true : false;
                    p2object.setMakeMove(makeMove);
                    if (makeMove) {
                        Precinct newSourceDistrict = removeFromDistrict(pickedPrecinct.getNewDistrict(), pickedPrecinct);
                        pickedPrecinct.setNewDistrict(newSourceDistrict);
                        p.setDemographic(temp.getDemographic());
                        p.setElectionData(temp.getElectionData());
                        p.setObjectiveFunction(total);
                    }
                    al.getPhase2Objects().add(p2object);
                }
            }
        }
    }

    public Precinct findMajMinDistrict() {
        Precinct target = new Precinct();
        UserInput input = UserInput.getInstance();
        double min = Integer.MIN_VALUE;
        for (Precinct p : newDistricts) {
            if (!p.isPrecinctDestroyed()) {
                double currentJoinability = 0;
                int counter = 0;
                for (int x = 0; x < input.getMinorityRace().size(); x++) {
                    try {
                        Race race = input.getMinorityRace().get(x);
                        int neiRace = p.getDemographic().getPopulationByRace(race);
                        int neiTot = p.getDemographic().getTotalPopulation();
                        currentJoinability = currentJoinability + (((neiRace * 1.0)
                                / (neiTot * 1.0)) * 100);
                        counter++;
                    } catch (NullPointerException e) {
                        continue; // hispanic pop is null
                    }
                }
                currentJoinability = currentJoinability / counter;
                if (currentJoinability > min) {
                    target = p;
                    min = currentJoinability;
                }
            }
        }
        return target;
    }

    public Precinct pickRandomPrecinct(Precinct district) {
        int x = ((int) (Math.random() * (district.getNeighbors().size() - 1 - 0))) + 0;
        int ctr = 0;
        Precinct temp = new Precinct();
        for (Precinct pre : district.getNeighbors()) {
            if (ctr == x) {
                temp = pre;
                break;
            }
            ctr++;
        }
        outerloop:
        for(Precinct cluster : newDistricts){
            for(Integer i : cluster.getPrecinctsInCluster()){
                if(i == temp.getPrecinctID()){
                    temp.setNewDistrict(cluster);
                    break outerloop;
                }
            }
        }
        return temp;
    }

    public Precinct combineTwoPrecinct(Precinct p1, Precinct p2) {
        Precinct temp = p1;
        temp.getDemographic().setTotalPopulation(temp.getDemographic().getTotalPopulation() + p2.getDemographic().getTotalPopulation());
        for (Race r : Race.values()) {
            try {
                int pop = temp.getDemographic().getPopulationByRace(r);
                pop = pop + p2.getDemographic().getPopulationByRace(r);
                temp.getDemographic().setPopulationByRace(r, pop);
            } catch (NullPointerException e) {
                continue;
            }
        }
        ElectionData ed = temp.getElectionData();
        //election data addition
        ed.setCongressionalDem2016(ed.getCongressionalDem2016() + p2.getElectionData().getCongressionalDem2016());
        ed.setCongressionalRep2016(ed.getCongressionalRep2016() + p2.getElectionData().getCongressionalRep2016());
        ed.setCongressionalOthers2016(ed.getCongressionalOthers2016() + p2.getElectionData().getCongressionalOthers2016());
        ed.setCongressionalDem2018(ed.getCongressionalDem2018() + p2.getElectionData().getCongressionalDem2018());
        ed.setCongressionalOthers2018(ed.getCongressionalOthers2018() + p2.getElectionData().getCongressionalOthers2018());
        ed.setCongressionalRep2018(ed.getCongressionalRep2018() + p2.getElectionData().getCongressionalRep2018());
        ed.setPresRep2016(ed.getPresRep2016() + p2.getElectionData().getPresRep2016());
        ed.setPresDem2016(ed.getPresDem2016() + p2.getElectionData().getPresDem2016());
        ed.setPresOthers2016(ed.getPresOthers2016() + p2.getElectionData().getPresOthers2016());
        return temp;
    }

    public Precinct removeFromDistrict(Precinct sourceDistrict, Precinct p2) {
        Precinct temp = sourceDistrict;
        temp.getDemographic().setTotalPopulation(temp.getDemographic().getTotalPopulation() - p2.getDemographic().getTotalPopulation());
        for (Race r : Race.values()) {
            try {
                int pop = temp.getDemographic().getPopulationByRace(r);
                pop = pop - p2.getDemographic().getPopulationByRace(r);
                temp.getDemographic().setPopulationByRace(r, pop);
            } catch (NullPointerException e) {
                continue;
            }
        }
        ElectionData ed = temp.getElectionData();
        //election data addition
        ed.setCongressionalDem2016(ed.getCongressionalDem2016() - p2.getElectionData().getCongressionalDem2016());
        ed.setCongressionalRep2016(ed.getCongressionalRep2016() - p2.getElectionData().getCongressionalRep2016());
        ed.setCongressionalOthers2016(ed.getCongressionalOthers2016() - p2.getElectionData().getCongressionalOthers2016());
        ed.setCongressionalDem2018(ed.getCongressionalDem2018() - p2.getElectionData().getCongressionalDem2018());
        ed.setCongressionalOthers2018(ed.getCongressionalOthers2018() - p2.getElectionData().getCongressionalOthers2018());
        ed.setCongressionalRep2018(ed.getCongressionalRep2018() - p2.getElectionData().getCongressionalRep2018());
        ed.setPresRep2016(ed.getPresRep2016() - p2.getElectionData().getPresRep2016());
        ed.setPresDem2016(ed.getPresDem2016() - p2.getElectionData().getPresDem2016());
        ed.setPresOthers2016(ed.getPresOthers2016() - p2.getElectionData().getPresOthers2016());
        return temp;
    }

    public void calculateGerryMander(District d) {
        double score = 0;
        efficiency_gapDistrict();
        score = (d.rateEfficiencyGap(d) + efficiency_gap + d.gerryDemo(d) + d.gerryRep(d)) / 4.0;
        score = Math.round(score * 100.0) / 100.0;
        d.setGerryManderScore(score);
    }

    public void calculateGerryMander(Precinct p) {
        double score = 0;
        efficiency_gapOne();
        score = (p.rateEfficiencyGap(p) + efficiency_gap + p.gerryDemo(p) + p.gerryRep(p)) / 4.0;
        score = Math.round(score * 100.0) / 100.0;
        p.setGerryManderScore(score);
    }
}