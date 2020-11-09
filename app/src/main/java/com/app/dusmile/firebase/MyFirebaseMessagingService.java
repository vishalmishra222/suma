package com.app.dusmile.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.app.dusmile.R;
import com.app.dusmile.activity.JobsActivity;
import com.app.dusmile.constant.AppConstant;
import com.app.dusmile.preferences.RecordUser;
import com.app.dusmile.preferences.UserPreference;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by admin on 11/25/16.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private NotificationManager mNotificationManager;
    public static final int NOTIFICATION_ID = 1;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        if(remoteMessage!=null) {
            Map<String,String> notificationData = remoteMessage.getData();
            sendNotification(remoteMessage.getNotification().getTitle());
            Log.e("data",remoteMessage.getData().toString());
            try
            {
                Map<String, String> params = remoteMessage.getData();
                JSONObject object = new JSONObject(params);
                Log.e("JSON_OBJECT", object.toString());

                String message = object.getString("message");

                RecordUser recordUser = UserPreference.getUserRecord(this);
                if (recordUser != null && !TextUtils.isEmpty(recordUser.getUserID())) {
                    sendNotification(message);
                }
            }catch (Exception e){

            }
           /* Log.e(TAG, "From: " + remoteMessage.getFrom());
            Log.e(TAG, "Notification Message Body: " + notificationData.get(0));
*/
            /*RecordUser recordUser = UserPreference.getUserRecord(this);
            if (recordUser != null && !TextUtils.isEmpty(recordUser.getUserID())) {
                sendNotification(remoteMessage.getNotification().getBody());
            }*/
        }

    }

    private void sendNotification(String msg) {

        mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        AppConstant.isAvilableJobs = false;
        AppConstant.isAssinedJobs = true;
        AppConstant.isCompletedJobs = false;
        Intent intent = new Intent(this, JobsActivity.class);
        intent.putExtra("isAppExit", false);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(
                this)//.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_logo))
                .setSmallIcon(R.drawable.sumaicon)
                .setContentTitle(msg)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.job_reminder)))
                .setAutoCancel(true)
                .setColor(getResources().getColor(R.color.white))
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentText(msg);

        mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        mBuilder.setVibrate(new long[] { 1000, 1000});
        mBuilder.setContentIntent(pendingIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }



}
