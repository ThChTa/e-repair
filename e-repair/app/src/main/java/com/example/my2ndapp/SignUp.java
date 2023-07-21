package com.example.my2ndapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private EditText FirstNameGap,LastNameGap,emailGap,EnterValidPasswordGap;
    private Button buttonForSignUp;
    private Spinner spinner;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        FirstNameGap = findViewById(R.id.FirstNameGap);
        LastNameGap = findViewById(R.id.LastNameGap);
        emailGap = findViewById(R.id.emailGap);
        EnterValidPasswordGap = findViewById(R.id.EnterValidPasswordGap);

        buttonForSignUp = findViewById(R.id.buttonForSignUp);

        buttonForSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String fname,lname,username,email,password;

                fname = String.valueOf(FirstNameGap.getText());
                lname = String.valueOf(LastNameGap.getText());
                email = String.valueOf(emailGap.getText());
                password = String.valueOf(EnterValidPasswordGap.getText());

                if(TextUtils.isEmpty(fname)){
                    Toast.makeText(SignUp.this,"Enter your Firstname", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(TextUtils.isEmpty(lname)){
                    Toast.makeText(SignUp.this,"Enter your Lastname", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(TextUtils.isEmpty(email)){
                    Toast.makeText(SignUp.this,"Enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(TextUtils.isEmpty(password)){
                    Toast.makeText(SignUp.this,"Enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {

                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        FirebaseUser user =  firebaseAuth.getCurrentUser();

                                        Toast.makeText(SignUp.this, "Account created!", Toast.LENGTH_SHORT).show();

                                        DocumentReference df = firebaseFirestore.collection("Users").document(user.getUid());
                                        Map<String,Object> userInfo = new HashMap<>();
                                        userInfo.put("FirstName", fname);
                                        userInfo.put("LastName", lname);
                                        userInfo.put("UserEmail", email);
                                        userInfo.put("IsAdmin", "1");

                                        df.set(userInfo);
                                        Intent intent = new Intent(getApplicationContext(), SignIn.class);
                                        startActivity(intent);
                                        finish();

                                        Toast.makeText(SignUp.this, "Now, sign in with your credentials", Toast.LENGTH_SHORT).show();


                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(SignUp.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }




            }
        });
    }
}