package com.furdell.nfl;

public class Standings {
    public static void main(String args[]) {
        League league = new League();
        // Week 1
        league.recordGame(League.GREEN_BAY_PACKERS,16,League.SEATTLE_SEAHAWKS,36);
        league.recordGame(League.NEW_ORLEANS_SAINTS,34,League.ATLANTA_FALCONS,37);
        league.recordGame(League.MINNESOTA_VIKINGS,34,League.ST_LOUIS_RAMS,6);
        league.recordGame(League.CLEVELAND_BROWNS,27,League.PITTSBURGH_STEELERS,30);
        league.recordGame(League.JACKSONVILLE_JAGUARS,17,League.PHILADELPHIA_EAGLES,34);
        league.recordGame(League.OAKLAND_RAIDERS,14,League.NEW_YORK_JETS,19);
        league.recordGame(League.CINCINATTI_BENGALS,23,League.BALTIMORE_RAVENS,16);
        league.recordGame(League.BUFFALO_BILLS,23,League.CHICAGO_BEARS,20);
        league.recordGame(League.WASHINGTON_REDSKINS,6,League.HOUSTON_TEXANS,17);
        league.recordGame(League.TENNESSEE_TITANS,26,League.KANSAS_CITY_CHIEFS,10);
        league.recordGame(League.NEW_ENGLAND_PATRIOTS,20,League.MIAMI_DOLPHINS,33);
        league.recordGame(League.CAROLINA_PANTHERS,20,League.TAMPA_BAY_BUCCANEERS,14);
        league.recordGame(League.SAN_FRANCISCO_49ERS,28,League.DALLAS_COWBOYS,17);
        league.recordGame(League.INDIANAPOLIS_COLTS,24,League.DENVER_BRONCOS,31);
        league.recordGame(League.NEW_YORK_GIANTS,14,League.DETROIT_LIONS,35);
        league.recordGame(League.SAN_DIEGO_CHARGERS,17,League.ARIZONA_CARDINALS,18);
        // Week 2
        league.recordGame(League.PITTSBURGH_STEELERS,6,League.BALTIMORE_RAVENS,26);
        league.recordGame(League.MIAMI_DOLPHINS,10,League.BUFFALO_BILLS,29);
        league.recordGame(League.JACKSONVILLE_JAGUARS,10,League.WASHINGTON_REDSKINS,41);
        league.recordGame(League.DALLAS_COWBOYS,26,League.TENNESSEE_TITANS,10);
        league.recordGame(League.ARIZONA_CARDINALS,25,League.NEW_YORK_GIANTS,14);
        league.recordGame(League.NEW_ENGLAND_PATRIOTS,30,League.MINNESOTA_VIKINGS,7);
        league.recordGame(League.NEW_ORLEANS_SAINTS,24,League.CLEVELAND_BROWNS,26);
        league.recordGame(League.ATLANTA_FALCONS,10,League.CINCINATTI_BENGALS,24);
        league.recordGame(League.DETROIT_LIONS,7,League.CAROLINA_PANTHERS,24);
        league.recordGame(League.ST_LOUIS_RAMS,19,League.TAMPA_BAY_BUCCANEERS,17);
        league.recordGame(League.SEATTLE_SEAHAWKS,21,League.SAN_DIEGO_CHARGERS,30);
        league.recordGame(League.HOUSTON_TEXANS,30,League.OAKLAND_RAIDERS,14);
        league.recordGame(League.NEW_YORK_JETS,24,League.GREEN_BAY_PACKERS,31);
        league.recordGame(League.KANSAS_CITY_CHIEFS,17,League.DENVER_BRONCOS,24);
        league.recordGame(League.CHICAGO_BEARS,28,League.SAN_FRANCISCO_49ERS,20);
        league.recordGame(League.PHILADELPHIA_EAGLES,30,League.INDIANAPOLIS_COLTS,27);
        // Week 3
        league.recordGame(League.TAMPA_BAY_BUCCANEERS,14,League.ATLANTA_FALCONS,56);
        league.recordGame(League.SAN_DIEGO_CHARGERS,22,League.BUFFALO_BILLS,10);
        league.recordGame(League.TENNESSEE_TITANS,7,League.CINCINATTI_BENGALS,33);
        league.recordGame(League.GREEN_BAY_PACKERS,7,League.DETROIT_LIONS,19);
        league.recordGame(League.INDIANAPOLIS_COLTS,44,League.JACKSONVILLE_JAGUARS,17);
        league.recordGame(League.OAKLAND_RAIDERS,9,League.NEW_ENGLAND_PATRIOTS,16);
        league.recordGame(League.SAN_FRANCISCO_49ERS,14,League.ARIZONA_CARDINALS,23);
        league.recordGame(League.KANSAS_CITY_CHIEFS,34,League.MIAMI_DOLPHINS,15);
        league.recordGame(League.BALTIMORE_RAVENS,23,League.CLEVELAND_BROWNS,21);
        league.recordGame(League.DALLAS_COWBOYS,34,League.ST_LOUIS_RAMS,31);
        league.recordGame(League.HOUSTON_TEXANS,17,League.NEW_YORK_GIANTS,30);
        league.recordGame(League.MINNESOTA_VIKINGS,9,League.NEW_ORLEANS_SAINTS,20);
        league.recordGame(League.WASHINGTON_REDSKINS,34,League.PHILADELPHIA_EAGLES,37);
        league.recordGame(League.DENVER_BRONCOS,20,League.SEATTLE_SEAHAWKS,26);
        league.recordGame(League.PITTSBURGH_STEELERS,37,League.CAROLINA_PANTHERS,19);
        league.recordGame(League.CHICAGO_BEARS,27,League.NEW_YORK_JETS,19);
        league.printStandings();
    }
}
