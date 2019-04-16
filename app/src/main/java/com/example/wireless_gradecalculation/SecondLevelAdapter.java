package com.example.wireless_gradecalculation;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

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
        }else if(isLastChild){
            convertView = inflater.inflate(R.layout.list3_last, null);
            View button = convertView.findViewById(R.id.addCourseButton);
            button.setFocusable(false);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Tor","Year: "+(NestedListAdapter.SelectedYear+1)+" Term: "+(groupPosition+1));
                    Mainpage.data.get(NestedListAdapter.SelectedYear).get("Term"+(groupPosition+1))[1].courseName="Fuck";
//                  String[] a = {"dsad","dsad"};
//                  ArrayList<String> test = new ArrayList<String>(Arrays.asList(a));
                    notifyDataSetChanged();
                }
            });
        }else{
            convertView = inflater.inflate(R.layout.list3, null);
            TextView courseName = (TextView) convertView.findViewById(R.id.courseName);
            TextView courseGrade = (TextView) convertView.findViewById(R.id.courseGrade);
            ImageView delete = (ImageView) convertView.findViewById(R.id.deleteCourse);
            delete.setFocusable(false);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Mainpage.data.get(NestedListAdapter.SelectedYear).get("Term"+(groupPosition+1))[1].grade=0;
                    notifyDataSetChanged();
                }
            });
            Grade[] childArray = data.get(groupPosition);
            String name = childArray[childPosition].courseName;
            double grade = childArray[childPosition].grade;
            courseName.setText(name);
            courseGrade.setText(String.format("%.2f",grade));
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }



}
