package com.example.my2ndapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignIn extends AppCompatActivity {

    private ImageView imageViewSignIn;
    private Button buttonForSignIn,buttonForSignUp;
    private EditText emailGap,passwordGap;

    String emailToSend; //send it to next activity
    FirebaseAuth firebaseAuthSignIn;
    FirebaseFirestore firebaseFirestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //back button

        firebaseAuthSignIn = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        imageViewSignIn = findViewById(R.id.imageView);
        imageViewSignIn.setBackgroundResource(R.drawable.logo);

        emailGap = findViewById(R.id.emailGap);
        passwordGap = findViewById(R.id.enterPasswordGap);
        buttonForSignIn = findViewById(R.id.button);
        buttonForSignUp = findViewById(R.id.button2);


        buttonForSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("TAG","onClick: " + emailGap.getText().toString());

                String email, password;
                email = String.valueOf(emailGap.getText()).toLowerCase();
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

                                    checkUserAccessLevel(firebaseAuthSignIn.getCurrentUser().getUid());



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

    private void checkUserAccessLevel(String uid){
        DocumentReference df = firebaseFirestore.collection("Users").document(uid);
        //extract data
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess " + documentSnapshot.getData());
                //identify user
                String isAdmin = documentSnapshot.getString("IsAdmin");

                if ( isAdmin!=null && isAdmin.compareTo("1")==0) {
                    //is admin
                    startActivity(new Intent(getApplicationContext(), Admin.class));
                    finish();
                }
                else if (isAdmin!=null && isAdmin.compareTo("0")==0) {
                    //is user
                    //startActivity(new Intent(getApplicationContext(), User.class));
                    Intent i = new Intent(getApplicationContext(), User.class);
                    emailToSend=emailGap.getText().toString().toLowerCase();
                    i.putExtra("emailFromSignIn", emailToSend);
                    startActivity(i);
                    finish();
                }
                else{
                    Toast.makeText(SignIn.this, "ERROR", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }
        });
    }


    public void openSignUpActivity(){
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

}