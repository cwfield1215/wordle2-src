package com.example.wordle2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class mainScreen extends AppCompatActivity {
    EditText edit;
    Button switchToSecondActivity;
    TextView tv;
    TextView textView;
    String userName;
    int username;

    @Override
    protected void onCreate(Bundle saveInstantState){
        super.onCreate(saveInstantState);
        setContentView(R.layout.loginscreen);
        edit=findViewById(R.id.tv1);
        edit.setText("");
        textView = findViewById(R.id.textView4);
        tv=findViewById(R.id.textView4);
        switchToSecondActivity=findViewById(R.id.loginButton);
        switchToSecondActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                userName=edit.getText().toString();
                if(userName==""){
                    Toast toast=Toast.makeText(getApplicationContext(), "Brandon can't spell my name correctly", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP,-300,0);
                    toast.show();
                }
                else {
                    switchActivities(userName);
                }
            }
        });
        tv.setText("Same as regular Wordle"+"\n"+"This is a tournament style competition where"+"\n"+"you are up against someone with the same word and whoever gets the word in fewer tries wins!"+"\n"+"If both players take the same amount of guesses whoever solved it faster wins!"+"\n"+"When you are ready to play press start!");

    }

    private void switchActivities(String userName){
        System.out.println("switchActivities");
        Intent switchActivityIntent=new Intent(this, MainActivity.class);
        switchActivityIntent.putExtra("userName", userName);
        startActivity(switchActivityIntent);
    }
}


