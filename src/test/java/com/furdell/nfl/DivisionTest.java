package com.furdell.nfl;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DivisionTest {

    private Team team1;
    private Team team2;
    private Team team3;
    private Team team4;
    private Team sameConferenceTeam;
    private Team otherConferenceTeam;
    private Division division;

    @Before
    public void before() {
        team1 = new Team("team1", Conference.AFC, Region.EAST);
        team2 = new Team("team2",Conference.AFC, Region.EAST);
        team3 = new Team("team3",Conference.AFC, Region.EAST);
        team4 = new Team("team4",Conference.AFC, Region.EAST);
        division = new Division("test", Arrays.asList(team1,team2,team3,team4));
        sameConferenceTeam = new Team("sameConferenceTeam", Conference.AFC, Region.WEST);
        otherConferenceTeam = new Team("otherConference", Conference.NFC, Region.NORTH);
        assertEquals(team1, division.get(0));
        assertEquals(team2, division.get(1));
        assertEquals(team3, division.get(2));
        assertEquals(team4, division.get(3));
        for (Team team : division) {
            assertTrue(team.getMessage().isEmpty());
        }
    }

    @Test
    public void divisionTeamNumberConstraint() {
        try {
            division = new Division("test", Arrays.asList(team1, team2, team3));
            fail();
        } catch (RuntimeException re) {
            assertEquals("Divisions must have exactly four teams!", re.getMessage());
        }

        try {
            Team team5 = new Team("team5", Conference.AFC, Region.EAST);
            division = new Division("test", Arrays.asList(team1, team2, team3, team4, team5));
            fail();
        } catch (RuntimeException re) {
            assertEquals("Divisions must have exactly four teams!", re.getMessage());
        }
    }

    @Test
    public void allTeamsWithinDivisionHaveSameConferenceAndRegion() {
        try {
            division = new Division("test", Arrays.asList(team1, team2, team3, otherConferenceTeam));
            fail();
        } catch (RuntimeException re) {
            assertEquals("All teams within a division must have the same conference and region!", re.getMessage());
        }
    }

    @Test
    public void divisionSortsTeamsByWinLossDrawPercentage() {
        Team otherTeam = new Team("otherteam", Conference.AFC, Region.NORTH);
        for (int i = 0; i < 4; i++) {
            team1.recordLoss(otherTeam);
            team2.recordWin(otherTeam);
        }
        for (int i = 0; i < 2; i++) {
            team3.recordWin(otherTeam);
            team4.recordWin(otherTeam);
            team4.recordLoss(otherTeam);
        }
        team3.recordLoss(otherTeam);
        team3.recordDraw(otherTeam);
        division.sort();
        assertEquals(team2.getName(), division.get(0).getName());
        assertEquals(team3.getName(), division.get(1).getName());
        assertEquals(team4.getName(), division.get(2).getName());
        assertEquals(team1.getName(), division.get(3).getName());
        for (Team team : division) {
            assertTrue(team.getMessage().isEmpty());
        }
    }

    @Test
    public void divisionSortsMultiWayTieResolvedByMultiWayHeadToHeadComparison() {
        Team otherTeam = new Team("otherteam", Conference.NFC, Region.WEST);

        team1.recordWin(team4);
        team1.recordWin(team4);
        team1.recordLoss(team4);
        team1.recordLoss(team2);

        team2.recordWin(team1);
        team2.recordWin(team4);
        team2.recordLoss(team3);
        team2.recordLoss(team3);

        team3.recordWin(team2);
        team3.recordWin(team2);
        team3.recordLoss(otherTeam);
        team3.recordLoss(otherTeam);

        team4.recordWin(team1);
        team4.recordLoss(team1);
        team4.recordLoss(team1);
        team4.recordLoss(team2);

        division.sort();
        assertEquals(team3.getName(), division.get(0).getName());
        assertEquals(team2.getName(), division.get(1).getName());
        assertEquals(team1.getName(), division.get(2).getName());
        assertEquals(team4.getName(), division.get(3).getName());
        assertEquals("Head-To-Head Win-Loss-Draw Percentage Against Multiple Teams: 1.0", team3.getMessage());
        assertEquals("Head-To-Head Win-Loss-Draw Percentage Against Multiple Teams: 0.333", team2.getMessage());
        assertEquals("Head-To-Head Win-Loss-Draw Percentage Against Multiple Teams: 0.0", team1.getMessage());
        assertTrue(team4.getMessage().isEmpty());
    }

    @Test
    public void divisionSortsMultiWayTieResolvedSecondByMultiWayDivisionComparison() {
        team1.recordWin(otherConferenceTeam);
        team1.recordWin(otherConferenceTeam);
        team1.recordWin(otherConferenceTeam);
        team1.recordLoss(otherConferenceTeam);

        team2.recordWin(team4);
        team2.recordWin(team4);
        team2.recordWin(otherConferenceTeam);
        team2.recordLoss(team4);

        team3.recordWin(team4);
        team3.recordWin(otherConferenceTeam);
        team3.recordWin(otherConferenceTeam);
        team3.recordLoss(otherConferenceTeam);

        team4.recordWin(team2);
        team4.recordLoss(team2);
        team4.recordLoss(team2);
        team4.recordLoss(team3);

        division.sort();
        assertEquals(team3.getName(), division.get(0).getName());
        assertEquals(team2.getName(), division.get(1).getName());
        assertEquals(team1.getName(), division.get(2).getName());
        assertEquals(team4.getName(), division.get(3).getName());
        assertEquals("Divisional Win-Loss-Draw Percentage: 1.0", team3.getMessage());
        assertEquals("Divisional Win-Loss-Draw Percentage: 0.667", team2.getMessage());
        assertEquals("Divisional Win-Loss-Draw Percentage: 0.0", team1.getMessage());
        assertTrue(team4.getMessage().isEmpty());
    }

    @Test
    public void divisionSortsMultiWayTieResolvedThirdByCommonGamesComparison() {
        Team otherTeam1 = new Team("otherteam1", Conference.NFC, Region.WEST);
        Team otherTeam2 = new Team("otherteam2", Conference.NFC, Region.WEST);
        Team otherTeam3 = new Team("otherteam3", Conference.NFC, Region.WEST);
        Team otherTeam4 = new Team("otherteam4", Conference.NFC, Region.WEST);
        Team commonTeam = new Team("commonTeam", Conference.NFC, Region.NORTH);

        for (int i = 0; i < 4; i++) {
            team4.recordWin(commonTeam);
            team4.recordLoss(otherTeam4);
        }

        for (int i = 0; i < 3; i++) {
            team2.recordWin(commonTeam);
            team2.recordLoss(otherTeam2);
            team3.recordLoss(commonTeam);
            team3.recordWin(otherTeam3);
        }
        team2.recordWin(otherTeam2);
        team2.recordLoss(commonTeam);
        team3.recordLoss(otherTeam3);
        team3.recordWin(commonTeam);

        for (int i = 0; i < 2; i++) {
            team1.recordWin(commonTeam);
            team1.recordLoss(commonTeam);
            team1.recordWin(otherTeam1);
            team1.recordLoss(otherTeam1);
        }

        division.sort();
        assertEquals("Win-Loss-Draw Percentage, Games in Common With Multiple Opponents: 1.0", team4.getMessage());
        assertEquals("Win-Loss-Draw Percentage, Games in Common With Multiple Opponents: 0.75", team2.getMessage());
        assertEquals("Win-Loss-Draw Percentage, Games in Common With Multiple Opponents: 0.5", team1.getMessage());
        assertEquals("Win-Loss-Draw Percentage, Games in Common With Multiple Opponents: 0.25", team3.getMessage());
        assertEquals(team4.getName(), division.get(0).getName());
        assertEquals(team2.getName(), division.get(1).getName());
        assertEquals(team1.getName(), division.get(2).getName());
        assertEquals(team3.getName(), division.get(3).getName());
    }

    @Test
    public void divisionSortsMultiWayTieResolvedFourthByMultiWayConferenceComparison() {
        team1.recordWin(sameConferenceTeam);
        team1.recordWin(sameConferenceTeam);
        team1.recordLoss(sameConferenceTeam);
        team1.recordLoss(otherConferenceTeam);

        team2.recordWin(otherConferenceTeam);
        team2.recordWin(otherConferenceTeam);
        team2.recordLoss(otherConferenceTeam);
        team2.recordLoss(sameConferenceTeam);

        team3.recordWin(sameConferenceTeam);
        team3.recordWin(sameConferenceTeam);
        team3.recordLoss(otherConferenceTeam);
        team3.recordLoss(otherConferenceTeam);

        team4.recordWin(sameConferenceTeam);
        team4.recordWin(otherConferenceTeam);
        team4.recordLoss(sameConferenceTeam);
        team4.recordLoss(sameConferenceTeam);

        division.sort();
        assertEquals(team3.getName(), division.get(0).getName());
        assertEquals(team1.getName(), division.get(1).getName());
        assertEquals(team4.getName(), division.get(2).getName());
        assertEquals(team2.getName(), division.get(3).getName());
        assertEquals("Conference Win-Loss-Draw Percentage: 1.0", team3.getMessage());
        assertEquals("Conference Win-Loss-Draw Percentage: 0.667", team1.getMessage());
        assertEquals("Conference Win-Loss-Draw Percentage: 0.333", team4.getMessage());
        assertEquals("Conference Win-Loss-Draw Percentage: 0.0", team2.getMessage());
    }

    @Test
    public void divisionSortsMultiWayTieResolvedFifthByMultiWayStrengthOfVictoryComparison() {
        Team goodTeam1 = new Team("goodTeam1", Conference.NFC, Region.WEST);
        Team goodTeam2 = new Team("goodTeam2", Conference.NFC, Region.WEST);
        Team goodTeam3 = new Team("goodTeam3", Conference.NFC, Region.WEST);
        Team badTeam1 = new Team("badTeam1", Conference.NFC, Region.NORTH);
        Team badTeam2 = new Team("badTeam2", Conference.NFC, Region.NORTH);
        Team badTeam3 = new Team("badTeam3", Conference.NFC, Region.NORTH);

        goodTeam1.recordWin(badTeam1);
        goodTeam2.recordWin(badTeam2);
        goodTeam3.recordWin(badTeam3);
        badTeam1.recordLoss(goodTeam1);
        badTeam2.recordLoss(badTeam2);
        badTeam3.recordLoss(badTeam3);

        team1.recordWin(goodTeam1);
        team1.recordWin(goodTeam1);
        team1.recordLoss(goodTeam1);

        team2.recordWin(badTeam2);
        team2.recordWin(badTeam2);
        team2.recordLoss(goodTeam2);

        team3.recordWin(goodTeam3);
        team3.recordWin(badTeam3);
        team3.recordLoss(goodTeam3);

        team4.recordWin(team1);
        team4.recordWin(team2);
        team4.recordLoss(team3);

        division.sort();
        assertEquals(team4.getName(), division.get(0).getName());
        assertEquals(team1.getName(), division.get(1).getName());
        assertEquals(team3.getName(), division.get(2).getName());
        assertEquals(team2.getName(), division.get(3).getName());
        assertEquals("Head-To-Head Win-Loss-Draw Percentage Against Multiple Teams: 0.667", team4.getMessage());
        assertEquals("Strength of Victory: 1.0", team1.getMessage());
        assertEquals("Strength of Victory: 0.5", team3.getMessage());
        assertEquals("Strength of Victory: 0.0", team2.getMessage());
    }

    @Test
    public void divisionSortsMultiWayTieResolvedSixthByMultiWayStrengthOfScheduleComparison() {
        Team bestTeam = new Team("bestTeam", Conference.NFC, Region.WEST);
        Team secondBestTeam = new Team("secondBestTeam", Conference.NFC, Region.WEST);
        Team thirdBestTeam = new Team("thirdBestTeam", Conference.NFC, Region.WEST);
        Team worstTeam = new Team("worstTeam", Conference.NFC, Region.WEST);

        bestTeam.recordWin(secondBestTeam);
        bestTeam.recordWin(thirdBestTeam);
        bestTeam.recordWin(worstTeam);

        secondBestTeam.recordWin(thirdBestTeam);
        secondBestTeam.recordWin(worstTeam);
        secondBestTeam.recordLoss(bestTeam);

        thirdBestTeam.recordWin(worstTeam);
        thirdBestTeam.recordLoss(bestTeam);
        thirdBestTeam.recordLoss(secondBestTeam);

        worstTeam.recordLoss(bestTeam);
        worstTeam.recordLoss(secondBestTeam);
        worstTeam.recordLoss(thirdBestTeam);

        team1.recordLoss(worstTeam);
        team2.recordLoss(thirdBestTeam);
        team3.recordLoss(secondBestTeam);
        team4.recordLoss(bestTeam);

        division.sort();
        assertEquals(team4.getName(), division.get(0).getName());
        assertEquals(team3.getName(), division.get(1).getName());
        assertEquals(team2.getName(), division.get(2).getName());
        assertEquals(team1.getName(), division.get(3).getName());
        assertEquals("Strength of Schedule: 1.0", team4.getMessage());
        assertEquals("Strength of Schedule: 0.667", team3.getMessage());
        assertEquals("Strength of Schedule: 0.333", team2.getMessage());
        assertEquals("Strength of Schedule: 0.0", team1.getMessage());
    }

    @Test
    public void onceUnbrokenMultiwayTieIsEstablishedNoFurtherSortingIsAttempted() {
        team1.recordWin(team2);
        team2.recordLoss(team1);
        team2.recordWin(team3);
        team3.recordLoss(team2);
        team3.recordWin(team4);
        team4.recordLoss(team3);
        team4.recordWin(team1);
        team1.recordLoss(team4);

        division.sort();
        for (Team team : division) {
            assertEquals("unresolved multiway tie", team.getMessage());
        }
    }

    @Test
    public void multiWayTieBreakersContinueSequentiallyUntilLessThanThreeTeamsAreTied() {
        Team goodOtherConferenceTeam = new Team("goodOtherConferenceTeam", Conference.NFC, Region.NORTH);
        goodOtherConferenceTeam.recordWin(otherConferenceTeam);
        otherConferenceTeam.recordLoss(goodOtherConferenceTeam);

        team1.recordWin(team2);
        team2.recordLoss(team1);
        team2.recordWin(team3);
        team3.recordLoss(team2);
        team3.recordWin(team4);
        team4.recordLoss(team3);
        team4.recordWin(team1);
        team1.recordLoss(team4);

        team4.recordWin(sameConferenceTeam);
        team1.recordWin(goodOtherConferenceTeam);
        team2.recordWin(otherConferenceTeam);
        team3.recordWin(otherConferenceTeam);

        division.sort();

        assertEquals(team4.getName(), division.get(0).getName());
        assertEquals(team1.getName(), division.get(1).getName());
        assertEquals(team2.getName(), division.get(2).getName());
        assertEquals(team3.getName(), division.get(3).getName());
        assertEquals("Conference Win-Loss-Draw Percentage: 0.667", team4.getMessage());
        assertEquals("Strength of Victory: 0.75", team1.getMessage());
        assertEquals("Head-To-Head Win-Loss-Draw Percentage Against " + team3.getName() + ": 1.0", team2.getMessage());
        assertEquals("Head-To-Head Win-Loss-Draw Percentage Against " + team2.getName() + ": 0.0", team3.getMessage());
    }
}
