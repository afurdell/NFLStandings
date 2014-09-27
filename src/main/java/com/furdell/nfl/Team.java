package com.furdell.nfl;

import java.util.*;

public class Team implements Comparable {
    private final String name;
    private final Conference conference;
    private final Region region;

    private WinRecord totalWinRecord = new WinRecord();
    private WinRecord divisionalWinRecord = new WinRecord();
    private WinRecord conferenceWinRecord = new WinRecord();
    private Map<Team, WinRecord> winRecordsByTeam = new HashMap<>();
    private String message = "";

    public Team(String name, Conference conference, Region region) {
        this.name = name;
        this.conference = conference;
        this.region = region;
    }

    public String getName() {
        return name;
    }

    public void recordWin(Team opponent) {
        totalWinRecord.recordWin();
        if (isSameConference(opponent)) {
            conferenceWinRecord.recordWin();
            if (isSameDivision(opponent)) {
                divisionalWinRecord.recordWin();
            }
        }
        initializeOpponent(opponent);
        winRecordsByTeam.get(opponent).recordWin();
    }

    public void recordLoss(Team opponent) {
        totalWinRecord.recordLoss();
        if (isSameConference(opponent)) {
            conferenceWinRecord.recordLoss();
            if (isSameDivision(opponent)) {
                divisionalWinRecord.recordLoss();
            }
        }
        initializeOpponent(opponent);
        winRecordsByTeam.get(opponent).recordLoss();
    }

    public void recordDraw(Team opponent) {
        totalWinRecord.recordDraw();
        if (isSameConference(opponent)) {
            conferenceWinRecord.recordDraw();
            if (isSameDivision(opponent)) {
                divisionalWinRecord.recordDraw();
            }
        }
        initializeOpponent(opponent);
        winRecordsByTeam.get(opponent).recordDraw();
    }

    private boolean isSameDivision(Team opponent) {
        return isSameConference(opponent) && opponent.getRegion().equals(getRegion());
    }

    private boolean isSameConference(Team opponent) {
        return opponent.getConference().equals(getConference());
    }

    private void initializeOpponent(Team opponent) {
        if (!winRecordsByTeam.containsKey(opponent)) {
            winRecordsByTeam.put(opponent, new WinRecord());
        }
    }

    protected Double winPercentage() {
        return totalWinRecord.winPercentage();
    }

    public String winRecord() {
        return totalWinRecord.toString();
    }

    protected Double winPercentage(Team opponent) {
        if (winRecordsByTeam.containsKey(opponent)) {
            return winRecordsByTeam.get(opponent).winPercentage();
        } else {
            return 0.0;
        }
    }

    protected Double winPercentage(Set<Team> opponents) {
        Set<WinRecord> winRecords = new HashSet<>();
        for (Team opponent : opponents) {
            if (winRecordsByTeam.containsKey(opponent)) {
                winRecords.add(winRecordsByTeam.get(opponent));
            }
        }
        return WinRecord.combinedWinRecord(winRecords).winPercentage();
    }

    private Double strengthOfVictory() {
        Set<WinRecord> defeatedOpponentsWinRecords = new HashSet<>();
        for (Team team : winRecordsByTeam.keySet()) {
            if (winRecordsByTeam.get(team).getWins() > 0) {
                defeatedOpponentsWinRecords.add(team.getTotalWinRecord());
            }
        }
        return WinRecord.combinedWinRecord(defeatedOpponentsWinRecords).winPercentage();
    }

    private Double strengthOfSchedule() {
        Set <WinRecord> opponentsWinRecords = new HashSet<>();
        for (Team team : opponents()) {
            opponentsWinRecords.add(team.getTotalWinRecord());
        }
        return WinRecord.combinedWinRecord(opponentsWinRecords).winPercentage();
    }

    public Double divisionalWinPercentage() {
        return divisionalWinRecord.winPercentage();
    }

    public Double conferenceWinPercentage() {
        return conferenceWinRecord.winPercentage();
    }

    public Set<Team> opponents() {
        return winRecordsByTeam.keySet();
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Conference getConference() {
        return conference;
    }

    public Region getRegion() {
        return region;
    }

    private WinRecord getTotalWinRecord() { return  totalWinRecord; }

    @Override
    public int compareTo(Object o) {
        if (getMessage().startsWith("unresolved")) {
            return 0;
        }
        Team other = (Team) o;
        Double rankValue = winPercentage();
        Double otherRankValue = other.winPercentage();
        int comparison = rankValue.compareTo(otherRankValue);
//        setMessage("Overall Win-Loss-Draw Percentage: " + rankValue);
//        other.setMessage("Overall Win-Loss-Draw Percentage: " + otherRankValue);
        if (comparison == 0) {
            rankValue = winPercentage(other);
            otherRankValue = other.winPercentage(this);
            comparison = rankValue.compareTo(otherRankValue);
            setMessage("Head-To-Head Win-Loss-Draw Percentage Against " + other.getName() + ": " + rankValue);
            other.setMessage("Head-To-Head Win-Loss-Draw Percentage Against " + getName() + ": " + otherRankValue);
        }
        if (comparison == 0) {
            rankValue = divisionalWinPercentage();
            otherRankValue = other.divisionalWinPercentage();
            comparison = rankValue.compareTo(otherRankValue);
            setMessage("Divisional Win-Loss-Draw Percentage: " + rankValue);
            other.setMessage("Divisional Win-Loss-Draw Percentage: " + otherRankValue);
        }
        if (comparison == 0) {
            rankValue = winPercentage(other.opponents());
            otherRankValue = other.winPercentage(opponents());
            comparison = rankValue.compareTo(otherRankValue);
            setMessage("Win-Loss-Draw Percentage, Games in Common With " + other.getName() + ": " + rankValue);
            other.setMessage("Win-Loss-Draw Percentage, Games in Common With " + getName() + ": " + otherRankValue);
        }
        if (comparison == 0) {
            rankValue = conferenceWinPercentage();
            otherRankValue = other.conferenceWinPercentage();
            comparison = rankValue.compareTo(otherRankValue);
            setMessage("Conference Win-Loss-Draw Percentage: " + rankValue);
            other.setMessage("Conference Win-Loss-Draw Percentage: " + otherRankValue);
        }
        if (comparison == 0) {
            rankValue = strengthOfVictory();
            otherRankValue = other.strengthOfVictory();
            comparison = rankValue.compareTo(otherRankValue);
            setMessage("Strength of Victory: " + rankValue);
            other.setMessage("Strength of Victory: " + otherRankValue);
        }
        if (comparison == 0) {
            rankValue = strengthOfSchedule();
            otherRankValue = other.strengthOfSchedule();
            comparison = rankValue.compareTo(otherRankValue);
            setMessage("Strength of Schedule: " + rankValue);
            other.setMessage("Strength of Schedule: " + otherRankValue);
        }
        if (comparison == 0) {
            setMessage("unresolved tie");
            other.setMessage("unresolved tie");
        }
        return comparison;
    }
}
