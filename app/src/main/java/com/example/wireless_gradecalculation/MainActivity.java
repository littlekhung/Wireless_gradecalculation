package com.example.wireless_gradecalculation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.localizationactivity.ui.LocalizationActivity;
import com.example.wireless_gradecalculation.studentgradedatabase.AppDatabase;
import com.example.wireless_gradecalculation.studentgradedatabase.CourseDao;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

public class MainActivity extends LocalizationActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button login;
    private Button signUp;
    private TextView email;
    private TextView pass;
    private ProgressDialog pd;
    private FirebaseFirestore db;
    private FirebaseUser FBuser;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.app_name));
//        Intent test = new Intent(this, Setting.class);
//        startActivity(test);
//        finish();
        storage = FirebaseStorage.getInstance();
        storageReference =  storage.getReference();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FBuser = firebaseAuth.getCurrentUser();
                if (FBuser != null) {
                    // User is signed in
//                    Toast.makeText(MainActivity.this, FBuser.getUid()+"", Toast.LENGTH_SHORT).show();
                    if(pd == null || !pd.isShowing()){
                        pd = new ProgressDialog(MainActivity.this);
                        pd.setMessage(getString(R.string.loading));
                        pd.show();
                    }
                    loadUserAndGoToMainPage();
                } else {
//                    Toast.makeText(MainActivity.this, "not sign", Toast.LENGTH_SHORT).show();
                }
                // ...
            }
        };
        login = (Button)findViewById(R.id.loginButton_login);
        signUp = (Button)findViewById(R.id.signUpButton_login);
        email = (TextView)findViewById(R.id.email_login);
        pass = (TextView)findViewById(R.id.password_login);
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                loginToApp(email.getText().toString(),pass.getText().toString());
            }
        });
        signUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                toSignUpAct();
            }
        });
    }
    User user;
    private void loadUserAndGoToMainPage(){
        DocumentReference docRef = db.collection("user").document(mAuth.getCurrentUser().getUid());
//        Log.e("user",mAuth.getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Gson gson = new Gson();
                user = new User(FBuser.getUid(),documentSnapshot.getString("firstname"),documentSnapshot.getString("lastname"));
                storageReference.child("images/"+FBuser.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        try {
                            final File localFile = File.createTempFile("Images", ".jpg");
                            storageReference.child("images/"+FBuser.getUid()).getFile(localFile).addOnSuccessListener(new OnSuccessListener< FileDownloadTask.TaskSnapshot >() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    user.setPicuri(localFile.toURI().toString());
                                    Intent mainpage = new Intent(MainActivity.this,Mainpage.class);
                                    mainpage.putExtra("user",new Gson().toJson(user));
                                    startActivity(mainpage);
                                    pd.dismiss();
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, "LoadImageFail", Toast.LENGTH_LONG).show();
                                    Intent mainpage = new Intent(MainActivity.this,Mainpage.class);
                                    mainpage.putExtra("user",new Gson().toJson(user));
                                    startActivity(mainpage);
                                    pd.dismiss();
                                    finish();
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Intent mainpage = new Intent(MainActivity.this,Mainpage.class);
                        mainpage.putExtra("user",new Gson().toJson(user));
                        startActivity(mainpage);
                        pd.dismiss();
                        finish();
                    }
                });
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void toSignUpAct(){
        Intent sign = new Intent(this, Signup.class);
        startActivity(sign);
    }

    private void loginToApp(String emailin,String passwordin){
        if(emailin.length()==0){
            Toast.makeText(MainActivity.this, "Please Input Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!validateEmail(emailin)){
            Toast.makeText(MainActivity.this, "Email Incorrect", Toast.LENGTH_SHORT).show();
            email.setTextColor(Color.RED);
            return;
        }
        if(passwordin.length()<6){
            Toast.makeText(MainActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!validatePassword(passwordin)){
            Toast.makeText(MainActivity.this, "Password must be either alphabet or number", Toast.LENGTH_SHORT).show();
            return;
        }
        email.setTextColor(Color.BLACK);
        if(pd==null || !pd.isShowing()){
            pd = new ProgressDialog(this);
            pd.setMessage(getString(R.string.loading));
            pd.show();
        }
        mAuth.signInWithEmailAndPassword(emailin, passwordin)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete( Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Login failed.", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }else{
                            Toast.makeText(MainActivity.this, "Login success.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateEmail(String email){
        // Email Regex from Andy Smith ref: http://regexlib.com/UserPatterns.aspx?authorId=15777db1-4c90-48f2-b323-905b509f16e8
        return email.matches("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
    }
    private boolean validatePassword(String password){
        // character or number
        // length must be at least 6
        return password.matches("[\\w\\d]{6}[\\w\\d]+");
    }
}
