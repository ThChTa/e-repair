package com.example.my2ndapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {

    private Button buttonForSignIn,buttonForSignUp;
    private EditText emailGap,passwordGap;

    FirebaseAuth firebaseAuthSignIn = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //back button

        emailGap = findViewById(R.id.emailGap);
        passwordGap = findViewById(R.id.enterPasswordGap);
        buttonForSignIn = findViewById(R.id.button);
        buttonForSignUp = findViewById(R.id.button2);


        buttonForSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = String.valueOf(emailGap.getText());
                password = String.valueOf(passwordGap.getText());

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(SignIn.this,"Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(SignIn.this,"Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

               firebaseAuthSignIn.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignIn.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(getApplicationContext(), PAGE1.class);
                                    startActivity(intent);
                                    finish();


                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(SignIn.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });




            }
        });

        buttonForSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpActivity();
            }
        });

    }


    public void openSignUpActivity(){
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

}