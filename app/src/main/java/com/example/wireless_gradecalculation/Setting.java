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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.localizationactivity.ui.LocalizationActivity;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class Setting extends LocalizationActivity {
    private TextView logoutButton;
    private Dialog myDialog;
    private Dialog myDialogCam;
    private TextView changepass;
    private TextView changephoto;
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
        myDialogCam = new Dialog(this);
        image = (ImageView) findViewById(R.id.ima);

        changephoto = (TextView) findViewById(R.id.changePhoto);
        changephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopupCamera();
            }
        });
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
        bitmap.createScaledBitmap(bitmap,160,160,false);
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();.s);
        image.setImageBitmap(bitmap);

    }
    ////popforcamera
    public void ShowPopupCamera() {
        myDialogCam.setContentView(R.layout.popupforcamera);
        ///Camera//
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.popupforcamera, null);
        Camera =(TextView) myDialogCam.findViewById(R.id.camera);

        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });
        TextView txtclose;

        txtclose =(TextView) myDialogCam.findViewById(R.id.txtclose);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialogCam.dismiss();
            }
        });
        myDialogCam.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialogCam.show();
    }
}
