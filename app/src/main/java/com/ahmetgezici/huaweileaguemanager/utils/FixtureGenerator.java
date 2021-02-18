package com.ahmetgezici.huaweileaguemanager.utils;

import com.ahmetgezici.huaweileaguemanager.model.Teams;

import java.util.LinkedList;
import java.util.List;

public class FixtureGenerator {

    public List<List<Fixture>> getFixtures(List<Teams> teams) {

        int numberOfTeams = teams.size();

        boolean ghost = false;
        if (numberOfTeams % 2 != 0) {
            numberOfTeams++;
            ghost = true;
        }

        int totalRounds = numberOfTeams - 1;
        int matchesPerRound = numberOfTeams / 2;
        List<List<Fixture>> rounds = new LinkedList<>();

        for (int round = 0; round < totalRounds; round++) {

            List<Fixture> fixtures = new LinkedList<>();

            for (int match = 0; match < matchesPerRound; match++) {

                int home = (round + match) % (numberOfTeams - 1);
                int away = (numberOfTeams - 1 - match + round) % (numberOfTeams - 1);

                if (match == 0) {
                    away = numberOfTeams - 1;
                }

                fixtures.add(new Fixture(teams.get(home).name, teams.get(home).logourl, teams.get(away).name, teams.get(away).logourl));
            }
            rounds.add(fixtures);
        }

        List<List<Fixture>> interleaved = new LinkedList<>();

        int evn = 0;
        int odd = (numberOfTeams / 2);

        for (int i = 0; i < rounds.size(); i++) {
            if (i % 2 == 0) {

                interleaved.add(rounds.get(evn++));

            } else {

                interleaved.add(rounds.get(odd++));

            }
        }

        rounds = interleaved;

        for (int roundNumber = 0; roundNumber < rounds.size(); roundNumber++) {
            if (roundNumber % 2 == 1) {

                Fixture fixture = rounds.get(roundNumber).get(0);
                rounds.get(roundNumber).set(0, new Fixture(fixture.getAwayTeam(), fixture.getAwayLogoUrl(), fixture.getHomeTeam(), fixture.getHomeLogoUrl()));

            }
        }

        // ReverseFixture

        List<List<Fixture>> reverseFixtures = new LinkedList<List<Fixture>>();

        for (List<Fixture> round : rounds) {

            List<Fixture> reverseRound = new LinkedList<Fixture>();

            for (Fixture fixture : round) {
                reverseRound.add(new Fixture(fixture.getAwayTeam(), fixture.getAwayLogoUrl(), fixture.getHomeTeam(), fixture.getHomeLogoUrl()));
            }

            reverseFixtures.add(reverseRound);
        }
        rounds.addAll(reverseFixtures);

        return rounds;
    }
}
