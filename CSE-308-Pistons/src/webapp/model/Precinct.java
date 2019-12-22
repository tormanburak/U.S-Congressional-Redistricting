package webapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.math.*;

@Entity
@Table(name = "Precinct")
public class Precinct implements Serializable {

    private int precinctID;
    private String stateName;
    private Race eligibleRace;
    private int districtID;
    private String coordinates;
    private int demographicID;
    private int electionDataID;
    private Demographic demographic;
    private ElectionData electionData;
    private boolean hasClusterPaired = false;
    private Precinct bestJoinablePrecinct = null;
    private Set<Precinct> neighbors = new HashSet<>();
    private HashMap<Integer, Integer> countyJoinability = new HashMap<>();
    private static DecimalFormat df = new DecimalFormat("0.00");
    private double bestFinalIterationCalc = Integer.MIN_VALUE;
    private Set<Integer> precinctsInCluster = new HashSet<>();
    private boolean isPrecinctDestroyed = false;
    private double currentBestJoinability = Integer.MIN_VALUE;
    private double bestNonMajMinJoinability = Integer.MIN_VALUE;
    private double objectiveFunction = 0;
    private double newObjectiveFunction = 0;
    private Precinct newDistrict;
    private double gerryManderScore;
    public Precinct() {
    }

    public Precinct(int precinctID, String stateName, int districtID, String coordinates, int demographicID, int electionDataID) {
        this.precinctID = precinctID;
        this.stateName = stateName;
        this.districtID = districtID;
        this.coordinates = coordinates;
        this.demographicID = demographicID;
        this.electionDataID = electionDataID;
    }

    @Id
    @Column(name = "precinctID")
    public int getPrecinctID() {
        return precinctID;
    }

    public void setPrecinctID(int precinctID) {
        this.precinctID = precinctID;
    }

    @Column(name = "stateName")
    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @Column(name = "districtID")
    public int getDistrictID() {
        return districtID;
    }

    public void setDistrictID(int districtID) {
        this.districtID = districtID;
    }

    @Column(name = "coordinates")
    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
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

    public double getNewObjectiveFunction() {
        return newObjectiveFunction;
    }

    public void setNewObjectiveFunction(double newObjectiveFunction) {
        this.newObjectiveFunction = newObjectiveFunction;
    }

    @Transient
    public Precinct getNewDistrict() {
        return newDistrict;
    }

    public void setNewDistrict(Precinct newDistrict) {
        this.newDistrict = newDistrict;
    }

    public Race retrieveEligibleRace() {
        findEligibleRace();
        return this.eligibleRace;
    }

    @Transient
    public Set<Precinct> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(Set<Precinct> neighbors) {
        this.neighbors = neighbors;
    }

    private void setEligibleRace(Race eligibleRace) {
        this.eligibleRace = eligibleRace;
    }

    private void findEligibleRace() {
        setEligibleRace(demographic.getEligibleRace());
    }

    @Transient
    public HashMap<Integer, Integer> getCountyJoinability() {
        return countyJoinability;
    }

    public void setCountyJoinability(HashMap<Integer, Integer> countyJoinability) {
        this.countyJoinability = countyJoinability;
    }

    @Transient
    public VotingBlocResultObject getEligibleVotingBlocResultObject() {
        if (electionData.getVotingBlockEligibility()) {
            return createVotingBlocResultObject();
        } else {
            return null;
        }
    }

    @Transient
    public double getObjectiveFunction() {
        return this.objectiveFunction;
    }

    public void setObjectiveFunction(double val) {
        this.objectiveFunction = val;
    }

    private VotingBlocResultObject createVotingBlocResultObject() {
        VotingBlocResultObject vo = new VotingBlocResultObject();
        vo.setPrecinctId(precinctID);
        vo.setEligibleRace(demographic.getHighestEligibleRace());
        vo.setEligibleRacePercent(Double.parseDouble(df.format(demographic.getHighestEligibleRacePercent())));
        vo.setWinningParty(electionData.getWinningParty());
        vo.setWinningPartyPercent(Double.parseDouble(df.format(electionData.getWinningPartyPercentage())));
        return vo;
    }

    @Transient
    public boolean hasClusterPaired() {
        return hasClusterPaired;
    }

    public void setHasClusterPaired(boolean hasClusterPaired) {
        this.hasClusterPaired = hasClusterPaired;
    }

