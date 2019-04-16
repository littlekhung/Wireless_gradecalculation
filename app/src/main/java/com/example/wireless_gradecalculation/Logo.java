package com.example.wireless_gradecalculation;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.akexorcist.localizationactivity.ui.LocalizationActivity;

public class Logo extends LocalizationActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_logo);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent main = new Intent(Logo.this,MainActivity.class);
                startActivity(main);
                finish();
            }
        }, 500);
    }
}
