package com.app.dusmile.jobreminder;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.app.dusmile.DBModel.AssignedJobs;
import com.app.dusmile.DBModel.AssignedJobsStatus;
import com.app.dusmile.R;
import com.app.dusmile.activity.JobsActivity;
import com.app.dusmile.activity.LoginActivity;
import com.app.dusmile.activity.SplashActivity;
import com.app.dusmile.constant.AppConstant;
import com.app.dusmile.database.AssignedJobsDB;
import com.app.dusmile.database.AssignedJobsStatusDB;
import com.app.dusmile.database.helper.DBHelper;
import com.app.dusmile.preferences.RecordUser;
import com.app.dusmile.preferences.UserPreference;
import com.app.dusmile.utils.IOUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    private DBHelper dbHelper;
    Date currentDate, jobEndDate, formUpdateDate;
    UserPreference userPreference;
    @Override
    public void onReceive(Context context, Intent intent) {

        // For our recurring task, we'll just display a message
        //Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
        checkEndTime(context);
    }

    private void checkEndTime(Context context) {
        try {
            dbHelper = DBHelper.getInstance(context);
            currentDate = IOUtils.getCurrentDate();
            String jobEndTime = "";
            AssignedJobs assignedJobs = AssignedJobsDB.getJobsByProgress(dbHelper, "true");


            if (assignedJobs.getAssigned_jobId() != null) {
                jobEndTime = assignedJobs.getJob_end_time();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                AssignedJobsStatus assignedJobsStatus = AssignedJobsStatusDB.getSingleAssignedJobsStatus(dbHelper, assignedJobs.getAssigned_jobId());

                try {
                    if(jobEndTime!=null) {
                        jobEndDate = df.parse(jobEndTime);
                        if (assignedJobsStatus.getJob_id() != null) {
                            formUpdateDate = df.parse(assignedJobsStatus.getForm_data_update_time());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (currentDate != null && jobEndDate != null) {
                String min = IOUtils.getDateHourDifference(currentDate, jobEndDate);
                if (min != null) {
                    int endHour = Integer.parseInt(min);

                    if (endHour > 0) {
                        sendNotification(context, "" + min + " remaining for completing job " + assignedJobs.getAssigned_jobId());

                    } else {
                        if (formUpdateDate != null) {
                            String minDiff = IOUtils.getDateHourDifference(formUpdateDate, jobEndDate);
                            int endJobHour = Integer.parseInt(minDiff);
                            if (endJobHour >= 30) {

                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void sendNotification(Context context, String messageBody) {
        AppConstant.isAvilableJobs = false;
        AppConstant.isAssinedJobs = true;
        AppConstant.isCompletedJobs = false;
        RecordUser recordUser = UserPreference.getUserRecord(context);
        String userName = recordUser.getUsername();
        Intent intent = null;
        if(!TextUtils.isEmpty(userName))
        {
            intent = new Intent(context, JobsActivity.class);
            intent.putExtra("isAppExit", false);
        }
        else
        {
            intent = new Intent(context,SplashActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(context.getString(R.string.job_reminder))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, notificationBuilder.build());
    }

}