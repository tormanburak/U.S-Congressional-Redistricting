package webapp.model;

import com.google.gson.Gson;

import javax.persistence.*;
import java.util.HashMap;


@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
public class ElectionData implements Cloneable{

    private int electionDataID;
    private String stateName;
    private int presDem2016;
    private int presRep2016;
    private int presOthers2016;
    private int congressionalDem2016;
    private int congressionalRep2016;
    private int congressionalOthers2016;
    private int congressionalDem2018;
    private int congressionalRep2018;
    private int congressionalOthers2018;
    private Party winningParty;
    private double winningPartyPercentage = 0.0;

    public ElectionData(){}



    //    public ElectionData(int electionDataID, int presDem2016, int presRep2016, int presOthers2016, int congressionalDem2016,int congressionalRep2016,int congressionalOthers2016, int congressionalDem2018, int congressionalRep2018 , int congressionalOthers2018){
//       this.electionDataID = electionDataID;
//       this.presDem2016 = presDem2016;
//       this.presRep2016 = presRep2016;
//       this.presOthers2016 = presOthers2016;
//       this.congressionalDem2016 = congressionalDem2016;
//       this.congressionalRep2016= congressionalRep2016;
//       this.congressionalOthers2016 = congressionalOthers2016;
//       this.congressionalDem2018 = congressionalDem2018;
//       this.congressionalRep2018 = congressionalRep2018;
//       this.congressionalOthers2018 = congressionalOthers2018;
//    }
    @Id
    @Column(name="electionDataID")
    public int getElectionDataID(){
        return electionDataID;
    }

    public void setElectionDataID(int electionDataID){
        this.electionDataID = electionDataID;
    }

    @Column(name="stateName")
    public String getStateName(){
        return stateName;
    }
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
    @Column(name="presDem2016")
    public int getPresDem2016() {
        return presDem2016;
    }

    public void setPresDem2016(int presDem2016) {
        this.presDem2016 = presDem2016;
    }

    @Column(name="presRep2016")
    public int getPresRep2016(){
        return presRep2016;
    }

    public void setPresRep2016(int presRep2016) {
        this.presRep2016 = presRep2016;
    }

    @Column(name="presOthers2016")
    public int getPresOthers2016(){
        return presOthers2016;
    }

    public void setPresOthers2016(int presOthers2016){
        this.presOthers2016 = presOthers2016;
    }

    @Column(name="congressionalDem2016")
    public int getCongressionalDem2016(){
        return congressionalDem2016;
    }

    public void setCongressionalDem2016(int congressionalDem2016){
        this.congressionalDem2016 = congressionalDem2016;
    }

    @Column(name="congressionalRep2016")
    public int getCongressionalRep2016() {
        return congressionalRep2016;
    }

    public void setCongressionalRep2016(int congressionalRep2016){
        this.congressionalRep2016 = congressionalRep2016;
    }

    @Column(name="congressionalOthers2016")
    public int getCongressionalOthers2016(){
        return congressionalOthers2016;
    }

    public void setCongressionalOthers2016(int congressionalOther2016) { this.congressionalOthers2016 = congressionalOther2016; }

    @Column(name="congressionalDem2018")
    public int getCongressionalDem2018(){
        return congressionalDem2018;
    }

    public void setCongressionalDem2018(int congressionalDem2018){
        this.congressionalDem2018 = congressionalDem2018;
    }

    @Column(name="congressionalRep2018")
    public int getCongressionalRep2018(){
        return congressionalRep2018;
    }

    public void setCongressionalRep2018(int congressionalRep2018){
        this.congressionalRep2018 = congressionalRep2018;
    }

    @Column(name="congressionalOthers2018")
    public int getCongressionalOthers2018(){
        return congressionalOthers2018;
    }

    public void setCongressionalOthers2018(int congressionalOther2018) { this.congressionalOthers2018 = congressionalOther2018; }

