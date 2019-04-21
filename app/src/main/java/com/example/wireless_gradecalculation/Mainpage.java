// mainpage for our applciation
// it provide function to calculate grade and change language
package com.example.wireless_gradecalculation;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.localizationactivity.ui.LocalizationActivity;
import com.example.wireless_gradecalculation.studentgradedatabase.Course;
import com.example.wireless_gradecalculation.studentgradedatabase.DBHelper;
import com.example.wireless_gradecalculation.studentgradedatabase.StudentGrade;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Share;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;



public class Mainpage extends LocalizationActivity {
    public static final int SETTING = 333;
    public static ExpandableListView listView;
    private ImageView en;
    private ImageView th;
    private ImageView setting;
    private TextView userName;
    private TextView gradeView;
    private DBHelper roomDB;
    private User user;
    public static String degType="Bachelor";

    //private ExpandableListAdapter listAdapter;
   // private List<String> listdata;
    //private HashMap<String, List<String>> listHashMap;
    /**
     * Second level array list
     */
    String[] bachelorParents;
    ArrayList<String[]> bachelorSecondLevel;
    String[] masterParents;
    ArrayList<String[]> masterSecondLevel;
    /**
     * Inner level data
     */
    public static List<LinkedHashMap<String, Grade[]>> bachelorData;
    public static List<LinkedHashMap<String, Grade[]>> masterData;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        ///facebook
        FacebookSdk.sdkInitialize( this.getApplicationContext() );
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
        setUpAdapterBachelor();
        setUpAdapterMaster();
        iniProf();
        gradeView = (TextView) findViewById(R.id.totalGrade);
        Spinner mySpinner = (Spinner) findViewById(R.id.degreeType);
        /////select bachelor or  maskter
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(Mainpage.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.education_level));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Spinner degSpinner = (Spinner) findViewById(R.id.degreeType);
        listView = (ExpandableListView) findViewById(R.id.lv);
        degSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        degType="Bachelor";
                        //passing three level of information to constructor
                        NestedListAdapter nestedListAdapterAdapter = new NestedListAdapter(Mainpage.this, bachelorParents, bachelorSecondLevel, bachelorData);
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
                        //////////////////grade calculation//////////////////
                        gradeView.setText(calculateGrade());
                        break;
                    case 1:
                        degType="Master";
                        NestedListAdapter nestedListAdapterAdapterMaster = new NestedListAdapter(Mainpage.this, masterParents, masterSecondLevel, masterData);
                        listView.setAdapter(nestedListAdapterAdapterMaster);
                        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                            int previousGroup = -1;

                            @Override
                            public void onGroupExpand(int groupPosition) {
                                if (groupPosition != previousGroup)
                                    listView.collapseGroup(previousGroup);
                                previousGroup = groupPosition;
                            }
                        });
                        //////////////////grade calculation//////////////////
                        gradeView.setText(calculateGrade());
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ////////////////collapse all parent//////////////////
        int count =  listView==null?0:listView.getCount();
        for (int i = 0; i <count ; i++)
            listView.collapseGroup(i);
        //////////////////change language////////////////////
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

//        printkey();




//        ////room
//        FloatingActionButton fab = findViewById(R.id.complete);
//        fab.setOnClickListener((view){
//                Snackbar.make( view, "Replace with your action", Snackbar.LENGTH_INDEFINITE.setAction("Action",null).show()
//        });
//        datainTable = ViewModelProviders.of(this).get(DatainTable.class);





    }
    ////share facebook get hash key for facebook api//////////////////////////////
    ////https://www.youtube.com/watch?v=2ZdzG_XObDM
    ////https://stackoverflow.com/questions/30224390/how-post-to-wall-facebook-android-sdk4-0-0
