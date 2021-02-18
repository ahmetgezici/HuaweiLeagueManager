package com.ahmetgezici.huaweileaguemanager.model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "versus")
public class Versus {

    @PrimaryKey(autoGenerate = true)
    public int uid = 0;

    @ColumnInfo(name = "home")
    @Embedded
    public TeamData home;

    @ColumnInfo(name = "away")
    @Embedded
    public TeamData away;

}
