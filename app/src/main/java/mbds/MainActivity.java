package mbds;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.FrameLayout;
import mbdse.R;

public class MainActivity extends AppCompatActivity implements iCallable {

     FrameLayout flm;
     FrameLayout fls;
     Button switchBtn;
     Database db;
     FragmentContacts contacts = new FragmentContacts();
     FragmentMessages messages = new FragmentMessages();
     boolean a = true;

     @Override
     protected void onCreate(Bundle saveActivityFragment) {
         super.onCreate(saveActivityFragment);
         setContentView(R.layout.activity_main);
         flm = findViewById(R.id.mainFrame);
         fls = findViewById(R.id.secFrame);
         db= Database.getIstance(getApplicationContext());
         switchBtn=findViewById(R.id.switchBtn);

         if(switchBtn == null){ // mode paysage (pas de bouton)
             FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
             fragmentTransaction.replace(flm.getId(), messages);
             fragmentTransaction.replace(fls.getId(), contacts);
             fragmentTransaction.commit();
             return;
         }
         switchBtn.setOnClickListener((v) -> handleClick());
         handleClick();
     }

    @Override
    public void transferData(String s) {
         messages.setText(s);
    }

    private void handleClick(){
        db.addPerson("ibeghouchene","Nadir");
        a = !a;
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        if (a) {
            fragmentTransaction.replace(flm.getId(), messages);
        } else {
            fragmentTransaction.replace(flm.getId(), contacts);
        }
        fragmentTransaction.commit();
    }
}

// transfer de données entre deux fragment

//3 a competer   envoie de message txt
//4
        /*
créer un adaptater
*/

// base de données
/*
class personne{
    nom;
    prenom;
}
une liste de personne
        nouvelle activité
                champs pour saisir nom
        champs pour saisir prenom
        bouton OK POUR ENVOY2 A LA BASE DE DONNEES
*/
// baobab.tokidev.fr