package com.andevindo.pemantauanjadwalimunisasibalita.Service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.andevindo.pemantauanjadwalimunisasibalita.Helper.PreferencesManager;
import com.andevindo.pemantauanjadwalimunisasibalita.R;
import com.andevindo.pemantauanjadwalimunisasibalita.View.Activity.MainActivity;

/**
 * Created by heendher on 10/23/2016.
 */

public class NotificationService extends IntentService {

    private int mId = 0;

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        sendNotification(intent.getExtras().getString("BabyName"), intent.getExtras().getString("VaccineName"), intent.getExtras().getString("VaccineDescription"));
    }

    void sendNotification(String babyName, String vaccineName, String vaccineDescription){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("VaccineDescription", vaccineDescription);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(MainActivity.class);
        taskStackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Ayo imunisasi");
        builder.setContentText(babyName + " saatnya vaksinasi " + vaccineName);
        builder.setAutoCancel(true);
        builder.setSound(defaultSoundUri);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(mId++, builder.build());

    }

}
