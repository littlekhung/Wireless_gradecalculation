/**
 * adapter for nested expanable listview
 * modify from https://www.youtube.com/watch?v=jZxZIFnJ9jE&feature=youtu.be
 */
package com.example.wireless_gradecalculation;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.wireless_gradecalculation.studentgradedatabase.Course;
import com.example.wireless_gradecalculation.studentgradedatabase.DBHelper;
import com.example.wireless_gradecalculation.studentgradedatabase.StudentGrade;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class SecondLevelAdapter extends BaseExpandableListAdapter {
    static int semester = -1;
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
        semester = groupPosition;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(childPosition==0){
            convertView = inflater.inflate(R.layout.list3_first, null);
            shareGrade(convertView);
            TextView semGrade = (TextView) convertView.findViewById(R.id.semesterGrade);
            double totalCreditGain = 0;
            double totalCredit = 0;
            for(int i =1;i<getChildrenCount(groupPosition)-1;i++){
                Grade g = (Grade)getChild(groupPosition,i);
                totalCredit+=g.credit;
                totalCreditGain+=g.credit*Mainpage.gradeStringtoInt(g.grade);
            }
            semGrade.setText(String.format("%.2f",totalCreditGain==0?0:(totalCreditGain/totalCredit)));
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
            final String name = childArray[childPosition].courseName;
            String grade = childArray[childPosition].grade;
            courseName.setText(name);
            courseGrade.setText(grade);
            TableRow tb = (TableRow)convertView.findViewById(R.id.updateRow);
            tb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateGrade(name);
                    notifyDataSetChanged();
                }
            });
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

    /**
     * show add course dialog
     * add the current selected course and grade from dialog to room database
     * no recreated need
     * @param year year selected
     * @param semester semester selected
     */
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

        final String[] courses = getNonEnrollCourseList(year,semester);

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
                String courseAdded;
                try {
                    if(!(courseAdded = courseList.getSelectedItem().toString()).isEmpty()) {
                        String cid,grade;
                        int credit = room.getCourseByID((cid=courseID.get(courseList.getSelectedItemPosition()))).credit;
                        room.insertGrade(new StudentGrade[]{new StudentGrade(FirebaseAuth.getInstance().getCurrentUser().getUid(),cid,(grade=gradeList.getSelectedItem().toString()),year,semester)});
                        ////////////////////////below is for adding course and update ui without recreate////////////////
                        String sem ="";
                        switch (semester){
                            case 1:
                                sem = context.getString(R.string.semester1);
                                break;
                            case 2:
                                sem = context.getString(R.string.semester2);
                                break;
                            case 3:
                                sem = context.getString(R.string.semester3);
                                break;

                        }
                        ////// whether it is now bachelor menu or master menu
                        List<LinkedHashMap<String, Grade[]>> data = null;
                        switch (Mainpage.degType){
                            case "Bachelor":
                                data = Mainpage.bachelorData;
                                break;
                            case "Master":
                                data = Mainpage.masterData;
                                break;
                        }
                        Grade[] temp = data.get(year-1).get(sem);
                        ArrayList<Grade> t = new ArrayList<>(Arrays.asList(temp));
                        t.add(t.size()-1,new Grade(courseList.getSelectedItem().toString(),credit,grade));
                        data.get(year-1).replace(sem,t.toArray(new Grade[t.size()]));
                        ((NestedListAdapter)Mainpage.listView.getExpandableListAdapter()).notifyDataSetChanged();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                NestedListAdapter.currentAdapter.expandGroup(semester-1);
                            }
                        }, 1);
                        ((TextView) ((Activity)context).findViewById(R.id.totalGrade)).setText(Mainpage.calculateGrade());
                        ///////////////////////////////////////////////////////////////////////

                    }
                }catch (Exception e){

                }
                addCourseDialog.dismiss();
