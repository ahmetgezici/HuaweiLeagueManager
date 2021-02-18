package com.ahmetgezici.huaweileaguemanager.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ahmetgezici.huaweileaguemanager.model.FixtureData;
import com.ahmetgezici.huaweileaguemanager.model.Teams;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {FixtureData.class, Teams.class}, version = 1)
public abstract class FixtureDatabase extends RoomDatabase {

    private static FixtureDatabase instance = null;

    public abstract FixtureDAO fixtureDAO();
    public abstract TeamsDAO teamsDAO();

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized FixtureDatabase getDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    FixtureDatabase.class, "fixture_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
