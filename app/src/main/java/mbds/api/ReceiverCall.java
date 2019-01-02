package mbds.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ReceiverCall extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("SERVICE", "Stops Ohhhhhhh");
        context.startService(new Intent(context, CheckMessagesService.class));;
    }
}