    @Transient
    public Precinct getBestJoinablePrecinct() {
        return bestJoinablePrecinct;
    }

    public void setBestJoinablePrecinct(Precinct bestJoinablePrecinct) {
        this.bestJoinablePrecinct = bestJoinablePrecinct;
    }

    public double getCurrentBestJoinability() {
        return currentBestJoinability;
    }

    public void setCurrentBestJoinability(double currentBestJoinability) {
        this.currentBestJoinability = currentBestJoinability;
    }

    public double getBestNonMajMinJoinability() {
        return bestNonMajMinJoinability;
    }

    public boolean isPrecinctDestroyed() {
        return isPrecinctDestroyed;
    }

    public void setPrecinctDestroyed(boolean precinctDestroyed) {
        isPrecinctDestroyed = precinctDestroyed;
    }

    public void setBestNonMajMinJoinability(double bestNonMajMinJoinability) {
        this.bestNonMajMinJoinability = bestNonMajMinJoinability;
    }

    public double getBestFinalIterationCalc() {
        return bestFinalIterationCalc;
    }

    public void setBestFinalIterationCalc(double bestFinalIterationCalc) {
        this.bestFinalIterationCalc = bestFinalIterationCalc;
    }

    @Transient
    public Set<Integer> getPrecinctsInCluster() {
        return precinctsInCluster;
    }

    public void setPrecinctsInCluster(Set<Integer> precinctsInCluster) {
        this.precinctsInCluster = precinctsInCluster;
    }

    @Transient
    public double getGerryManderScore() {
        return gerryManderScore;
    }

    public void setGerryManderScore(double gerryManderScore) {
        this.gerryManderScore = gerryManderScore;
    }

    public void findMajMinPrecincts() {
        //neighbors or every precincts?
        UserInput input = UserInput.getInstance();
        int joined = 0;
        for (Precinct neighbor : neighbors) {
            if (!neighbor.hasClusterPaired() && !neighbor.isPrecinctDestroyed() && neighbor != this) {
                //get the minority race population for all the user selected for current precinct
                double currentJoinability = 0;
                int counter = 0;
                for (int x = 0; x < input.getMinorityRace().size(); x++) {
                    try {
                        Race race = input.getMinorityRace().get(x);
                        int neiRace = neighbor.getDemographic().getPopulationByRace(race);
                        int neiTot = neighbor.getDemographic().getTotalPopulation();
                        currentJoinability = currentJoinability + (((neiRace * 1.0)
                                / (neiTot * 1.0)) * 100);
                        counter++;
                    } catch (NullPointerException e) {
                        continue; // hispanic pop is null
                    }
                }
                currentJoinability = currentJoinability / counter;

                if (currentJoinability > input.getMinorityMax() && currentJoinability < input.getMinorityMin()) {
                    continue;
                }
                //normalize the dat
                currentJoinability = ((currentJoinability - input.getMinorityMin()) * 1.0) / ((input.getMinorityMax() - input.getMinorityMin()) * 1.0);
                if (currentJoinability > currentBestJoinability) {
                    joined = 1;
                    currentBestJoinability = currentJoinability;
                    setBestJoinablePrecinct(neighbor);
                }
            }
        }
        if (joined == 1) {
            setHasClusterPaired(true);
            bestJoinablePrecinct.setHasClusterPaired(true);
            bestJoinablePrecinct.setCurrentBestJoinability(currentBestJoinability);
        }
    }

    public void findNonMajMinPrecincts() {
        int joined = 0;
        for (Precinct neighbor : neighbors) {
            if (!neighbor.hasClusterPaired() && !neighbor.isPrecinctDestroyed() && neighbor != this) {

//                Algorithm a = Algorithm.getInstance();
//                int total = precinctsInCluster.size() + neighbor.precinctsInCluster.size();
//                if(a.getCurrentStateName().equals("TX") && total>400){
//                    continue;
//                }
                double totalJoinability = 0;
                //compare population
                double populationJoinability = calculateEquPopulation(neighbor);
                totalJoinability += populationJoinability;
                //compactness?
                double compactnessJoinability = 0;

                totalJoinability += compactnessJoinability;
                //single county
//                try {
//                    totalJoinability += countyJoinability.get(neighbor.getPrecinctID());
//                }catch (NullPointerException e){
//
//                }
                //political fairness
                double politicalFairness = calculatePolFairness(neighbor);
                totalJoinability += politicalFairness;
                double joinability = totalJoinability / 2.0;
//                if(politicalFairness<0 || populationJoinability<0){
//                    continue;
//                }
                System.out.print("Non maj min" + populationJoinability + " and " + politicalFairness);

                if (joinability > bestNonMajMinJoinability) {
                    joined = 1;
                    bestNonMajMinJoinability = joinability;
                    setBestJoinablePrecinct(neighbor);
                }
            }
        }
        System.out.println();
        if (joined == 1) {
            setHasClusterPaired(true);
            bestJoinablePrecinct.setHasClusterPaired(true);
            bestJoinablePrecinct.setBestNonMajMinJoinability(bestNonMajMinJoinability);
        }
    }

