package com.example.gon.triphelper.Network_MainActivity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 김장민 on 2017-02-19.
 */

public class ApiDataTable {
    @SerializedName("title")
    String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
