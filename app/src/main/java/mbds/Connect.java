package mbds;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import mbdse.R;

public class Connect extends AppCompatActivity {

    EditText loginBox;
    EditText passBox;
    Button validBtn;
    Button registerBtn;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_connect);
        final Intent i = new Intent(this, Register.class);
        final Intent ma = new Intent(this, MainActivity.class);
        loginBox=(EditText)findViewById(R.id.loginbox);
        passBox=(EditText)findViewById(R.id.passbox);
        registerBtn=(Button)findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener((v) -> startActivity(i));
        validBtn=(Button)findViewById(R.id.validBtn);
        validBtn.setOnClickListener((v) -> {
            login();
            startActivity(ma);
        });
    }

    public void login (){
        //TODO do login with database check
        if(loginBox.getText().toString().equals("toto") && passBox.getText().toString().equals("tata")){
            validBtn.setBackgroundColor(Color.GREEN);
        }else{
            validBtn.setBackgroundColor(Color.RED);

        }

    }
}
