package com.jm.gon.triphelper;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jm.gon.triphelper.db.DbHelper;
import com.jm.gon.triphelper.db.DbTable;
import com.jm.gon.triphelper.functionplan2.FunctionPlan2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class FunctionPlan1 extends AppCompatActivity implements TextWatcher{

    private AutoCompleteTextView actv_FunctionPlan1_spot;
    private List<String> listarr;
    private SQLiteDatabase sqLiteDatabase;
    private DbHelper dbHelper;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ImageButton iv_FunctionPlan1_add;
    private ImageButton ib_FunctionPlan1_Departure;
    private ImageButton ib_FunctionPlan1_Arrival;
    private TextView tv_FunctionPlan1_DateStart;
    private TextView tv_FunctionPlan1_DateEnd;
    private Spinner sp_FunctionPlan1_theme;
    private Spinner sp_FunctionPlan1_city;
    private Toolbar t_FunctionPlan1_toolbar;

    private String theme[] = {"0","12","14","15","25","28","38","39"};
    private String city[] = {"1","2","3","4","5","6","7","8","31","32","33","34","35","36","37","38","39"};
    private String selectedtheme;
    private String selectedcity;
    private boolean isthemeSelected;
    private boolean iscitySelected;

    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_function_plan1_1);

        tv_FunctionPlan1_DateStart = (TextView)findViewById(R.id.tv_FunctionPlan1_DateStart);
        tv_FunctionPlan1_DateEnd = (TextView)findViewById(R.id.tv_FunctionPlan1_DateEnd);
        ib_FunctionPlan1_Departure = (ImageButton) findViewById(R.id.ib_FunctionPlan1_Departure);
        ib_FunctionPlan1_Arrival = (ImageButton) findViewById(R.id.ib_FunctionPlan1_Arrival);
        iv_FunctionPlan1_add = (ImageButton)findViewById(R.id.iv_FunctionPlan1_add);
        sp_FunctionPlan1_theme = (Spinner)findViewById(R.id.sp_FunctionPlan1_theme);
        sp_FunctionPlan1_city = (Spinner)findViewById(R.id.sp_FunctionPlan1_city);
        t_FunctionPlan1_toolbar = (Toolbar)findViewById(R.id.t_FunctionPlan1_toolbar);

        tv_FunctionPlan1_DateStart.setOnClickListener(dateStartClickListener);
        ib_FunctionPlan1_Departure.setOnClickListener(dateStartClickListener);
        ib_FunctionPlan1_Arrival.setOnClickListener(dateEndClickListener);
        tv_FunctionPlan1_DateEnd.setOnClickListener(dateEndClickListener);
        iv_FunctionPlan1_add.setOnClickListener(autoCompleteClickListener);
        sp_FunctionPlan1_theme.setOnItemSelectedListener(themeSetListener);
        sp_FunctionPlan1_city.setOnItemSelectedListener(citySetListener);
        selectedtheme="0";
        selectedcity ="1";


        setSupportActionBar(t_FunctionPlan1_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        t_FunctionPlan1_toolbar.setTitle("");

        GregorianCalendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        dbHelper = new DbHelper(this);
        sqLiteDatabase = dbHelper.getReadableDatabase();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView = (ListView)findViewById(R.id.list_spot);
        listView.setAdapter(adapter);

        listarr = new ArrayList<>();


        actv_FunctionPlan1_spot = (AutoCompleteTextView) findViewById(R.id.actv_FunctionPlan1_spot);
        actv_FunctionPlan1_spot.addTextChangedListener(this);
        actv_FunctionPlan1_spot.setTextColor(getResources().getColor(R.color.fontColor));
        filter();
    }
    private void filter(){
        Cursor cursor;
        if(iscitySelected==false && isthemeSelected == false){
            Log.i("TAG","all of things is false");
            cursor = sqLiteDatabase.query(DbTable.AutoCompleteTable.TABLENAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
        }else if(iscitySelected == true && isthemeSelected == true){
            Log.i("TAG","all of things is true");
            cursor =sqLiteDatabase.query(DbTable.AutoCompleteTable.TABLENAME,
                    null,
                    DbTable.AutoCompleteTable.CONTENTTYPEID+" = ? and "+ DbTable.AutoCompleteTable.AREACODE+" = ?",
                    new String[]{selectedtheme, selectedcity},
                    null,
                    null,
                    null);
        }else{
            Log.i("TAG","one of things is true");
            cursor =sqLiteDatabase.query(DbTable.AutoCompleteTable.TABLENAME,
                    null,
                    DbTable.AutoCompleteTable.CONTENTTYPEID+" = ? or "+ DbTable.AutoCompleteTable.AREACODE+" = ?",
                    new String[]{selectedtheme, selectedcity},
                    null,
                    null,
                    null);
        }
        listarr.clear();
        while(cursor.moveToNext()){
            listarr.add(cursor.getString(cursor.getColumnIndex(DbTable.AutoCompleteTable.TITLE)));
        }
        actv_FunctionPlan1_spot.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                listarr));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.functionplan1_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.Funcion_Plan1:
                gettingInfo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void gettingInfo(){
        Intent intent = new Intent(FunctionPlan1.this, FunctionPlan2.class);
        if((tv_FunctionPlan1_DateStart.getText().toString()).equals("Depature date")){
            new DatePickerDialog(FunctionPlan1.this, dateStartSetListener, year, month, day).show();
            Toast.makeText(FunctionPlan1.this, "input departure date",Toast.LENGTH_SHORT).show();
            return;
        }
        if((tv_FunctionPlan1_DateEnd.getText().toString()).equals("Arrival date")){
            new DatePickerDialog(FunctionPlan1.this, dateEndSetListener, year, month, day).show();
            Toast.makeText(FunctionPlan1.this, "input Arrival date",Toast.LENGTH_SHORT).show();
            return;
        }
        int date = diffDate();
        ArrayList<String> spotlist =new ArrayList<>();
        for(int i=0;i<adapter.getCount(); i++){
            spotlist.add(adapter.getItem(i));
        }
        intent.putExtra("date",date);
        intent.putExtra("theme",selectedtheme);
        intent.putExtra("city", selectedcity);
        intent.putExtra("spot", spotlist);
        startActivity(intent);
    }

    private int diffDate() {
        String start = tv_FunctionPlan1_DateStart.getText().toString();
        String end = tv_FunctionPlan1_DateEnd.getText().toString();
        long date = -1;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = format.parse(start);
            Date endDate = format.parse(end);

            date = (endDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000) + 1;


        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return (int)date;
    }

    private View.OnClickListener autoCompleteClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            adapter.add(actv_FunctionPlan1_spot.getText().toString());
            actv_FunctionPlan1_spot.setText("");
        }
    };
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
            tv_FunctionPlan1_DateStart.setText(date);
        }
    };
    private DatePickerDialog.OnDateSetListener dateEndSetListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String date = String.format("%d-%d-%d",year,monthOfYear+1,dayOfMonth);
            tv_FunctionPlan1_DateEnd.setText(date);
        }
    };

    private AdapterView.OnItemSelectedListener themeSetListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectedtheme = theme[position];
            isthemeSelected = true;
            filter();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            selectedtheme = theme[0];
        }
    };

    private AdapterView.OnItemSelectedListener citySetListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectedcity = city[position];
            if(selectedcity.equals(city[0])){
                iscitySelected = false;
            }
            else{
                iscitySelected = true;
            }
            filter();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            selectedcity = city[0];
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
}
