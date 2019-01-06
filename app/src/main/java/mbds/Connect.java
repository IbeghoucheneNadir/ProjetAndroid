package mbds;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import mbds.api.ApiService;
import mbds.api.CheckMessagesService;
import mbds.api.CreateUser;
import mbds.api.Login;
import mbds.api.RetrofitClient;
import mbdse.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Connect extends AppCompatActivity {

    private EditText login;
    private EditText password;
    private Button validBtn;
    private Button registerBtn;
    private Database db;
    private ApiService mAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = Database.getIstance(getApplicationContext());
        mAPIService = RetrofitClient.getAPIService();
        setContentView(R.layout.layout_connect);
        final Intent i = new Intent(this, Register.class);
        login = findViewById(R.id.loginbox);
        password = findViewById(R.id.passbox);
        registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener((v) -> startActivity(i));
        validBtn = findViewById(R.id.validBtn);
        validBtn.setOnClickListener((v) -> login());
        Log.i("Connect ONCREATE", "created!");
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
                //the user already exists in the database
                //we enter directly to next screen for offline access
                //and then try to connect to the remote server
                validBtn.setBackgroundColor(Color.GREEN);
                Intent intent = new Intent(Connect.this, CheckMessagesService.class);
                intent.putExtra("login",login.getText().toString());
                intent.putExtra("password", password.getText().toString());
                intent.putExtra("userID", db.readUserID(login.getText().toString()));
                stopService(intent);
                startService(intent);
                final Intent ma = new Intent(this, MainActivity.class);
                db.readUser();
                ma.putExtra("UserID", db.readUserID(login.getText().toString()));
                startActivity(ma);
                Toast.makeText(getApplicationContext(),
                        R.string.connnexionReussi, Toast.LENGTH_SHORT).show();
            } else {
                // the user is not in the database...but maybe he has valid credentials?
                Map<String, String> params = new HashMap<>();
                params.put("username", login.getText().toString());
                params.put("password", password.getText().toString());
                Log.i("Test", login.toString());
                Log.i("Test", password.toString());
                mAPIService.login(params).enqueue(new Callback<Login>() {

                    @Override
                    public void onResponse(Call<Login> call, Response<Login> response) {
                        //message send but what result?
                        if(response.isSuccessful()) {
                            //valid credentials, we have to save them in the database
                            db = Database.getIstance(getApplicationContext());
                            db.addUser(params.get("username") , params.get("password"));
                            validBtn.setBackgroundColor(Color.GREEN);
                            Intent intent = new Intent(Connect.this, CheckMessagesService.class);
                            Log.i("LOGIN", response.body().getAccessToken());
                            intent.putExtra("token","Bearer " + response.body().getAccessToken());
                            intent.putExtra("userID", db.readUserID(login.getText().toString()));
                            stopService(intent);
                            startService(intent);
                            final Intent ma = new Intent(Connect.this, MainActivity.class);
                            ma.putExtra("UserID", db.readUserID(params.get("username")));
                            startActivity(ma);
                            Toast.makeText(getApplicationContext(),
                                    R.string.connnexionReussi, Toast.LENGTH_SHORT).show();
                            Log.i("LOGIN", "post submitted to API." + response.toString());
                        }else {
                            //server error or wrong credentials...
                            Toast.makeText(getApplicationContext(),
                                    R.string.connexionEchoue, Toast.LENGTH_SHORT).show();
                            validBtn.setBackgroundColor(Color.RED);
                            Log.w("LOGIN", "post submitted to API." + response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<Login> call, Throwable t) {
                        //message not send...
                        Toast.makeText(getApplicationContext(),
                                "Connection Problem! Request not send!", Toast.LENGTH_SHORT).show();
                        validBtn.setBackgroundColor(Color.RED);
                        Log.e("LOGIN", "Unable to submit post to API. " + t.getMessage());
                        Log.e("LOGIN", call.toString());
                    }
                });
            }
        }
    }
}
