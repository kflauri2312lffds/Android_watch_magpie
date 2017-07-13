package hevs.aislab.magpie.watch.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import hevs.aislab.magpie.watch.NotificationActivity;
import hevs.aislab.magpie.watch.R;
import hevs.aislab.magpie.watch.shared_pref.PrefAccessor;

/**
 * Used to create notification to the screen and play a song
 */

public class NotificationGenerator implements Runnable {

    Context context;
    String title;
    String content;

    @Override
    public void run() {
        generateNotification();
        if (PrefAccessor.getInstance().getBoolean(context,"hasSpeacker"))
            playSong();
    }

    public NotificationGenerator(Context context, String title, String content)
    {
        this.context=context;
        this.title=title;
        this.content=content;

    }


    public void generateNotification()
    {
        int notificationId = 001;

        // Intent viewIntent=new Intent(this)
        Intent notificationIntent=new Intent(context,NotificationActivity.class);
        notificationIntent.putExtra("", "");
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);

        long[]notificationPattern={10,5,10,5};

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.notification_warning)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setContentIntent(viewPendingIntent)
                        .setDefaults(Notification.DEFAULT_ALL)
                ;

        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);

        // Issue the notification with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());
    }
    private void playSong()
    {
        MediaPlayer mediaPlayer=MediaPlayer.create(context,R.raw.warning_song);
        mediaPlayer.start();
    }
}
