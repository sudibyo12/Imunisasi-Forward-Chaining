package com.andevindo.pemantauanjadwalimunisasibalita.Receiver;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.andevindo.pemantauanjadwalimunisasibalita.Service.NotificationService;

/**
 * Created by heendher on 10/23/2016.
 */

public class AlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReceiver", "WOkke");
        ComponentName componentName = new ComponentName(context.getPackageName(), NotificationService.class.getName());
        startWakefulService(context, (intent.setComponent(componentName)));
        setResultCode(Activity.RESULT_OK);
    }
}
