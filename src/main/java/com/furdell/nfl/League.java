package com.furdell.nfl;

import java.util.*;

public class League {

    public static final String MIAMI_DOLPHINS = "MIA";
    public static final String NEW_YORK_JETS = "NYJ";
    public static final String BUFFALO_BILLS = "BUF";
    public static final String NEW_ENGLAND_PATRIOTS = "NE";
    public static final String CINCINATTI_BENGALS = "CIN";
    public static final String BALTIMORE_RAVENS = "BAL";
    public static final String PITTSBURGH_STEELERS = "PIT";
    public static final String CLEVELAND_BROWNS = "CLE";
    public static final String TENNESSEE_TITANS = "TEN";
    public static final String HOUSTON_TEXANS = "HOU";
    public static final String INDIANAPOLIS_COLTS = "IND";
    public static final String JACKSONVILLE_JAGUARS = "JAX";
    public static final String DENVER_BRONCOS = "DEN";
    public static final String SAN_DIEGO_CHARGERS = "SD";
    public static final String OAKLAND_RAIDERS = "OAK";
    public static final String KANSAS_CITY_CHIEFS = "KC";
    public static final String PHILADELPHIA_EAGLES = "PHI";
    public static final String WASHINGTON_REDSKINS = "WAS";
    public static final String DALLAS_COWBOYS = "DAL";
    public static final String NEW_YORK_GIANTS = "NYG";
    public static final String MINNESOTA_VIKINGS = "MIN";
    public static final String DETROIT_LIONS = "DET";
    public static final String CHICAGO_BEARS = "CHI";
    public static final String GREEN_BAY_PACKERS = "GB";
    public static final String CAROLINA_PANTHERS = "CAR";
    public static final String ATLANTA_FALCONS = "ATL";
    public static final String NEW_ORLEANS_SAINTS = "NO";
    public static final String TAMPA_BAY_BUCCANEERS = "TB";
    public static final String SEATTLE_SEAHAWKS = "SEA";
    public static final String SAN_FRANCISCO_49ERS = "SF";
    public static final String ARIZONA_CARDINALS = "ARI";
    public static final String ST_LOUIS_RAMS = "STL";
    private List<Division> divisions;
    private Map<String,Team> teams;

