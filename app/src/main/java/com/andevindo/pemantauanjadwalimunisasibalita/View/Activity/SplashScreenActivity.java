package com.andevindo.pemantauanjadwalimunisasibalita.View.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.andevindo.pemantauanjadwalimunisasibalita.Model.Baby;
import com.andevindo.pemantauanjadwalimunisasibalita.R;

import java.security.Permission;

import io.realm.Realm;
import io.realm.RealmResults;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Realm.init(SplashScreenActivity.this);
                Realm realm = Realm.getDefaultInstance();
                RealmResults<Baby> realmResults = realm.where(Baby.class).findAll();
                if (realmResults.size()==0){
                    Intent intent = new Intent(SplashScreenActivity.this, AddBabyActivity.class);
                    intent.putExtra("isBeginning", true);
                    startActivity(intent);
                }else{
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                }
                finish();
            }
        }, 3000);

    }

}
