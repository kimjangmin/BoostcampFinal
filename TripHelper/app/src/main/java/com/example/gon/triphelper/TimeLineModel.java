package com.example.gon.triphelper;

import java.io.Serializable;

/**
 * Created by 김장민 on 2017-02-21.
 */

public class TimeLineModel implements Serializable {

    private String title;
    private String tel;
    private String addr;
    private String mapx;
    private String mapy;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getMapx() {
        return mapx;
    }

    public void setMapx(String mapx) {
        this.mapx = mapx;
    }

    public String getMapy() {
        return mapy;
    }

    public void setMapy(String mapy) {
        this.mapy = mapy;
    }
}