    public League() {
        Team dolphins = new Team(MIAMI_DOLPHINS, Conference.AFC, Region.EAST);
        Team jets = new Team(NEW_YORK_JETS, Conference.AFC, Region.EAST);
        Team bills = new Team(BUFFALO_BILLS, Conference.AFC, Region.EAST);
        Team patriots = new Team(NEW_ENGLAND_PATRIOTS, Conference.AFC, Region.EAST);
        Division afcEast = new Division("AFC East", Arrays.asList(dolphins, jets, bills, patriots));
        Team bengals = new Team(CINCINATTI_BENGALS, Conference.AFC, Region.NORTH);
        Team ravens = new Team(BALTIMORE_RAVENS, Conference.AFC, Region.NORTH);
        Team steelers = new Team(PITTSBURGH_STEELERS, Conference.AFC, Region.NORTH);
        Team browns = new Team(CLEVELAND_BROWNS, Conference.AFC, Region.NORTH);
        Division afcNorth = new Division("AFC North", Arrays.asList(bengals, ravens, steelers, browns));
        Team titans = new Team(TENNESSEE_TITANS, Conference.AFC, Region.SOUTH);
        Team texans = new Team(HOUSTON_TEXANS, Conference.AFC, Region.SOUTH);
        Team colts = new Team(INDIANAPOLIS_COLTS, Conference.AFC, Region.SOUTH);
        Team jaguars = new Team(JACKSONVILLE_JAGUARS, Conference.AFC, Region.SOUTH);
        Division afcSouth = new Division("AFC South", Arrays.asList(titans, texans, colts, jaguars));
        Team broncos = new Team(DENVER_BRONCOS, Conference.AFC, Region.WEST);
        Team chargers = new Team(SAN_DIEGO_CHARGERS, Conference.AFC, Region.WEST);
        Team raiders = new Team(OAKLAND_RAIDERS, Conference.AFC, Region.WEST);
        Team chiefs = new Team(KANSAS_CITY_CHIEFS, Conference.AFC, Region.WEST);
        Division afcWest = new Division("AFC West", Arrays.asList(broncos, chargers, raiders, chiefs));
        Team eagles = new Team(PHILADELPHIA_EAGLES, Conference.NFC, Region.EAST);
        Team redskins = new Team(WASHINGTON_REDSKINS, Conference.NFC, Region.EAST);
        Team cowboys = new Team(DALLAS_COWBOYS, Conference.NFC, Region.EAST);
        Team giants = new Team(NEW_YORK_GIANTS, Conference.NFC, Region.EAST);
        Division nfcEast = new Division("NFC East", Arrays.asList(eagles, redskins, cowboys, giants));
        Team vikings = new Team(MINNESOTA_VIKINGS, Conference.NFC, Region.NORTH);
        Team lions = new Team(DETROIT_LIONS, Conference.NFC, Region.NORTH);
        Team bears = new Team(CHICAGO_BEARS, Conference.NFC, Region.NORTH);
        Team packers = new Team(GREEN_BAY_PACKERS, Conference.NFC, Region.NORTH);
        Division nfcNorth = new Division("NFC North", Arrays.asList(vikings, lions, bears, packers));
        Team panthers = new Team(CAROLINA_PANTHERS, Conference.NFC, Region.SOUTH);
        Team falcons = new Team(ATLANTA_FALCONS, Conference.NFC, Region.SOUTH);
        Team saints = new Team(NEW_ORLEANS_SAINTS, Conference.NFC, Region.SOUTH);
        Team buccaneers = new Team(TAMPA_BAY_BUCCANEERS, Conference.NFC, Region.SOUTH);
        Division nfcSouth = new Division("NFC South", Arrays.asList(panthers, falcons, saints, buccaneers));
        Team seahawks = new Team(SEATTLE_SEAHAWKS, Conference.NFC, Region.WEST);
        Team fortyNiners = new Team(SAN_FRANCISCO_49ERS, Conference.NFC, Region.WEST);
        Team cardinals = new Team(ARIZONA_CARDINALS, Conference.NFC, Region.WEST);
        Team rams = new Team(ST_LOUIS_RAMS, Conference.NFC, Region.WEST);
        Division nfcWest = new Division("NFC West", Arrays.asList(seahawks, fortyNiners, cardinals, rams));
        divisions = Arrays.asList(afcEast, afcNorth, afcSouth, afcWest, nfcEast, nfcNorth, nfcSouth, nfcWest);
        teams = new HashMap<>();
        for (Division division : divisions) {
            for (Team team : division) {
                teams.put(team.getName(), team);
            }
        }
    }

    public void printStandings() {
        for (Division division: divisions) {
            division.sort();
            printStandings(division);
        }
    }

    private void printStandings(Division division) {
        System.out.println();
        System.out.println(division.getName());
        System.out.println("-------");
        for (Team team : division) {
            System.out.println(team.getName() + "\t" + team.winRecord() + "\t" + team.getMessage());
        }
    }

    public void recordGame(String awayName, int awayScore, String homeName, int homeScore) {
        Team awayTeam = teams.get(awayName);
        Team homeTeam = teams.get(homeName);
        if (awayScore > homeScore) {
            awayTeam.recordWin(homeTeam);
            homeTeam.recordLoss(awayTeam);
        } else if (awayScore < homeScore) {
            awayTeam.recordLoss(homeTeam);
            homeTeam.recordWin(awayTeam);
        } else {
            awayTeam.recordDraw(homeTeam);
            homeTeam.recordDraw(awayTeam);
        }
    }
}
