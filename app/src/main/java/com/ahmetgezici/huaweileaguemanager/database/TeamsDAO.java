package com.ahmetgezici.huaweileaguemanager.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ahmetgezici.huaweileaguemanager.model.Teams;

import java.util.List;

@Dao
public interface TeamsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAllTeams(List<Teams> teams);

    @Query("SELECT * FROM teams")
    LiveData<List<Teams>> getAllTeams();

    @Query("DELETE FROM teams")
    void deleteAllTeams();

}
