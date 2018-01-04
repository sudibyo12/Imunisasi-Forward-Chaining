package com.andevindo.pemantauanjadwalimunisasibalita;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.andevindo.pemantauanjadwalimunisasibalita.Model.Baby;
import com.andevindo.pemantauanjadwalimunisasibalita.Model.Schedule;
import com.andevindo.pemantauanjadwalimunisasibalita.Model.Vaccine;

import java.io.IOException;
import java.io.InputStream;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;

/**
 * Created by -H- on 10/30/2015.
 */
public class Application extends android.app.Application {

    private static Application sApplication;

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    private static boolean activityVisible = false;


    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        Realm.init(this);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("com.andevindo.pemantauanjadwalimunisasibalita")
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(17)
                .initialData(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        try {
                            InputStream inputStream = getContext().getAssets().open("vaccine.json");
                            realm.createAllFromJson(Vaccine.class, inputStream);
                            InputStream inputStream1 = getContext().getAssets().open("schedule.json");
                            realm.createAllFromJson(Schedule.class, inputStream1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                })
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);
        //Realm.getInstance(realmConfiguration);
    }

    public static Application getInstance() {
        return sApplication;
    }

    public static Context getContext() {
        return sApplication.getApplicationContext();
    }
}
