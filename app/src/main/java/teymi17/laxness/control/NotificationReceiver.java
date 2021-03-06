package teymi17.laxness.control;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import teymi17.laxness.MainActivity;
import teymi17.laxness.R;


/**
 * Created by Gunnar Marel on 26.2.2018.
 */

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){
        Intent notificationIntent = new Intent(context,MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);


        PendingIntent pendingIntent = stackBuilder.getPendingIntent(100,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"100");

     //   Bitmap logo = BitmapFactory.decodeResource(context.getResources(),
     //           R.drawable.ic_laxness);
        Notification notification = builder.setContentTitle("Tilvitnun dagsins")
                .setContentText("Þú hefur fengið nýja tilvitnun frá Halldór Laxness")
                .setTicker("Tilvitnun dagsins")
                //.setLargeIcon(logo)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager =(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(100,notification);
    }

}
