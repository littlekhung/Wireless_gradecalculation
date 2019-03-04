package com.example.wireless_gradecalculation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signup extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private TextView password;
    private TextView password2;
    private Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
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

    private void signUp(String emailin,String passwordin,String firstNamein, String lastNamein, String password2in){
        mAuth.createUserWithEmailAndPassword(emailin, passwordin)
                .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(Signup.this, "Signup failed.", Toast.LENGTH_SHORT).show();
                        }
                        // ...
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
