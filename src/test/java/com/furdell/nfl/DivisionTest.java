package com.furdell.nfl;

import com.furdell.nfl.Conference;
import com.furdell.nfl.Division;
import com.furdell.nfl.Region;
import com.furdell.nfl.Team;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class DivisionTest {

    @Test
    public void divisionSortsTeamsByWinLossDrawPercentage() {
        Team team1 = new Team("team1", Conference.AFC, Region.EAST);
        Team team2 = new Team("team2",Conference.AFC, Region.EAST);
        Team team3 = new Team("team3",Conference.AFC, Region.EAST);
        Team team4 = new Team("team4",Conference.AFC, Region.EAST);
        Team otherTeam = new Team("otherteam", Conference.AFC, Region.NORTH);
        Division division = new Division("test", Arrays.asList(team1,team2,team3,team4));
        assertEquals(team1, division.get(0));
        assertEquals(team2, division.get(1));
        assertEquals(team3, division.get(2));
        assertEquals(team4, division.get(3));
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
        assertEquals("Win-Loss-Draw Percentage: 1.0", team2.getMessage());
        assertEquals("Win-Loss-Draw Percentage: 0.625", team3.getMessage());
        assertEquals("Win-Loss-Draw Percentage: 0.5", team4.getMessage());
        assertEquals("Win-Loss-Draw Percentage: 0.0", team1.getMessage());
    }

    @Test
    public void divisionSortsMultiWayTieResolvedByMultiWayHeadToHeadComparison() {
        Team team1 = new Team("team1",Conference.AFC, Region.EAST); //(2-2; 0-1)
        Team team2 = new Team("team2",Conference.AFC, Region.EAST); // (2-2; 1-2)
        Team team3 = new Team("team3",Conference.AFC, Region.EAST); // (2-2; 2-0)
        Team team4 = new Team("team4",Conference.AFC, Region.EAST); // (1-3)
        Team otherTeam = new Team("otherteam", Conference.NFC, Region.WEST);
        Division division = new Division("test", Arrays.asList(team1,team2,team3,team4));
        assertEquals(team1, division.get(0));
        assertEquals(team2, division.get(1));
        assertEquals(team3, division.get(2));
        assertEquals(team4, division.get(3));

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
        assertEquals("Win-Loss-Draw Percentage: 0.25", team4.getMessage());
    }

    @Test
    public void divisionSortsMultiWayTieResolvedSecondByMultiWayDivisionComparison() {
        Team team1 = new Team("team1",Conference.AFC, Region.EAST); // (3-1; 0-0)
        Team team2 = new Team("team2",Conference.AFC, Region.EAST); // (3-1; 2-1)
        Team team3 = new Team("team3",Conference.AFC, Region.EAST); // (3-1; 1-0)
        Team team4 = new Team("team4",Conference.AFC, Region.EAST); // (1-3)
        Team otherTeam = new Team("otherteam", Conference.NFC, Region.WEST);
        Division division = new Division("test", Arrays.asList(team1,team2,team3,team4));
        assertEquals(team1, division.get(0));
        assertEquals(team2, division.get(1));
        assertEquals(team3, division.get(2));
        assertEquals(team4, division.get(3));

        team1.recordWin(otherTeam);
        team1.recordWin(otherTeam);
        team1.recordWin(otherTeam);
        team1.recordLoss(otherTeam);

        team2.recordWin(team4);
        team2.recordWin(team4);
        team2.recordWin(otherTeam);
        team2.recordLoss(team4);

        team3.recordWin(team4);
        team3.recordWin(otherTeam);
        team3.recordWin(otherTeam);
        team3.recordLoss(otherTeam);

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
        assertEquals("Win-Loss-Draw Percentage: 0.25", team4.getMessage());
    }

    @Test
    public void divisionSortsMultiWayTieResolvedThirdByCommonGamesComparison() {
        Team team1 = new Team("team1",Conference.AFC, Region.EAST); // (4-4; 2-2)
        Team team2 = new Team("team2",Conference.AFC, Region.EAST); // (4-4; 3-1)
        Team team3 = new Team("team3",Conference.AFC, Region.EAST); // (4-4; 1-3)
        Team team4 = new Team("team4",Conference.AFC, Region.EAST); // (4-4; 4-0)
        Team otherTeam1 = new Team("otherteam1", Conference.NFC, Region.WEST);
        Team otherTeam2 = new Team("otherteam2", Conference.NFC, Region.WEST);
        Team otherTeam3 = new Team("otherteam3", Conference.NFC, Region.WEST);
        Team otherTeam4 = new Team("otherteam4", Conference.NFC, Region.WEST);
        Team commonTeam = new Team("commonTeam", Conference.NFC, Region.NORTH);
        Division division = new Division("test", Arrays.asList(team1,team2,team3,team4));
        assertEquals(team1, division.get(0));
        assertEquals(team2, division.get(1));
        assertEquals(team3, division.get(2));
        assertEquals(team4, division.get(3));

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

}
