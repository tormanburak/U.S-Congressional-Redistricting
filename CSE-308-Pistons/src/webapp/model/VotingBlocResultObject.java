package webapp.model;


public class VotingBlocResultObject {
    private int precinctId;
    private Race eligibleRace;
    private double eligibleRacePercent;
    private Party winningParty;
    private double winningPartyPercent;


    public VotingBlocResultObject() {
    }

    public void setPrecinctId(int precinctId) {
        this.precinctId = precinctId;
    }

    public void setEligibleRace(Race eligibleRace) {
        this.eligibleRace = eligibleRace;
    }

    public void setEligibleRacePercent(double eligibleRacePercent) {
        this.eligibleRacePercent = eligibleRacePercent;
    }

    public void setWinningParty(Party winningParty) {
        this.winningParty = winningParty;
    }

    public void setWinningPartyPercent(double winningPartyPercent) {
        this.winningPartyPercent = winningPartyPercent;
    }
}
