package com.example.wireless_gradecalculation;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private LinearLayout Camera;
    private Uri imageUri;
    public static final int TAKEPICTURE = 666;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 123;
    private ImageView image;
    private LinearLayout Selectphoto;
    private static final int PICK_IMAGE = 100;
    private Uri imageUrl;
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
        switch (requestCode){
            case PICK_IMAGE:
                if(resultCode == RESULT_OK)
                {
                    try{
                        imageUrl = data.getData();
                        Bitmap pic = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUrl);
                        pic.createScaledBitmap(pic,160,160,false);
//                        pic = rotateIfNeed(pic,imageUrl);
                        image.setImageBitmap(pic);
                        myDialogCam.dismiss();
                    }catch (Exception e){

                    }
                }
                break;
            case TAKEPICTURE:
                if(resultCode == Activity.RESULT_OK){
                    try {
                        Bitmap pic = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                        pic.createScaledBitmap(pic,160,160,false);
                        pic = rotateIfNeed(pic,imageUri);
                        image.setImageBitmap(pic);

                    }catch (Exception e){

                    }
                }
                break;
        }

//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();.s);

    }
    //from https://stackoverflow.com/questions/14066038/why-does-an-image-captured-using-camera-intent-gets-rotated-on-some-devices-on-a?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
    public Bitmap rotateIfNeed(Bitmap pic, Uri imUri){
        Bitmap rotate = null;
        try{
            ExifInterface ei = new ExifInterface(getContentResolver().openInputStream(imUri));
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            switch(orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = rotateImage(pic, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = rotateImage(pic, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = rotateImage(pic, 270);
                    break;

            }
        }catch (Exception e){

        }
        return rotate;
    }
    ////popforcamera
    public void ShowPopupCamera() {
        myDialogCam.setContentView(R.layout.popupforcamera);
        ///Camera//
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.popupforcamera, null);
        Camera =(LinearLayout) myDialogCam.findViewById(R.id.camera);
        //https://stackoverflow.com/questions/47027264/overcoming-photo-results-from-android-cameras-that-produce-low-resolution?rq=1
        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicturePermission();
            }
        });
        ////////////////////
        // from https://youtu.be/OPnusBmMQTw
        ///select camera//
        Selectphoto = (LinearLayout) myDialogCam.findViewById(R.id.selectPic);
        Selectphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery,PICK_IMAGE);
            }
        });
        /////


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
    void takePicture(){
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE,"ProfilePicture");
        cv.put(MediaStore.Images.Media.DESCRIPTION,"");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,cv);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,TAKEPICTURE);
        myDialogCam.dismiss();
    }
    //from https://stackoverflow.com/questions/14066038/why-does-an-image-captured-using-camera-intent-gets-rotated-on-some-devices-on-a?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
    ///////////////////
    ////// from https://developer.android.com/training/permissions/requesting.html#java
    private void takePicturePermission(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            //request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            }
        else {
            // Permission has already been granted
            takePicture();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    takePicture();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this,"Need Permission!!!",Toast.LENGTH_SHORT);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
