package com.furdell.nfl;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TeamTest {

    @Test
    public void teamsComparedByWinLossDrawPercentage() {
        Team team1 = new Team("team1", Conference.AFC, Region.EAST);
        Team team2 = new Team("team2",Conference.AFC, Region.EAST);
        team1.recordGame(team2, 3, 2);
        team2.recordGame(team1, 2, 3);
        assertTrue(team1.winPercentage() > team2.winPercentage());
        assertEquals(-1, team2.compareTo(team1));
        assertTrue(team1.getMessage().isEmpty());
        assertTrue(team2.getMessage().isEmpty());
        assertEquals(1, team1.compareTo(team2));
    }

    @Test
    public void firstTieBreakerIsHeadToHeadWinLossDrawPercentage() {
        Team team1 = new Team("team1",Conference.AFC, Region.EAST);
        Team team2 = new Team("team2",Conference.AFC, Region.EAST);
        Team otherTeam = new Team("otherTeam",Conference.AFC, Region.EAST);
        team1.recordGame(otherTeam, 2, 3);
        team1.recordGame(team2, 3, 2);
        team2.recordGame(team1, 2, 3);
        team2.recordGame(otherTeam, 3, 2);
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
        team1.recordGame(otherDivisionTeam, 2, 3);
        team1.recordGame(sameDivisionTeam, 3, 2);
        team2.recordGame(otherDivisionTeam, 2, 3);
        team2.recordGame(otherDivisionTeam, 3, 2);
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
        team1.recordGame(otherTeam1, 3, 2);
        team1.recordGame(otherTeam2, 2, 3);
        team2.recordGame(otherTeam2, 3, 2);
        team2.recordGame(otherTeam3, 2, 3);
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
        team1.recordGame(otherConferenceTeam1, 2, 3);
        team1.recordGame(otherConferenceTeam2, 3, 2);
        team2.recordGame(otherConferenceTeam3, 2, 3);
        team2.recordGame(sameConferenceTeam, 3, 2);
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
    public void fifthTieBreakerIsStrengthOfVictory() {
        Team team1 = new Team("team1",Conference.AFC, Region.EAST);
        Team team2 = new Team("team2",Conference.AFC, Region.EAST);
        Team goodTeam = new Team("goodTeam", Conference.NFC, Region.NORTH);
        Team badTeam = new Team("badTeam", Conference.NFC, Region.NORTH);
        for (int i = 0; i < 4; i++) {
            goodTeam.recordGame(badTeam, 3, 2);
            badTeam.recordGame(goodTeam, 2, 3);
        }
        team1.recordGame(goodTeam, 3, 2);
        team1.recordGame(badTeam, 2, 3);
        team2.recordGame(badTeam, 3, 2);
        team2.recordGame(goodTeam, 2, 3);
        assertEquals(team1.winPercentage(), team2.winPercentage());
        assertEquals(team1.winPercentage(team2), team2.winPercentage(team1));
        assertEquals(team1.divisionalWinPercentage(), team2.divisionalWinPercentage());
        assertEquals(team1.winPercentage(team2.opponents()), team2.winPercentage(team1.opponents()));
        assertEquals(team1.conferenceWinPercentage(), team2.conferenceWinPercentage());
        assertTrue(goodTeam.winPercentage() > badTeam.winPercentage());
        assertEquals(1, team1.compareTo(team2));
        assertEquals("Strength of Victory: " + goodTeam.winPercentage(), team1.getMessage());
        assertEquals("Strength of Victory: " + badTeam.winPercentage(), team2.getMessage());
        assertEquals(-1, team2.compareTo(team1));
    }

    @Test
    public void sixthTieBreakerIsStrengthOfSchedule() {
        Team team1 = new Team("team1",Conference.AFC, Region.EAST);
        Team team2 = new Team("team2",Conference.AFC, Region.EAST);
        Team goodTeam = new Team("goodTeam", Conference.NFC, Region.NORTH);
        Team badTeam = new Team("badTeam", Conference.NFC, Region.NORTH);
        for (int i = 0; i < 4; i++) {
            goodTeam.recordGame(badTeam, 3, 2);
            badTeam.recordGame(goodTeam, 2, 3);
        }
        team1.recordGame(goodTeam, 2, 3);
        team2.recordGame(badTeam, 2, 3);
        assertEquals(team1.winPercentage(), team2.winPercentage());
        assertEquals(team1.winPercentage(team2), team2.winPercentage(team1));
        assertEquals(team1.divisionalWinPercentage(), team2.divisionalWinPercentage());
        assertEquals(team1.winPercentage(team2.opponents()), team2.winPercentage(team1.opponents()));
        assertEquals(team1.conferenceWinPercentage(), team2.conferenceWinPercentage());
        assertTrue(goodTeam.winPercentage() > badTeam.winPercentage());
        assertEquals(1, team1.compareTo(team2));
        assertEquals("Strength of Schedule: " + goodTeam.winPercentage(), team1.getMessage());
        assertEquals("Strength of Schedule: " + badTeam.winPercentage(), team2.getMessage());
        assertEquals(-1, team2.compareTo(team1));
    }

    @Test
    public void drawsCountHalf() {
        Team team1 = new Team("team1",Conference.AFC, Region.EAST);
        Team team2 = new Team("team2",Conference.AFC, Region.EAST);
        team1.recordGame(team2, 3, 3);
        assertEquals(0.5, team1.winPercentage(), 0.1);
    }
}
