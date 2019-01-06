package mbds.api;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import mbds.Connect;
import mbds.Database;
import mbdse.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckMessagesService extends Service {

    /**
     * Command to the service to display a message
     */
    public static final int MSG_SAY_HELLO = 1;
    public static final int MSG_GET_TOKEN = 4;
    public static boolean serviceRunning = false;
    private ApiService mAPIService;
    private Database db;
    static private String token; //this is temporarly... have to move it in to the key store...
    private String login;
    private long userID;
    private String password;

    /**
     * Handler of incoming messages from clients.
     */
    static class IncomingHandler extends Handler {
        private Context applicationContext;

        IncomingHandler(Context context) {
            applicationContext = context.getApplicationContext();
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SAY_HELLO:
                    Toast.makeText(applicationContext, "hello!", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_GET_TOKEN:
                    Message responseMsg = Message.obtain(null, MSG_GET_TOKEN);
                    Bundle b = new Bundle();
                    b.putString("str1", CheckMessagesService.token);
                    responseMsg.setData(b);
                    try {
                        msg.replyTo.send(responseMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    Messenger mMessenger;

    @Override
    public void onCreate() {
        if(serviceRunning) return;
        super.onCreate();
        mAPIService = RetrofitClient.getAPIService();

        Intent notificationIntent = new Intent(this, Connect.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, "M_CH_ID")
                .setSmallIcon(R.drawable.stat_sample)
                .setContentTitle("Our Awesome App")
                .setContentText("Doing some work...")
                .setContentIntent(pendingIntent).build();

        startForeground(1337, notification);
        Log.i("SERVICE", "onCreate Called!");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.i("SERVICE", "task removed!");
        myJob.stopJob();
        serviceRunning = false;
        Intent intent = new Intent();
        intent.putExtra("token",this.token);
        intent.putExtra("password", this.password);
        intent.putExtra("login", this.login);
        intent.putExtra("userID", this.userID);
        intent.setAction("mbds.api.restartService");
        intent.setClass(this, ReceiverCall.class);
        sendBroadcast(intent);
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        // Tell the user we stopped.
        myJob.stopJob();
        serviceRunning = false;
        Toast.makeText(this, R.string.remote_service_stopped, Toast.LENGTH_SHORT).show();
        Log.e("SERVICE", "DESTROYED!!!");
        Intent intent = new Intent("mbds.api.restartService");
        sendBroadcast(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT).show();
        mMessenger = new Messenger(new IncomingHandler(this));
        return mMessenger.getBinder();
    }

    Thread thread;
    Job myJob;
    class Job implements Runnable{
        private boolean doStop = false;

        public synchronized  void stopJob(){
            doStop = true;
        }

        private synchronized boolean keepRunning() {
            return !doStop;
        }

        @Override
        public void run() {
            int i = 0;
            Looper.prepare();
            while(keepRunning()) {
                try {
                    i++;
                    final int ii = i;
                    Thread.sleep(1000);
                    Log.i("SERVICE", "log " + i);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CheckMessagesService.this, "Service " + ii, Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(serviceRunning) return START_NOT_STICKY;
        super.onStartCommand(intent,flags,startId);
        token = intent.getStringExtra("token");
        login = intent.getStringExtra("login");
        password = intent.getStringExtra("password");
        userID = intent.getLongExtra("userID", -1);
        Log.i("SERVICEMESSAGE", "token="+this.token);
        Toast.makeText(this, "Started Service!!!", Toast.LENGTH_SHORT).show();
        //have to run indefinitely...
        // return don't stops the service, its a loop...
        if(token != null) {
            startTread();
        }else{
            getToken();
        }
        serviceRunning = true;

        return START_STICKY;
    }

    private void startTread(){
        myJob = new Job();
        thread = new Thread(myJob);
        thread.start();
        Log.i("SERVICE", "fetching message...");
        fetchMessageFromServer();
    }

    private void getToken() {
        Map<String, String> params = new HashMap<>();
        params.put("username", login);
        params.put("password", password);
        Log.i("SERVICEMESSAGE", "trying to get token...");
        mAPIService.login(params).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                //message send but what result?
                if(response.isSuccessful()) {
                    //valid credentials, we have to save them in the database
                    CheckMessagesService.this.token = "Bearer " + response.body().getAccessToken();
                    startTread();
                    Log.i("SERVICEMESSAGE", "post submitted to API." + response.toString());
                }else {
                    Log.e("SERVICEMESSAGE", "post submitted to API." + response.toString());
                    //we don't retry because pass or login is false...
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                //message not send...
                Log.w("SERVICEMESSAGE", "Unable to submit post to API. " + t.getMessage());
                Log.w("SERVICEMESSAGE", call.toString());
                Timer timer = new Timer("onFailHaveToRetry!", true);
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        getToken();
                    }
                }, 30000);
            }
        });
    }

    public void fetchMessageFromServer(){
        mAPIService.fetchMessages(this.token).enqueue(new Callback<List<mbds.api.Message>>() {

              @Override
              public void onResponse(Call<List<mbds.api.Message>> call, Response<List<mbds.api.Message>> response) {
                  if(response.isSuccessful()){
                      List<mbds.api.Message> listMessage= response.body();
                      Log.i("SERVICEMESSAGE", "success! "+ response.toString());
                      for (mbds.api.Message message: listMessage){
                          Log.e("SERVICEMESSAGE", message.toString());
                          String content="";
                          int id =message.getId() ;
                          String author =message.getAuthor();
                          String textMessage =message.getTextmessage();
                          String dateCreated =message.getDateCreated();
                          db = Database.getIstance(getApplicationContext());
                          db.addMessage(author,CheckMessagesService.this.login,author,textMessage,dateCreated, CheckMessagesService.this.userID);
                      }
                  }else{
                      Log.w("SERVICEMESSAGE", "post submitted to API. " + response.toString());
                  }
              }
              @Override
              public void onFailure(Call<List<mbds.api.Message>> call, Throwable t) {
                  Log.e("SERVICEMESSAGE", "Unable to submit post to API. " + t.getMessage());
                  Log.e("SERVICEMESSAGE", call.toString());
              }
          });
        }
}
