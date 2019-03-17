package com.example.wireless_gradecalculation;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class mainpage extends AppCompatActivity {
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> lisdata;
    private HashMap<String, List<String>> listHashMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
        /////select bachelor or  maskter
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(mainpage.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.education_level));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);
        /////expand////
        listView = (ExpandableListView)findViewById(R.id.ListView);
        data();
        listAdapter = new Expanable(this,lisdata,listHashMap);
        listView.setAdapter(listAdapter);

    }
    ///expand data
    private void data()
    {
        lisdata = new ArrayList<>();
        listHashMap = new HashMap<>();
        lisdata.add("Year1");
        lisdata.add("Year2");
        lisdata.add("Year3");
        lisdata.add("Year4");

        List<String> year1 = new ArrayList<>();
        year1.add("term1");
        List<String> year2 = new ArrayList<>();
        year2.add("term1");
        List<String> year3 = new ArrayList<>();
        year3.add("term1");
        List<String> year4 = new ArrayList<>();
        year4.add("term1");

        listHashMap.put(lisdata.get(0),year1);
        listHashMap.put(lisdata.get(0),year2);
        listHashMap.put(lisdata.get(0),year3);
        listHashMap.put(lisdata.get(0),year4);
    }
}