    @Transient
    public boolean getVotingBlockEligibility() {
        UserInput input = UserInput.getInstance();
        winningParty= getWinningPartyByYear(input.getVotingYear(), input.getVotingType());
        int winningPartyVotes= getWinningPartyVotes(input.getVotingYear(), input.getVotingType());
        int totalVotes= getTotalVotes(input.getVotingYear(), input.getVotingType());
        winningPartyPercentage= ((winningPartyVotes*1.0)/(totalVotes*1.0))*100.0;
        try{
            Gson g = new Gson();
            g.toJson(winningPartyPercentage);
        }catch (IllegalArgumentException e ){
            winningPartyPercentage = 0.0;
        }
        return compareAgainstThresholds();
    }
    private boolean compareAgainstThresholds() {
        UserInput input = UserInput.getInstance();
        HashMap<ThresholdType, Double> thresholds = input.getThresholds();
        if(winningPartyPercentage>= thresholds.get(ThresholdType.VOTING_PERCENT_MIN) && winningPartyPercentage<= thresholds.get(ThresholdType.VOTING_PERCENT_MAX)){
            return true;
        }
        return false;
    }

    private int getWinningPartyVotes(int votingYear, String votingType) {
        if(votingYear==2016 && votingType.equals("Presidential") && winningParty.equals(Party.DEMOCRAT)){
            return getPresDem2016();
        }
        if(votingYear==2016 && votingType.equals("Presidential") && winningParty.equals(Party.REPUBLICAN)) {
            return getPresRep2016();
        }
        if(votingYear==2016 && votingType.equals("Congressional") && winningParty.equals(Party.DEMOCRAT)){
            return getCongressionalDem2016();
        }
        if(votingYear==2016 && votingType.equals("Congressional") && winningParty.equals(Party.REPUBLICAN)){
            return getCongressionalRep2016();
        }
        if(votingYear==2018 && votingType.equals("Congressional") && winningParty.equals(Party.REPUBLICAN)){
            return getCongressionalRep2018();
        }
        if(votingYear==2018 && votingType.equals("Congressional") && winningParty.equals(Party.DEMOCRAT)){
            return getCongressionalDem2018();
        }
        return 0;
    }

    private Party getWinningPartyByYear(int votingYear, String votingType) {
        if(votingYear==2016 && votingType.equals("Presidential")){
            if(getPresDem2016()>getPresRep2016()){
                return Party.DEMOCRAT;
            }else{
                return Party.REPUBLICAN;
            }
        }
        if(votingYear==2016 && votingType.equals("Congressional")){
            if(getCongressionalDem2016()>getCongressionalRep2016()){
                return Party.DEMOCRAT;
            }else{
                return Party.REPUBLICAN;
            }
        }
        if(votingYear==2018 && votingType.equals("Congressional")){
            if(getCongressionalDem2018()>getCongressionalRep2018()){
                return Party.DEMOCRAT;
            }else{
                return Party.REPUBLICAN;
            }
        }
        return Party.REPUBLICAN;
    }

    private int getTotalVotes(int votingYear, String votingType) {
        int totalVotes=0;
        if(votingYear==2016 && votingType.equals("Presidential")){
            totalVotes=getPresRep2016()+getPresOthers2016()+getPresDem2016();
        }
        if(votingYear==2016 && votingType.equals("Congressional")){
           totalVotes= getCongressionalDem2016()+getCongressionalRep2016()+getCongressionalOthers2016();
        }
        if(votingYear==2018 && votingType.equals("Congressional")){
            totalVotes= getCongressionalDem2018()+getCongressionalRep2018()+getCongressionalOthers2018();
        }
        return totalVotes;
    }

    public Party getWinningParty() {
        return winningParty;
    }

    public void setWinningParty(Party winningParty) {
        this.winningParty = winningParty;
    }

    public double getWinningPartyPercentage() {
        return winningPartyPercentage;
    }

    public void setWinningPartyPercentage(double winningPartyPercentage) {
        this.winningPartyPercentage = winningPartyPercentage;
    }
    protected Object clone() throws CloneNotSupportedException {
        ElectionData ed= (ElectionData)super.clone();
        return ed;
    }
}
