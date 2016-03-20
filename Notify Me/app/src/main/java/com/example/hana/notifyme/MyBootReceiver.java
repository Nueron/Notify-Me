package com.example.hana.notifyme;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


//public class MyBootReceiver extends BroadcastReceiver {
public class MyBootReceiver extends Activity {
    MyDBHandler dbHandler;
    ImageView imageView;
    TextView textView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Vibrator vibrator;
        MediaPlayer mp;
        Toast toast = Toast.makeText(this, "Alarm clicked",Toast.LENGTH_LONG);
        toast.show();
        dbHandler = new MyDBHandler(this, null, null,1);
        Intent intent = this.getIntent();
        //Get the extra data set in the alarm Intent (In this case alarmID)
        String alarmIdString = intent.getStringExtra("ALARM_ID");
        Integer alarmId = Integer.valueOf(alarmIdString);

        //Get the data from the DB
        List<Alarms> alarms;
        Alarms alarm;
        alarms = dbHandler.readDatabase(alarmId);
        //Get the first object of list as in this case only one alarm
        alarm = alarms.get(0);

        //Set the data in the view from the above fetched alarm Object
        imageView = (ImageView) this.findViewById(R.id.imageView);
        textView = (TextView) this.findViewById(R.id.textView);
        imageView.setImageURI(Uri.parse(alarm.get_image()));
        textView.setText(alarm.get_inputString());

        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mp= MediaPlayer.create(this, alert);
        mp.setVolume(100, 100);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            @Override
            public void onCompletion(MediaPlayer mp){
                mp.release();
            }
        });

        vibrator = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
        vibrator.vibrate(400);

    }

    public void onReceive(Context context, Intent intent) {
        Vibrator vibrator;
        MediaPlayer mp;
        Toast toast = Toast.makeText(context, "Alarm clicked",Toast.LENGTH_LONG);
        toast.show();
        //Intent i = new Intent(context,MainActivity.class);
        //context.startActivity(i);
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mp= MediaPlayer.create(context, alert);
        mp.setVolume(100, 100);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            @Override
            public void onCompletion(MediaPlayer mp){
                mp.release();
            }
        });

        vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        vibrator.vibrate(400);

    }

}
