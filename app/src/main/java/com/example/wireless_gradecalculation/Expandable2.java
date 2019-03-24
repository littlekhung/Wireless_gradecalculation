package com.example.wireless_gradecalculation;

import android.content.Context;
import android.widget.ExpandableListView;

public class Expandable2 extends ExpandableListView {
    public Expandable2(Context context) {
        super(context);
    }
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(999999, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
