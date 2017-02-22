package com.jm.gon.triphelper.functionplan2;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by 김장민 on 2017-02-21.
 */

public class TimeLineModel implements Parcelable {



    private String title;
    private String tel;
    private String addr;
    private String mapx;
    private String mapy;
    private String url;
    private String startdate;
    private String enddate;



    public TimeLineModel(){

    }
    protected TimeLineModel(Parcel in) {
        title = in.readString();
        tel = in.readString();
        addr = in.readString();
        mapx = in.readString();
        mapy = in.readString();
        url = in.readString();
        startdate = in.readString();
        enddate = in.readString();
    }

    public static final Creator<TimeLineModel> CREATOR = new Creator<TimeLineModel>() {
        @Override
        public TimeLineModel createFromParcel(Parcel in) {
            return new TimeLineModel(in);
        }

        @Override
        public TimeLineModel[] newArray(int size) {
            return new TimeLineModel[size];
        }
    };

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(tel);
        dest.writeString(addr);
        dest.writeString(mapx);
        dest.writeString(mapy);
        dest.writeString(url);
        dest.writeString(startdate);
        dest.writeString(enddate);
    }
}
