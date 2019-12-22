package webapp.model;

import com.google.gson.Gson;
import java.util.*;
import javax.persistence.*;

public class Algorithm {
    private static Algorithm algorithm = null;
    private static HashMap<ThresholdType, Double> thresholds;
    private State currentState;
    private String currentStateName;
    private Set<State> states = new HashSet<>();
    private Operation operation;
    private FinalResult finalResult;
    private int currentDistricts = 0;
    private int callCounter=0;
    public int phase0call=0;
    private Set<Phase2Object> phase2Objects;
    private static final int ROUNDS = 100;
    public static Algorithm getInstance() {
        if (algorithm == null) {
            algorithm = new Algorithm();
            thresholds = new HashMap<>();

        }
        return algorithm;
    }

    public String handler(UserInput input) throws CloneNotSupportedException {
        String jsonResult;
        setUserInput(input);
        if (operation == Operation.DISPLAY_DISTRICTS) {
            jsonResult = displayDistricts(currentStateName);
            return jsonResult;
        } else if (operation == Operation.PHASE_0) {
            if(phase0call>0){
                this.currentState.resetPhaseZero();
            }
            jsonResult = runPhase0();
            phase0call++;
            return jsonResult;
        } else if (operation == Operation.PHASE_1) {
            if(callCounter>0 && input.getRunPreference().equals("ITERATE")){
                jsonResult = runPhase1();
                callCounter++;
                if (currentDistricts == UserInput.getInstance().getDesiredDistricts()) {
                    this.currentState.resetEverything();
                    callCounter=0;
                    currentDistricts=0;
                }
                return jsonResult;
            }
            if(callCounter>0){
                this.currentState.resetEverything();
                callCounter=0;
                currentDistricts=0;
            }
            if(callCounter==0){
                this.currentState.saveCopy();
            }
            this.currentState.loadNeighbors();
            jsonResult = runPhase1();
            callCounter++;
            return jsonResult;
        } else if (operation == Operation.PHASE_2) {
            jsonResult = runPhase2();
            return jsonResult;
        }

        return null;
    }

    public String displayDistricts(String stateName) {
        EntityManager em = HibernateUtil.getEntityManager();
        this.currentState = em.find(State.class, stateName);
        FinalResult finalResult = new FinalResult();
        @SuppressWarnings("unchecked")
        Set<District> districts = new HashSet<>(em.createQuery("SELECT x FROM District x WHERE x.stateName=:name", District.class)
                .setParameter("name", stateName)
                .getResultList());
        ;
        List<NeighborDistrict> distrctNeigh = em.createQuery("SELECT x FROM NeighborDistrict x WHERE x.stateName=:name", NeighborDistrict.class).
                setHint("org.hibernate.fetchSize", 200).setParameter("name", stateName).getResultList();
        HashMap<Integer, ArrayList<Integer>> neighborDistrictMap = new HashMap<Integer, ArrayList<Integer>>();

        for (NeighborDistrict nd : distrctNeigh) {
            if (neighborDistrictMap.containsKey(nd.getDistrictID())) {
                ArrayList<Integer> list = neighborDistrictMap.get(nd.getDistrictID());
                list.add(nd.getNeighborID());
                neighborDistrictMap.put(nd.getDistrictID(), list);
                continue;
            }
            ArrayList<Integer> list = new ArrayList<>();
            list.add(nd.getNeighborID());
            neighborDistrictMap.put(nd.getDistrictID(), list);
        }
        Set<Precinct> precincts;
        if (!stateName.equals("TX")) {
            precincts = new HashSet<>(em.createQuery("SELECT x FROM Precinct x WHERE x.stateName=:name", Precinct.class).setHint("org.hibernate.fetchSize", 5000)
                    .setParameter("name", stateName).getResultList());
            List<ElectionData> e = em.createQuery("SELECT x FROM ElectionData x WHERE x.stateName=:name ", ElectionData.class).
                    setHint("org.hibernate.fetchSize", 5000).setParameter("name", stateName).getResultList();
            List<Demographic> d = em.createQuery("SELECT x FROM Demographic x WHERE x.stateName=:name", Demographic.class).
                    setHint("org.hibernate.fetchSize", 5000).setParameter("name", stateName).getResultList();

        } else {
            precincts = new HashSet<>(em.createQuery("SELECT x FROM Precinct x WHERE x.stateName=:name", Precinct.class)
                    .setHint("org.hibernate.fetchSize", 10000).setParameter("name", stateName).getResultList());
            List<ElectionData> e = em.createQuery("SELECT x FROM ElectionData x WHERE x.stateName=:name ", ElectionData.class).
                    setHint("org.hibernate.fetchSize", 10000).setParameter("name", stateName).getResultList();
            List<Demographic> d = em.createQuery("SELECT x FROM Demographic x WHERE x.stateName=:name", Demographic.class).
                    setHint("org.hibernate.fetchSize", 10000).setParameter("name", stateName).getResultList();
        }
        ElectionData stateElectionData = em.find(ElectionData.class, currentState.getElectionDataID());
        Demographic stateDemographic = em.find(Demographic.class, currentState.getDemographicID());
        this.currentState.setElectionData(stateElectionData);
        this.currentState.setDemographic(stateDemographic);
        for (District district : districts) {
            ElectionData districtElectionData = em.find(ElectionData.class, district.getElectionDataID());
            Demographic districtDemographic = em.find(Demographic.class, district.getDemographicID());
            district.setElectionData(districtElectionData);
            district.setDemographic(districtDemographic);
            district.setNeighborDistrct(neighborDistrictMap.get(district.getDistrictID()));
        }
        for (Precinct precinct : precincts) {
            ElectionData precinctElectionData = em.find(ElectionData.class, precinct.getElectionDataID());
            Demographic precinctDemographic = em.find(Demographic.class, precinct.getDemographicID());
            precinct.setElectionData(precinctElectionData);
            precinct.setDemographic(precinctDemographic);
        }


        this.currentState.setPrecincts(precincts);
        this.currentState.setDistricts(districts);
        this.currentState.setStateName(stateName);
        states.add(this.currentState);
        finalResult.setState(currentState);
        finalResult.setDistricts(districts);
        finalResult.setPrecincts(precincts);
        Gson gson = new Gson();

        String jsonResult = gson.toJson(finalResult);
        return jsonResult;
    }

