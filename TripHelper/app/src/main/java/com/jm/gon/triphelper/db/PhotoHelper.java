package com.jm.gon.triphelper.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 김장민 on 2017-02-25.
 */

public class PhotoHelper extends SQLiteOpenHelper {
    public PhotoHelper(Context context){
        super(context, DbTable.AutoCompleteTable.PHOTODBNAME,null, DbTable.AutoCompleteTable.PHOTOVERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ DbTable.AutoCompleteTable.PHOTOTABLENAME +" ( "+
                " _id integer primary key autoincrement, " +
                DbTable.AutoCompleteTable.PHOTOURL +" string not null, "+
                DbTable.AutoCompleteTable.PHOTOCOMMENT + " string not null);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("drop table " + DbTable.AutoCompleteTable.TABLENAME);
        db.execSQL("drop table " + DbTable.AutoCompleteTable.PHOTOTABLENAME);
        onCreate(db);
    }
}