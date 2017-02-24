package com.jm.gon.triphelper;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.jm.gon.triphelper.db.DbHelper;
import com.jm.gon.triphelper.db.DbTable;

import java.util.ArrayList;
import java.util.List;

public class FunctionSearch extends AppCompatActivity implements TextWatcher {

   List<String> arrayList;

    private AutoCompleteTextView actv_FunctionSearch;
    private ImageButton ib_FunctionSearch_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_function_search);
        actv_FunctionSearch = (AutoCompleteTextView)findViewById(R.id.actv_FunctionSearch);
        ib_FunctionSearch_search = (ImageButton)findViewById(R.id.ib_FunctionSearch_search);
        arrayList = new ArrayList<>();

        actv_FunctionSearch.addTextChangedListener(this);

        ib_FunctionSearch_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FunctionSearch.this , DetailActivity.class);
                intent.putExtra("title", actv_FunctionSearch.getText().toString());
                startActivity(intent);
            }
        });

        init();
    }
    public void init(){
        SQLiteDatabase sqLiteDatabase;
        DbHelper helper;
        Cursor cursor;
        helper = new DbHelper(this);
        sqLiteDatabase = helper.getReadableDatabase();
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        Log.i("GAG","searching type = "+type);

        if(type.equals("32")){
            ib_FunctionSearch_search.setImageDrawable(getResources().getDrawable(R.drawable.hotel));
            cursor = sqLiteDatabase.query(DbTable.AutoCompleteTable.TABLENAME,
                    null,
                    DbTable.AutoCompleteTable.CONTENTTYPEID+ " =? ",
                    new String[]{type} ,
                    null,
                    null,
                    null);
        }else if( type.equals("39")){
            ib_FunctionSearch_search.setImageDrawable(getResources().getDrawable(R.drawable.restaurant));
            cursor = sqLiteDatabase.query(DbTable.AutoCompleteTable.TABLENAME,
                    null,
                    DbTable.AutoCompleteTable.CONTENTTYPEID+ " =? ",
                    new String[]{type} ,
                    null,
                    null,
                    null);
        }
        else{
            ib_FunctionSearch_search.setImageDrawable(getResources().getDrawable(R.drawable.temple));
            cursor = sqLiteDatabase.query(DbTable.AutoCompleteTable.TABLENAME,
                    null,
                    DbTable.AutoCompleteTable.CONTENTTYPEID+" != ? and " + DbTable.AutoCompleteTable.CONTENTTYPEID + " != ?",
                    new String[]{"32","39"} ,
                    null,
                    null,
                    null);
        }
        Log.i("TAG","cursor = "+cursor.getCount());
        arrayList.clear();
        while(cursor.moveToNext()){
            Log.i("TAG","cursor = "+cursor.getPosition());
            arrayList.add(cursor.getString(cursor.getColumnIndex(DbTable.AutoCompleteTable.TITLE)));
        }
        actv_FunctionSearch.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                arrayList));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
