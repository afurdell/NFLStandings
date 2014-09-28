package com.furdell.nfl;

import java.util.*;

public class Division extends ArrayList<Team> {
    private static final String UNRESOLVED_TIE_MESSAGE = "unresolved multiway tie";
    private final String name;

    public Division(String name, List<Team> teams) {
        if (teams.size() != 4) {
            throw new RuntimeException("Divisions must have exactly four teams!");
        }
        Conference conference = teams.get(0).getConference();
        Region region = teams.get(1).getRegion();
        for (int i = 1; i < teams.size(); i++) {
            if (!(teams.get(i).getRegion().equals(region) && teams.get(i).getConference().equals(conference))) {
                throw new RuntimeException("All teams within a division must have the same conference and region!");
            }
        }
        addAll(teams);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void sort() {
        List<RankedTeam> rankedTeams = new ArrayList<>();
        for (Team team : this) {
            rankedTeams.add(new RankedTeam(team, team.winPercentage()));
        }
        Collections.sort(rankedTeams);
        for (int i = 0; i < size()-1; i++) {
            if (!rankedTeams.get(i).getTeam().getMessage().equals(UNRESOLVED_TIE_MESSAGE)) {
                int ties = calculateTies(rankedTeams.subList(i, rankedTeams.size()));
                if (ties == 1) {
                    if (rankedTeams.get(i).getTeam().compareTo(rankedTeams.get(i+1).getTeam()) < 0) {
                        Collections.swap(rankedTeams, i, i + 1);
                    }
                } else if (ties > 1) {
                    List<RankedTeam> copy = new ArrayList<>(rankedTeams);
                    List<RankedTeam> resolvedTies = new ArrayList<>(resolveTies(copy.subList(i, i + ties + 1)));
                    for (int j = 0; j < resolvedTies.size(); j++) {
                        rankedTeams.set(i+j, resolvedTies.get(j));
                    }
                }
            }
        }
        this.removeAll(this);
        for (int i = 0; i < rankedTeams.size(); i++) {
            this.add(i, rankedTeams.get(i).getTeam());
        }
    }

    private List<RankedTeam> resolveTies(List<RankedTeam> tiedRankedTeams) {
        List<RankedTeam> finalRankings = new ArrayList<>();
        for (RankedTeam rankedTeam : tiedRankedTeams) {
            finalRankings.add(null);
        }
        Set<Team> tiedTeams = new HashSet<>();
        for (RankedTeam tiedRankedTeam : tiedRankedTeams) {
            tiedTeams.add(tiedRankedTeam.getTeam());
        }
        for (RankedTeam tiedRankedTeam : tiedRankedTeams) {
            Double rankValue = tiedRankedTeam.getTeam().winPercentage(tiedTeams);
            tiedRankedTeam.setRankedValue(rankValue, "Head-To-Head Win-Loss-Draw Percentage Against Multiple Teams");
        }
        Collections.sort(tiedRankedTeams);
        if (topTeamFound(tiedRankedTeams)) {
            RankedTeam topTeam = tiedRankedTeams.get(0);
            setFirstAvailablePosition(finalRankings, topTeam);
            tiedRankedTeams.remove(topTeam);
        }
        if (bottomTeamFound(tiedRankedTeams)) {
            RankedTeam bottomTeam = tiedRankedTeams.get(tiedRankedTeams.size()-1);
            setLastAvailablePosition(finalRankings, bottomTeam);
            tiedRankedTeams.remove(bottomTeam);
        }
        int ties = calculateTies(tiedRankedTeams);
        if (ties == 0) {
            return assembledList(finalRankings, tiedRankedTeams);
        } else if (ties == 1) {
            if (tiedRankedTeams.get(0).getTeam().compareTo(tiedRankedTeams.get(1).getTeam()) < 0) {
                Collections.swap(tiedRankedTeams, 0, 1);
            }
        } else if (ties > 1) {
            for (RankedTeam tiedRankedTeam : tiedRankedTeams) {
                Double rankValue = tiedRankedTeam.getTeam().divisionalWinPercentage();
                tiedRankedTeam.setRankedValue(rankValue, "Divisional Win-Loss-Draw Percentage");
            }
        }
        Collections.sort(tiedRankedTeams);
        if (topTeamFound(tiedRankedTeams)) {
            RankedTeam topTeam = tiedRankedTeams.get(0);
            setFirstAvailablePosition(finalRankings, topTeam);
            tiedRankedTeams.remove(topTeam);
        }
        if (bottomTeamFound(tiedRankedTeams)) {
            RankedTeam bottomTeam = tiedRankedTeams.get(tiedRankedTeams.size()-1);
            setLastAvailablePosition(finalRankings, bottomTeam);
            tiedRankedTeams.remove(bottomTeam);
        }
        ties = calculateTies(tiedRankedTeams);
        if (ties == 0) {
            return assembledList(finalRankings, tiedRankedTeams);
        } else if (ties == 1) {
            if (tiedRankedTeams.get(0).getTeam().compareTo(tiedRankedTeams.get(1).getTeam()) < 0) {
                Collections.swap(tiedRankedTeams, 0, 1);
            }
        } else if (ties > 1) {
            assignRanksByCommonGames(tiedRankedTeams.subList(0, ties + 1));
        }
        Collections.sort(tiedRankedTeams);
        if (topTeamFound(tiedRankedTeams)) {
            RankedTeam topTeam = tiedRankedTeams.get(0);
            setFirstAvailablePosition(finalRankings, topTeam);
            tiedRankedTeams.remove(topTeam);
        }
        if (bottomTeamFound(tiedRankedTeams)) {
            RankedTeam bottomTeam = tiedRankedTeams.get(tiedRankedTeams.size()-1);
            setLastAvailablePosition(finalRankings, bottomTeam);
            tiedRankedTeams.remove(bottomTeam);
        }
        ties = calculateTies(tiedRankedTeams);
        if (ties == 0) {
            return assembledList(finalRankings, tiedRankedTeams);
        } else if (ties == 1) {
            if (tiedRankedTeams.get(0).getTeam().compareTo(tiedRankedTeams.get(1).getTeam()) < 0) {
                Collections.swap(tiedRankedTeams, 0, 1);
            }
        } else if (ties > 1) {
            for (RankedTeam tiedRankedTeam : tiedRankedTeams) {
                Double rankValue = tiedRankedTeam.getTeam().conferenceWinPercentage();
                tiedRankedTeam.setRankedValue(rankValue, "Conference Win-Loss-Draw Percentage");
            }
        }
        Collections.sort(tiedRankedTeams);
        if (topTeamFound(tiedRankedTeams)) {
            RankedTeam topTeam = tiedRankedTeams.get(0);
            setFirstAvailablePosition(finalRankings, topTeam);
            tiedRankedTeams.remove(topTeam);
        }
        if (bottomTeamFound(tiedRankedTeams)) {
            RankedTeam bottomTeam = tiedRankedTeams.get(tiedRankedTeams.size()-1);
            setLastAvailablePosition(finalRankings, bottomTeam);
            tiedRankedTeams.remove(bottomTeam);
        }
        ties = calculateTies(tiedRankedTeams);
        if (ties == 0) {
            return assembledList(finalRankings, tiedRankedTeams);
        } else if (ties == 1) {
            if (tiedRankedTeams.get(0).getTeam().compareTo(tiedRankedTeams.get(1).getTeam()) < 0) {
                Collections.swap(tiedRankedTeams, 0, 1);
            }
        } else if (ties > 1) {
            for (RankedTeam tiedRankedTeam : tiedRankedTeams) {
                Double rankValue = tiedRankedTeam.getTeam().strengthOfVictory();
                tiedRankedTeam.setRankedValue(rankValue, "Strength of Victory");
            }
        }
        Collections.sort(tiedRankedTeams);
        if (topTeamFound(tiedRankedTeams)) {
            RankedTeam topTeam = tiedRankedTeams.get(0);
            setFirstAvailablePosition(finalRankings, topTeam);
            tiedRankedTeams.remove(topTeam);
        }
        if (bottomTeamFound(tiedRankedTeams)) {
            RankedTeam bottomTeam = tiedRankedTeams.get(tiedRankedTeams.size()-1);
            setLastAvailablePosition(finalRankings, bottomTeam);
            tiedRankedTeams.remove(bottomTeam);
        }
        ties = calculateTies(tiedRankedTeams);
        if (ties == 0) {
            return assembledList(finalRankings, tiedRankedTeams);
        } else if (ties == 1) {
            if (tiedRankedTeams.get(0).getTeam().compareTo(tiedRankedTeams.get(1).getTeam()) < 0) {
                Collections.swap(tiedRankedTeams, 0, 1);
            }
        } else if (ties > 1) {
            for (RankedTeam tiedRankedTeam : tiedRankedTeams) {
                Double rankValue = tiedRankedTeam.getTeam().strengthOfSchedule();
                tiedRankedTeam.setRankedValue(rankValue, "Strength of Schedule");
            }
        }
        Collections.sort(tiedRankedTeams);
        if (topTeamFound(tiedRankedTeams)) {
            RankedTeam topTeam = tiedRankedTeams.get(0);
            setFirstAvailablePosition(finalRankings, topTeam);
            tiedRankedTeams.remove(topTeam);
        }
        if (bottomTeamFound(tiedRankedTeams)) {
            RankedTeam bottomTeam = tiedRankedTeams.get(tiedRankedTeams.size()-1);
            setLastAvailablePosition(finalRankings, bottomTeam);
            tiedRankedTeams.remove(bottomTeam);
        }
        ties = calculateTies(tiedRankedTeams);
        if (ties == 0) {
            return assembledList(finalRankings, tiedRankedTeams);
        } else if (ties == 1) {
            if (tiedRankedTeams.get(0).getTeam().compareTo(tiedRankedTeams.get(1).getTeam()) < 0) {
                Collections.swap(tiedRankedTeams, 0, 1);
            }
        } else if (ties > 1) {
            for (RankedTeam rankedTeam : tiedRankedTeams) {
                rankedTeam.getTeam().setMessage(UNRESOLVED_TIE_MESSAGE);
            }
        }
        return assembledList(finalRankings, tiedRankedTeams);
    }

    private void setLastAvailablePosition(List<RankedTeam> rankedTeams, RankedTeam bottomTeam) {
        for (int i = rankedTeams.size()-1; i >= 0; i--) {
            if (rankedTeams.get(i) == null) {
                rankedTeams.set(i, bottomTeam);
                break;
            }
        }
    }

    private void setFirstAvailablePosition(List<RankedTeam> rankedTeams, RankedTeam topTeam) {
        for (int i = 0; i < rankedTeams.size(); i++) {
            if (rankedTeams.get(i) == null) {
                rankedTeams.set(i, topTeam);
                break;
            }
        }
    }

    private List<RankedTeam> assembledList(List<RankedTeam> rankedTeams, List<RankedTeam> teamsToInsert) {
        int j = 0;
        List<RankedTeam> assembledList = new ArrayList<>();
        for (int i = 0; i < rankedTeams.size(); i++) {
            if (rankedTeams.get(i) == null) {
                assembledList.add(teamsToInsert.get(j++));
            } else {
                assembledList.add(rankedTeams.get(i));
            }
        }
        return assembledList;
    }

    private boolean bottomTeamFound(List<RankedTeam> rankedTeams) {
        return (!rankedTeams.get(rankedTeams.size()-1).getRankedValue().equals(rankedTeams.get(rankedTeams.size()-2).getRankedValue()));

    }

    private boolean topTeamFound(List<RankedTeam> rankedTeams) {
        return (!rankedTeams.get(0).getRankedValue().equals(rankedTeams.get(1).getRankedValue()));
    }

    private void assignRanksByCommonGames(List<RankedTeam> rankedTeams) {
        Set<Team> commonTeams = new HashSet<>();
        commonTeams.addAll(rankedTeams.get(0).getTeam().opponents());
        for (int i = 1; i < rankedTeams.size(); i++) {
            commonTeams.retainAll(rankedTeams.get(i).getTeam().opponents());
        }
        for (RankedTeam rankedTeam: rankedTeams) {
            Double rankedValue = rankedTeam.getTeam().winPercentage(commonTeams);
            rankedTeam.setRankedValue(rankedValue, "Win-Loss-Draw Percentage, Games in Common With Multiple Opponents");

        }
    }

    private int calculateTies(List<RankedTeam> rankedTeams) {
        int ties = 0;
        Double valueToTie = rankedTeams.get(0).getRankedValue();
        String valueDescription = rankedTeams.get(0).getRankDescription();
        for (int j = 1; j <rankedTeams.size(); j++) {
            if (rankedTeams.get(j).getRankedValue().equals(valueToTie) && rankedTeams.get(j).getRankDescription().equals(valueDescription)) {
                ties++;
            }
        }
        return ties;
    }
}
