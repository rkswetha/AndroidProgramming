package heartbeatmonitor.project.edu.heartbeatmonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;


public class AlarmReceiver extends BroadcastReceiver {

    private Context mContext;

    public AlarmReceiver() {

        Log.i("AlarmReceiver", "Constructor called: ");

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("AlarmReceiver", "onReceive called: function start");
        mContext = context;
        Vibrator vibrator = null;

        Toast.makeText(context, "Don't panik but your time is up!!!!.",
                Toast.LENGTH_LONG).show();
        try {
            // Vibrate the mobile phone
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }catch (Exception e) {
            Log.e("AlarmReceiver", "onReceive called: - Vibration creation error");
        }

        if (vibrator != null) {
            try {
                vibrator.vibrate(2000);
                Log.i("AlarmReceiver", "onReceive called: - Start Vibration for 2000sec");

            } catch (Exception e) {}
        }

        Log.i("AlarmReceiver", "onReceive called: - Vibration complete");

        /*Intent service = new Intent(context, AlarmService.class);
        service.putExtra("ACTION", "PLAY_MUSIC");
        context.startService(service); */

        Intent intent1 = new Intent(context, SpotifyActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);

    }
}
