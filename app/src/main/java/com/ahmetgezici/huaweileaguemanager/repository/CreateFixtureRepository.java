package com.ahmetgezici.huaweileaguemanager.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.ahmetgezici.huaweileaguemanager.database.FixtureDAO;
import com.ahmetgezici.huaweileaguemanager.database.FixtureDatabase;
import com.ahmetgezici.huaweileaguemanager.model.FixtureData;

import java.util.List;

public class CreateFixtureRepository {

    LiveData<List<FixtureData>> fixtureDBLiveData;

    FixtureDAO fixtureDAO;

    ////////

    public CreateFixtureRepository(Application application) {

        FixtureDatabase database = FixtureDatabase.getDatabase(application);
        fixtureDAO = database.fixtureDAO();

        fixtureDBLiveData = fixtureDAO.getAllFixture();
    }


    // Get Fixture from Database

    public LiveData<List<FixtureData>> getFixtureDBLiveData() {
        return fixtureDBLiveData;
    }


    // Save Fixture to Database

    public void insertFixture(FixtureData fixtureData) {
        FixtureDatabase.databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                fixtureDAO.addFixtureData(fixtureData);
            }
        });
    }


    // Delete Fixture from Database

    public void deleteFixture(Integer uid) {
        FixtureDatabase.databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                fixtureDAO.deleteFixture(uid);
            }
        });
    }


    // Delete All Fixtures from Database

    public void deleteAllFixture() {
        FixtureDatabase.databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                fixtureDAO.deleteAllFixture();
            }
        });
    }
}
