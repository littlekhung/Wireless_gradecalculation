package com.example.wireless_gradecalculation;

import android.app.Application;
import android.util.Log;

import com.akexorcist.localizationactivity.ui.LocalizationActivity;

public class DatainTable extends LocalizationActivity {
    private String TAG = this.getClass().getSimpleName();

    public DatainTable(Application application)
    {
        super(application);
    }

    protected  void onCleared(){
        super.onCleared();
        Log.i(TAG,"ViewModel Destroyes");
    }
}