    public String runPhase0() throws CloneNotSupportedException {
        this.currentState.totalPrecinctsPop();
        try {
            this.currentState.identifyPrecinctsDemoBloc();
        } catch (Exception e) {
            System.out.println("Error in demographic");
            e.printStackTrace();
        }
        try {
            Gson gson = new Gson();
            String votingBlock = gson.toJson(this.currentState.getDemoVoteBlocPrecincts());
            return votingBlock;
            //need to stringify and display as table
        } catch (Exception e) {
            System.out.println("Error in Voting Block");
            e.printStackTrace();
        }
        return "good";
    }

    private String runPhase1() {
        UserInput ui = UserInput.getInstance();
        this.currentState.setTotalPrecincts();
        String returnString = "";
        for(District d : this.currentState.getDistricts()){
            this.currentState.calculateGerryMander(d);
        }
        try {
            int oldCurentDistricts = -1;
            int newCurrentDistricts = -2;
            if (ui.getRunPreference().equals("FINAL")) {
                currentDistricts = 0;
                while (currentDistricts != ui.getDesiredDistricts()) {
                    if (oldCurentDistricts == newCurrentDistricts) {
                        return returnString;
                    }
                    oldCurentDistricts = currentDistricts;
                    returnString = callPhase1Methods();
                    newCurrentDistricts = currentDistricts;
                }
                //find neighbors
                Set<Precinct> newDistrict = new HashSet<>();
                for(Precinct p : this.currentState.getPrecincts()){
                    if(!p.isPrecinctDestroyed()){
                        newDistrict.add(p);
                    }
                }

                this.currentState.setNewDistricts(newDistrict);
                return returnString;
            } else if (ui.getRunPreference().equals("ITERATE")) {
                //currentDistricts gets saved and continues where left off...
                while (currentDistricts != ui.getDesiredDistricts()) {
                    returnString = callPhase1Methods();
//                    if (currentDistricts == ui.getDesiredDistricts()) {
//                        this.currentState.resetEverything();
//                    }
                    return returnString;
                }
                return returnString;
            }
        } catch (Exception e) {
            System.out.println("Error in finding maj min pairs");
            e.printStackTrace();
        }
        return returnString;
    }

    private String runPhase2() {
        phase2Objects = new HashSet<>();
        String returnString = "";
        returnString = callPhase2Methods();
        return returnString;
    }

