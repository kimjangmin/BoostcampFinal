package com.example.gon.triphelper;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class FunctionPlan1 extends AppCompatActivity implements TextWatcher{

    private AutoCompleteTextView autoComplete;
    private String item[] = {"C","C++","Java",".NET","iPhone",
            "Android","ASP.NET","PHP"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_plan1);
        autoComplete = (AutoCompleteTextView) findViewById(R.id.myautocomplete);
        autoComplete.addTextChangedListener(this);
        autoComplete.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                item));
        autoComplete.setTextColor(Color.RED);
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
