/**
 * nested view
 *  * modify from https://www.youtube.com/watch?v=jZxZIFnJ9jE&feature=youtu.be
 */
package com.example.wireless_gradecalculation;

import android.content.Context;
import android.widget.ExpandableListView;

public class SecondELV extends ExpandableListView {
    public SecondELV(Context context) {
        super(context);
    }
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(999999, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
