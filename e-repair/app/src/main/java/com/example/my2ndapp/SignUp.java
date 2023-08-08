package com.example.my2ndapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.Member;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private EditText FirstNameGap,LastNameGap,emailGap,EnterValidPasswordGap;
    private Button buttonForSignUp;
    private Spinner spinner;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    FirebaseDatabase database;
    DatabaseReference reference;

    //Member member;
    int maxId = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        FirstNameGap = findViewById(R.id.FirstNameGap);
        LastNameGap = findViewById(R.id.LastNameGap);
        spinner = findViewById(R.id.ProfessionSpinner);
        emailGap = findViewById(R.id.emailGap);
        EnterValidPasswordGap = findViewById(R.id.EnterValidPasswordGap);

        buttonForSignUp = findViewById(R.id.buttonForSignUp);

        //member = new Member();
        reference = database.getInstance().getReference().child("Users");

        List<String> Categories = new ArrayList<>();
        Categories.add(0,"user");
        Categories.add("aaa");
        Categories.add("bbb");

        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,Categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //    if(parent.getItemAtPosition(position).equals("Choose Category")){

              //  }else{
                   // selected.setText(parent.getSelectedItem().toString());
               // }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        buttonForSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fname,lname,categ,email,password,isAdmin;

                fname = String.valueOf(FirstNameGap.getText());
                lname = String.valueOf(LastNameGap.getText());
                categ = spinner.getSelectedItem().toString();
                email = String.valueOf(emailGap.getText());
                password = String.valueOf(EnterValidPasswordGap.getText());


               if(spinner.getSelectedItemPosition()==0) {
                   isAdmin = "0";
               }
               else{
                   isAdmin = "1";
                }

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
                                        userInfo.put("Category", categ);
                                        userInfo.put("UserEmail", email);
                                        userInfo.put("IsAdmin", isAdmin);


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

               // member.setSpinner(spinner.getSelectedItem().toString());
                Toast.makeText(SignUp.this, "Spinner Successfully", Toast.LENGTH_SHORT).show();
                insertUserAdminData();




            }


        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    maxId = (int)snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void insertUserAdminData(){

        String fname,lname,categ,email,isAdmin;

        fname = String.valueOf(FirstNameGap.getText());
        lname = String.valueOf(LastNameGap.getText());
        categ = spinner.getSelectedItem().toString();
        email = String.valueOf(emailGap.getText());

        if(categ.equals("user")){
            isAdmin="0";
        }
        else{
            isAdmin="1";
        }


        UserAdminData userAdminData = new UserAdminData(fname,lname,email,categ,isAdmin);

        //reference.push().setValue(userAdminData);  ITAN PALIA GIA NA VGAZEI RANDOM TIMES
        reference.child(String.valueOf(maxId+1)).setValue(userAdminData); //gia auto increment
        Toast.makeText(SignUp.this, "Data Inserted  .",Toast.LENGTH_SHORT).show();




    }
}