    private double calculatePolFairness(Precinct neighbor) {
        UserInput userInput = UserInput.getInstance();
        String votingType = userInput.getVotingType();
        int votingYear = userInput.getVotingYear();
        int totalVote = 0;
        int totalGOPvote = 0;
        int totalPrecincts = 0;
        int totalGOPPrecincts = 0;
        int gv = 0, dv = 0;
        if (votingType.equals("Presidential") && votingYear == 2016) {
            for (Precinct sd : Algorithm.getInstance().getCurrentState().getPrecincts()) {
                totalVote += sd.getElectionData().getPresRep2016();
                totalVote += sd.getElectionData().getPresDem2016();
                totalGOPvote += sd.getElectionData().getPresRep2016();
                totalPrecincts += 1;
                if (sd.getElectionData().getPresRep2016() >= sd.getElectionData().getPresDem2016()) {
                    totalGOPPrecincts += 1;
                }
            }
            gv = neighbor.getElectionData().getPresRep2016();
            dv = neighbor.getElectionData().getPresDem2016();
        } else if (votingType.equals("Congressional") && votingYear == 2016) {
            for (Precinct sd : Algorithm.getInstance().getCurrentState().getPrecincts()) {
                totalVote += sd.getElectionData().getCongressionalRep2016();
                totalVote += sd.getElectionData().getCongressionalDem2016();
                totalGOPvote += sd.getElectionData().getCongressionalRep2016();
                totalPrecincts += 1;
                if (sd.getElectionData().getCongressionalRep2016() >= sd.getElectionData().getCongressionalDem2016()) {
                    totalGOPPrecincts += 1;
                }
            }
            gv = neighbor.getElectionData().getCongressionalRep2016();
            dv = neighbor.getElectionData().getCongressionalDem2016();
        } else if (votingType.equals("Congressional") && votingYear == 2018) {
            for (Precinct sd : Algorithm.getInstance().getCurrentState().getPrecincts()) {
                totalVote += sd.getElectionData().getCongressionalRep2018();
                totalVote += sd.getElectionData().getCongressionalDem2018();
                totalGOPvote += sd.getElectionData().getCongressionalRep2018();
                totalPrecincts += 1;
                if (sd.getElectionData().getCongressionalRep2018() >= sd.getElectionData().getCongressionalDem2018()) {
                    totalGOPPrecincts += 1;
                }
            }
            gv = neighbor.getElectionData().getCongressionalRep2018();
            dv = neighbor.getElectionData().getCongressionalDem2018();
        }

        int idealPrecinctChange = ((int) Math.round(totalPrecincts * ((1.0 * totalGOPvote) / totalVote))) - totalGOPPrecincts;
        // End temporary section
        if (idealPrecinctChange == 0) {
            return 1.0;
        }
        int tv = gv + dv;
        int margin = gv - dv;
        if (tv == 0) {
            return 1.0;
        }
        int win_v = Math.max(gv, dv);
        int loss_v = Math.min(gv, dv);
        int inefficient_V;
        if (idealPrecinctChange * margin > 0) {
            inefficient_V = win_v - loss_v;
        } else {
            inefficient_V = loss_v;
        }
        return 1.0 - ((inefficient_V * 1.0) / tv);
    }

