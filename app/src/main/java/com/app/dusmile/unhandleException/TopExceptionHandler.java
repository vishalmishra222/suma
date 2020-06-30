package com.app.dusmile.unhandleException;

import android.content.Intent;

import com.app.dusmile.application.DusmileApplication;
import com.app.dusmile.service.UploadLogFileHttpService;
import com.app.dusmile.utils.IOUtils;

public class TopExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler defaultUEH;

    public TopExceptionHandler() {
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }

    public void uncaughtException(Thread t, Throwable e) {
        StackTraceElement[] arr = e.getStackTrace();
        StringBuilder report = new StringBuilder();
        report.append(e.toString()+"\n\n");
        report.append("--------- Crash ---------\n\n");
        for (int i=0; i<arr.length; i++) {
            report.append("    "+arr[i].toString()+"\n");
        }
        report.append( "-------------------------------\n\n");

        // If the exception was thrown in a background thread inside
        // AsyncTask, then the actual exception can be found with getCause

        /*report.append("--------- Cause ---------\n\n");
        Throwable cause = e.getCause();
        if(cause != null) {
            report.append(cause.toString() + "\n\n");
            arr = cause.getStackTrace();
            for (int i=0; i<arr.length; i++) {
                report.append("    "+arr[i].toString()+"\n");
            }
        }
        report.append( "-------------------------------\n\n");*/

        try {
           /* FileOutputStream trace = app.openFileOutput("stack.trace",
                    Context.MODE_PRIVATE);
            trace.write(report.toString().getBytes());
            trace.close();*/
            IOUtils.appendLog(report.toString());
        } catch(Exception ioe) {
            // ...
        }
        Intent serviceIntent = new Intent(DusmileApplication.getAppContext(), UploadLogFileHttpService.class);
        DusmileApplication.getAppContext().startService(serviceIntent);
        defaultUEH.uncaughtException(t, e);
    }
}