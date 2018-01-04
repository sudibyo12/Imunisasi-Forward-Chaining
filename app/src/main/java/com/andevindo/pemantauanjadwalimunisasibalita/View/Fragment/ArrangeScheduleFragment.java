package com.andevindo.pemantauanjadwalimunisasibalita.View.Fragment;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.andevindo.pemantauanjadwalimunisasibalita.Adapter.ArrangeScheduleAdapter;
import com.andevindo.pemantauanjadwalimunisasibalita.Adapter.BaseRealmAdapter;
import com.andevindo.pemantauanjadwalimunisasibalita.Helper.PreferencesManager;
import com.andevindo.pemantauanjadwalimunisasibalita.Helper.TimeConverter;
import com.andevindo.pemantauanjadwalimunisasibalita.Model.ArrangeSchedule;
import com.andevindo.pemantauanjadwalimunisasibalita.Model.Baby;
import com.andevindo.pemantauanjadwalimunisasibalita.Model.Schedule;
import com.andevindo.pemantauanjadwalimunisasibalita.Model.Vaccine;
import com.andevindo.pemantauanjadwalimunisasibalita.R;
import com.andevindo.pemantauanjadwalimunisasibalita.Receiver.AlarmReceiver;
import com.andevindo.pemantauanjadwalimunisasibalita.View.Activity.AddBabyActivity;
import com.andevindo.pemantauanjadwalimunisasibalita.View.Custom.FabItemDecoration;

import java.util.ArrayList;
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
public class ArrangeScheduleFragment extends Fragment implements BaseRealmAdapter.BaseRealmAdapterPresenter<ArrangeSchedule> {

    private RecyclerView mRecyclerView;
    private FloatingActionButton mFab;
    private ArrangeScheduleAdapter mAdapter;
    private List<Vaccine> mVaccines;
    private EditText mDate;
    private Spinner mVaccineSpinner;
    private Baby mBaby;

