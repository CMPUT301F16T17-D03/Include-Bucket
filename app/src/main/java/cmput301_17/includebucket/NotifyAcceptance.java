package cmput301_17.includebucket;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

/**
 * Driver is notified if acceptance is accepted
 */
public class NotifyAcceptance extends Service {

    /**
     * https://developer.android.com/guide/topics/ui/notifiers/notifications.html
     */
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public NotifyAcceptance()

    {
        NotificationManager notificationmanager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notification = new Intent(this, RiderCurrentRequestsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notification, 0);


        Notification mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(0)
                .setContentTitle("New Notifications")
                .setContentText("Your Acceptance Has Been Accepted")
                .setContentIntent(pendingIntent)
                .build();

        notificationmanager.notify(002, mBuilder);
    }

}
