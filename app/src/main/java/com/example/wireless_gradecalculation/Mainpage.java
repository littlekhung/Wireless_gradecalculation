package com.example.wireless_gradecalculation;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.akexorcist.localizationactivity.ui.LocalizationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;



public class Mainpage extends LocalizationActivity {
    private ExpandableListView listView;
    private ImageView en;
    private ImageView th;
    private ImageView setting;
    private TextView userName;
    User user;
    String[] parent = new String[]{"Year1", "Year2", "Year3","Year4"};
    String[] q1 = new String[]{"Term1", "Term2"};
    String[] q2 = new String[]{"Term1", "Term2"};
    String[] q3 = new String[]{"Term1", "Term2"};
    String[] q4 = new String[]{"Term1", "Term2"};
    String[] des1 = new String[]{"wow","wow","wow","wow","wow","wow","wow","wow","wow","wow"};
    String[] des2 = new String[]{"???"};
    String[] des3 = new String[]{"wow"};
    String[] des4 = new String[]{"???"};
    String[] des5 = new String[]{"wow"};
    String[] des6 = new String[]{"???"};
    String[] des7 = new String[]{"wow"};
    String[] des8 = new String[]{"???"};

    //private ExpandableListAdapter listAdapter;
   // private List<String> listdata;
    //private HashMap<String, List<String>> listHashMap;


    LinkedHashMap<String, String[]> thirdLevelq1 = new LinkedHashMap<>();
    LinkedHashMap<String, String[]> thirdLevelq2 = new LinkedHashMap<>();
    LinkedHashMap<String, String[]> thirdLevelq3 = new LinkedHashMap<>();
    LinkedHashMap<String, String[]> thirdLevelq4 = new LinkedHashMap<>();
    /**
     * Second level array list
     */
    List<String[]> secondLevel = new ArrayList<>();
    /**
     * Inner level data
     */
    List<LinkedHashMap<String, String[]>> data = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        setTitle(getString(R.string.app_name));
        Gson gson = new Gson();
        Intent i = getIntent();
        user = gson.fromJson(i.getStringExtra("user"),User.class);
        userName = (TextView) findViewById(R.id.mainpageUserName);
        userName.setText(user.getFirstname()+" "+user.getLastname());
        Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
        /////select bachelor or  maskter
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(Mainpage.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.education_level));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);
        setUpAdapter();

        en = (ImageView)findViewById(R.id.en);
        th = (ImageView)findViewById(R.id.th);
        en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLanguage("en");
            }
        });
        th.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLanguage("th");
            }
        });
        setting = (ImageView) findViewById(R.id.settingButton);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                Intent settingpage = new Intent(Mainpage.this,Setting.class);
                settingpage.putExtra("user",gson.toJson(user));
                startActivity(settingpage);
            }
        });

    }



    private void setUpAdapter() {
        secondLevel.add(q1);
        secondLevel.add(q2);
        secondLevel.add(q3);
        secondLevel.add(q4);

        thirdLevelq1.put(q1[0], des1);
        thirdLevelq1.put(q1[1], des2);
        thirdLevelq2.put(q2[0], des3);
        thirdLevelq2.put(q2[1], des4);
        thirdLevelq3.put(q3[0], des5);
        thirdLevelq3.put(q3[1], des6);
        thirdLevelq4.put(q4[0], des7);
        thirdLevelq4.put(q4[1], des8);


        data.add(thirdLevelq1);
        data.add(thirdLevelq2);
        data.add(thirdLevelq3);
        data.add(thirdLevelq4);

        listView = (ExpandableListView) findViewById(R.id.lv);
        //passing three level of information to constructor
        NestedListAdapter nestedListAdapterAdapter = new NestedListAdapter(this, parent, secondLevel, data);
        listView.setAdapter(nestedListAdapterAdapter);
        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    listView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });


    }

}






