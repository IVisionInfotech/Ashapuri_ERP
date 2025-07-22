package com.madhavsteel.firebaseUtil;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.madhavsteel.R;
import com.madhavsteel.activity.HomeActivity;
import com.madhavsteel.activity.NotificationsActivity;
import com.madhavsteel.utils.Constant;
import com.madhavsteel.utils.Session;

public class FirebaseMessagingUtils extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessagingUtils";
    private Session session;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.

        Log.e(TAG, "From: " + remoteMessage.getData().get("title"));
        Log.e(TAG, "Notification Message Body: " + remoteMessage.getData().get("message"));

        Intent intent = new Intent(Constant.messageEvent);
        intent.putExtra("messageTitle", remoteMessage.getData().get("title"));
        intent.putExtra("messageDetails", remoteMessage.getData().get("message"));
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        session = new Session(getApplicationContext());
        if (session.getLoginStatus()) {
            Constant.notificationArray = null;
            sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));
        }
    }

    @SuppressLint("WrongConstant")
    private void sendNotification(String title, String messageBody) {
        String name = getString(R.string.default_notification_channel_id);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        Intent intent;
        if (session.getLoginStatus()) {
            intent = new Intent(this, NotificationsActivity.class);
        } else {
            intent = new Intent(this, HomeActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder mBuilder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.ic_launcher_small)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setLights(Color.WHITE, 3000, 3000)
                    .setChannelId(name)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setStyle(new Notification.BigTextStyle().bigText(messageBody))
                    .setSound(defaultSoundUri)
                    .setShowWhen(true)
                    .setContentIntent(pendingIntent);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                mBuilder = new Notification.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher_small)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setLights(Color.WHITE, 3000, 3000)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setStyle(new Notification.BigTextStyle().bigText(messageBody))
                        .setSound(defaultSoundUri)
                        .setShowWhen(true)
                        .setContentIntent(pendingIntent);
            } else {
                mBuilder = new Notification.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher_small)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setLights(Color.WHITE, 3000, 3000)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setStyle(new Notification.BigTextStyle().bigText(messageBody))
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
            }
        }

        int mNotificationId = (int) System.currentTimeMillis();
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(name, name, importance);
            mNotifyMgr.createNotificationChannel(mChannel);
        }

        if (mNotifyMgr == null)
            mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}
