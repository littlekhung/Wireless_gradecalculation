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

public class Signup extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private TextView password;
    private TextView password2;
    private Button confirm;
    private ProgressDialog pd;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firstName = (TextView) findViewById(R.id.firstName_signUp);
        lastName = (TextView) findViewById(R.id.lastName_signUp);
        email = (TextView) findViewById(R.id.email_signUp);
        password = (TextView) findViewById(R.id.password_signUp);
        password2 = (TextView) findViewById(R.id.confirmPassword_signUp);
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
            return;
        }
        if(lastNamein.length()==0){
            Toast.makeText(Signup.this, "Please Input Lastname", Toast.LENGTH_SHORT).show();
            return;
        }
        if(emailin.length()==0){
            Toast.makeText(Signup.this, "Please Input Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!validateEmail(emailin)){
            Toast.makeText(Signup.this, "Email Incorrect", Toast.LENGTH_SHORT).show();
            email.setTextColor(Color.RED);
            return;
        }
        if(passwordin.length()<6){
            Toast.makeText(Signup.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!validatePassword(passwordin)){
            Toast.makeText(Signup.this, "Password must be either alphabet or number", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!validateConfirmPassword(passwordin,password2in)){
            Toast.makeText(Signup.this, "Confirm pass word must be the same as password", Toast.LENGTH_SHORT).show();
            return;
        }
        pd = new ProgressDialog(this);
        pd.setMessage("loading");
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
                            createUserInDB(firstNamein,lastNamein);
                            finish();
                        }
                        // ...
                    }
                });
    }

    private void createUserInDB(String fn,String ln){
        Map<String, Object> data = new HashMap<>();
        data.put("firstname", fn);
        data.put("lastname", ln);
        db.collection("user").document(mAuth.getCurrentUser().getUid()).set(data)
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
        // length must be at least 6
        return password.matches("[\\w\\d]{6}[\\w\\d]+");
    }
    private boolean validateConfirmPassword(String password, String confirmpassword){
        return password.equals(confirmpassword);
    }
}