    private int getGv(Precinct sd) {
        int gv = 0;
        UserInput userInput = UserInput.getInstance();
        String votingType = userInput.getVotingType();
        int votingYear = userInput.getVotingYear();
        if (votingType.equals("Presidential") && votingYear == 2016) {
            gv = sd.getElectionData().getPresRep2016();
        } else if (votingType.equals("Congressional") && votingYear == 2016) {
            gv = sd.getElectionData().getCongressionalRep2016();
        } else if (votingType.equals("Congressional") && votingYear == 2018) {
            gv = sd.getElectionData().getCongressionalRep2018();
        }
        return gv;
    }

    private int getDv(Precinct sd) {
        int dv = 0;
        UserInput userInput = UserInput.getInstance();
        String votingType = userInput.getVotingType();
        int votingYear = userInput.getVotingYear();
        if (votingType.equals("Presidential") && votingYear == 2016) {
            dv = sd.getElectionData().getPresDem2016();
        } else if (votingType.equals("Congressional") && votingYear == 2016) {
            dv = sd.getElectionData().getCongressionalDem2016();
        } else if (votingType.equals("Congressional") && votingYear == 2018) {
            dv = sd.getElectionData().getCongressionalDem2018();
        }
        return dv;
    }

    public double calculateEquPopulation(Precinct neighbor) {
        int idealPopulation = (Algorithm.getInstance().getCurrentState().totalPrecinctsPop) /
                (Algorithm.getInstance().getCurrentState().getPrecincts().size());
        int truePopulation = neighbor.getDemographic().getTotalPopulation();
        if (idealPopulation >= truePopulation) {
            return 1 - Math.pow(
                    Math.abs(idealPopulation - (double) truePopulation) / idealPopulation, 1.25);
        }
        return 1 - Math.pow(
                Math.abs(truePopulation - (double) idealPopulation)
                        / idealPopulation, 1.25);
    }
    public double calculateEquPopulationDistrict(Precinct neighbor) {
        int idealPopulation = (Algorithm.getInstance().getCurrentState().totalPrecinctsPop) /
                (Algorithm.getInstance().getCurrentState().getNewDistricts().size());
        int truePopulation = neighbor.getDemographic().getTotalPopulation();
        if (idealPopulation >= truePopulation) {
            return 1 - Math.pow(
                    Math.abs(idealPopulation - (double) truePopulation) / idealPopulation, 1.25);
        }
        return 1 - Math.pow(
                Math.abs(truePopulation - (double) idealPopulation)
                        / idealPopulation, 1.25);
    }

    public double calculateCompactness() {
        return Math.random();
    }

    public double rateEfficiencyGap(Precinct sd) {
        int gv;
        int dv;
        gv = getGv(sd);
        dv = getDv(sd);
        int tv = gv + dv;
        if (tv == 0) {
            return 1.0;
        }
        int win_v = Math.max(gv, dv);
        int loss_v = Math.min(gv, dv);
        int inefficient_V = Math.abs(loss_v - (win_v - loss_v));
        return 1.0 - ((inefficient_V * 1.0) / tv);
    }

    public double competitiveness(Precinct sd) {
        int gv;
        int dv;
        gv = getGv(sd);
        dv = getDv(sd);
        return 1.0 - (((double) Math.abs(gv - dv)) / (gv + dv));
    }

    public double gerryDemo(Precinct sd) {
        int gv = getGv(sd);
        int dv = getDv(sd);
        int tv = gv + dv;
        int margin = dv - gv;
        if (tv == 0) {
            return 1.0;
        }
        int win_v = Math.max(gv, dv);
        int loss_v = Math.min(gv, dv);
        int inefficient_V;
        if (margin > 0) {
            inefficient_V = win_v - loss_v;
        } else {
            inefficient_V = loss_v;
        }
        return 1.0 - ((inefficient_V * 1.0) / tv);
    }

    public double gerryRep(Precinct sd) {
        int gv = getGv(sd);
        int dv = getDv(sd);
        int tv = gv + dv;
        int margin = gv - dv;
        if (tv == 0) {
            return 1.0;
        }
        int win_v = Math.max(gv, dv);
        int loss_v = Math.min(gv, dv);
        int inefficient_V;
        if (margin > 0) {
            inefficient_V = win_v - loss_v;
        } else {
            inefficient_V = loss_v;
        }
        return 1.0 - ((inefficient_V * 1.0) / tv);
    }

