package com.furdell.nfl;

public class RankedTeam implements Comparable {
    private final Team team;
    private Double rankedValue;

    private String rankDescription;

    public RankedTeam(Team team, Double rankedValue, String rankDescription) {
        this.team = team;
        this.rankedValue = rankedValue;
        this.rankDescription = rankDescription;
    }

    public Team getTeam() {
        return team;
    }

    public Double getRankedValue() {
        return rankedValue;
    }

    public void setRankedValue(Double rankedValue, String rankDescription) {
        this.rankedValue = rankedValue;
        this.rankDescription = rankDescription;
    }


    public String getRankDescription() {
        return rankDescription;
    }

    @Override
    public int compareTo(Object o) {
        RankedTeam other = (RankedTeam) o;
        getTeam().setMessage(rankDescription + ": " + rankedValue);
        other.getTeam().setMessage(other.rankDescription + ": " + other.rankedValue);
        return rankedValue.compareTo(other.rankedValue) * -1;
    }
}
