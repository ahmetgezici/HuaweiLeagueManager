package com.ahmetgezici.huaweileaguemanager.model;

import androidx.room.ColumnInfo;

public class TeamData {

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "logourl")
    public String logourl;

}
