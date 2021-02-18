package com.ahmetgezici.huaweileaguemanager.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ahmetgezici.huaweileaguemanager.model.Teams;
import com.ahmetgezici.huaweileaguemanager.repository.TeamsRepository;
import com.ahmetgezici.huaweileaguemanager.utils.datautils.Resource;

import java.util.List;

public class TeamsViewModel extends AndroidViewModel {

    TeamsRepository repository;

    //////////////////

    public MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();

    private MutableLiveData<Resource<List<Teams>>> teamsLiveData;

    public LiveData<List<Teams>> teamsDBLiveData;

    public MutableLiveData<List<Teams>> teamListLiveData = new MutableLiveData<>();

    ////////

    public TeamsViewModel(@NonNull Application application) {
        super(application);

        repository = new TeamsRepository(application);
        teamsDBLiveData = repository.getTeamsDBLiveData();
    }

    // Get Teams from Service

    public LiveData<Resource<List<Teams>>> getTeams() {

        if (teamsLiveData == null) {
            teamsLiveData = repository.getTeams();
        }

        return teamsLiveData;
    }


    // Save Teams to Database

    public void insertTeams(List<Teams> teams){
        repository.insertTeams(teams);
    }


    // Delete All Teams from Database

    public void deleteAllTeams(){
        repository.deleteAllTeams();
    }

}
