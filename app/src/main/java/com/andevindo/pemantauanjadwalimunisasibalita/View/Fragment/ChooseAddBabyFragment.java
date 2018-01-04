package com.andevindo.pemantauanjadwalimunisasibalita.View.Fragment;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.andevindo.pemantauanjadwalimunisasibalita.Adapter.BabyListAdapter;
import com.andevindo.pemantauanjadwalimunisasibalita.Adapter.BaseRealmAdapter;
import com.andevindo.pemantauanjadwalimunisasibalita.Helper.PreferencesManager;
import com.andevindo.pemantauanjadwalimunisasibalita.Helper.TimeConverter;
import com.andevindo.pemantauanjadwalimunisasibalita.Model.Baby;
import com.andevindo.pemantauanjadwalimunisasibalita.Model.Schedule;
import com.andevindo.pemantauanjadwalimunisasibalita.Model.Vaccine;
import com.andevindo.pemantauanjadwalimunisasibalita.R;
import com.andevindo.pemantauanjadwalimunisasibalita.Receiver.AlarmReceiver;
import com.andevindo.pemantauanjadwalimunisasibalita.View.Activity.AddBabyActivity;
import com.andevindo.pemantauanjadwalimunisasibalita.View.Activity.MainActivity;
import com.andevindo.pemantauanjadwalimunisasibalita.View.Custom.FabItemDecoration;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

import static android.content.Context.ALARM_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseAddBabyFragment extends Fragment implements BaseRealmAdapter.BaseRealmAdapterPresenter<Baby>{

    private RecyclerView mRecyclerView;
    private BabyListAdapter mAdapter;
    private FloatingActionButton mFab;

    public ChooseAddBabyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choose_add_baby, container, false);
        mFab = (FloatingActionButton)getActivity().findViewById(R.id.fab);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler);
        mAdapter = new BabyListAdapter(getContext());
        mAdapter.setBasePresenter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new FabItemDecoration(getContext()));
        Realm.init(getContext());
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Baby> realmResults = realm.where(Baby.class).findAll();
        if (realmResults.size()!=0){
            RealmList<Baby> realmList = new RealmList<>();
            realmList.addAll(realmResults.subList(0, realmResults.size()));
            mAdapter.setData(realmList);
        }
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddBabyActivity.class);
                intent.putExtra("isBeginning", false);
                startActivityForResult(intent, 1);
            }
        });
        return view;
    }

    void showChooseDialog(final Baby baby){
        if (baby.getId()!= PreferencesManager.getBabyId(getContext())){
            new MaterialDialog.Builder(getContext())
                    .title("Pilih balita")
                    .content("Anda akan memilih balita ini untuk dipantau?")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            cancelAlarm();
                            PreferencesManager.setBabyId(getContext(), baby.getId());
                            ((MainActivity)getActivity()).updateIdentity();
                            Realm realm = Realm.getDefaultInstance();
                            RealmResults<Baby> realmResults = realm.where(Baby.class).findAll();
                            if (realmResults.size()!=0){
                                RealmList<Baby> realmList = new RealmList<>();
                                realmList.addAll(realmResults.subList(0, realmResults.size()));
                                mAdapter.setData(realmList);
                            }
                            setAlarm();
                        }
                    })
                    .negativeText("Tutup")
                    .positiveText("Pilih")
                    .build()
                    .show();
        }

    }

    void cancelAlarm(){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Schedule> schedules = realm.where(Schedule.class).findAll();
        RealmResults<Baby> babies = realm.where(Baby.class).equalTo("id", PreferencesManager.getBabyId(getContext())).findAll();
        Baby baby = babies.first();
        Date babyBirthDate = TimeConverter.fromLocalStringToDate(baby.getDateOfBirth());
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager)getContext().getSystemService(ALARM_SERVICE);
        for (int i = 0; i < schedules.size(); i++) {
            Schedule schedule = schedules.get(i);
            Date vaccineTime = TimeConverter.getVaccineDate(baby.getDateOfBirth(), schedule.getTime(), schedule.getTimeFormat());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), schedule.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
        }
    }

    void setAlarm() {
        ComponentName receiver = new ComponentName(getContext(), AlarmReceiver.class);
        PackageManager pm = getContext().getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Schedule> schedules = realm.where(Schedule.class).findAll();
        RealmResults<Baby> babies = realm.where(Baby.class).equalTo("id", PreferencesManager.getBabyId(getContext())).findAll();
        Baby baby = babies.first();
        Date babyBirthDate = TimeConverter.fromLocalStringToDate(baby.getDateOfBirth());
        Intent intent = new Intent(getContext(), AlarmReceiver.class);

        AlarmManager alarmManager = (AlarmManager)getContext().getSystemService(ALARM_SERVICE);
        for (int i = 0; i < schedules.size(); i++) {
            Schedule schedule = schedules.get(i);
            intent.putExtra("VaccineName", schedule.getVaccineName());
            intent.putExtra("BabyName", baby.getBabyName());
            RealmResults<Vaccine> vaccines = realm.where(Vaccine.class).equalTo("id", schedule.getVaccineId()).findAll();
            intent.putExtra("VaccineDescription", vaccines.first().getDescription());
            Date vaccineTime = TimeConverter.getVaccineDate(baby.getDateOfBirth(), schedule.getTime(), schedule.getTimeFormat());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), schedule.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            if (schedule.isRepeated()) {
                if (schedule.getTimeFormat().equals("tahun")){
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(babyBirthDate);
                    calendar.add(Calendar.YEAR, schedule.getTime());
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, vaccineTime.getTime() + (vaccineTime.getTime()<System.currentTimeMillis() ? calendar.getTimeInMillis():0), calendar.getTimeInMillis(), pendingIntent);
                }

            } else {
                if (vaccineTime.getTime() - System.currentTimeMillis() > 0) {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, vaccineTime.getTime(), pendingIntent);

                }
                Log.d("Vaccine", schedule.getVaccineName() + "/" + babyBirthDate + ":" + vaccineTime);
            }
        }


    }


    @Override
    public void onClick(Baby baby) {
        showChooseDialog(baby);
    }

    @Override
    public void onLongClick(Baby baby) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1)
            if (resultCode==getActivity().RESULT_OK){
                Realm realm = Realm.getDefaultInstance();
                RealmResults<Baby> realmResults = realm.where(Baby.class).findAll();
                if (realmResults.size()!=0){
                    RealmList<Baby> realmList = new RealmList<>();
                    realmList.addAll(realmResults.subList(0, realmResults.size()));
                    mAdapter.setData(realmList);
                }
            }
    }
}