    public ArrangeScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_arrange_schedule, container, false);

        Realm.init(getContext());

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Vaccine> realmResults = realm.where(Vaccine.class).findAll();
        mBaby = realm.where(Baby.class).equalTo("id", PreferencesManager.getBabyId(getContext())).findFirst();
        mVaccines = new ArrayList<>(realmResults.size());
        mVaccines.addAll(realmResults.subList(0, realmResults.size()));
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new FabItemDecoration(getContext()));
        mFab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputSchedule();
            }
        });
        mAdapter = new ArrangeScheduleAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setBasePresenter(this);
        loadData();
        return view;
    }

    void loadData() {
        Realm realm = Realm.getDefaultInstance();
        RealmList<ArrangeSchedule> realmList = new RealmList<>();
        RealmResults<ArrangeSchedule> realmResults = realm.where(ArrangeSchedule.class).equalTo("baby.id", PreferencesManager.getBabyId(getContext())).findAll();
        realmList.addAll(realmResults.subList(0, realmResults.size()));
        mAdapter.setData(realmList);
    }

    void showInputSchedule() {
        MaterialDialog inputDialog = new MaterialDialog.Builder(getContext())
                .customView(R.layout.dialog_input_schedule, true)
                .positiveText("Masukkan")
                .negativeText("Batal")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Realm realm = Realm.getDefaultInstance();
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                RealmResults<ArrangeSchedule> realmResults = realm.where(ArrangeSchedule.class).findAllSorted("id");

                                ArrangeSchedule arrangeSchedule = realm.createObject(ArrangeSchedule.class);
                                arrangeSchedule.setBaby(mBaby);
                                if (realmResults.size() == 0)
                                    arrangeSchedule.setId(1);
                                else
                                    arrangeSchedule.setId(realmResults.size() + 1);
                                arrangeSchedule.setVaccine((Vaccine) mVaccineSpinner.getSelectedItem());
                                arrangeSchedule.setDueDate(mDate.getText().toString());
                                loadData();
                                setAlarm(arrangeSchedule);
                            }
                        });
                    }
                })
                .build();
        final View positive = inputDialog.getActionButton(DialogAction.POSITIVE);
        positive.setEnabled(false);
        View view = inputDialog.getView();
        mDate = (EditText) view.findViewById(R.id.date);
        mDate.clearFocus();
        mDate.setSelected(false);
        mVaccineSpinner = (Spinner) view.findViewById(R.id.vaccine_list);
        ArrayAdapter<Vaccine> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, mVaccines);
        mVaccineSpinner.setAdapter(arrayAdapter);
        final Calendar calendar = Calendar.getInstance();
        mDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            mDate.setText(TimeConverter.fromCalendarDateToLocal(i2 + "-" + (i1 + 1) + "-" + i));
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });
        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        mDate.setText(TimeConverter.fromCalendarDateToLocal(i2 + "-" + (i1 + 1) + "-" + i));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        mDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    positive.setEnabled(true);
                } else {
                    positive.setEnabled(false);
                }
            }
        });
        inputDialog.show();
    }

    void showInputGivenDate(final ArrangeSchedule arrangeSchedule) {
        MaterialDialog inputDialog = new MaterialDialog.Builder(getContext())
                .customView(R.layout.dialog_input_given_date, true)
                .positiveText("Masukkan")
                .negativeText("Batal")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Realm realm = Realm.getDefaultInstance();
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                arrangeSchedule.setGivenDate(mDate.getText().toString());
                                mAdapter.updateDate(arrangeSchedule);
                                cancelAlarm(arrangeSchedule);
                            }
                        });


                    }
                })
                .build();

        final View positive = inputDialog.getActionButton(DialogAction.POSITIVE);
        positive.setEnabled(false);

        View view = inputDialog.getView();
        mDate = (EditText) view.findViewById(R.id.date);
        final Calendar calendar = Calendar.getInstance();
        mDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            mDate.setText(TimeConverter.fromCalendarDateToLocal(i2 + "-" + (i1 + 1) + "-" + i));
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });
        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        mDate.setText(TimeConverter.fromCalendarDateToLocal(i2 + "-" + (i1 + 1) + "-" + i));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        mDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    positive.setEnabled(true);
                } else {
                    positive.setEnabled(false);
                }
            }
        });
        inputDialog.show();
    }

    void cancelAlarm(ArrangeSchedule arrangeSchedule){
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager)getContext().getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), arrangeSchedule.getId() + 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);

    }

    void setAlarm(ArrangeSchedule arrangeSchedule) {
        ComponentName receiver = new ComponentName(getContext(), AlarmReceiver.class);
        PackageManager pm = getContext().getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        Intent intent = new Intent(getContext(), AlarmReceiver.class);

        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
        intent.putExtra("VaccineName", arrangeSchedule.getVaccine().getName());
        intent.putExtra("BabyName", arrangeSchedule.getBaby().getBabyName());
        intent.putExtra("VaccineDescription", arrangeSchedule.getVaccine().getDescription());
        Date vaccineTime = TimeConverter.fromLocalDateStringToDate(arrangeSchedule.getDueDate());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), arrangeSchedule.getId() + 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar vaccineDate = Calendar.getInstance();
        Calendar nowDate = Calendar.getInstance();
        vaccineDate.setTime(vaccineTime);
        nowDate.setTime(new Date());

        if (vaccineDate.get(Calendar.DATE)==nowDate.get(Calendar.DATE))
            alarmManager.set(AlarmManager.RTC_WAKEUP, vaccineTime.getTime(), pendingIntent);
        else if (vaccineTime.getTime() - System.currentTimeMillis() > 0) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, vaccineTime.getTime(), pendingIntent);


        }


    }

    @Override
    public void onClick(ArrangeSchedule arrangeSchedule) {
        showInputGivenDate(arrangeSchedule);
    }

    @Override
    public void onLongClick(ArrangeSchedule arrangeSchedule) {

    }
}
