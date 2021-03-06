package me.nuwan.seofficial.Fireabse;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import me.nuwan.seofficial.UI.LoaderActivity;
import me.nuwan.seofficial.UI.LoginActivity;
import me.nuwan.seofficial.R;

public class FirebaseMsgService extends FirebaseMessagingService {
    private static final String TAG = "FCMService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        createNotification(remoteMessage.getNotification().getBody());
    }

    private void createNotification( String messageBody) {
        Intent intent = new Intent( this , LoaderActivity. class );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultIntent = PendingIntent.getActivity( this , 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder( this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("SE Official")
                .setContentText(messageBody)
                .setAutoCancel( true )
                .setSmallIcon(R.mipmap.se_logo)
                .setBadgeIconType(R.mipmap.se_logo)
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(notificationManager != null)
            notificationManager.notify(0, mNotificationBuilder.build());
    }

}