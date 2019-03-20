package com.example.wireless_gradecalculation;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class Expanable extends BaseExpandableListAdapter {
    private Context context;
    private List<String> list_item;
    private HashMap<String,List<String>> listHashMap;

    public Expanable(Context context, List<String>list_item, HashMap listHashMap)
    {
        this.context=context;
        this.list_item=list_item;
        this.listHashMap=listHashMap;
    }
    @Override
    public int getGroupCount() {
        return list_item.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return listHashMap.get(list_item.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return list_item.get(i);
    }

    @Override
    public Object getChild(int i, int y) {
        return listHashMap.get(list_item.get(i)).get(y); ///y=item1
    }

    @Override
    public long getGroupId(int i) {
        return i ;
    }

    @Override
    public long getChildId(int i, int y) {
        return y;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view , ViewGroup parent) {
        String header = (String)getGroup(i);
        if(view==null)
        {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listgroup,null);
        }
        TextView Header = (TextView) view.findViewById(R.id.listheader);
        Header.setTypeface(null, Typeface.BOLD);
        Header.setText(header);
        return view;
    }

    @Override
    public View getChildView(int i, int y, boolean b, View view, ViewGroup parent) {
        final String chilText = (String)getChild(i,y);
        if(view==null)
        {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listitem,null);

        }
        TextView txt = (TextView)view.findViewById(R.id.list_item);
        txt.setText(chilText);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int y) {
        return true;
    }
}
