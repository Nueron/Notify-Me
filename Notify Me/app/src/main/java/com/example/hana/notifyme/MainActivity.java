package com.example.hana.notifyme;

import android.app.AlarmManager;
import android.app.Fragment;

import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements Timer.MyCallback{
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri fileUri;
    ImageView imageCapture;
    EditText inputText;
    MyDBHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHandler = new MyDBHandler(this, null, null,1);
        imageCapture =(ImageView)this.findViewById(R.id.imageView2);
        inputText = (EditText) this.findViewById(R.id.editText);
        imageCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCamera(v);
            }
        });
    }

    public void setAlarm(long miliseconds){
        Toast.makeText(this,"from main activity",Toast.LENGTH_LONG).show();
        AlarmManager alarmMgr =
                (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        long wakeTime = SystemClock.elapsedRealtime() + miliseconds;
        Intent alarmIntent = new Intent(this, MyBootReceiver.class);
        //Create the Alarm object to store in DB
        Alarms alarm = new Alarms();
        //Get the alarm id by concatenating Date and time
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        Integer alarmID = (Integer)Integer.valueOf(timeStamp);
        alarm.set_alarmID(alarmID);
        alarm.set_image(fileUri.toString());
        alarm.set_inputString(inputText.getText().toString());
        //Alarm Object created with values, now insert in DB
        dbHandler.addAlarm(alarm);

        // now put the alarmID in the alarm intent

        alarmIntent.putExtra("ALARM_ID", alarmID);

        PendingIntent pend =
                PendingIntent.getActivity(this,
                        alarmID,
                        alarmIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, wakeTime, pend);
    }

public void callTimerDialog(View view){


    // Create and show the dialog.
    FragmentManager fm = getSupportFragmentManager();
    // Create and show the dialog.
    DialogFragment newFragment = new Timer();
    newFragment.show(fm, "Timer");

}

    public void cancelTimer()
    {
        DialogFragment dialogFragment;
        dialogFragment = (DialogFragment)getSupportFragmentManager().findFragmentByTag("Timer");
        if (dialogFragment != null)
        {
            dialogFragment.dismiss();
        }
    }

    private void callCamera(View v)
    {
        if(checkCameraHardware(v.getContext())) {
            // create Intent to take a picture and return control to the calling application
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            fileUri = getOutputMediaFileUri(); // create a file to save the image
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

            // start the image capture Intent
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }
    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(){
        return Uri.fromFile(getOutputMediaFile());
    }
    /** Create a File for saving an image or video */
    private static File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "NotifyMe");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("NotifyMe", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        return  mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Image saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
                imageCapture =(ImageView)this.findViewById(R.id.imageView2);
                imageCapture.setImageURI(fileUri);
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }
    }
}