    private String callPhase1Methods() {
        this.currentState.findMajMinPairs();
        this.currentState.findNonMajMinPairs();
        this.currentState.joinCandidatePairs();
        this.currentState.resetForNextIteration();
        String returnString = this.currentState.returnClusters();
        currentDistricts = this.currentState.precinctCounter;
        if (this.currentStateName.equals("TX")
                && ((currentDistricts - this.currentState.districtCtr) <= UserInput.getInstance().getDesiredDistricts())) {
            currentDistricts = UserInput.getInstance().getDesiredDistricts();
            return  returnString;
        }
        if (currentDistricts == UserInput.getInstance().getDesiredDistricts()) {
            return returnString;
        }
        this.currentState.finalIteration();
        returnString = this.currentState.returnClusters();
        currentDistricts = this.currentState.precinctCounter;
        this.currentState.resetForNextIteration();
        if (this.currentStateName.equals("TX")
                && ((currentDistricts - this.currentState.districtCtr) <= UserInput.getInstance().getDesiredDistricts())) {
            currentDistricts = UserInput.getInstance().getDesiredDistricts();
            return  returnString;
        }
        return returnString;
    }

    private String callPhase2Methods() {
        // calc obj function
        for(int i=0; i < ROUNDS ; i++) {
            this.currentState.calculateObjFunction();
            Precinct district = this.currentState.findMajMinDistrict();
            Precinct precinct = this.currentState.pickRandomPrecinct(district);
            this.currentState.calculateNewObjectiveFunction(precinct);
        }
        Map<Integer,Double> oldDistrict = new HashMap<>();
        Map<Integer,Double> newDistrict = new HashMap<>();

        for(District d : this.currentState.getDistricts()){
            this.currentState.calculateGerryMander(d);
            oldDistrict.put(d.getDistrictID(),d.getGerryManderScore());
        }
        for(Precinct p : this.currentState.getNewDistricts()){
            this.currentState.calculateGerryMander(p);
            newDistrict.put(p.getPrecinctID(),p.getGerryManderScore());
        }
        Phase2Result result = new Phase2Result();
        result.setSet(phase2Objects);
        result.setNewDistricts(newDistrict);
        result.setOldDistricts(oldDistrict);
        Gson gson = new Gson();
        return gson.toJson(result);
    }

    public void setUserInput(UserInput givenInput) {
        UserInput userInput = UserInput.getInstance();
        this.operation = givenInput.getOperation();

        if (operation == Operation.DISPLAY_DISTRICTS) {
            this.currentStateName = givenInput.getSelectedState();
            userInput.setSelectedState(givenInput.getSelectedState());
        } else if (operation == Operation.PHASE_0) {
            HashMap<ThresholdType, Double> thresholds = userInput.getThresholds();

            HashMap<ThresholdType, Double> given = givenInput.getThresholds();
            thresholds.put(ThresholdType.POPULATION_PERCENT_MIN, given.get(ThresholdType.POPULATION_PERCENT_MIN));
            thresholds.put(ThresholdType.POPULATION_PERCENT_MAX, givenInput.getThresholds().get(ThresholdType.POPULATION_PERCENT_MAX));
            thresholds.put(ThresholdType.VOTING_PERCENT_MIN, givenInput.getThresholds().get(ThresholdType.VOTING_PERCENT_MIN));
            thresholds.put(ThresholdType.VOTING_PERCENT_MAX, givenInput.getThresholds().get(ThresholdType.VOTING_PERCENT_MAX));
            userInput.setThresholds(thresholds);
            userInput.setVotingType(givenInput.getVotingType());
            userInput.setVotingYear(givenInput.getVotingYear());
        } else if (operation == Operation.PHASE_1) {
            userInput.setMinorityRace(givenInput.getMinorityRace());
            userInput.setMinorityMin(givenInput.getMinorityMin());
            userInput.setMinorityMax(givenInput.getMinorityMax());
            userInput.setRunPreference(givenInput.getRunPreference());
            userInput.setDesiredDistricts(givenInput.getDesiredDistricts());
            userInput.setContinousRun(givenInput.getContinousRun());
            userInput.setDesiredMajMinDistricts(givenInput.getDesiredMajMinDistricts());
        }
    }

    public static HashMap<ThresholdType, Double> getThresholds() {
        return thresholds;
    }

    public static void setThresholds(HashMap<ThresholdType, Double> thresholds) {
        Algorithm.thresholds = thresholds;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public String getCurrentStateName() {
        return currentStateName;
    }

    public void setCurrentStateName(String currentStateName) {
        this.currentStateName = currentStateName;
    }

    public Set<State> getStates() {
        return states;
    }

    public void setStates(Set<State> states) {
        this.states = states;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public FinalResult getFinalResult() {
        return finalResult;
    }

    public void setFinalResult(FinalResult finalResult) {
        this.finalResult = finalResult;
    }

    public Set<Phase2Object> getPhase2Objects() {
        return phase2Objects;
    }

    public void setPhase2Objects(Set<Phase2Object> phase2Objects) {
        this.phase2Objects = phase2Objects;
    }
}