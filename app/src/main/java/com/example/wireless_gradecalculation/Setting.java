package com.example.wireless_gradecalculation;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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
    public static final int MY_PERMISSIONS_REQUEST_FOR_CAMERA = 123;
    private ImageView image;
    private LinearLayout Selectphoto;
    private static final int PICK_IMAGE = 100;
    private User user;
    private TextView userName;
    private Button Saveme;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseFirestore cfs;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        cfs = FirebaseFirestore.getInstance();
        setTitle(R.string.app_name);
        Gson gson = new Gson();
        Intent i = getIntent();
        user = gson.fromJson(i.getStringExtra("user"),User.class);
        mAuth = FirebaseAuth.getInstance();
        myDialog = new Dialog(this);
        myDialogCam = new Dialog(this);
        image = (ImageView) findViewById(R.id.image);
        iniProf();
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

        /////firebase photo
        storage = FirebaseStorage.getInstance();
        storageReference =  storage.getReference();
        ////saveme////
        Saveme = (Button) findViewById(R.id.Saveme);
        Saveme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

    }
    //////// initiate profile////////////
    public void iniProf(){
        userName = (TextView) findViewById(R.id.settingUserName);
        userName.setText(user.getFirstname()+" "+user.getLastname());
        if(user.getPicuri()!=null){
            try {
                Uri getPic = Uri.parse(user.getPicuri());
                Bitmap pic = MediaStore.Images.Media.getBitmap(this.getContentResolver(), getPic);
                pic = rotateIfNeed(pic,getPic);
                image.setImageBitmap(pic);
            }catch (Exception e){
            }
        }
    }
    ////////from  https://youtu.be/h62bcMwahTU //////
    private void uploadImage()
    {
        if(imageUri!= null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle(R.string.loading);
            progressDialog.show();
            StorageReference ref = storageReference.child("images/"+user.getUID() /*UUID.randomUUID().toString()*/);
            ref.putFile(imageUri)
                    .addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            try {
                                final File localFile = File.createTempFile("Images", ".jpg");
                                storageReference.child("images/"+user.getUID()).getFile(localFile).addOnSuccessListener(new OnSuccessListener< FileDownloadTask.TaskSnapshot >() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        user.setPicuri(localFile.toURI().toString());
                                        progressDialog.dismiss();
                                        Toast.makeText(Setting.this,"Upload",Toast.LENGTH_SHORT );
                                        changeName();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Setting.this, "LoadImageFail", Toast.LENGTH_LONG);
                                        progressDialog.dismiss();
                                        changeName();
                                    }
                                });
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    } )
                    .addOnFailureListener( new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Setting.this,"Fail"+e.getMessage(),Toast.LENGTH_SHORT ).show();
                            progressDialog.dismiss();
                        }
                    } )
                    .addOnProgressListener( new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double process = (100.0*taskSnapshot.getBytesTransferred()/ taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage(getString(R.string.loading)+" "+(int)process+"%");
                        }
                    } );
        }else{
            changeName();
        }
    }
    public void changeName(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading)+" 100%");
        progressDialog.setTitle(R.string.loading);
        progressDialog.show();
        TextView fn = (TextView) findViewById(R.id.firstName_setting);
        TextView ln = (TextView) findViewById(R.id.lastName_setting);
        final String sfn = fn.getText().toString();
        final String sln = ln.getText().toString();
        if(!sfn.isEmpty() && !sln.isEmpty()){
            Map<String, Object> data = new HashMap<>();
            data.put("firstname", sfn);
            data.put("lastname", sln);
            cfs.collection("user").document(mAuth.getCurrentUser().getUid()).set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            user.setFirstname(sfn);
                            user.setLastname(sln);
                            progressDialog.dismiss();
                            goToMain();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Setting.this,"Error occure while setting name",Toast.LENGTH_SHORT);
                        }
                    });
        }
        else if(!sfn.isEmpty()){
            Map<String, Object> data = new HashMap<>();
            data.put("firstname", sfn);
            data.put("lastname", user.getLastname());
            cfs.collection("user").document(mAuth.getCurrentUser().getUid()).set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            user.setFirstname(sfn);
                            progressDialog.dismiss();
                            goToMain();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Setting.this,"Error occure while setting name",Toast.LENGTH_SHORT);
                        }
                    });
        }
        else if(!sln.isEmpty()){
            Map<String, Object> data = new HashMap<>();
            data.put("firstname", user.getFirstname());
            data.put("lastname", sln);
            cfs.collection("user").document(mAuth.getCurrentUser().getUid()).set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            user.setLastname(sln);
                            progressDialog.dismiss();
                            goToMain();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Setting.this,"Error occure while setting name",Toast.LENGTH_SHORT);
                        }
                    });
        }else{
            progressDialog.dismiss();
            goToMain();
        }

    }
    ////////// Go to Mainpage /////////////////////
    private void goToMain(){
        Gson gson = new Gson();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("user",gson.toJson(user));
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
    /////////Change Password Popup///////
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
        final String oldPass = ((TextView) myDialog.findViewById(R.id.oldPass)).getText().toString();
        final String newPass = ((TextView) myDialog.findViewById(R.id.newPass)).getText().toString();
        final String conNewPass = ((TextView) myDialog.findViewById(R.id.conNewPass)).getText().toString();
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(changePassword(oldPass,newPass,conNewPass))
                    Toast.makeText(Setting.this,R.string.changePassSuccess,Toast.LENGTH_LONG);
                else
                    Toast.makeText(Setting.this,R.string.changePassFail,Toast.LENGTH_LONG);
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
    //////Change password/////
    public boolean changePassword(String oldPass,String newPass, String conNewPass){
        return false;
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
                        imageUri = data.getData();
                        Bitmap pic = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//                        pic.createScaledBitmap(pic,120,120,false);
                        pic = rotateIfNeed(pic,imageUri);
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
//                        pic.createScaledBitmap(pic,120,120,false);
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
                    default:
                        rotate = pic;

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
        ///select photo on device//
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
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED){
            // Permission has already been granted
            takePicture();
            return;
        }
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                MY_PERMISSIONS_REQUEST_FOR_CAMERA);
//        if(ContextCompat.checkSelfPermission(this,
//                Manifest.permission.CAMERA)
//                != PackageManager.PERMISSION_GRANTED){
//
//        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FOR_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
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
