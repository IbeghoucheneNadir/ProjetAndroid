package mbds;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mbdse.R;

public class Register extends AppCompatActivity {

    EditText login;
    EditText password1;
    EditText password2;
    Button inscriptionBtn;
    Button retourBtn;
    Database db;
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        login=(EditText)findViewById(R.id.login);
        password1=(EditText)findViewById(R.id.password1);
        password2=(EditText)findViewById(R.id.password2);
        inscriptionBtn=(Button)findViewById(R.id.inscriptionBtn);
        inscriptionBtn.setOnClickListener((v) -> handleRegister());
        retourBtn=(Button)findViewById(R.id.retourBtn);
        retourBtn.setOnClickListener((v) -> finish());
    }

    private void handleRegister(){

        String name = this.login.getText().toString();
        String pwd1 = this.password1.getText().toString();
        String pwd2 = this.password2.getText().toString();

        if(name.length()==0){
            Toast.makeText(getApplicationContext(),
                    R.string.errorLogin, Toast.LENGTH_SHORT).show();
        }
        else if( pwd1.length()==0){
             Toast.makeText(getApplicationContext(),
                    R.string.errorPwdVide, Toast.LENGTH_SHORT).show();
        }
        else if(( pwd1.length()!=0 && pwd2.length()==0) ){
            Toast.makeText(getApplicationContext(),
                    R.string.errorPwd2Vide, Toast.LENGTH_SHORT).show();
        }
        else if(!(pwd1.equals(pwd2)) ){
           Toast.makeText(getApplicationContext(),
                 R.string.errorPwd, Toast.LENGTH_SHORT).show();
        }
        else{
            db= Database.getIstance(getApplicationContext());
            db.addPerson(name, pwd1);
            Toast.makeText(getApplicationContext(),
                    R.string.saveDb, Toast.LENGTH_SHORT).show();
            //redirect to login page
            Intent intent = new Intent(this, Connect.class);
            startActivity(intent);
        }
    }
}
