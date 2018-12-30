package mbds;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mbdse.R;

public class Connect extends AppCompatActivity {

    private EditText login;
    private EditText password;
    private Button validBtn;
    private Button registerBtn;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = Database.getIstance(getApplicationContext());
        setContentView(R.layout.layout_connect);
        final Intent i = new Intent(this, Register.class);
        login = (EditText) findViewById(R.id.loginbox);
        password = (EditText) findViewById(R.id.passbox);
        registerBtn = (Button) findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener((v) -> startActivity(i));
        validBtn = (Button) findViewById(R.id.validBtn);
        validBtn.setOnClickListener((v) -> {
            login();
        });
    }

    public int checkUser(String login, String password) {
        return db.checkUser(login, password);
    }

    public void login() {
        //TODO do login with database check

        if (login.length() == 0) {
            Toast.makeText(getApplicationContext(),
                    R.string.errorLogin, Toast.LENGTH_SHORT).show();
        } else if (password.length() == 0) {
            Toast.makeText(getApplicationContext(),
                    R.string.errorPwdVide, Toast.LENGTH_SHORT).show();
        } else if (login.length() != 0 && password.length() != 0) {
            if (checkUser(login.getText().toString(), password.getText().toString()) != -1) {
                validBtn.setBackgroundColor(Color.GREEN);
                final Intent ma = new Intent(this, MainActivity.class);
                startActivity(ma);
                Toast.makeText(getApplicationContext(),
                        R.string.connnexionReussi, Toast.LENGTH_SHORT).show();
            } else {
                validBtn.setBackgroundColor(Color.RED);
                Toast.makeText(getApplicationContext(),
                        R.string.connexionEchoue, Toast.LENGTH_SHORT).show();
            }
    /*    if(loginBox.getText().toString().equals("toto") && passBox.getText().toString().equals("tata")){
            validBtn.setBackgroundColor(Color.GREEN);
            final Intent ma = new Intent(this, MainActivity.class);
            startActivity(ma);
        }else{
            validBtn.setBackgroundColor(Color.RED);
            final Intent ma = new Intent(this, MainActivity.class);
            startActivity(ma); // this is practical for debugging
        */}
    }
}
