package com.ahmetgezici.huaweileaguemanager.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ahmetgezici.huaweileaguemanager.model.FixtureData;
import com.ahmetgezici.huaweileaguemanager.model.TeamData;
import com.ahmetgezici.huaweileaguemanager.model.Teams;
import com.ahmetgezici.huaweileaguemanager.model.Versus;
import com.ahmetgezici.huaweileaguemanager.repository.CreateFixtureRepository;
import com.ahmetgezici.huaweileaguemanager.utils.Fixture;
import com.ahmetgezici.huaweileaguemanager.utils.FixtureGenerator;

import java.util.ArrayList;
import java.util.List;

public class CreateFixtureViewModel extends AndroidViewModel {

    CreateFixtureRepository repository;

    //////////////////

    public LiveData<List<FixtureData>> fixtureDBLiveData;

    public MutableLiveData<List<Teams>> teamListLiveData = new MutableLiveData<>();

    public MutableLiveData<Boolean> loadingLiveDate = new MutableLiveData<>();

    ////////

    public CreateFixtureViewModel(@NonNull Application application) {
        super(application);

        repository = new CreateFixtureRepository(application);
        fixtureDBLiveData = repository.getFixtureDBLiveData();
    }

    // Save Fixture to Database

    public void insertFixture(FixtureData fixtureData) {
        repository.insertFixture(fixtureData);
    }


    // Delete All Fixtures from Database

    public void deleteFixture(Integer uid) {
        repository.deleteFixture(uid);
    }


    // Delete Fixture from Database

    public void deleteAllFixture() {
        repository.deleteAllFixture();
    }

    //////////////////////////////////////////////

    // Calculate Fixture

    public ArrayList<ArrayList<Versus>> calculateFixture(List<Teams> teamList) {

        ArrayList<ArrayList<Versus>> fixtureDataList = new ArrayList<>();

        FixtureGenerator fixtureGenerator = new FixtureGenerator();
        List<List<Fixture>> rounds = fixtureGenerator.getFixtures(teamList);

        for (int i = 0; i < rounds.size(); i++) {

            List<Fixture> round = rounds.get(i);

            ArrayList<Versus> versusList = new ArrayList<>();

            for (Fixture fixture : round) {

                Versus versus = new Versus();

                TeamData homeTeam = new TeamData();
                homeTeam.name = fixture.getHomeTeam();
                homeTeam.logourl = fixture.getHomeLogoUrl();

                TeamData awayTeam = new TeamData();
                awayTeam.name = fixture.getAwayTeam();
                awayTeam.logourl = fixture.getAwayLogoUrl();

                versus.home = homeTeam;
                versus.away = awayTeam;

                versusList.add(versus);
            }

            fixtureDataList.add(versusList);
        }

        return fixtureDataList;
    }

}
