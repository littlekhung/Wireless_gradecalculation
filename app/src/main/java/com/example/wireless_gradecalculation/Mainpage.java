package com.example.wireless_gradecalculation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class Mainpage extends AppCompatActivity {
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listdata;
    private HashMap<String, List<String>> listHashMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
        /////select bachelor or  maskter
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(Mainpage.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.education_level));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);
        /////expand////
        listView = (ExpandableListView)findViewById(R.id.lv);
        data();
        listAdapter = new Expanable(this,listdata,listHashMap);
        listView.setAdapter(listAdapter);

    }
    ///expand data
    private void data()
    {
        listdata = new ArrayList<>();
        listHashMap = new HashMap<>();
        listdata.add("Year1");
        listdata.add("Year2");
        listdata.add("Year3");
        listdata.add("Year4");

        List<String> year1 = new ArrayList<>();
        year1.add("term1");
        year1.add("term2");
        List<String> year2 = new ArrayList<>();
        year2.add("term1");
        year2.add("term2");
        List<String> year3 = new ArrayList<>();
        year3.add("term1");
        year3.add("term2");
        List<String> year4 = new ArrayList<>();
        year4.add("term1");
        year4.add("term2");

        listHashMap.put(listdata.get(0),year1);
        listHashMap.put(listdata.get(1),year2);
        listHashMap.put(listdata.get(2),year3);
        listHashMap.put(listdata.get(3),year4);
    }
}






