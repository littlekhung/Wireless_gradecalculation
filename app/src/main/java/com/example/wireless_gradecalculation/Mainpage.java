package com.example.wireless_gradecalculation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.localizationactivity.ui.LocalizationActivity;
import com.example.wireless_gradecalculation.studentgradedatabase.AppDatabase;
import com.example.wireless_gradecalculation.studentgradedatabase.Course;
import com.example.wireless_gradecalculation.studentgradedatabase.CourseDao;
import com.example.wireless_gradecalculation.studentgradedatabase.DBHelper;
import com.example.wireless_gradecalculation.studentgradedatabase.StudentGrade;
import com.example.wireless_gradecalculation.studentgradedatabase.StudentGradeDao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;



public class Mainpage extends LocalizationActivity {
    public static final int SETTING = 333;
    private ExpandableListView listView;
    private ImageView en;
    private ImageView th;
    private ImageView setting;
    private TextView userName;
    DBHelper roomDB;
    User user;

    //private ExpandableListAdapter listAdapter;
   // private List<String> listdata;
    //private HashMap<String, List<String>> listHashMap;
    /**
     * Second level array list
     */
    String[] parent;
    ArrayList<String[]>secondLevel = new ArrayList<>();
    /**
     * Inner level data
     */
    public static List<LinkedHashMap<String, Grade[]>> data = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        ////get DB HELPER///////
        roomDB = new DBHelper(this);
        ////////////////////////
        setTitle(getString(R.string.app_name));
        if (savedInstanceState == null) {
            Gson gson = new Gson();
            Intent i = getIntent();
            user = gson.fromJson(i.getStringExtra("user"),User.class);
        }else{
            user = new Gson().fromJson(savedInstanceState.getString("user"),User.class);
        }

        iniProf();

        Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
        /////select bachelor or  maskter
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(Mainpage.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.education_level));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        setUpAdapterBachelor();
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
                startActivityForResult(settingpage,SETTING);
            }
        });

