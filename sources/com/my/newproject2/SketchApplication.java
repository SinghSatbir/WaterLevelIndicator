package com.my.newproject2;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Process;
import android.support.v4.app.NotificationCompat;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

public class SketchApplication extends Application {
    private UncaughtExceptionHandler uncaughtExceptionHandler;

    public void onCreate() {
        this.uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            public void uncaughtException(Thread thread, Throwable th) {
                Intent intent = new Intent(SketchApplication.this.getApplicationContext(), DebugActivity.class);
                intent.setFlags(32768);
                intent.putExtra("error", SketchApplication.this.getStackTrace(th));
                ((AlarmManager) SketchApplication.this.getSystemService(NotificationCompat.CATEGORY_ALARM)).set(2, 1000, PendingIntent.getActivity(SketchApplication.this.getApplicationContext(), 11111, intent, 1073741824));
                Process.killProcess(Process.myPid());
                System.exit(2);
                SketchApplication.this.uncaughtExceptionHandler.uncaughtException(thread, th);
            }
        });
        super.onCreate();
    }

    private String getStackTrace(Throwable th) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        while (th != null) {
            th.printStackTrace(printWriter);
            th = th.getCause();
        }
        String obj = stringWriter.toString();
        printWriter.close();
        return obj;
    }
}
