package com.receptix.batterybuddy;

import android.Manifest;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SOME_FEATURES_PERMISSIONS = 101;
    private static final String TAG = "BatteryBuddy";
    static WindowManager.LayoutParams params;
    static WindowManager wm;
    static ViewGroup lockScreen;
    NotificationCompat.Builder myBuilder;
    Intent intent;
    PendingIntent pintent;
    NotificationManager nmanager;
    KeyguardManager keyguard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();

        try {

            boolean isScreenOn = bundle.getBoolean("isScreenOn");
            Log.d(TAG, String.valueOf(isScreenOn));
            keyguard = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
            boolean isLockedScreen = keyguard.inKeyguardRestrictedInputMode();
            Log.d(TAG,"isLockedScren: "+isLockedScreen);



            if (!isLockedScreen) {

                String from = bundle.getString("from");
                if (from.equalsIgnoreCase("broadcast")) {
                    startActivity(new Intent(getApplicationContext(), BatteryAdActivity.class));
                    finish();

                }
            } else {
                String from = bundle.getString("from");
                if (from.equalsIgnoreCase("broadcast")) {
                    startActivity(new Intent(getApplicationContext(), LockAdsActivity.class));
                    finish();

                }

            }


        } catch (Exception ex) {

        }

        getPermission();
        sendNotification();

    }

    private void sendNotification() {

        intent = new Intent(getApplicationContext(), MainActivity.class);
        pintent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        nmanager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        myBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle("This is the content")
                .setContentText("This is the description of the notification")
                .setContentIntent(pintent);

        nmanager.notify(1, myBuilder.build());
    }

    private void getPermission() {
        String[] PERMISSIONS = {Manifest.permission.BATTERY_STATS, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.REORDER_TASKS};
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_CODE_SOME_FEATURES_PERMISSIONS);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}
