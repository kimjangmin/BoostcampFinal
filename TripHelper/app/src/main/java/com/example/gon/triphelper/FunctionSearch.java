package com.example.gon.triphelper;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;

import com.example.gon.triphelper.db.DbHelper;
import com.example.gon.triphelper.db.DbTable;

import java.util.ArrayList;

public class FunctionSearch extends AppCompatActivity {

    FunctionSearchAdapter functionSearchAdapter;
    EditText et_FunctionSearch_keyword;
    ListView lv_FunctionSearch_list;
    ArrayList<String> arrayList;
    SQLiteDatabase sqLiteDatabase;
    DbHelper helper;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_search);

        init();
        setAdapter();
        et_FunctionSearch_keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                FunctionSearch.this.functionSearchAdapter.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    public void init(){
        et_FunctionSearch_keyword = (EditText)findViewById(R.id.et_FunctionSearch_keyword);
        lv_FunctionSearch_list = (ListView)findViewById(R.id.lv_FunctionSearch_list);
        helper = new DbHelper(this);
        sqLiteDatabase = helper.getReadableDatabase();
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");

        if(type.equals("32") || type.equals("39")) {
            Log.i("TAG","init if");
            cursor = sqLiteDatabase.query(DbTable.AutoCompleteTable.TABLENAME,
                    null,
                    DbTable.AutoCompleteTable.CONTENTTYPEID+ " =? ",
                    new String[]{type} ,
                    null,
                    null,
                    null);
        }else{
            Log.i("TAG","init else");
            cursor = sqLiteDatabase.query(DbTable.AutoCompleteTable.TABLENAME,
                    null,
                    DbTable.AutoCompleteTable.CONTENTTYPEID+" != ? and " + DbTable.AutoCompleteTable.CONTENTTYPEID + " != ?",
                    new String[]{"32","39"} ,
                    null,
                    null,
                    null);
        }
        Log.i("TAG","cursor = "+cursor.getCount());
        for(int i=0;i<cursor.getCount();i++){
            arrayList.add(cursor.getString(cursor.getColumnIndex(DbTable.AutoCompleteTable.TITLE)));
        }
        String[] str = arrayList.toArray(new String[arrayList.size()]);
    }
    private void setAdapter(){
        functionSearchAdapter = new FunctionSearchAdapter(this, arrayList);
        lv_FunctionSearch_list.setAdapter(functionSearchAdapter);
    }
    public class KeywordFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String str = constraint.toString();
            FilterResults results = new Filter.FilterResults();
            if(constraint != null && constraint.toString().length()>0){
                ArrayList<String> filterlist = new ArrayList<>();
                synchronized (this){
                    for(int i = 0; i< arrayList.size(); i++){
                        if(arrayList.get(i).contains(str)){
                            filterlist.add(arrayList.get(i));
                        }
                    }
                    results.count = filterlist.size();
                    results.values = filterlist;
                }
            } else{
                synchronized (this){
                    results.count = arrayList.size();
                    results.values = arrayList;
                }
            }
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            new listAsync().execute((ArrayList<String>[]) results.values);
        }
    }
    public class listAsync extends AsyncTask<ArrayList<String>,Void, Void>{
        @Override
        protected Void doInBackground(ArrayList<String>... params) {
            arrayList.clear();
            if(params[0].size() >0){
                for(int i=0;i<params[0].size() ; i++){
                    arrayList.add(params[0].get(i));
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if(!isCancelled()){
                if(arrayList.size()>0){
                    setAdapter();
                }
            }
            super.onPostExecute(aVoid);
        }
    }
}
