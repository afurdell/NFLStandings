package com.furdell.nfl;

import java.util.*;

public class Division extends ArrayList<Team> {
    private final String name;

    public Division(String name, List<Team> teams) {
        addAll(teams);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void sort() {
        RankedTeam[] rankedTeams = new RankedTeam[size()];
        for (int i = 0; i < size(); i++) {
            Team team = get(i);
            rankedTeams[i] = new RankedTeam(team, team.winPercentage());
        }
        Arrays.sort(rankedTeams);
        for (int i = 0; i < size()-1; i++) {
            int ties = calculateTies(Arrays.copyOfRange(rankedTeams, i, rankedTeams.length));
            if (ties == 1) {
                if (rankedTeams[i].getTeam().compareTo(rankedTeams[i+1].getTeam()) < 0) {
                    swap(rankedTeams, i, i+1);
                }
            } else if (ties > 1) {
                RankedTeam[] resolvedTies = resolveTies(Arrays.copyOfRange(rankedTeams, i, i+ties+1));
                System.arraycopy(resolvedTies, i, rankedTeams, i + i, resolvedTies.length - i);
            }
        }
        this.removeAll(this);
        for (int i = 0; i < rankedTeams.length; i++) {
            this.add(i, rankedTeams[i].getTeam());
        }
    }

    private RankedTeam[] resolveTies(RankedTeam[] tiedRankedTeams) {
        Set<Team> tiedTeams = new HashSet<>();
        for (RankedTeam tiedRankedTeam : tiedRankedTeams) {
            tiedTeams.add(tiedRankedTeam.getTeam());
        }
        for (RankedTeam tiedRankedTeam : tiedRankedTeams) {
            Double rankValue = tiedRankedTeam.getTeam().winPercentage(tiedTeams);
            tiedRankedTeam.setRankedValue(rankValue, "Head-To-Head Win-Loss-Draw Percentage Against Multiple Teams");
        }
        Arrays.sort(tiedRankedTeams);
        int ties = calculateTies(tiedRankedTeams);
        if (ties == 0) {
            return tiedRankedTeams;
        } else if (ties == 1) {
            if (tiedRankedTeams[0].getTeam().compareTo(tiedRankedTeams[1].getTeam()) < 0) {
                swap(tiedRankedTeams, 0, 1);
            }
        } else if (ties > 1) {
            for (RankedTeam tiedRankedTeam : tiedRankedTeams) {
                Double rankValue = tiedRankedTeam.getTeam().divisionalWinPercentage();
                tiedRankedTeam.setRankedValue(rankValue, "Divisional Win-Loss-Draw Percentage");
            }
        }
        Arrays.sort(tiedRankedTeams);
        ties = calculateTies(tiedRankedTeams);
        if (ties == 0) {
            return tiedRankedTeams;
        } else if (ties == 1) {
            if (tiedRankedTeams[0].getTeam().compareTo(tiedRankedTeams[1].getTeam()) < 0) {
                swap(tiedRankedTeams, 0, 1);
            }
        } else if (ties > 1) {
            assignRanksByCommonGames(Arrays.copyOfRange(tiedRankedTeams, 0, ties+1));
        }
        Arrays.sort(tiedRankedTeams);
        ties = calculateTies(tiedRankedTeams);
        if (ties == 0) {
            return tiedRankedTeams;
        } else if (ties == 1) {
            if (tiedRankedTeams[0].getTeam().compareTo(tiedRankedTeams[1].getTeam()) < 0) {
                swap(tiedRankedTeams, 0, 1);
            }
        } else if (ties > 1) {
            for (RankedTeam tiedRankedTeam : tiedRankedTeams) {
                Double rankValue = tiedRankedTeam.getTeam().conferenceWinPercentage();
                tiedRankedTeam.setRankedValue(rankValue, "Conference Win-Loss-Draw Percentage");
            }
        }
        Arrays.sort(tiedRankedTeams);
        ties = calculateTies(tiedRankedTeams);
        if (ties == 0) {
            return tiedRankedTeams;
        } else if (ties == 1) {
            if (tiedRankedTeams[0].getTeam().compareTo(tiedRankedTeams[1].getTeam()) > 0) {
                swap(tiedRankedTeams, 0, 1);
            }
        } else if (ties > 1) {
            for (RankedTeam rankedTeam : tiedRankedTeams) {
                rankedTeam.getTeam().setMessage("unresolved multiway tie");
            }
        }
        return tiedRankedTeams;
    }

    private void assignRanksByCommonGames(RankedTeam[] rankedTeams) {
        Set<Team> commonTeams = rankedTeams[0].getTeam().opponents();
        for (int i = 1; i < rankedTeams.length; i++) {
            commonTeams.retainAll(rankedTeams[i].getTeam().opponents());
        }
        for (RankedTeam rankedTeam: rankedTeams) {
            Double rankedValue = rankedTeam.getTeam().winPercentage(commonTeams);
            rankedTeam.setRankedValue(rankedValue, "Win-Loss-Draw Percentage, Games in Common With Multiple Opponents");

        }
    }

    private int calculateTies(RankedTeam[] rankedTeams) {
        int ties = 0;
        Double valueToTie = rankedTeams[0].getRankedValue();
        String valueDescription = rankedTeams[0].getRankDescription();
        for (int j = 1; j <rankedTeams.length; j++) {
            if (rankedTeams[j].getRankedValue().equals(valueToTie) && rankedTeams[j].getRankDescription().equals(valueDescription)) {
                ties++;
            }
        }
        return ties;
    }

    private void swap(RankedTeam[] rankedTeams, int i, int j) {
        RankedTeam temp = rankedTeams[i];
        rankedTeams[i] = rankedTeams[j];
        rankedTeams[j] = temp;
    }

}
