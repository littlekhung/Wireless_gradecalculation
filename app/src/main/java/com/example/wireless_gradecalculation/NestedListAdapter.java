/**
 * class for nested expanable listview
 *  * modify from https://www.youtube.com/watch?v=jZxZIFnJ9jE&feature=youtu.be
 */
package com.example.wireless_gradecalculation;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.wireless_gradecalculation.Grade;
import com.example.wireless_gradecalculation.SecondELV;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class NestedListAdapter extends BaseExpandableListAdapter {
    public static SecondELV currentAdapter;
    String[] parentHeaders;
    List<String[]> secondLevel;
    private Context context;
    List<LinkedHashMap<String, Grade[]>> data;
    public static int SelectedYear;

    public NestedListAdapter(Context context, String[] parentHeader, List<String[]> secondLevel, List<LinkedHashMap<String, Grade[]>> data) {
        this.context = context;
        this.parentHeaders = parentHeader;
        this.secondLevel = secondLevel;
        this.data = data;
    }


    @Override
    public int getGroupCount() {
        return parentHeaders.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupPosition;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childPosition;
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
        convertView = inflater.inflate(R.layout.list1, null);
        TextView text = (TextView) convertView.findViewById(R.id.ELV_level1);
        text.setText(this.parentHeaders[groupPosition]);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final SecondELV secondLevelELV = new SecondELV(context);
        SelectedYear = groupPosition;
        String[] headers = secondLevel.get(groupPosition);


        List<Grade[]> childData = new ArrayList<>();
        HashMap<String, Grade[]> secondLevelData = data.get(groupPosition);

        for (String key : secondLevelData.keySet()) {

            childData.add(secondLevelData.get(key));

        }


        secondLevelELV.setAdapter(new SecondLevelAdapter(context, headers, childData));

        secondLevelELV.setGroupIndicator(null);


        secondLevelELV.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    secondLevelELV.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });
        currentAdapter = secondLevelELV;
        return secondLevelELV;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
