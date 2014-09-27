package com.furdell.nfl;

import java.text.DecimalFormat;
import java.util.List;

public class WinRecord {
    private int wins = 0;
    private int losses = 0;
    private int draws = 0;

    public void recordWin() {
        wins++;
    }

    public void recordLoss() {
        losses++;
    }

    public void recordDraw() {
        draws++;
    }

    public Double winPercentage() {
        int totalGames = wins + losses + draws;
        if (totalGames == 0) return 0.0;
        double totalScore = wins + (draws / 2.0);
        DecimalFormat hundredths = new DecimalFormat("#.###");
        return Double.valueOf(hundredths.format(totalScore / totalGames));
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getDraws() {
        return draws;
    }

    public static WinRecord combinedWinRecord(List<WinRecord> winRecords) {
        WinRecord combinedWinRecord = new WinRecord();
        for (WinRecord winRecord : winRecords) {
            combinedWinRecord.wins += winRecord.getWins();
            combinedWinRecord.losses += winRecord.getLosses();
            combinedWinRecord.draws += winRecord.getDraws();
        }
        return combinedWinRecord;
    }

    @Override
    public String toString() {
        return getWins() + "-" + getLosses() + "-" + getDraws();
    }
}
