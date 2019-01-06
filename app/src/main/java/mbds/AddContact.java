package mbds;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import mbds.api.ApiService;
import mbds.api.RetrofitClient;
import mbdse.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddContact extends AppCompatActivity {

    private Database db;
    private ApiService mAPIService;
    private Button addContactBtn;
    private long userID;
    private String token;
    String contact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        userID = intent.getLongExtra("UserID", 0);
        token = intent.getStringExtra("token");
        Log.i("Addcontact", token);
        setContentView(R.layout.layout_addcontact);
        db = Database.getIstance(getApplicationContext());
        mAPIService = RetrofitClient.getAPIService();
        addContactBtn = findViewById(R.id.addContactBtn);
        addContactBtn.setOnClickListener((v1) -> onClick());
    }

    private void onClick() {
        EditText editText = findViewById(R.id.contactText);
        contact = editText.getText().toString();
        if (contact.length()==0){
            Toast.makeText(getApplicationContext(),
                    R.string.contactnonVide, Toast.LENGTH_SHORT).show();
        }
        else if (contact.length() != 0 && contact.matches("[a-zA-z_0-9]*")) {
            sendPing();
        }
        else {
            Toast.makeText(getApplicationContext(),
                    R.string.contactnonAjoute, Toast.LENGTH_SHORT).show();
        }
    }

    private void sendPing() {
        String message = "PING";
        String login = db.getLogin(this.userID);

        Map<String, String> params = new HashMap<>();
        params.put("message", message);
        params.put("receiver", contact);
        Log.i("Addcontact", "sending messsage: " + message + " to: " + contact);
        //TODO send message with service...have to get the token...
        mAPIService.sendMsg(token, params ).enqueue(new Callback<ResponseBody>(){

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(AddContact.this,
                            "Contact added successful!", Toast.LENGTH_SHORT).show();
                    try {
                        Log.i("Addcontact", "post submitted to API." + response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    db.addPerson(contact, userID);
                    Toast.makeText(getApplicationContext(),
                            R.string.contactAjoute, Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(AddContact.this,
                            "Contact not found!", Toast.LENGTH_SHORT).show();
                    Log.w("Addcontact", "post submitted to API." + response.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(AddContact.this,
                        "Contact not added!", Toast.LENGTH_SHORT).show();
                Log.e("Addcontact", call.toString());
                Log.e("Addcontact", t.getMessage());
            }
        });
    }

}
