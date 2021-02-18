package com.ahmetgezici.huaweileaguemanager.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ahmetgezici.huaweileaguemanager.model.FixtureData;

import java.util.List;

@Dao
public interface FixtureDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addFixtureData(FixtureData fixtureData);

    @Query("SELECT * FROM fixture_json")
    LiveData<List<FixtureData>> getAllFixture();

    @Query("DELETE FROM fixture_json")
    void deleteAllFixture();

    @Query("DELETE FROM fixture_json WHERE uid=:uid")
    void deleteFixture(Integer uid);

}
