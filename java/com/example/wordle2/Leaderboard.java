package com.example.wordle2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class Leaderboard extends AppCompatActivity {
    TextView tv1;
    TextView tv2;
    TextView tv3;
    TextView tv4;
    TextView tv5;
    TextView tv6;
    ImageView image;
    ImageView image2;

    Button switchToLoginScreen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboardxml);
        tv1 = findViewById(R.id.nameTitle);
        tv2 = findViewById(R.id.guessTitle);
        tv3 = findViewById(R.id.timeTitle);
        tv4 = findViewById(R.id.nameDisplay);
        tv5 = findViewById(R.id.guessDisplay);
        tv6 = findViewById(R.id.timeDisplay);
        image=findViewById(R.id.line);
        image2=findViewById(R.id.line2);
        switchToLoginScreen = findViewById(R.id.homeButton);
        switchToLoginScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchActivities2();
            }
        });

    }
    private void switchActivities2(){
        Intent switchActivityIntent=new Intent(this,mainScreen.class);
        startActivity(switchActivityIntent);
    }
}
