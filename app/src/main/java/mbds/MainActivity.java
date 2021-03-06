package mbds;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.FileDescriptor;
import java.util.Timer;
import java.util.TimerTask;

import mbds.api.CheckMessagesService;
import mbdse.R;

public class MainActivity extends AppCompatActivity implements iCallable {

    private FrameLayout flm;
    private FrameLayout fls;
    private Button switchBtn;
    private Database db;
    private FragmentContacts contacts = new FragmentContacts();
    private FragmentMessages messages = new FragmentMessages();
    private boolean a = true;
    private long userID;
    private String token;
    final Messenger mMessenger = new Messenger(new IncomingHandler());



     @Override
     protected void onCreate(Bundle saveActivityFragment) {
         super.onCreate(saveActivityFragment);
         setContentView(R.layout.activity_main);
         flm = findViewById(R.id.mainFrame);
         fls = findViewById(R.id.secFrame);
         db= Database.getIstance(getApplicationContext());
         switchBtn=findViewById(R.id.switchBtn);
         Intent intent = getIntent();
         userID = intent.getLongExtra("UserID", 0);
         contacts.setUserID(userID);
         if(switchBtn == null){ // mode paysage (pas de bouton)
             FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
             fragmentTransaction.replace(flm.getId(), messages);
             fragmentTransaction.replace(fls.getId(), contacts);
             fragmentTransaction.commit();
             return;
         }
         switchBtn.setOnClickListener((v) -> handleClick());
         // Bind to the service
         bindService(new Intent(this,CheckMessagesService.class), mConnection,
                 Context.BIND_AUTO_CREATE);
         handleClick();
         getTokenFromService();
     }

    @Override
    public void transferData(String s) {
         if(switchBtn != null){
             handleClick();
         }
         messages.setText(s, userID);
     }

    @Override
    public String getToken() {
         return this.token;
    }

    public void getTokenFromService(){
        if(mBound){
            Message msg = Message.obtain(null, CheckMessagesService.MSG_GET_TOKEN, 0, 0);
            msg.replyTo = mMessenger;
            try {
                Log.i("MainActivity", "getting token from service...");
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        Timer timer = new Timer("waiting for token...", true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(MainActivity.this.token == null){
                   getTokenFromService();
                }
            }
        }, 1000);
    }

    Messenger mService = null;
    boolean mBound = false;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the object we can use to
            // interact with the service.  We are communicating with the
            // service using a Messenger, so here we get a client-side
            // representation of that from the raw IBinder object.
            mService = new Messenger(service);
            mBound = true;
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null;
            mBound = false;
        }
    };

    private void handleClick(){
        a = !a;
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        if (a) {
            fragmentTransaction.replace(flm.getId(), messages);
        } else {
            fragmentTransaction.replace(flm.getId(), contacts);
        }
        fragmentTransaction.commit();
        if (!mBound) return;
        // Create and send a message to the service, using a supported 'what' value
        Message msg = Message.obtain(null, CheckMessagesService.MSG_SAY_HELLO, 0, 0);
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    private class IncomingHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CheckMessagesService.MSG_GET_TOKEN:
                    Log.i("MainActivity", "getting response from service...");
                    MainActivity.this.token = msg.getData().getString("str1");
                default:
                    super.handleMessage(msg);
            }
        }
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