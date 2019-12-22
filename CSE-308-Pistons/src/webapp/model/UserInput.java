package webapp.model;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.ArrayList;

public class UserInput implements Serializable {
    private static UserInput userInput = null;
    private String selectedState;
    private int votingYear;
    private String votingType;
    private ArrayList<Race> minorityRace = new ArrayList<>();
    private double minorityMin;
    private double minorityMax;
    private HashMap<ThresholdType, Double> thresholds;
    private Operation operation;
    private int desiredDistricts;
    private int desiredMajMinDistricts;
    private String runPreference;
    private Boolean continousRun;


    public UserInput() {
        thresholds = new HashMap<>();

    }

    public static UserInput getInstance() {
        if (userInput == null) {
            userInput = new UserInput();
        }
        return userInput;
    }

    public String getSelectedState() {
        return selectedState;
    }

    public void setSelectedState(String selectedState) {
        this.selectedState = selectedState;
    }

    public HashMap<ThresholdType, Double> getThresholds() {
        return thresholds;
    }

    public void setThresholds(HashMap<ThresholdType, Double> thresholds) {
        this.thresholds = thresholds;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public int getVotingYear() {
        return votingYear;
    }

    public void setVotingYear(int votingYear) {
        this.votingYear = votingYear;
    }

    public String getVotingType() {
        return votingType;
    }

    public void setVotingType(String votingType) {
        this.votingType = votingType;
    }

    public ArrayList<Race> getMinorityRace() {
        return minorityRace;
    }

    public void setMinorityRace(ArrayList<Race> minorityRace) {
        this.minorityRace = minorityRace;
    }

    public double getMinorityMin() {
        return minorityMin;
    }

    public void setMinorityMin(double minorityMin) {
        this.minorityMin = minorityMin;
    }

    public double getMinorityMax() {
        return minorityMax;
    }

    public void setMinorityMax(double minorityMax) {
        this.minorityMax = minorityMax;
    }

    public String getRunPreference() {
        return runPreference;
    }

    public void setRunPreference(String runPreference) {
        this.runPreference = runPreference;
    }

    public int getDesiredDistricts() {
        return desiredDistricts;
    }

    public void setDesiredDistricts(int desiredDistricts) {
        this.desiredDistricts = desiredDistricts;
    }

    public int getDesiredMajMinDistricts() {
        return desiredMajMinDistricts;
    }

    public void setDesiredMajMinDistricts(int desiredMajMinDistricts) {
        this.desiredMajMinDistricts = desiredMajMinDistricts;
    }
    public Boolean getContinousRun() {
        return continousRun;
    }

    public void setContinousRun(Boolean continousRun) {
        this.continousRun = continousRun;
    }
}
