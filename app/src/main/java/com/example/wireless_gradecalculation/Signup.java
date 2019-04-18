package com.example.wireless_gradecalculation;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.localizationactivity.ui.LocalizationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Signup extends LocalizationActivity {
    private FirebaseAuth mAuth;
    private TextView firstName;
    private TextView firstNameWarning;
    private TextView lastName;
    private TextView lastNameWarning;
    private TextView email;
    private TextView emailWarning;
    private TextView password;
    private TextView passwordWarning;
    private TextView password2;
    private TextView password2Warning;
    private Button confirm;
    private ProgressDialog pd;
    private FirebaseFirestore cfs;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle(getString(R.string.app_name));
        mAuth = FirebaseAuth.getInstance();
        cfs = FirebaseFirestore.getInstance();
        firstName = (TextView) findViewById(R.id.firstName_signUp);
        firstNameWarning = (TextView) findViewById(R.id.firstNameWarning);
        lastName = (TextView) findViewById(R.id.lastName_signUp);
        lastNameWarning = (TextView) findViewById(R.id.lastNameWarning);
        email = (TextView) findViewById(R.id.email_signUp);
        emailWarning = (TextView) findViewById(R.id.emailWarning);
        password = (TextView) findViewById(R.id.password_signUp);
        passwordWarning = (TextView) findViewById(R.id.passwordWarning);
        password2 = (TextView) findViewById(R.id.confirmPassword_signUp);
        password2Warning = (TextView) findViewById(R.id.confirmPasswordWarning);
        confirm = (Button) findViewById(R.id.confirmButton_signUp);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp(email.getText().toString(),password.getText().toString(),firstName.getText().toString(),lastName.getText().toString(),password2.getText().toString());
            }
        });
    }

    private void signUp(String emailin, String passwordin, final String firstNamein, final String lastNamein, String password2in){
        if(firstNamein.length()==0){
            Toast.makeText(Signup.this, "Please Input Firstname", Toast.LENGTH_SHORT).show();
            firstNameWarning.setText(R.string.firstNameWarnS);
            return;
        }
        firstNameWarning.setText("");
        if(lastNamein.length()==0){
            Toast.makeText(Signup.this, "Please Input Lastname", Toast.LENGTH_SHORT).show();
            lastNameWarning.setText(R.string.lastNameWarnS);
            return;
        }
        lastNameWarning.setText("");
        if(emailin.length()==0){
            Toast.makeText(Signup.this, "Please Input Email", Toast.LENGTH_SHORT).show();
            emailWarning.setText(R.string.emailWarnS_noInput);
            return;
        }
        if(!validateEmail(emailin)){
            Toast.makeText(Signup.this, "Email Incorrect", Toast.LENGTH_SHORT).show();
            emailWarning.setText(R.string.emailWarnS_invalid);
            return;
        }
        emailWarning.setText("");
        if(passwordin.length()<7){
            Toast.makeText(Signup.this, "Password must be at least 7 characters", Toast.LENGTH_SHORT).show();
            passwordWarning.setText(R.string.passwordWarnS_short);
            return;
        }
        if(!validatePassword(passwordin)){
            Toast.makeText(Signup.this, "Password must be either alphabet or number", Toast.LENGTH_SHORT).show();
            passwordWarning.setText(R.string.passwordWarnS_invalid);
            return;
        }
        passwordWarning.setText("");
        if(!validateConfirmPassword(passwordin,password2in)){
            Toast.makeText(Signup.this, "Confirm pass word must be the same as password", Toast.LENGTH_SHORT).show();
            password2Warning.setText(R.string.confirmPasswordWarnS_notMatch);
            return;
        }
        password2Warning.setText("");
        pd = new ProgressDialog(this);

        pd.setMessage(getString(R.string.loading));
        pd.show();

        mAuth.createUserWithEmailAndPassword(emailin, passwordin)
                .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        pd.dismiss();
                        if (!task.isSuccessful()) {
                            Toast.makeText(Signup.this, "Signup failed.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            //Toast.makeText(Signup.this, "Signup Complete.", Toast.LENGTH_SHORT).show();
                            createUserInFB(firstNamein,lastNamein);
                            finish();
                        }
                        // ...
                    }
                });
    }

    private void createUserInFB(String fn,String ln){
        Map<String, Object> data = new HashMap<>();
        data.put("firstname", fn);
        data.put("lastname", ln);
        cfs.collection("user").document(mAuth.getCurrentUser().getUid()).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("DBlog", "Add in database success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("DBlog", "Error adding document", e);
                    }
                });
    }

    private boolean validateEmail(String email){
        // Email Regex from Andy Smith ref: http://regexlib.com/UserPatterns.aspx?authorId=15777db1-4c90-48f2-b323-905b509f16e8
        return email.matches("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
    }
    private boolean validatePassword(String password){
        // character or number
        // length must be at least 7
        return password.matches("[\\w\\d]{7,}");
    }
    private boolean validateConfirmPassword(String password, String confirmpassword){
        return password.equals(confirmpassword);
    }
}
