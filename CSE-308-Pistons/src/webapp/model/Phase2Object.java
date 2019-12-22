package webapp.model;

import java.io.Serializable;

public class Phase2Object implements Serializable {
    private int pickedPrecinctID;
    private int sourceDistrictID;
    private int targetDistrictID;
    private double targetOldObjFunc;
    private double targetNewObjFunc;
    private boolean makeMove;

    public int getPickedPrecinctID() {
        return pickedPrecinctID;
    }

    public void setPickedPrecinctID(int pickedPrecinctID) {
        this.pickedPrecinctID = pickedPrecinctID;
    }

    public int getSourceDistrictID() {
        return sourceDistrictID;
    }

    public void setSourceDistrictID(int sourceDistrictID) {
        this.sourceDistrictID = sourceDistrictID;
    }

    public int getTargetDistrictID() {
        return targetDistrictID;
    }

    public void setTargetDistrictID(int targetDistrictID) {
        this.targetDistrictID = targetDistrictID;
    }

    public double getTargetOldObjFunc() {
        return targetOldObjFunc;
    }

    public void setTargetOldObjFunc(double targetOldObjFunc) {
        this.targetOldObjFunc = targetOldObjFunc;
    }

    public double getTargetNewObjFunc() {
        return targetNewObjFunc;
    }

    public void setTargetNewObjFunc(double targetNewObjFunc) {
        this.targetNewObjFunc = targetNewObjFunc;
    }

    public boolean isMakeMove() {
        return makeMove;
    }

    public void setMakeMove(boolean makeMove) {
        this.makeMove = makeMove;
    }
}
