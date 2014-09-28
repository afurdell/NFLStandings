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
        List<RankedTeam> finalRankings = new ArrayList<>(rankedTeams);
        Collections.fill(finalRankings, null);
        Collections.sort(rankedTeams);
        removeAndStoreUnTiedRankedTeams(rankedTeams, finalRankings);
        if (rankedTeams.size() == 0) {
            rebuildDivision(finalRankings);
            return;
        } else if (rankedTeams.size() == 2) {
            if (rankedTeams.get(0).getTeam().compareTo(rankedTeams.get(1).getTeam()) < 0) {
                Collections.swap(rankedTeams, 0, 1);
            }
            storeTeams(finalRankings, rankedTeams);
            rebuildDivision(finalRankings);
            return;
        } else if (rankedTeams.size() > 2) {
            Set<Team> tiedTeams = new HashSet<>();
            for (RankedTeam tiedRankedTeam : rankedTeams) {
                tiedTeams.add(tiedRankedTeam.getTeam());
            }
            for (RankedTeam tiedRankedTeam : rankedTeams) {
                Double rankValue = tiedRankedTeam.getTeam().winPercentage(tiedTeams);
                tiedRankedTeam.setRankedValue(rankValue, "Head-To-Head Win-Loss-Draw Percentage Against Multiple Teams");
            }
        }
        Collections.sort(rankedTeams);
        removeAndStoreUnTiedRankedTeams(rankedTeams, finalRankings);
        if (rankedTeams.size() == 0) {
            rebuildDivision(finalRankings);
            return;
        } else if (rankedTeams.size() == 2) {
            if (rankedTeams.get(0).getTeam().compareTo(rankedTeams.get(1).getTeam()) < 0) {
                Collections.swap(rankedTeams, 0, 1);
            }
            storeTeams(finalRankings, rankedTeams);
            rebuildDivision(finalRankings);
            return;
        } else if (rankedTeams.size() > 2) {
            for (RankedTeam tiedRankedTeam : rankedTeams) {
                Double rankValue = tiedRankedTeam.getTeam().divisionalWinPercentage();
                tiedRankedTeam.setRankedValue(rankValue, "Divisional Win-Loss-Draw Percentage");
            }
        }
        Collections.sort(rankedTeams);
        removeAndStoreUnTiedRankedTeams(rankedTeams, finalRankings);
        if (rankedTeams.size() == 0) {
            rebuildDivision(finalRankings);
            return;
        } else if (rankedTeams.size() == 2) {
            if (rankedTeams.get(0).getTeam().compareTo(rankedTeams.get(1).getTeam()) < 0) {
                Collections.swap(rankedTeams, 0, 1);
            }
            storeTeams(finalRankings, rankedTeams);
            rebuildDivision(finalRankings);
            return;
        } else if (rankedTeams.size() > 2) {
            assignRanksByCommonGames(rankedTeams);
        }
        Collections.sort(rankedTeams);
        removeAndStoreUnTiedRankedTeams(rankedTeams, finalRankings);
        if (rankedTeams.size() == 0) {
            rebuildDivision(finalRankings);
            return;
        } else if (rankedTeams.size() == 2) {
            if (rankedTeams.get(0).getTeam().compareTo(rankedTeams.get(1).getTeam()) < 0) {
                Collections.swap(rankedTeams, 0, 1);
            }
            storeTeams(finalRankings, rankedTeams);
            rebuildDivision(finalRankings);
            return;
        } else if (rankedTeams.size() > 2) {
            for (RankedTeam tiedRankedTeam : rankedTeams) {
                Double rankValue = tiedRankedTeam.getTeam().conferenceWinPercentage();
                tiedRankedTeam.setRankedValue(rankValue, "Conference Win-Loss-Draw Percentage");
            }
        }
        Collections.sort(rankedTeams);
        removeAndStoreUnTiedRankedTeams(rankedTeams, finalRankings);
        if (rankedTeams.size() == 0) {
            rebuildDivision(finalRankings);
            return;
        } else if (rankedTeams.size() == 2) {
            if (rankedTeams.get(0).getTeam().compareTo(rankedTeams.get(1).getTeam()) < 0) {
                Collections.swap(rankedTeams, 0, 1);
            }
            storeTeams(finalRankings, rankedTeams);
            rebuildDivision(finalRankings);
            return;
        } else if (rankedTeams.size() > 2) {
            for (RankedTeam tiedRankedTeam : rankedTeams) {
                Double rankValue = tiedRankedTeam.getTeam().strengthOfVictory();
                tiedRankedTeam.setRankedValue(rankValue, "Strength of Victory");
            }
        }
        Collections.sort(rankedTeams);
        removeAndStoreUnTiedRankedTeams(rankedTeams, finalRankings);
        if (rankedTeams.size() == 0) {
            rebuildDivision(finalRankings);
            return;
        } else if (rankedTeams.size() == 2) {
            if (rankedTeams.get(0).getTeam().compareTo(rankedTeams.get(1).getTeam()) < 0) {
                Collections.swap(rankedTeams, 0, 1);
            }
            storeTeams(finalRankings, rankedTeams);
            rebuildDivision(finalRankings);
            return;
        } else if (rankedTeams.size() > 2) {
            for (RankedTeam tiedRankedTeam : rankedTeams) {
                Double rankValue = tiedRankedTeam.getTeam().strengthOfSchedule();
                tiedRankedTeam.setRankedValue(rankValue, "Strength of Schedule");
            }
        }
        Collections.sort(rankedTeams);
        removeAndStoreUnTiedRankedTeams(rankedTeams, finalRankings);
        if (rankedTeams.size() == 0) {
            rebuildDivision(finalRankings);
            return;
        } else if (rankedTeams.size() == 2) {
            if (rankedTeams.get(0).getTeam().compareTo(rankedTeams.get(1).getTeam()) < 0) {
                Collections.swap(rankedTeams, 0, 1);
            }
            storeTeams(finalRankings, rankedTeams);
            rebuildDivision(finalRankings);
            return;
        } else if (rankedTeams.size() > 2) {
            for (RankedTeam rankedTeam : rankedTeams) {
                rankedTeam.getTeam().setMessage(UNRESOLVED_TIE_MESSAGE);
            }
        }
        rebuildDivision(rankedTeams);
    }

    private void rebuildDivision(List<RankedTeam> rankedTeams) {
        this.removeAll(this);
        for (int i = 0; i < rankedTeams.size(); i++) {
            this.add(i, rankedTeams.get(i).getTeam());
        }
    }

     private void removeAndStoreUnTiedRankedTeams(List<RankedTeam> tiedRankedTeams, List<RankedTeam> finalRankings) {
        while (topTeamFound(tiedRankedTeams)) {
            RankedTeam topTeam = tiedRankedTeams.get(0);
            setFirstAvailablePosition(finalRankings, topTeam);
            tiedRankedTeams.remove(topTeam);
        }
        while (bottomTeamFound(tiedRankedTeams)) {
            RankedTeam bottomTeam = tiedRankedTeams.get(tiedRankedTeams.size()-1);
            setLastAvailablePosition(finalRankings, bottomTeam);
            tiedRankedTeams.remove(bottomTeam);
        }
    }

    private void storeTeams(List<RankedTeam> rankedTeams, List<RankedTeam> rankedTeamsToStore) {
        for (RankedTeam rankedTeamToStore : rankedTeamsToStore) {
            setFirstAvailablePosition(rankedTeams, rankedTeamToStore);
        }
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

    private boolean topTeamFound(List<RankedTeam> rankedTeams) {
        if (rankedTeams.size() == 0) {
            return false;
        } else if (rankedTeams.size() == 1) {
            return true;
        } else {
            return (!rankedTeams.get(0).getRankedValue().equals(rankedTeams.get(1).getRankedValue()));
        }
    }

    private boolean bottomTeamFound(List<RankedTeam> rankedTeams) {
        if (rankedTeams.size() == 0) {
            return false;
        } else if (rankedTeams.size() == 1) {
            return true;
        } else {
            return (!rankedTeams.get(rankedTeams.size() - 1).getRankedValue().equals(rankedTeams.get(rankedTeams.size() - 2).getRankedValue()));
        }

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
}
