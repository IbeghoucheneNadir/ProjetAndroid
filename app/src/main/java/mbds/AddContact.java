package mbds;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mbds.api.ApiService;
import mbds.api.RetrofitClient;
import mbdse.R;

public class AddContact extends AppCompatActivity {

    private Database db;
    private ApiService mAPIService;
    private Button addContactBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_addcontact);
        db = Database.getIstance(getApplicationContext());
        mAPIService = RetrofitClient.getAPIService();
        addContactBtn = findViewById(R.id.addContactBtn);
        addContactBtn.setOnClickListener((v1) -> onClick());
    }

    private void onClick(){
        EditText editText =  findViewById(R.id.contactText);
        String contact = editText.getText().toString();
        db.addPerson(contact, contact);
        Toast.makeText(getApplicationContext(),
                R.string.contactAjoute, Toast.LENGTH_SHORT).show();
        finish();
    }


}