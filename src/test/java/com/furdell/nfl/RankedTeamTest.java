package com.furdell.nfl;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class RankedTeamTest {

    @Test
    public void attemptToCompareUnrelatedRankedTeamsThrowsException() {
        RankedTeam rankedTeam1 = new RankedTeam(new Team("team1", Conference.NFC, Region.WEST), 0.0);
        RankedTeam rankedTeam2 = new RankedTeam(new Team("team2", Conference.NFC, Region.WEST), 0.0);
        rankedTeam1.setRankedValue(0.0, "Message One");
        rankedTeam2.setRankedValue(0.0, "Message Two");
        try {
            rankedTeam1.compareTo(rankedTeam2);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("Can't compare two RankedTeams with different descriptions!",iae.getMessage());
        }
    }

    @Test
    public void comparingRankedTeamsTransposesDescriptionToTeamMessage() {
        String description = "This is the message";
        Double value = 0.0;
        String message = description + ": " + value.toString();
        RankedTeam rankedTeam1 = new RankedTeam(new Team("team1", Conference.NFC, Region.WEST), 0.0);
        RankedTeam rankedTeam2 = new RankedTeam(new Team("team2", Conference.NFC, Region.WEST), 0.0);
        rankedTeam1.setRankedValue(0.0, description);
        rankedTeam2.setRankedValue(0.0, description);
        assertEquals(0, rankedTeam1.compareTo(rankedTeam2));
        assertEquals(message, rankedTeam1.getTeam().getMessage());
        assertEquals(message, rankedTeam2.getTeam().getMessage());
    }
}
