package com.andevindo.pemantauanjadwalimunisasibalita.Helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by heendher on 10/19/2016.
 */

public class PreferencesManager {

    private static String sName = "com.andevindo.pemantauanjadwalimunisasibalita.Helper.Preferences";

    public static void setBabyId(Context context, int id){
        SharedPreferences.Editor editor = context.getSharedPreferences(sName, Context.MODE_PRIVATE).edit();
        editor.putInt("babyId", id);
        editor.apply();
    }

    public static int getBabyId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(sName, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("babyId", 1);
    }

}
