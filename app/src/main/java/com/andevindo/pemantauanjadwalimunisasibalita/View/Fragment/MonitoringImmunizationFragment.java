package com.andevindo.pemantauanjadwalimunisasibalita.View.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.andevindo.pemantauanjadwalimunisasibalita.Helper.PreferencesManager;
import com.andevindo.pemantauanjadwalimunisasibalita.Helper.TimeConverter;
import com.andevindo.pemantauanjadwalimunisasibalita.Model.Baby;
import com.andevindo.pemantauanjadwalimunisasibalita.Model.Schedule;
import com.andevindo.pemantauanjadwalimunisasibalita.R;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class MonitoringImmunizationFragment extends Fragment {

    private CheckBox mVa1, mVa2, mVa3, mVa4, mVa5, mVa6, mVa7, mVa8, mVa9, mVa10, mVa11, mVa12, mVa13, mVa14;
    private Button mAnalyze;
    private TextView mResult;
    private boolean[] mVaccineIds;
    private SparseBooleanArray mVaccineNeeded;

    public MonitoringImmunizationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monitoring_immunization2, container, false);

        Realm.init(getContext());

        mVa1 = (CheckBox) view.findViewById(R.id.heb);
        mVa8 = (CheckBox) view.findViewById(R.id.inf);
        mVa2 = (CheckBox) view.findViewById(R.id.po);
        mVa9 = (CheckBox) view.findViewById(R.id.cam);
        mVa3 = (CheckBox) view.findViewById(R.id.bcg);
        mVa10 = (CheckBox) view.findViewById(R.id.mmr);
        mVa4 = (CheckBox) view.findViewById(R.id.dpt);
        mVa11 = (CheckBox) view.findViewById(R.id.tif);
        mVa5 = (CheckBox) view.findViewById(R.id.hib);
        mVa12 = (CheckBox) view.findViewById(R.id.hea);
        mVa6 = (CheckBox) view.findViewById(R.id.pcv);
        mVa13 = (CheckBox) view.findViewById(R.id.var);
        mVa7 = (CheckBox) view.findViewById(R.id.rot);
        mVa14 = (CheckBox) view.findViewById(R.id.hpv);
        mAnalyze = (Button) view.findViewById(R.id.analyze);
        mAnalyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holdUserInput();
                analyze();
                Log.d("VaccineLength", mVaccineIds.length + "");
            }
        });
        mResult = (TextView) view.findViewById(R.id.result);

        return view;
    }

    void holdUserInput() {
        mVaccineIds = new boolean[14];
        mVaccineIds[0] = mVa1.isChecked();
        mVaccineIds[1] = mVa2.isChecked();
        mVaccineIds[2] = mVa3.isChecked();
        mVaccineIds[3] = mVa4.isChecked();
        mVaccineIds[4] = mVa5.isChecked();
        mVaccineIds[5] = mVa6.isChecked();
        mVaccineIds[6] = mVa7.isChecked();
        mVaccineIds[7] = mVa8.isChecked();
        mVaccineIds[8] = mVa9.isChecked();
        mVaccineIds[9] = mVa10.isChecked();
        mVaccineIds[10] = mVa11.isChecked();
        mVaccineIds[11] = mVa12.isChecked();
        mVaccineIds[12] = mVa13.isChecked();
        mVaccineIds[13] = mVa14.isChecked();

    }

    void analyze() {
        boolean isOk = true;
        Calendar calendar = Calendar.getInstance();
        //calendar.setTimeInMillis();
        Date timeNow = new Date();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Baby> babies = realm.where(Baby.class).equalTo("id", PreferencesManager.getBabyId(getContext())).findAll();
        Baby baby = babies.first();
        Date babyBirthDate = TimeConverter.fromLocalStringToDate(baby.getDateOfBirth());
        if (timeNow.getTime() < babyBirthDate.getTime()) {
            mResult.setText("Balita Anda belum dilahirkan");
        } else {
            RealmResults<Schedule> schedules = realm.where(Schedule.class).findAll();
            mVaccineNeeded = new SparseBooleanArray(schedules.size());
            for (int j = 0; j < schedules.size(); j++) {
                Schedule schedule = schedules.get(j);
                Date vaccineTime = TimeConverter.getVaccineDate(baby.getDateOfBirth(), schedule.getTime(), schedule.getTimeFormat());
                if (timeNow.getTime() - vaccineTime.getTime() > 0) {
                    mVaccineNeeded.put(schedule.getVaccineId(), true);


                } else {
                    if (!mVaccineNeeded.get(schedule.getVaccineId()))
                        mVaccineNeeded.put(schedule.getVaccineId(), false);
                }
            }
            for (int i = 0; i < mVaccineIds.length; i++) {

                Log.d("VaksinChecked", (i + 1) + " " + mVaccineIds[i]);

                if (mVaccineIds[i]) {

                    if (!mVaccineNeeded.get(i + 1)) {
                        isOk = false;
                        break;
                    }

                } else {

                    if (mVaccineNeeded.get(i + 1)) {
                        isOk = false;
                        break;
                    }

                }

            }

            if (isOk)
                mResult.setText("Balita Anda sudah melakukan vaksinasi dengan benar");
            else
                mResult.setText("Balita Anda tidak melakukan vaksinasi dengan benar, segera konsultasikan ke dokter anak");
        }

    }

}
