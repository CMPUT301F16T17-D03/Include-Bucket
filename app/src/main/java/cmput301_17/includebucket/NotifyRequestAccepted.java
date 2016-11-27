package cmput301_17.includebucket;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

//rider is notified if their request is accepted

//http://stackoverflow.com/questions/19018655/notification-builder-cant-accept-this-as-replacement-for-context-why
//https://developer.android.com/guide/topics/ui/notifiers/notifications.html

public class NotifyRequestAccepted extends Service {

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public NotifyRequestAccepted()

    {
        NotificationManager notificationmanager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notification = new Intent(this, RiderCurrentRequestsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notification, 0);


        Notification mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(0)
                .setContentTitle("New Notifications")
                .setContentText("Your Request Has Been Accepted")
                .setContentIntent(pendingIntent)
                .build();

        notificationmanager.notify(001, mBuilder);
    }

}
