package com.example.mbdse.tp1android;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity implements iCallable{


     FrameLayout fl;
     FrameLayout fl1;

     Button btn,btnSend ;
     boolean a=true;
     boolean isSingleViewed;
     Database db;

     MainActivityFragment maf = new MainActivityFragment();
     SecActivityFragment sec = new SecActivityFragment();


     @Override
     protected void onCreate(Bundle saveActivityFragment) {
         super.onCreate(saveActivityFragment);
         setContentView(R.layout.activity_main);

         fl = (FrameLayout) findViewById(R.id.frameBtn);
         fl1 = (FrameLayout) findViewById(R.id.idSec);
         db= Database.getIstance(getApplicationContext());


         btn=findViewById(R.id.btn);

         if(btn == null) // mode paysage (pas de bouton)
         {
             FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
             fragmentTransaction.replace(fl.getId(), sec);
             fragmentTransaction.replace(fl1.getId(), maf);
             fragmentTransaction.commit();
             return;
         }

         btn.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                db.addPerson("ibeghouchene","Nadir");
                 a = !a;

                 FragmentTransaction fragmentTransaction =
                         getSupportFragmentManager().beginTransaction();
                 if (a) {
                     fragmentTransaction.replace(fl.getId(), maf);
                 } else {
                     fragmentTransaction.replace(fl.getId(), sec);
                 }
                 fragmentTransaction.commit();
             }
         });
     }
    @Override
    public void transferData(String s)
    {
        sec.setText(s);
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