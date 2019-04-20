package com.example.wireless_gradecalculation;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wireless_gradecalculation.studentgradedatabase.AppDatabase;
import com.example.wireless_gradecalculation.studentgradedatabase.Course;
import com.example.wireless_gradecalculation.studentgradedatabase.DBHelper;
import com.example.wireless_gradecalculation.studentgradedatabase.StudentGrade;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.example.wireless_gradecalculation.Setting.MY_PERMISSIONS_REQUEST_FOR_CAMERA;
import static java.util.Arrays.asList;

public class SecondLevelAdapter extends BaseExpandableListAdapter {

    private Context context;
    List<Grade[]> data;
    String[] headers;
    public SecondLevelAdapter(Context context, String[] headers, List<Grade[]> childData) {
        this.context = context;
        this.data = childData;
        this.headers = headers;
    }

    @Override
    public int getGroupCount() {

        return headers.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Grade[] children = data.get(groupPosition);


        return children.length;
    }

    @Override
    public Object getGroup(int groupPosition) {

        return headers[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Grade[] childData;

        childData = data.get(groupPosition);


        return childData[childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list2, null);
        TextView text = (TextView) convertView.findViewById(R.id.ELV_level2);
        String groupText = getGroup(groupPosition).toString();
        text.setText(groupText);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(childPosition==0){
            convertView = inflater.inflate(R.layout.list3_first, null);
            shareGrade(convertView);
            TextView semGrade = (TextView) convertView.findViewById(R.id.semesterGrade);
            int totalCreditGain = 0;
            int totalCredit = 0;
            for(int i =1;i<getChildrenCount(groupPosition)-1;i++){
                Grade g = (Grade)getChild(groupPosition,i);
                totalCredit+=g.credit;
                totalCreditGain+=g.credit*Mainpage.gradeStringtoInt(g.grade);
            }
            semGrade.setText(String.format("%.2f",totalCreditGain==0?0:((double)totalCreditGain)/totalCredit));
        }else if(isLastChild){
            convertView = inflater.inflate(R.layout.list3_last, null);
            View button = convertView.findViewById(R.id.addCourseButton);
            button.setFocusable(false);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.d("Tor","Year: "+(NestedListAdapter.SelectedYear+1)+" Term: "+(groupPosition+1));
//                    Mainpage.data.get(NestedListAdapter.SelectedYear).get("Term"+(groupPosition+1))[1].courseName="Fuck";
                    addCourse(NestedListAdapter.SelectedYear+1,groupPosition+1);
                    //                  String[] a = {"dsad","dsad"};
//                  ArrayList<String> test = new ArrayList<String>(Arrays.asList(a));
                    notifyDataSetChanged();
                }
            });
        }else{
            convertView = inflater.inflate(R.layout.list3, null);
            final TextView courseName = (TextView) convertView.findViewById(R.id.courseName);
            TextView courseGrade = (TextView) convertView.findViewById(R.id.courseGrade);
            ImageView delete = (ImageView) convertView.findViewById(R.id.deleteCourse);
            delete.setFocusable(false);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Mainpage.data.get(NestedListAdapter.SelectedYear).get("Term"+(groupPosition+1))[1].grade=0;
                    deleteCourse(courseName.getText().toString());
                    notifyDataSetChanged();
                }
            });
            Grade[] childArray = data.get(groupPosition);
            String name = childArray[childPosition].courseName;
            String grade = childArray[childPosition].grade;
            courseName.setText(name);
            courseGrade.setText(grade);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private Dialog addCourseDialog;
    private Spinner courseList;
    private Spinner gradeList;
    private ArrayList<String> courseID;
    private void addCourse(final int year, final int semester){
        addCourseDialog = new Dialog(context);
        addCourseDialog.setContentView(R.layout.popupcourse);
        addCourseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addCourseDialog.show();
        ((TextView)addCourseDialog.findViewById(R.id.txtclose)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCourseDialog.dismiss();
            }
        });
        courseList = (Spinner) addCourseDialog.findViewById(R.id.courseList);
        gradeList = (Spinner) addCourseDialog.findViewById(R.id.gradeList);
        Button addCourseButton = (Button) addCourseDialog.findViewById(R.id.addCourseButton);

        String[] courses = getNonEnrollCourseList(year,semester);

        ArrayAdapter<String> adapterCourseList = new ArrayAdapter<String>(context.getApplicationContext(),
                android.R.layout.simple_spinner_item, courses);
        adapterCourseList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseList.setAdapter(adapterCourseList);

        String[] grades = new String[] {
                "A", "B+", "B", "C+", "C", "D+", "D", "F"
        };
        ArrayAdapter<String> adapterGradeList = new ArrayAdapter<String>(context.getApplicationContext(),
                android.R.layout.simple_spinner_item, grades);
        adapterGradeList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gradeList.setAdapter(adapterGradeList);

        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper room = new DBHelper(context);
                try {
                    if(!courseList.getSelectedItem().toString().isEmpty()) {
                        room.insertGrade(new StudentGrade[]{new StudentGrade(FirebaseAuth.getInstance().getCurrentUser().getUid(),courseID.get(courseList.getSelectedItemPosition()),gradeList.getSelectedItem().toString())});
                    }
                }catch (Exception e){

                }
                ((Activity) context).recreate();
            }
        });
    }
    private String[] getNonEnrollCourseList(int year,int semester){
        DBHelper room = new DBHelper(context);
        List<Course> courses=null;
        try {
            courses= room.loadNonEnrollCourse(FirebaseAuth.getInstance().getCurrentUser().getUid(),year,semester,Mainpage.degType);
        }catch (Exception e){

        }
        courseID = new ArrayList<String>();
        ArrayList<String> courselist = new ArrayList<String>();
        for(Course c : courses){
            courselist.add(c.CID+"_"+c.courseName);
            courseID.add(c.CID);
        }
        return courselist.toArray(new String[courselist.size()]);
    }

    private void deleteCourse(String courseName){
        DBHelper room = new DBHelper(context);
        try {
            room.deleteGrade(FirebaseAuth.getInstance().getCurrentUser().getUid(),courseName.split("_")[0]);
        }catch (Exception e){

        }
        ((Activity) context).recreate();
    }

    ////share facebook///
    ////https://developers.facebook.com/docs/sharing/android
    ////https://www.youtube.com/watch?v=2ZdzG_XObDM
    ////https://stackoverflow.com/questions/30224390/how-post-to-wall-facebook-android-sdk4-0-0
    private void shareGrade(View view){
        ImageView fb = (ImageView) view.findViewById(R.id.fb);
        fb.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap image = takeScreenshot();
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareDialog shareDialog = new ShareDialog( (Activity)context );
//                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
//                            .setContentUrl(Uri.parse("http://facebook.com"))
//                            .setQuote("เทสการแชร์")
//                            .setShareHashtag(new ShareHashtag.Builder()
//                                    .setHashtag("#Androidproject")
//                                    .build())
//                            .build();
                    SharePhoto photo = new SharePhoto.Builder()
                            .setBitmap(image)
                            .build();
                    SharePhotoContent content = new SharePhotoContent.Builder()
                            .addPhoto(photo).setShareHashtag(new ShareHashtag.Builder()
                                    .setHashtag("#คนจริงต้องขิงเกรด #Androidproject")
                                    .build())
                            .build();
                    shareDialog.show(content);
                }
            }
        } );
    }
    public static int PERMISSION_REQUEST_FOR_SCREENSHOT=1789;
    ///////https://stackoverflow.com/questions/2661536/how-to-programmatically-take-a-screenshot//////
    private Bitmap takeScreenshot() {
        if(ContextCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
            // Permission has already been granted
            ActivityCompat.requestPermissions((Activity)context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_FOR_SCREENSHOT);
            return null;
        }
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        try {
            // image naming and path  to include sd card  appending name you choose for file
//            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = ((Activity)context).getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);
            return bitmap;
//            File imageFile = new File(mPath);

//            FileOutputStream outputStream = new FileOutputStream(imageFile);
//            int quality = 100;
//            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
//            outputStream.flush();
//            outputStream.close();

        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
        return null;
    }
}
