package mbds.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ReceiverCall extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("SERVICE", "Stops Ohhhhhhh");
        Intent theIntent = new Intent(context, CheckMessagesService.class);
        theIntent.putExtra("token", intent.getStringExtra("token"));
        theIntent.putExtra("login", intent.getStringExtra("login"));
        theIntent.putExtra("password", intent.getStringExtra("password"));
        context.startService(theIntent);
    }
}