//                ((Activity) context).recreate();
            }
        });
    }

    /**
     * get all non enrolled course to user to add grade in
     * @param year
     * @param semester
     * @return string of non enrolled course name
     */
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

    /**
     * delete course from database and also refresh display
     * no recreate need
     * @param courseName course name to be deleted
     */
    private void deleteCourse(String courseName){
        DBHelper room = new DBHelper(context);
        try {
            room.deleteGrade(FirebaseAuth.getInstance().getCurrentUser().getUid(),courseName.split("_")[0]);
            ////// whether it is now bachelor menu or master menu
            List<LinkedHashMap<String, Grade[]>> data = null;
            String sem ="";
            switch (semester+1){
                case 1:
                    sem = context.getString(R.string.semester1);
                    break;
                case 2:
                    sem = context.getString(R.string.semester2);
                    break;
                case 3:
                    sem = context.getString(R.string.semester3);
                    break;

            }
            switch (Mainpage.degType){
                case "Bachelor":
                    data = Mainpage.bachelorData;
                    break;
                case "Master":
                    data = Mainpage.masterData;
                    break;
            }
            Grade[] temp = data.get(NestedListAdapter.SelectedYear).get(sem);
            ArrayList<Grade> t = new ArrayList<>(Arrays.asList(temp));
            for(int i =0;i<t.size();i++)
                if(t.get(i).courseName.equals(courseName)){
                    t.remove(i);
                    break;
                }
            data.get(NestedListAdapter.SelectedYear).replace(sem,t.toArray(new Grade[t.size()]));
            ((NestedListAdapter)Mainpage.listView.getExpandableListAdapter()).notifyDataSetChanged();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    NestedListAdapter.currentAdapter.expandGroup(semester);
                }
            }, 1);
            ((TextView) ((Activity)context).findViewById(R.id.totalGrade)).setText(Mainpage.calculateGrade());
            ///////////////////////////////////////////////////////////////////////
        }catch (Exception e){

        }
//        ((Activity) context).recreate();
    }

    /**
     * function to share grade to face book
     * this will capture screen image add hashtag and show facebook share dialog
     *     ////https://developers.facebook.com/docs/sharing/android
     *     ////https://www.youtube.com/watch?v=2ZdzG_XObDM
     *     ////https://stackoverflow.com/questions/30224390/how-post-to-wall-facebook-android-sdk4-0-0
     * @param view view to display dialog
     */
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

    /**
     * function to take screenshot and request permission to write external image
     * if not granted
     * @return bitmap image of screenshot
     */
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
    /////////////////////update grade/////////////////////////////
    Dialog updateGradeDialog;

    /**
     * show dialog
     * update grade of course the grade to be update is the grade selected in dialog
     * @param course course to be update
     */
    public void updateGrade(final String course){
        updateGradeDialog = new Dialog(context);
        updateGradeDialog.setContentView(R.layout.popupforupdategrade);
        updateGradeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        updateGradeDialog.show();
        ((TextView)updateGradeDialog.findViewById(R.id.txtclose)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateGradeDialog.dismiss();
            }
        });
        TextView courseName = (TextView) updateGradeDialog.findViewById(R.id.updateCourse);
        courseName.setText(course);
        gradeList = (Spinner) updateGradeDialog.findViewById(R.id.gradeList);
        String[] grades = new String[] {
                "A", "B+", "B", "C+", "C", "D+", "D", "F"
        };
        ArrayAdapter<String> adapterGradeList = new ArrayAdapter<String>(context.getApplicationContext(),
                android.R.layout.simple_spinner_item, grades);
        adapterGradeList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gradeList.setAdapter(adapterGradeList);
        Button updateCourseButton = (Button) updateGradeDialog.findViewById(R.id.updateCourseButton);
        updateCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = NestedListAdapter.SelectedYear;
                int s = semester;
                DBHelper room = new DBHelper(context);
                try {
                    room.updateGrade(course.split("_")[0],FirebaseAuth.getInstance().getCurrentUser().getUid(),gradeList.getSelectedItem().toString(),year+1,s+1);
                }catch (Exception e){

                }
                ////////////////////////below is for adding course and update ui without recreate////////////////
                String sem ="";
                switch (s+1){
                    case 1:
                        sem = context.getString(R.string.semester1);
                        break;
                    case 2:
                        sem = context.getString(R.string.semester2);
                        break;
                    case 3:
                        sem = context.getString(R.string.semester3);
                        break;

                }
                ////// whether it is now bachelor menu or master menu
                List<LinkedHashMap<String, Grade[]>> data = null;
                switch (Mainpage.degType){
                    case "Bachelor":
                        data = Mainpage.bachelorData;
                        break;
                    case "Master":
                        data = Mainpage.masterData;
                        break;
                }
                Grade[] temp = data.get(year).get(sem);
                int i;
                for(i=0;i<temp.length&!temp[i].courseName.equals(course);i++){

                }
                data.get(year).get(sem)[i].grade=gradeList.getSelectedItem().toString();
                ((TextView) ((Activity)context).findViewById(R.id.totalGrade)).setText(Mainpage.calculateGrade());
                notifyDataSetChanged();
                updateGradeDialog.dismiss();
                ///////////////////////////////////////////////////////////////////////
            }
        });


    }
}
