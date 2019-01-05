package mbds.api;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import mbds.Connect;
import mbds.Database;
import mbdse.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckMessagesService extends Service {

    /**
     * Command to the service to display a message
     */
    public static final int MSG_SAY_HELLO = 1;
    public static boolean serviceRunning = false;
    private ApiService mAPIService;
    private Database db;

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

        Toast.makeText(this, "Started Service!!!", Toast.LENGTH_SHORT).show();
        //have to run indefinitely...
        // return don't stops the service, its a loop...
        myJob = new Job();
        thread = new Thread(myJob);
        thread.start();
        fetchMessageFromServer();
        serviceRunning = true;

        return START_STICKY;
    }

      public void fetchMessageFromServer(){

        mAPIService.fetchMessages().enqueue(new Callback<List<mbds.api.Message>>() {

              @Override
              public void onResponse(Call<List<mbds.api.Message>> call, Response<List<mbds.api.Message>> response) {
                  if(response.isSuccessful()){
                      List<mbds.api.Message> listMessage= response.body();
                      for (mbds.api.Message message: listMessage){
                          String content="";
                          int id =message.getId() ;
                          String author =message.getAuthor();
                          String textMessage =message.getTextmessage();
                          String dateCreated =message.getDateCreated() ;
                          db = Database.getIstance(getApplicationContext());
                          db.addMessage(id,author,textMessage,dateCreated);
                      }
                  }
              }
              @Override
              public void onFailure(Call<List<mbds.api.Message>> call, Throwable t) {
                  // rien pour l'instant
              }
          });
        }
}
