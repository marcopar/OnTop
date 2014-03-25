package eu.flatworld.android.ontop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver  extends BroadcastReceiver {

	@Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
        	Log.d(Main.LOGTAG, "boot completed");
        	Log.d(Main.LOGTAG, "starting service");
            Intent myIntent = new Intent(context, MainService.class);
            //context.startService(myIntent);
        }
    }

}
