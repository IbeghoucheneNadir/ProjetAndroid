package mbds;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import mbdse.R;

public class Register extends AppCompatActivity {

    EditText loginBox;
    EditText passBox;
    Button inscriptionBtn;
    Button retourBtn;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        inscriptionBtn=(Button)findViewById(R.id.inscriptionBtn);
        inscriptionBtn.setOnClickListener((v) -> handleRegister());
        retourBtn=(Button)findViewById(R.id.retourBtn);
        retourBtn.setOnClickListener((v) -> finish());
    }

    private void handleRegister(){
        loginBox=(EditText)findViewById(R.id.loginbox);
        passBox=(EditText)findViewById(R.id.passbox);
        //TODO handle Registration
        finish();
    }

}

