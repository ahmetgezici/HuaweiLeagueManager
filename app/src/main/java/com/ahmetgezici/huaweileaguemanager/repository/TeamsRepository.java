package com.ahmetgezici.huaweileaguemanager.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ahmetgezici.huaweileaguemanager.api.ApiClient;
import com.ahmetgezici.huaweileaguemanager.api.ApiInterface;
import com.ahmetgezici.huaweileaguemanager.database.FixtureDatabase;
import com.ahmetgezici.huaweileaguemanager.database.TeamsDAO;
import com.ahmetgezici.huaweileaguemanager.model.Teams;
import com.ahmetgezici.huaweileaguemanager.utils.datautils.Resource;

import java.util.List;

import io.reactivex.rxjava3.schedulers.Schedulers;

public class TeamsRepository {

    ApiInterface apiInterface;

    LiveData<List<Teams>> teamsDBLiveData;

    TeamsDAO teamsDAO;

    ////////

    public TeamsRepository(Application application) {

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        FixtureDatabase database = FixtureDatabase.getDatabase(application);
        teamsDAO = database.teamsDAO();

        teamsDBLiveData = teamsDAO.getAllTeams();
    }


    // Get Teams from Service

    public MutableLiveData<Resource<List<Teams>>> getTeams() {

        MutableLiveData<Resource<List<Teams>>> liveData = new MutableLiveData<>();

        liveData.postValue(Resource.loading());

        apiInterface.getTeams()
                .subscribeOn(Schedulers.io())
                .subscribe(
                        teams -> {
                            liveData.postValue(Resource.success(teams));
                        },
                        throwable -> {
                            liveData.postValue(Resource.error(throwable.getMessage()));
                        }
                )
                .isDisposed();

        return liveData;
    }

    // Get Teams from Database

    public LiveData<List<Teams>> getTeamsDBLiveData() {
        return teamsDBLiveData;
    }


    // Save Teams to Database

    public void insertTeams(List<Teams> teams) {
        FixtureDatabase.databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                teamsDAO.addAllTeams(teams);
            }
        });
    }


    // Delete All Teams from Database

    public void deleteAllTeams() {
        FixtureDatabase.databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                teamsDAO.deleteAllTeams();
            }
        });
    }
}
