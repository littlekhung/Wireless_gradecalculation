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
                signUp(email.getText().toString(),password.getText().toString());
            }
        });
    }

    private void signUp(String email,String password){
        mAuth.createUserWithEmailAndPassword(email, password)
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
}
