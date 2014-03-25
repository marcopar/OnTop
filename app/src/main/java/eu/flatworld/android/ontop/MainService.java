package eu.flatworld.android.ontop;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MainService extends Service {
	boolean lockActivityEnabled = true;
	CallStateListener callStateListener = new CallStateListener();

	class ActionReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
				screenOff();
			} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
				screenOn();
			}
		}

	}

	class CallStateListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			if (state == TelephonyManager.CALL_STATE_IDLE) {
				Log.d(Main.LOGTAG, "call idle");
				lockActivityEnabled = true;
			} else if (state == TelephonyManager.CALL_STATE_RINGING) {
				Log.d(Main.LOGTAG, "call ringing");
				lockActivityEnabled = false;
			} else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
				Log.d(Main.LOGTAG, "call offhook");
				lockActivityEnabled = false;
			}
		}

	}

	static boolean RUNNING = false;
	ActionReceiver mReceiver;

	@Override
	public void onCreate() {
		Log.d(Main.LOGTAG, "service started");
		super.onCreate();
		// register receiver that handles screen on and screen off logic
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		Log.d(Main.LOGTAG, "registering screen receiver");
		mReceiver = new ActionReceiver();
		registerReceiver(mReceiver, filter);
		Log.d(Main.LOGTAG, "registering call state listener");
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		tm.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);
		RUNNING = true;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			Log.d(Main.LOGTAG, "unregistering screen receiver");
			unregisterReceiver(mReceiver);
			Log.d(Main.LOGTAG, "unregistering call state listener");
			TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			tm.listen(callStateListener, PhoneStateListener.LISTEN_NONE);
			Log.d(Main.LOGTAG, "service stopped");
		} finally {
			RUNNING = false;
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	void screenOn() {
		Log.d(Main.LOGTAG, "screen on");
		if (lockActivityEnabled) {
			Log.d(Main.LOGTAG, "starting activity");
			Intent i = new Intent(this, OnTopActivity.class);
			// new task required for our service activity start to succeed.
			// exception otherwise
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_NO_USER_ACTION);
			startActivity(i);
		}
	}

	void screenOff() {
		Log.d(Main.LOGTAG, "screen off");
	}

}