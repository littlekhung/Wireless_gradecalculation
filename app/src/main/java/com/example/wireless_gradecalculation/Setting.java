package com.example.wireless_gradecalculation;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.localizationactivity.ui.LocalizationActivity;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class Setting extends LocalizationActivity {
    private TextView logoutButton;
    private Dialog myDialog;
    private TextView changepass;
    private FirebaseAuth mAuth;
    private TextView Camera;
    ImageView image;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle(R.string.app_name);
        mAuth = FirebaseAuth.getInstance();
        myDialog = new Dialog(this);
        changepass = (TextView) findViewById(R.id.changePass);
        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopup();
            }
        });
        logoutButton = (TextView) findViewById(R.id.logoutTextView);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent mainAct = new Intent(Setting.this,MainActivity.class);
                startActivity(mainAct);
                finish();
            }
        });
        ///Camera//
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.popupforcamera, null);
        Camera =(TextView) convertView.findViewById(R.id.camera);
        image = (ImageView) findViewById(R.id.ima);

        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });


    }
    public void ShowPopup() {
        TextView txtclose;
        Button complete;
        myDialog.setContentView(R.layout.popup);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        complete = (Button) myDialog.findViewById(R.id.complete);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    //////Camera//////

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap =(Bitmap)data.getExtras().get("data");
        image.setImageBitmap(bitmap);

    }
    ////popforcamera
    public void ShowPopup1() {
        TextView txtclose;
        Button complete;
        myDialog.setContentView(R.layout.popup);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        complete = (Button) myDialog.findViewById(R.id.complete);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}
