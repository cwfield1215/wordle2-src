package com.example.wordle2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


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
        getLeaderboard();

    }
    public void getLeaderboard() {
        try {
            //URL url = new URL("http://localhost:8080/leaderboard");
            URL url = new URL("https://kordle-svr-5bu27xjxoq-ue.a.run.app/leaderboard");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/text");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String userColText="";
            String userTriesText="";
            String userTimeText="";
            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println();
                String [] parts =output.split(",");
                System.out.println(parts[0]);
                System.out.println(parts[1]);
                System.out.println(parts[2]);
                userColText=userColText+parts[0]+"\n\n";
                userTriesText=userTriesText+parts[1]+"\n\n";
                userTimeText=userTimeText+parts[2]+"\n\n";

            }
            conn.disconnect();
            tv4.setText(userColText);
            tv5.setText(userTriesText);
            tv6.setText(userTimeText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void switchActivities2(){
        Intent switchActivityIntent=new Intent(this,mainScreen.class);
        startActivity(switchActivityIntent);
    }
}