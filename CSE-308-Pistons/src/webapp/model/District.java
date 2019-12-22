package webapp.model;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class District  {
    private int districtID;
    private String stateName;
    private int demographicID;
    private String coordinates;
    private int electionDataID;
    private Demographic demographic;
    private ElectionData electionData;
    private ArrayList<Integer> neighborDistrct = new ArrayList<>();
    private double gerryManderScore;

    public District(){}

    @Id
    @Column(name = "districtID")
    public int getDistrictID(){
        return districtID;
    }

    public void setDistrictID(int districtID){
        this.districtID = districtID;
    }

    @Column(name = "stateName")
    public String getStateName(){
        return this.stateName;
    }

    public void setStateName(String name){
        this.stateName = name;
    }

    @Column(name = "demographicID")
    public int getDemographicID(){
        return this.demographicID;
    }

    public void setDemographicID(int id){
        this.demographicID = id;
    }

    @Column(name = "coordinates")
    public String getCoordinates(){
        return this.coordinates;
    }

    public void setCoordinates(String coordinates){
        this.coordinates = coordinates;
    }

    @Column(name = "electionDataID")
    public  int getElectionDataID(){
        return this.electionDataID;
    }

    public void setElectionDataID(int id){
        this.electionDataID=id;
    }

    @Transient
    public Demographic getDemographic(){
        return demographic;
    }
    public void setDemographic(Demographic demographic){
        this.demographic = demographic;
    }

    @Transient
    public ElectionData getElectionData(){
        return electionData;
    }
    public void setElectionData(ElectionData electionData){
        this.electionData = electionData;
    }


    public void setNeighborDistrct(ArrayList<Integer> e){
        neighborDistrct = e;
    }

    public double getGerryManderScore() {
        return gerryManderScore;
    }

    public void setGerryManderScore(double gerryManderScore) {
        this.gerryManderScore = gerryManderScore;
    }
    public double rateEfficiencyGap(District sd) {
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
    public double gerryDemo(District sd) {
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

    public double gerryRep(District sd) {
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
    private int getGv(District sd) {
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

    private int getDv(District sd) {
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
}
