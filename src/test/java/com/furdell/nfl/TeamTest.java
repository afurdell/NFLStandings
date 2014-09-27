package com.furdell.nfl;

import com.furdell.nfl.Conference;
import com.furdell.nfl.Region;
import com.furdell.nfl.Team;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TeamTest {

    @Test
    public void teamsComparedByWinLossDrawPercentage() {
        Team team1 = new Team("team1", Conference.AFC, Region.EAST);
        Team team2 = new Team("team2",Conference.AFC, Region.EAST);
        team1.recordWin(team2);
        team2.recordLoss(team1);
        assertTrue(team1.winPercentage() > team2.winPercentage());
        assertEquals(-1, team2.compareTo(team1));
        assertEquals("Overall Win-Loss-Draw Percentage: " + team1.winPercentage().toString(), team1.getMessage());
        assertEquals("Overall Win-Loss-Draw Percentage: " + team2.winPercentage().toString(), team2.getMessage());
        assertEquals(1, team1.compareTo(team2));
    }

    @Test
    public void firstTieBreakerIsHeadToHeadWinLossDrawPercentage() {
        Team team1 = new Team("team1",Conference.AFC, Region.EAST);
        Team team2 = new Team("team2",Conference.AFC, Region.EAST);
        Team otherTeam = new Team("otherTeam",Conference.AFC, Region.EAST);
        team1.recordLoss(otherTeam);
        team1.recordWin(team2);
        team2.recordLoss(team1);
        team2.recordWin(otherTeam);
        assertEquals(team1.winPercentage(), team2.winPercentage());
        assertTrue(team1.winPercentage(team2) > team2.winPercentage(team1));
        assertEquals(-1, team2.compareTo(team1));
        assertEquals("Head-To-Head Win-Loss-Draw Percentage Against " + team2.getName() + ": " + team1.winPercentage(team2).toString(), team1.getMessage());
        assertEquals("Head-To-Head Win-Loss-Draw Percentage Against " + team1.getName() + ": " + team2.winPercentage(team1).toString(), team2.getMessage());
        assertEquals(1, team1.compareTo(team2));
    }

    @Test
    public void secondTieBreakerIsDivisionWinLossDrawPercentage() {
        Team team1 = new Team("team1",Conference.AFC, Region.EAST);
        Team team2 = new Team("team2",Conference.AFC, Region.EAST);
        Team sameDivisionTeam = new Team("sameDivisionTeam",Conference.AFC, Region.EAST);
        Team otherDivisionTeam = new Team("otherDivisionTeam",Conference.NFC, Region.SOUTH);
        team1.recordLoss(otherDivisionTeam);
        team1.recordWin(sameDivisionTeam);
        team2.recordLoss(otherDivisionTeam);
        team2.recordWin(otherDivisionTeam);
        assertEquals(team1.winPercentage(), team2.winPercentage());
        assertEquals(team1.winPercentage(team2), team2.winPercentage(team1));
        assertTrue(team1.divisionalWinPercentage() > team2.divisionalWinPercentage());
        assertEquals(-1, team2.compareTo(team1));
        assertEquals("Divisional Win-Loss-Draw Percentage: " + team1.divisionalWinPercentage().toString(), team1.getMessage());
        assertEquals("Divisional Win-Loss-Draw Percentage: " + team2.divisionalWinPercentage().toString(), team2.getMessage());
        assertEquals(1, team1.compareTo(team2));
    }

    @Test
    public void thirdTieBreakerIsCommonGamesWinLossDrawPercentage() {
        Team team1 = new Team("team1",Conference.AFC, Region.EAST);
        Team team2 = new Team("team2",Conference.AFC, Region.EAST);
        Team otherTeam1 = new Team("otherTeam1",Conference.NFC, Region.WEST);
        Team otherTeam2 = new Team("otherTeam2",Conference.NFC, Region.WEST);
        Team otherTeam3 = new Team("otherTeam3",Conference.NFC, Region.WEST);
        team1.recordWin(otherTeam1);
        team1.recordLoss(otherTeam2);
        team2.recordWin(otherTeam2);
        team2.recordLoss(otherTeam3);
        assertEquals(team1.winPercentage(), team2.winPercentage());
        assertEquals(team1.winPercentage(team2), team2.winPercentage(team1));
        assertEquals(team1.divisionalWinPercentage(), team2.divisionalWinPercentage());
        Double expectedValue1 = team1.winPercentage(team2.opponents());
        Double expectedValue2 = team2.winPercentage(team1.opponents());
        assertTrue(expectedValue1 < expectedValue2);
        assertEquals(1, team2.compareTo(team1));
        assertEquals("Win-Loss-Draw Percentage, Games in Common With " + team2.getName() + ": " + expectedValue1, team1.getMessage());
        assertEquals("Win-Loss-Draw Percentage, Games in Common With " + team1.getName() + ": " + expectedValue2, team2.getMessage());
        assertEquals(-1, team1.compareTo(team2));
    }

    @Test
    public void fourthTieBreakerIsConferenceWinLossDrawPercentage() {
        Team team1 = new Team("team1",Conference.AFC, Region.EAST);
        Team team2 = new Team("team2",Conference.AFC, Region.EAST);
        Team otherConferenceTeam1 = new Team("otherConferenceTeam1",Conference.NFC, Region.WEST);
        Team otherConferenceTeam2 = new Team("otherConferenceTeam2",Conference.NFC, Region.NORTH);
        Team otherConferenceTeam3 = new Team("otherConferenceTeam2",Conference.NFC, Region.SOUTH);
        Team sameConferenceTeam = new Team("sameConferenceTeam",Conference.AFC, Region.SOUTH);
        team1.recordLoss(otherConferenceTeam1);
        team1.recordWin(otherConferenceTeam2);
        team2.recordLoss(otherConferenceTeam3);
        team2.recordWin(sameConferenceTeam);
        assertEquals(team1.winPercentage(), team2.winPercentage());
        assertEquals(team1.winPercentage(team2), team2.winPercentage(team1));
        assertEquals(team1.divisionalWinPercentage(), team2.divisionalWinPercentage());
        assertEquals(team1.winPercentage(team2.opponents()), team2.winPercentage(team1.opponents()));
        assertTrue(team1.conferenceWinPercentage() < team2.conferenceWinPercentage());
        assertEquals(1, team2.compareTo(team1));
        assertEquals("Conference Win-Loss-Draw Percentage: " + team1.conferenceWinPercentage().toString(), team1.getMessage());
        assertEquals("Conference Win-Loss-Draw Percentage: " + team2.conferenceWinPercentage().toString(), team2.getMessage());
        assertEquals(-1, team1.compareTo(team2));
    }

    @Test
    public void drawsCountHalf() {
        Team team1 = new Team("team1",Conference.AFC, Region.EAST);
        Team team2 = new Team("team2",Conference.AFC, Region.EAST);
        team1.recordDraw(team2);
        assertEquals(0.5, team1.winPercentage(), 0.1);
    }

}
