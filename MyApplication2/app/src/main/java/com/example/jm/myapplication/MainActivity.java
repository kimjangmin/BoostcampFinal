package com.example.jm.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class MainActivity extends Activity
        implements TextWatcher {

    private AutoCompleteTextView autoComplete;
    private String item[] = {"C","C++","Java",".NET","iPhone",
            "Android","ASP.NET","PHP"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autoComplete = (AutoCompleteTextView) findViewById(R.id.myautocomplete);
        autoComplete.addTextChangedListener(this);
        autoComplete.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                item));
        autoComplete.setTextColor(Color.RED);
    }

    public void afterTextChanged(Editable arg0) {
        // TODO Auto-generated method stub

    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // TODO Auto-generated method stub

    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub

    }
}
