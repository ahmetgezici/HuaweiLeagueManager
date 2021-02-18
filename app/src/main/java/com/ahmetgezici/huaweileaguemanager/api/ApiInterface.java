package com.ahmetgezici.huaweileaguemanager.api;

import com.ahmetgezici.huaweileaguemanager.model.Teams;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("teams.json")
    Single<List<Teams>> getTeams();

}
