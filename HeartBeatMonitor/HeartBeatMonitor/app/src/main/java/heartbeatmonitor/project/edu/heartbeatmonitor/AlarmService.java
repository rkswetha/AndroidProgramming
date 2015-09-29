package heartbeatmonitor.project.edu.heartbeatmonitor;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class AlarmService extends Service
 {
    IBinder mBinder;

    public AlarmService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The service is starting, due to a call to startService()
        Log.d("AlarmService", "onStartCommand");

        if ( (null != intent) && intent.getExtras().containsKey("ACTION")
                && "PLAY_MUSIC".equals(intent.getExtras().getString("ACTION")) )
        {
            playSpotifyMusic();
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // A client is binding to the service with bindService()
        return null;
    }

    private void playSpotifyMusic()
    {
        Log.d("AlarmService", "playSpotifyMusic");
/*
        NotificationManager notificationManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(this.getApplicationContext(),SpotifyActivity.class);
        //notificationIntent.putExtra("message", val);
        //notificationIntent.putExtra("reminder", true);

        Notification notification = new Notification();
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingNotificationIntent = PendingIntent.getActivity( this.getApplicationContext(),0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.setLatestEventInfo(this.getApplicationContext(), "Reminder", val, pendingNotificationIntent);

        notificationManager.notify(0, notification);
*/

    }

     @Override
     public void onDestroy()
     {
         // TODO Auto-generated method stub
         super.onDestroy();
     }
}
