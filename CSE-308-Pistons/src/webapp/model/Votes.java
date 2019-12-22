package webapp.model;

import java.util.Map;

public class Votes {

    private String election;
    private Map<Party, Long> votes;

    public String getElection() {
        return election;
    }

    public void setElection(String election) {
        this.election = election;
    }

    public Map<Party, Long> getVotes() {
        return votes;
    }

    public void setVotes(Map<Party, Long> votes) {
        this.votes = votes;
    }
}