//    private void printkey() {
//        try{
//            PackageInfo info = getPackageManager().getPackageInfo( "com.example.wireless_gradecalculation",
//                    PackageManager.GET_SIGNATURES);
//            for(Signature signature : info.signatures)
//            {
//                MessageDigest md = MessageDigest.getInstance("SHA" );
//                md.update(signature.toByteArray());
//                Log.d("KeyHash", Base64.encodeToString( md.digest(),Base64.DEFAULT ) );
//            }
//        }catch(PackageManager.NameNotFoundException e)
//        {
//            e.printStackTrace();
//        }catch (NoSuchAlgorithmException e)
//        {
//            e.printStackTrace();
//        }
//    }

    //////// save state//////////
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("user", new Gson().toJson(user));
        super.onSaveInstanceState(outState);
    }

    /**
     * //////// initiate user object////////////
     */
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


    /**
     * set up data for bachelor degree
     * load all graded course from database and initiate data to generate
     * expandable listview for grade calculation
     */
    private void setUpAdapterBachelor() {
        bachelorSecondLevel = new ArrayList<>();
        bachelorData = new ArrayList<>();
        bachelorParents = new String[]{getString(R.string.year1),getString(R.string.year2),getString(R.string.year3),getString(R.string.year4)};
        String[] termlist = new String[]{getString(R.string.semester1),getString(R.string.semester2)};
        bachelorSecondLevel.add(termlist);
        bachelorSecondLevel.add(termlist);
        bachelorSecondLevel.add(termlist);
        bachelorSecondLevel.add(termlist);

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

        bachelorData.add(Year1);
        bachelorData.add(Year2);
        bachelorData.add(Year3);
        bachelorData.add(Year4);

    }

    /**
     *     same as above but it is master degree
     */
    private void setUpAdapterMaster(){
        masterSecondLevel = new ArrayList<>();
        masterData = new ArrayList<>();
        masterParents = new String[]{getString(R.string.masterDegree)};
        String[] termlist = new String[]{getString(R.string.semester1),getString(R.string.semester2),getString(R.string.semester3)};
        masterSecondLevel.add(termlist);
        List<Course> S1=null;
        List<Course> S2=null;
        List<Course> S3=null;
        try{
            S1 = roomDB.loadEnrollCourse(user.getUID(),1,1,"Master");
            S2 = roomDB.loadEnrollCourse(user.getUID(),1,2,"Master");
            S3 = roomDB.loadEnrollCourse(user.getUID(),1,3,"Master");
        }catch (Exception e){
            e.printStackTrace();
        }
        List<Grade> gS1 = getGradesFromCourses(S1);
        List<Grade> gS2 = getGradesFromCourses(S2);
        List<Grade> gS3 = getGradesFromCourses(S3);

        Grade[] grade1 = createGradeArray(gS1);
        Grade[] grade2 = createGradeArray(gS2);
        Grade[] grade3 = createGradeArray(gS3);

        LinkedHashMap<String, Grade[]> master = new LinkedHashMap<>();

        master.put(termlist[0], grade1);
        master.put(termlist[1], grade2);
        master.put(termlist[2], grade3);

        masterData.add(master);
    }


    /**
     * @param c course
     * @return grade from c (course)
     */
    Grade getGradeFromCourse(Course c){
        StudentGrade gradeRetrieve=null;
        try {
            gradeRetrieve = roomDB.loadGradeByCID(c.CID, user.getUID());
        }catch (Exception e){
            e.printStackTrace();
        }
        return gradeRetrieve==null?null:new Grade(c.CID+"_"+c.courseName,c.credit,gradeRetrieve.grade);
    }

    /**
     *
     * @param cs list of course
     * @return list of grade from cs
     */
    List<Grade> getGradesFromCourses(List<Course> cs){
        ArrayList<Grade> ret = new ArrayList<>();
        for(Course c : cs){
            ret.add(getGradeFromCourse(c));
        }
        return ret;
    }

    /**
     * make grade array from list of grade
     * we will add empty grade to first and last for expandable listview ui
     * @param gs list of grade
     * @return array of grade ready to use
     */
    public static Grade[] createGradeArray(List<Grade> gs){
        gs.add(0,new Grade());
        gs.add(new Grade());
        return gs.toArray(new Grade[gs.size()]);
    }


    /**
     *     get image if it is changed //////////////
      */
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


    /**
     *   rotate image to proper image
     *   from https://stackoverflow.com/questions/14066038/why-does-an-image-captured-using-camera-intent-gets-rotated-on-some-devices-on-a?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
     * @param pic bitmap of image
     * @param imUri uri of image
     * @return rotated image
     */
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


    /**
     * used by rotatedimageifneed method
     * from https://stackoverflow.com/questions/14066038/why-does-an-image-captured-using-camera-intent-gets-rotated-on-some-devices-on-a?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
     * @param source bitmap image
     * @param angle rotation angle
     * @return rotated image
     */
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    /**
     *  calculate grade from the data we have
     * @return grade in string format X.XX
     */
    public static String calculateGrade(){
        double grade=-1;
        switch (degType){
            case "Bachelor":
                grade = calculateGradeOf(bachelorData);
                break;
            case "Master":
                grade = calculateGradeOf(masterData);
                break;
        }

        return String.format("%.2f",grade);
    }

    /**
     * calculate grade of the data
     * @param data the data from expanable listview contain all grade
     * @return grade in double
     */
    public static double calculateGradeOf(List<LinkedHashMap<String,Grade[]>> data){
        double totalCreditGain = 0;
        double totalCredit = 0;
        for(LinkedHashMap<String, Grade[]> y : data){
            for(Grade[] gs : y.values()){
                for(Grade g: gs){
                    totalCredit+=g.credit;
                    totalCreditGain+=g.credit*gradeStringtoInt(g.grade);
                }
            }
        }
        return totalCreditGain==0?0:(totalCreditGain/totalCredit);
    }

    /**
     * transform string to grade
     * @param grade grade (A,B+,B,...)
     * @return numerical grade
     */
    public static double gradeStringtoInt(String grade){
        switch (grade){
            case "A":
                return 4;
            case "B+":
                return 3.5;
            case "B":
                return 3;
            case "C+":
                return 2.5;
            case "C":
                return 2;
            case "D+":
                return 1.5;
            case "D":
                return 1;
            case "F":
                return 0;
                default:
                    return 999;
        }
    }
}






