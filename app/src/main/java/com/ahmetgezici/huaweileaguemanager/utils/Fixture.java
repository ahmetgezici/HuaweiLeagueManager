package com.ahmetgezici.huaweileaguemanager.utils;

public class Fixture {

    String homeTeam;
    String homeLogoUrl;

    String awayTeam;
    String awayLogoUrl;

    public Fixture(String homeTeam, String homeLogoUrl, String awayTeam, String awayLogoUrl) {
        this.homeTeam = homeTeam;
        this.homeLogoUrl = homeLogoUrl;
        this.awayTeam = awayTeam;
        this.awayLogoUrl = awayLogoUrl;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getHomeLogoUrl() {
        return homeLogoUrl;
    }

    public void setHomeLogoUrl(String homeLogoUrl) {
        this.homeLogoUrl = homeLogoUrl;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getAwayLogoUrl() {
        return awayLogoUrl;
    }

    public void setAwayLogoUrl(String awayLogoUrl) {
        this.awayLogoUrl = awayLogoUrl;
    }
}
