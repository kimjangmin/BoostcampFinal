package com.jm.gon.triphelper.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 김장민 on 2017-02-20.
 */

public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(Context context){
        super(context, DbTable.AutoCompleteTable.DBNAME,null, DbTable.AutoCompleteTable.VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ DbTable.AutoCompleteTable.TABLENAME +"( "+
                " _id integer primary key autoincrement, " +
                DbTable.AutoCompleteTable.TITLE +" string not null, "+
                DbTable.AutoCompleteTable.CONTENTTYPEID+ " string not null "+");");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table " + DbTable.AutoCompleteTable.TABLENAME);
        onCreate(db);
    }
}