    public void combineBestNeighbor() {
        if (isPrecinctDestroyed == true) {
            return;
        }
        // loop through all races and add population for neighbor and this.Precinct
        demographic.setTotalPopulation(demographic.getTotalPopulation() + bestJoinablePrecinct.demographic.getTotalPopulation());

        for (Race r : Race.values()) {
            try {
                int pop = demographic.getPopulationByRace(r);
                pop = pop + bestJoinablePrecinct.getDemographic().getPopulationByRace(r);
                demographic.setPopulationByRace(r, pop);
            } catch (NullPointerException e) {
                continue;
            }
        }
        precinctsInCluster.addAll(bestJoinablePrecinct.getPrecinctsInCluster());
        precinctsInCluster.add(bestJoinablePrecinct.getPrecinctID());

        for (Precinct p : bestJoinablePrecinct.getNeighbors()) {
            if (p == this) {
                continue;
            } if(!precinctsInCluster.contains(p.getPrecinctID())){
                neighbors.add(p);
            }

        }
        neighbors.remove(bestJoinablePrecinct);

        Iterator<Precinct> iter = bestJoinablePrecinct.neighbors.iterator();
        while (iter.hasNext()) {
            Precinct p = iter.next();
            if (p == this) {
                continue;
            }
            p.getNeighbors().add(this);
            Iterator<Precinct> iter2 = p.neighbors.iterator();
            while(iter2.hasNext()) {
                Precinct p2 = iter2.next();
                if (p2 == bestJoinablePrecinct) {
                    p2.getNeighbors().remove(bestJoinablePrecinct);
                }
            }
        }
        // neighbors.removeAll(precinctsInCluster);
        Iterator<Precinct> iter3 = neighbors.iterator();
        while (iter3.hasNext()) {
            Precinct p = iter3.next();
            if(precinctsInCluster.contains(p.getPrecinctID())){
                iter3.remove();
            }
        }
        // ADD ALL DAMN CLUSTERS :)

        //election data neighbor addition
        electionData.setCongressionalDem2016(electionData.getCongressionalDem2016() + bestJoinablePrecinct.getElectionData().getCongressionalDem2016());
        electionData.setCongressionalRep2016(electionData.getCongressionalRep2016() + bestJoinablePrecinct.getElectionData().getCongressionalRep2016());
        electionData.setCongressionalOthers2016(electionData.getCongressionalOthers2016() + bestJoinablePrecinct.getElectionData().getCongressionalOthers2016());

        electionData.setCongressionalDem2018(electionData.getCongressionalDem2018() + bestJoinablePrecinct.getElectionData().getCongressionalDem2018());
        electionData.setCongressionalOthers2018(electionData.getCongressionalOthers2018() + bestJoinablePrecinct.getElectionData().getCongressionalOthers2018());
        electionData.setCongressionalRep2018(electionData.getCongressionalRep2018() + bestJoinablePrecinct.getElectionData().getCongressionalRep2018());
        electionData.setPresRep2016(electionData.getPresRep2016() + bestJoinablePrecinct.getElectionData().getPresRep2016());
        electionData.setPresDem2016(electionData.getPresDem2016() + bestJoinablePrecinct.getElectionData().getPresDem2016());
        electionData.setPresOthers2016(electionData.getPresOthers2016() + bestJoinablePrecinct.getElectionData().getPresOthers2016());
        bestJoinablePrecinct.setPrecinctDestroyed(true);
        Algorithm.getInstance().getCurrentState().destroyedCounter++;
        //System.out.println("Destroyed: " + bestJoinablePrecinct.getPrecinctID() + " Added to: " + precinctID + " size: " + precinctsInCluster.size());

    }

    public void calculateFinalBestNeighbor() {
        boolean foundNeighbor = false;
        int min = Integer.MIN_VALUE;
        for (Precinct neighbor : neighbors) {
            if (!neighbor.hasClusterPaired() && !neighbor.isPrecinctDestroyed()) {
                double neighborCalc = calculateEquPopulation(neighbor);
                if (neighborCalc > bestFinalIterationCalc) {
                    bestFinalIterationCalc = neighborCalc;
                    bestJoinablePrecinct = neighbor;
                    foundNeighbor = true;
                }
            }
        }
        if (foundNeighbor) {
            hasClusterPaired = true;
            bestJoinablePrecinct.setHasClusterPaired(true);
            bestNonMajMinJoinability = bestFinalIterationCalc;
            bestFinalIterationCalc = 0;
            combineBestNeighbor();
        }
    }
}