package io.dourl.mqtt.core;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import io.dourl.mqtt.base.MqttBaseApp;
import io.dourl.mqtt.ui.BaseActivity;

/**
 * Created by dourl on 2018/2/2.
 */

class NotificationHelper {

    static final String ID = "mqtt";
    private static final String CHANNEL_DEFAULT = "mqtt";
    static final int REQUEST_CODE_TESTACTIVITY = 100;
    static void showNoti(ActionListener.Action action, boolean state, String message) {
        //if (BuildConfig.RELEASE) return;
        Intent intent = new Intent(MqttBaseApp.getApp(), BaseActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MqttBaseApp.getApp(), REQUEST_CODE_TESTACTIVITY, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager manager = (NotificationManager) MqttBaseApp.getApp()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder notificationBuilder;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_DEFAULT,CHANNEL_DEFAULT,NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
            notificationBuilder = new NotificationCompat.Builder(MqttBaseApp.getApp(),CHANNEL_DEFAULT);
        }else {
            notificationBuilder = new NotificationCompat.Builder(MqttBaseApp.getApp(), (Notification) null);
        }

        notificationBuilder.setAutoCancel(false);
        notificationBuilder.setSmallIcon(state?android.R.drawable.ic_dialog_info:android.R.drawable.ic_delete);
        notificationBuilder.setContentTitle("Mqtt连接状态");
        String text = null;
        if (state) {
            text = action.toString() + " SUCCESS";
        } else {
            text = action.toString() + " FAIL";
        }
        notificationBuilder.setContentText(message == null ? text : message);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setWhen(System.currentTimeMillis());
        notificationBuilder.setSound(null);
//        notificationBuilder.setPriority(Notification.PRIORITY_DEFAULT);
        Notification notification = notificationBuilder.build();
        if (manager != null) {
            manager.notify(999, notification);
        }
    }

    static void cancelNoti() {
        NotificationManager manager = (NotificationManager) MqttBaseApp.getApp()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.cancel(999);
        }
    }



}
