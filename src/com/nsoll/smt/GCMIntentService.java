package com.nsoll.smt;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String tag = "GCMIntentService";
    private static final String PROJECT_ID = "1003709731095";
    
	public GCMIntentService() {
		this(PROJECT_ID);
	}

	public GCMIntentService(String... senderIds) {
		super(senderIds);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onError(Context context, String errorId) {
		Log.d(tag, "onError.errorId:" + errorId);

	}

	@Override
	protected void onMessage(Context context, Intent intent) {

		/*
		Bundle b = intent.getExtras();

		Handler h = new Handler(Looper.getMainLooper());
        Iterator<String> iterator = b.keySet().iterator();
        while(iterator.hasNext()) {
            String key = iterator.next();
            final String value = b.get(key).toString();
            Log.d(tag, "onMessage. "+key+" : "+value);
            Log.d(tag, "onMessage. "+key+" : "+value);
            if (key.toString() == "smt"){
            	Log.d(tag, "onMessage. "+key+" : "+value);
            	h.post(new Runnable(){
	            	public void run() {
	            		Toast.makeText(GCMIntentService.this, value, 1000).show();
	            	}
	
	            });
            }
        }
     	*/
		
		if (BuildConfig.DEBUG)
			Log.d(tag, "GCMReceiver Message");
		try {
			String title = intent.getStringExtra("title");
			String message = intent.getStringExtra("msg");
			Vibrator vibrator = 
			 (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(1000);
			setNotification(context, title, message);
			if (BuildConfig.DEBUG)
				Log.d(tag, title + ":" + message);
		} catch (Exception e) {
			Log.e(tag, "[onMessage] Exception : " + e.getMessage());
		}
		
	}
	
	private void setNotification(Context context, String title, String message) {
		NotificationManager notificationManager = null;
		Notification notification = null;
		try {
			notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			notification = new Notification(R.drawable.ic_launcher,
					message, System.currentTimeMillis());
			Intent intent = new Intent(context, MainActivity.class);
			PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
			notification.setLatestEventInfo(context, title, message, pi);
			notificationManager.notify(0, notification);
		} catch (Exception e) {
			Log.e(tag, "[setNotification] Exception : " + e.getMessage());
		}
	}
	
	@Override
	protected void onRegistered(Context context, String regId) {
		Log.d(tag, "onRegistered.regId:" + regId);
	}

	@Override
	protected void onUnregistered(Context context, String regId) {
		Log.d(tag, "onUnregistered.regId:" + regId);
	}
}