//        ////room
//        FloatingActionButton fab = findViewById(R.id.complete);
//        fab.setOnClickListener((view){
//                Snackbar.make( view, "Replace with your action", Snackbar.LENGTH_INDEFINITE.setAction("Action",null).show()
//        });
//        datainTable = ViewModelProviders.of(this).get(DatainTable.class);
    }
    //////// save state//////////
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("user", new Gson().toJson(user));
        super.onSaveInstanceState(outState);
    }
    //////// initiate profile////////////
    public void iniProf(){
        userName = (TextView) findViewById(R.id.mainpageUserName);
        userName.setText(user.getFirstname()+" "+user.getLastname());
        if(user.getPicuri()!=null){
            try {
                Uri getPic = Uri.parse(user.getPicuri());
                Bitmap pic = MediaStore.Images.Media.getBitmap(this.getContentResolver(), getPic);
                pic = rotateIfNeed(pic,getPic);
                ((ImageView) findViewById(R.id.image)).setImageBitmap(pic);
            }catch (Exception e){
            }
        }
    }

    //load all course student register from database
    private void setUpAdapterBachelor() {
        parent = new String[]{"Year1", "Year2", "Year3","Year4"};
        String[] termlist = new String[]{"Term1", "Term2"};
        secondLevel.add(termlist);
        secondLevel.add(termlist);
        secondLevel.add(termlist);
        secondLevel.add(termlist);

        List<Course> Y1S1=null;
        List<Course> Y1S2=null;
        List<Course> Y2S1=null;
        List<Course> Y2S2=null;
        List<Course> Y3S1=null;
        List<Course> Y3S2=null;
        List<Course> Y4S1=null;
        List<Course> Y4S2=null;
        try{
            Y1S1 = roomDB.loadEnrollCourse(user.getUID(),1,1,"Bachelor");
            Y1S2 = roomDB.loadEnrollCourse(user.getUID(),1,2,"Bachelor");
            Y2S1 = roomDB.loadEnrollCourse(user.getUID(),2,1,"Bachelor");
            Y2S2 = roomDB.loadEnrollCourse(user.getUID(),2,2,"Bachelor");
            Y3S1 = roomDB.loadEnrollCourse(user.getUID(),3,1,"Bachelor");
            Y3S2 = roomDB.loadEnrollCourse(user.getUID(),3,2,"Bachelor");
            Y4S1 = roomDB.loadEnrollCourse(user.getUID(),4,1,"Bachelor");
            Y4S2 = roomDB.loadEnrollCourse(user.getUID(),4,2,"Bachelor");
        }catch (Exception e){
            e.printStackTrace();
        }
        List<Grade> gY1S1 = getGradesFromCourses(Y1S1);
        List<Grade> gY1S2 = getGradesFromCourses(Y1S2);
        List<Grade> gY2S1 = getGradesFromCourses(Y2S1);
        List<Grade> gY2S2 = getGradesFromCourses(Y2S2);
        List<Grade> gY3S1 = getGradesFromCourses(Y3S1);
        List<Grade> gY3S2 = getGradesFromCourses(Y3S2);
        List<Grade> gY4S1 = getGradesFromCourses(Y4S1);
        List<Grade> gY4S2 = getGradesFromCourses(Y4S2);

        Grade[] grade1 = createGradeArray(gY1S1);
        Grade[] grade2 = createGradeArray(gY1S2);
        Grade[] grade3 = createGradeArray(gY2S1);
        Grade[] grade4 = createGradeArray(gY2S2);
        Grade[] grade5 = createGradeArray(gY3S1);
        Grade[] grade6 = createGradeArray(gY3S2);
        Grade[] grade7 = createGradeArray(gY4S1);
        Grade[] grade8 = createGradeArray(gY4S2);

        LinkedHashMap<String, Grade[]> Year1 = new LinkedHashMap<>();
        LinkedHashMap<String, Grade[]> Year2 = new LinkedHashMap<>();
        LinkedHashMap<String, Grade[]> Year3 = new LinkedHashMap<>();
        LinkedHashMap<String, Grade[]> Year4 = new LinkedHashMap<>();

        Year1.put(termlist[0], grade1);
        Year1.put(termlist[1], grade2);
        Year2.put(termlist[0], grade3);
        Year2.put(termlist[1], grade4);
        Year3.put(termlist[0], grade5);
        Year3.put(termlist[1], grade6);
        Year4.put(termlist[0], grade7);
        Year4.put(termlist[1], grade8);

        data.add(Year1);
        data.add(Year2);
        data.add(Year3);
        data.add(Year4);

    }

    ///////////transform CourseID to Grade////////////////////////////
    Grade getGradeFromCourse(Course c){
        StudentGrade gradeRetrieve=null;
        try {
            gradeRetrieve = roomDB.loadGradeByCID(c.CID, user.getUID());
        }catch (Exception e){
            e.printStackTrace();
        }
        return gradeRetrieve==null?null:new Grade(c.CID+"_"+c.courseName,c.credit,gradeRetrieve.grade);
    }

    List<Grade> getGradesFromCourses(List<Course> cs){
        ArrayList<Grade> ret = new ArrayList<>();
        for(Course c : cs){
            ret.add(getGradeFromCourse(c));
        }
        return ret;
    }
    ////////// make grade array/////////////////////////
    public static Grade[] createGradeArray(List<Grade> gs){
        gs.add(0,new Grade());
        gs.add(new Grade());
        return gs.toArray(new Grade[gs.size()]);
    }
    //////////// get image if it is changed //////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case SETTING:
                if(resultCode == Activity.RESULT_OK)
                {
                    try{
                        Gson gson = new Gson();
                        user = gson.fromJson(data.getStringExtra("user"),User.class);
                        userName.setText(user.getFirstname()+" "+user.getLastname());
                        Uri getPic = Uri.parse(user.getPicuri());
                        Bitmap pic = MediaStore.Images.Media.getBitmap(getContentResolver(), getPic);
                        pic = rotateIfNeed(pic,getPic);
                        ((ImageView) findViewById(R.id.image) ).setImageBitmap(pic);
                    }catch (Exception e){

                    }
                }
                break;
        }
    }
    //from https://stackoverflow.com/questions/14066038/why-does-an-image-captured-using-camera-intent-gets-rotated-on-some-devices-on-a?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
    public Bitmap rotateIfNeed(Bitmap pic, Uri imUri){
        Bitmap rotate = null;
        try{
            ExifInterface ei = new ExifInterface(getContentResolver().openInputStream(imUri));
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            switch(orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = rotateImage(pic, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = rotateImage(pic, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = rotateImage(pic, 270);
                    break;
                default:
                    rotate = pic;

            }
        }catch (Exception e){

        }
        return rotate;
    }
    //from https://stackoverflow.com/questions/14066038/why-does-an-image-captured-using-camera-intent-gets-rotated-on-some-devices-on-a?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}






