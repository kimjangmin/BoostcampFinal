package com.jm.gon.triphelper.db;

import android.provider.BaseColumns;

/**
 * Created by 김장민 on 2017-02-20.
 */

public class DbTable {
    public static final class AutoCompleteTable implements BaseColumns{
        private AutoCompleteTable(){}
        public static final String DBNAME = "autocomplete.db";
        public static final int VERSION = 10;
        public static final String TABLENAME = "autucompletetable";
        public static final String TITLE = "title";
        public static final String CONTENTTYPEID = "contentTypeID";
        public static final String AREACODE ="areacode";

        public static final String PHOTODBNAME = "photo.db";
        public static final int PHOTOVERSION=1;
        public static final String PHOTOTABLENAME = "phototable";
        public static final String PHOTOURL = "photourl";
        public static final String PHOTOCOMMENT = "comment";
    }
}
