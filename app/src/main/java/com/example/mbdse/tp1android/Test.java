package com.example.mbdse.tp1android;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Test extends AppCompatActivity {


    EditText loginBox;
    EditText passBox;
    Button validBtn;
    Button registerBtn;


    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_test);
        loginBox=(EditText)findViewById(R.id.loginbox);
        passBox=(EditText)findViewById(R.id.passbox);

        final Intent i= new Intent(this, Test2.class);
        final Intent ma= new Intent(this, MainActivity.class);


        registerBtn=(Button)findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(i);
            }
        });

        validBtn=(Button)findViewById(R.id.validBtn);
        validBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(ma);
            }
        });



}
public void login (){
    if(loginBox.getText().toString().equals("toto") && passBox.getText().toString().equals("tata")){
        validBtn.setBackgroundColor(Color.GREEN);

    }
    else
    {
        validBtn.setBackgroundColor(Color.RED);

    }
    }
}
