package com.jm.gon.triphelper;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 김장민 on 2017-02-21.
 */

public class DataModel implements Parcelable {


    private String title;
    private String tel;
    private String addr;
    private String mapx;
    private String mapy;
    private String url;
    private String startdate;
    private String enddate;
    private String contenttypeid;


    private String contentid;


    public DataModel() {

    }

    protected DataModel(Parcel in) {
        title = in.readString();
        tel = in.readString();
        addr = in.readString();
        mapx = in.readString();
        mapy = in.readString();
        url = in.readString();
        startdate = in.readString();
        enddate = in.readString();
        contenttypeid = in.readString();
        contentid = in.readString();
    }

    public static final Creator<DataModel> CREATOR = new Creator<DataModel>() {
        @Override
        public DataModel createFromParcel(Parcel in) {
            return new DataModel(in);
        }

        @Override
        public DataModel[] newArray(int size) {
            return new DataModel[size];
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

    public String getContenttypeid() {
        return contenttypeid;
    }

    public void setContenttypeid(String contenttypeid) {
        this.contenttypeid = contenttypeid;
    }

    public String getContentid() {
        return contentid;
    }

    public void setContentid(String contentid) {
        this.contentid = contentid;
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
        dest.writeString(contenttypeid);
        dest.writeString(contentid);
    }
}
