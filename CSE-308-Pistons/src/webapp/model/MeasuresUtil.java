package webapp.model;

public class MeasuresUtil {

    public static double calculateMeasure(Measure measure, BaseDistrict district) {
        switch (measure) {
            case PARTISAN_FAIRNESS:
                return calculatePartisanFairness(district);
            case COMPACTNESS:
                return calculateCompactness(district);
            case EFFICIENCY_GAP:
                return calculateEfficiencyGap(district);
            case POPULATION_EQUALITY:
                return calculatePopulationEquality(district);
            case COMPETITIVENESS:
                return calculateCompetitiveness(district);
            case GERRYMANDER_REPUBLICAN:
                return calculateGerryManderGOP(district);
            case POPULATION_HOMOGENEITY:
                return calculatePopulationHomogeneity(district);
            case GERRYMANDER_DEMOCRAT:
                return calculateGerryManderDEM(district);
            default:
                return -1;
        }
    }

    /*
            Partisan fairness:
            100% - underrepresented party's winning margin
            OR
            underrepresented party's losing margin
            (We want our underrepresented party to either win by a little or lose by a lot - fewer wasted votes)
        */
    public static double calculatePartisanFairness(BaseDistrict d) {
        // Temporary section
        int totalVote = 0;
        int totalGOPvote = 0;
        int totalDistricts = 0;
        int totalGOPDistricts = 0;
        BaseState state = d.getState();
        for (BaseDistrict sd : state.getDistricts()) {
            totalVote += sd.getVotes().get(Party.REPUBLICAN);
            totalVote += sd.getVotes().get(Party.DEMOCRAT);
            totalGOPvote += sd.getVotes().get(Party.REPUBLICAN);
            totalDistricts += 1;
            if (sd.getVotes().get(Party.REPUBLICAN) > sd.getVotes().get(Party.DEMOCRAT)) {
                totalGOPDistricts += 1;
            }
        }
        int idealDistrictChange = ((int) Math.round(totalDistricts * ((1.0 * totalGOPvote) / totalVote))) - totalGOPDistricts;
        // End temporary section
        if (idealDistrictChange == 0) {
            return 1.0;
        }
        int gv = d.getVotes().get(Party.REPUBLICAN);
        int dv = d.getVotes().get(Party.DEMOCRAT);
        int tv = gv + dv;
        int margin = gv - dv;
        if (tv == 0) {
            return 1.0;
        }
        int win_v = Math.max(gv, dv);
        int loss_v = Math.min(gv, dv);
        int inefficient_V;
        if (idealDistrictChange * margin > 0) {
            inefficient_V = win_v - loss_v;
        } else {
            inefficient_V = loss_v;
        }
        return 1.0 - ((inefficient_V * 1.0) / tv);
    }


    /*
    Compactness:
    perimeter / (circle perimeter for same area)
    */
    public static double calculateCompactness(BaseDistrict d) {
        double internalEdges = d.getInternalEdges();
        double totalEdges = internalEdges + d.getExternalEdges();
        return internalEdges / totalEdges;
    }


    /*
    Wasted votes:
    Statewide: abs(Winning party margin - losing party votes)
    */
    public static double calculateEfficiencyGap(BaseDistrict d) {
        int iv_g = 0;
        int iv_d = 0;
        int tv = 0;
        BaseState state = d.getState();
        for (BaseDistrict sd : state.getDistricts()) {
            int gv = d.getVotes().get(Party.REPUBLICAN);
            int dv = d.getVotes().get(Party.DEMOCRAT);
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
        return 1.0 - ((Math.abs(iv_g - iv_d) * 1.0) / tv);
    }


    public static double calculatePopulationEquality(BaseDistrict d) {
        BaseState state = d.getState();
        int idealPopulation = state.getPopulation() / state.getDistricts().size();
        int truePopulation = d.getPopulation();
        if (idealPopulation >= truePopulation) {
            return ((double) truePopulation) / idealPopulation;
        }
        return ((double) idealPopulation) / truePopulation;
    }


    /*
    COMPETITIVENESS:
    1.0 - margin of victory
    */
    public static double calculateCompetitiveness(BaseDistrict d) {
        int gv = d.getVotes().get(Party.REPUBLICAN);
        int dv = d.getVotes().get(Party.DEMOCRAT);
        return 1.0 - (Math.abs(gv - dv) / (gv + dv));
    }


    /*
        GERRYMANDER_REPUBLICAN:
        Partisan fairness, but always working in the GOP's favor
    */
    public static double calculateGerryManderGOP(BaseDistrict d) {
        int gv = d.getVotes().get(Party.REPUBLICAN);
        int dv = d.getVotes().get(Party.DEMOCRAT);
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

    //calculate square error of population, normalized to 0,1
    public static double calculatePopulationHomogeneity(BaseDistrict district) {
        if (district.getPrecincts().size() == 0)
            return 0;
        double sum = district.getPrecincts().stream().mapToDouble(BasePrecinct::getPopulation).sum();
        final double mean = sum / district.getPrecincts().size();
        double sqError = district.getPrecincts()
                .stream().mapToDouble(
                        (precinct) -> (Math.pow(precinct.getPopulation() - mean, 2))
                ).sum();
        sqError /= (district.getPrecincts().size());
        double averagePopulation = 2000;


        return Math.tanh(sqError / (averagePopulation * 200));
    }


    /*
            GERRYMANDER_DEMOCRAT:
            Partisan fairness, but always working in the DNC's favor
    */
    public static double calculateGerryManderDEM(BaseDistrict d) {
        int gv = d.getVotes().get(Party.REPUBLICAN);
        int dv = d.getVotes().get(Party.DEMOCRAT);
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

}
