package com.example.my2ndapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
public class MainActivity extends AppCompatActivity {


    private ImageView imageView;

    private TextView fadingTextView;

    private Button button1,button2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        imageView.setBackgroundResource(R.drawable.logo);

        fadingTextView = findViewById(R.id.fading_text_view);

        // Create the fading animation
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(4000); // Adjust the duration as needed
        alphaAnimation.setRepeatCount(Animation.INFINITE); // Repeat indefinitely
        alphaAnimation.setRepeatMode(Animation.REVERSE); // Reverse the animation
        animationSet.addAnimation(alphaAnimation);

        // Apply the animation to the TextView
        fadingTextView.startAnimation(animationSet);



        button1 = findViewById(R.id.btn1);
        button2 = findViewById(R.id.btn2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserActivity();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpActivity();
            }
        });
    }

    public void openUserActivity(){
        Intent intent = new Intent(this, SignIn.class);
        startActivity(intent);
    }

    public void openSignUpActivity(){
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }
}