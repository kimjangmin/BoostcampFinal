package com.example.gon.triphelper.db;

import android.provider.BaseColumns;

/**
 * Created by 김장민 on 2017-02-20.
 */

public class DbTable {
    public static final class AutoCompleteTable implements BaseColumns{
        private AutoCompleteTable(){}
        public static final String DBNAME = "autocomplete.db";
        public static final int VERSION = 2;
        public static final String TABLENAME = "autucompletetable";
        public static final String TITLE = "title";
    }
}
