package com.andevindo.pemantauanjadwalimunisasibalita.View.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.andevindo.pemantauanjadwalimunisasibalita.Helper.PreferencesManager;
import com.andevindo.pemantauanjadwalimunisasibalita.Model.Baby;
import com.andevindo.pemantauanjadwalimunisasibalita.R;
import com.andevindo.pemantauanjadwalimunisasibalita.View.Fragment.ArrangeScheduleFragment;
import com.andevindo.pemantauanjadwalimunisasibalita.View.Fragment.BabyInfoFragment;
import com.andevindo.pemantauanjadwalimunisasibalita.View.Fragment.ChooseAddBabyFragment;
import com.andevindo.pemantauanjadwalimunisasibalita.View.Fragment.MonitoringImmunizationFragment;
import com.andevindo.pemantauanjadwalimunisasibalita.View.Fragment.VaccineInfoFragment;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButton mFab;
    private View mHeaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try{
            String vaccineDescription = getIntent().getExtras().getString("VaccineDescription");
            showVaccineDescription(vaccineDescription);
        }catch (NullPointerException e){

        }

        mFab = (FloatingActionButton)findViewById(R.id.fab);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_monitoring);
        navigationView.getMenu().performIdentifierAction(R.id.nav_monitoring, 0);


        mHeaderView = navigationView.getHeaderView(0);

        updateIdentity();

    }

    void showVaccineDescription(String description){
        new MaterialDialog.Builder(this)
                .title("Ayo Imunisasi")
                .content(description)
                .negativeText("Tutup")
                .build()
                .show();
    }

    public void updateIdentity(){
        CircleImageView photo = (CircleImageView)mHeaderView.findViewById(R.id.photo);
        TextView babyName = (TextView)mHeaderView.findViewById(R.id.baby_name);

        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Baby> realmResults = realm.where(Baby.class).equalTo("id", PreferencesManager.getBabyId(this)).findAll();
        if (realmResults.size()>0){
            Baby baby = realmResults.first();
            Log.d("Photo", baby.getPhoto());
            File file = new File(baby.getPhoto());
            Picasso.with(this).load(file).fit().into(photo);
            babyName.setText(baby.getBabyName());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;

        if (id == R.id.nav_monitoring) {
            mFab.setVisibility(FloatingActionButton.GONE);
            fragment = new MonitoringImmunizationFragment();
            setTitle("Pemantauan Imunisasi");
        } else if (id == R.id.nav_arrange_schedule) {
            mFab.setVisibility(FloatingActionButton.VISIBLE);
            mFab.setImageResource(R.drawable.ic_arrange_schedule_plus);
            fragment = new ArrangeScheduleFragment();
            setTitle("Atur Jadwal");
        }else if (id == R.id.nav_vaccine) {
            mFab.setVisibility(FloatingActionButton.GONE);
            fragment = new VaccineInfoFragment();
            setTitle("Info Vaksin");
        } else if (id == R.id.nav_baby_info) {
            mFab.setVisibility(FloatingActionButton.GONE);
            fragment = new BabyInfoFragment();
            setTitle("Info Balita");
        } else if (id == R.id.nav_choose_baby){
            mFab.setVisibility(FloatingActionButton.VISIBLE);
            mFab.setImageResource(R.drawable.ic_baby_plus);
            fragment = new ChooseAddBabyFragment();
            setTitle("Pilih atau Tambah Balita");
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
