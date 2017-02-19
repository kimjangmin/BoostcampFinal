package com.example.gon.triphelper;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gon.triphelper.db.DbHelper;
import com.example.gon.triphelper.db.DbTable;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class FunctionPlan1 extends AppCompatActivity implements TextWatcher{

    private AutoCompleteTextView autoComplete;
    private String item[] = {"C","C++","Java",".NET","iPhone",
            "Android","ASP.NET","PHP"};
    private List<String> listarr;
    private SQLiteDatabase sqLiteDatabase;
    private DbHelper dbHelper;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private Button btn;
    private Button btn_FunctionPlan_DateStart;
    private Button btn_FunctionPlan_DateEnd;
    private TextView tv_FunctionPlan_DateStart;
    private TextView tv_FunctionPlan_DateEnd;
    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_plan1);


        btn = (Button)findViewById(R.id.btn_FunctionPlan_add);
        btn_FunctionPlan_DateStart = (Button)findViewById(R.id.btn_FunctionPlan_DateStart);
        btn_FunctionPlan_DateEnd = (Button)findViewById(R.id.btn_FunctionPlan_DateEnd);
        tv_FunctionPlan_DateStart = (TextView)findViewById(R.id.tv_FunctionPlan_DateStart);
        tv_FunctionPlan_DateEnd = (TextView)findViewById(R.id.tv_FunctionPlan_DateEnd);
        GregorianCalendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        btn_FunctionPlan_DateStart.setOnClickListener(dateStartClickListener);
        btn_FunctionPlan_DateEnd.setOnClickListener(dateEndClickListener);

        dbHelper = new DbHelper(this);
        sqLiteDatabase = dbHelper.getReadableDatabase();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView = (ListView)findViewById(R.id.list_spot);
        listView.setAdapter(adapter);


        listarr = new ArrayList<String>();
        Cursor cursor = sqLiteDatabase.query(DbTable.AutoCompleteTable.TABLENAME, null, null, null, null, null, null);
        while(cursor.moveToNext()){
            listarr.add(cursor.getString(cursor.getColumnIndex(DbTable.AutoCompleteTable.TITLE)));
            Log.i("TAG","cursor add = "+cursor.getString(cursor.getColumnIndex(DbTable.AutoCompleteTable.TITLE)));
        }

        autoComplete = (AutoCompleteTextView) findViewById(R.id.myautocomplete);
        autoComplete.addTextChangedListener(this);
        autoComplete.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                listarr));
        autoComplete.setTextColor(Color.RED);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.add(autoComplete.getText().toString());
                autoComplete.setText("");
            }
        });
    }
    private View.OnClickListener dateStartClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new DatePickerDialog(FunctionPlan1.this, dateStartSetListener, year, month, day).show();
        }
    };
    private View.OnClickListener dateEndClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new DatePickerDialog(FunctionPlan1.this, dateEndSetListener, year, month, day).show();
        }
    };
    private DatePickerDialog.OnDateSetListener dateStartSetListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String date = String.format("%d-%d-%d",year,monthOfYear+1,dayOfMonth);
            tv_FunctionPlan_DateStart.setText(date);
        }
    };
    private DatePickerDialog.OnDateSetListener dateEndSetListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String date = String.format("%d-%d-%d",year,monthOfYear+1,dayOfMonth);
            tv_FunctionPlan_DateEnd.setText(date);
        }
    };

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
    private void getList(){
        String url = "";
    }
}
