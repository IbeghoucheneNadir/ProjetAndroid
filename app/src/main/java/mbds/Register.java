package mbds;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import mbds.api.ApiService;
import mbds.api.CreateUser;
import mbds.api.RetrofitClient;
import mbdse.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    private EditText login;
    private EditText password1;
    private EditText password2;
    private Button inscriptionBtn;
    private Button retourBtn;
    private Database db;
    private ApiService mAPIService;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        login = findViewById(R.id.login);
        password1 = findViewById(R.id.password1);
        password2 = findViewById(R.id.password2);
        inscriptionBtn = findViewById(R.id.inscriptionBtn);
        inscriptionBtn.setOnClickListener((v) -> handleRegister());
        retourBtn = findViewById(R.id.retourBtn);
        retourBtn.setOnClickListener((v) -> finish());
        mAPIService = RetrofitClient.getAPIService();
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
            sendCreateUser(name, pwd1);
        }
    }

    public void sendCreateUser(String name, String pass) {
        Log.i("CREATEUSER", "Sending ... ");
        Map<String, String> params = new HashMap<>();
        params.put("username", name);
        params.put("password", pass);
        mAPIService.createUser(params).enqueue(new Callback<CreateUser>() {

            @Override
            public void onResponse(Call<CreateUser> call, Response<CreateUser> response) {
                //message send but what result?
                if(response.isSuccessful()) {
                    db = Database.getIstance(getApplicationContext());
                    db.addUser(name, pass);
                    Toast.makeText(getApplicationContext(),
                            "Account for " + response.body().getUsername() + " created!", Toast.LENGTH_SHORT).show();
                    finish();
                    Log.i("CREATEUSER", "post submitted to API." + response.toString());
                }else {
                    //server error or username already exists...
                    Toast.makeText(getApplicationContext(),
                            "Bad Request! The username probably already exists!", Toast.LENGTH_SHORT).show();
                    Log.w("CREATEUSER", "post submitted to API." + response.toString());
                }
            }

            @Override
            public void onFailure(Call<CreateUser> call, Throwable t) {
                //message not send...
                Toast.makeText(getApplicationContext(),
                        "Connection Problem! Request not send!", Toast.LENGTH_SHORT).show();
                Log.e("CREATEUSER", "Unable to submit post to API. " + t.getMessage());
                Log.e("CREATEUSER", call.toString());
            }
        });
    }
}
