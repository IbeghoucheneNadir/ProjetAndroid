package com.example.mbdse.tp1android;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Test2 extends AppCompatActivity {

    EditText loginBox;
    EditText passBox;
    Button validBtn;
    Button registerBtn;
    Button retourBtn;


    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_test2);
        loginBox=(EditText)findViewById(R.id.loginbox);
        passBox=(EditText)findViewById(R.id.passbox);

        registerBtn=(Button)findViewById(R.id.registerBtn);
        validBtn=(Button)findViewById(R.id.validBtn);

        final Intent i= new Intent(this, Test.class);

        retourBtn=(Button)findViewById(R.id.retourBtn);
        retourBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });



    }

}

