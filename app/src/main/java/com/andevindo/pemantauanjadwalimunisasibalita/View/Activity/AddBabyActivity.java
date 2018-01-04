package com.andevindo.pemantauanjadwalimunisasibalita.View.Activity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.andevindo.pemantauanjadwalimunisasibalita.Helper.FileManager;
import com.andevindo.pemantauanjadwalimunisasibalita.Helper.PreferencesManager;
import com.andevindo.pemantauanjadwalimunisasibalita.Helper.TimeConverter;
import com.andevindo.pemantauanjadwalimunisasibalita.Model.Baby;
import com.andevindo.pemantauanjadwalimunisasibalita.Model.Schedule;
import com.andevindo.pemantauanjadwalimunisasibalita.Model.Vaccine;
import com.andevindo.pemantauanjadwalimunisasibalita.R;
import com.andevindo.pemantauanjadwalimunisasibalita.Receiver.AlarmReceiver;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import io.realm.Realm;
import io.realm.RealmResults;

public class AddBabyActivity extends AppCompatActivity {

    private EditText mBabyName, mDate, mTime;
    private RadioButton mBoy, mGirl;
    private Button mAddBaby;
    private TextInputLayout mBabyNameLayout, mDateLayout, mTimeLayout;
    private boolean mBabyNameStatus = false, mDateStatus = false, mTimeStatus = false, mPhotoStatus = false;
    private CircleImageView mPhoto;
    private final String[] sChooseFile = {"Camera", "Gallery"};
    private Uri mOutputUri;
    private FileManager mManager;
    private String mPhotoPath;
    private boolean mIsBeginning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_baby);

        mIsBeginning = getIntent().getExtras().getBoolean("isBeginning");

        KenBurnsView background = (KenBurnsView) findViewById(R.id.background);
        RandomTransitionGenerator randomTransitionGenerator = new RandomTransitionGenerator();
        int[] backgroundSources = {R.drawable.baby1, R.drawable.baby2};
        Random random = new Random();
        Picasso.with(this).load(backgroundSources[random.nextInt(backgroundSources.length)]);
        background.setTransitionGenerator(randomTransitionGenerator);

        mBabyNameLayout = (TextInputLayout) findViewById(R.id.baby_name_layout);
        mDateLayout = (TextInputLayout) findViewById(R.id.date_layout);
        mTimeLayout = (TextInputLayout) findViewById(R.id.time_layout);

        mBabyName = (EditText) findViewById(R.id.baby_name);
        mBabyName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    mBabyNameStatus = false;
                    mBabyNameLayout.setError("Nama balita tidak boleh kosong");
                } else {
                    mBabyNameStatus = true;
                    mBabyNameLayout.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final Calendar calendar = Calendar.getInstance();

        mDate = (EditText) findViewById(R.id.date);
        mDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new DatePickerDialog(AddBabyActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                new DatePickerDialog(AddBabyActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                if (s.length() == 0) {
                    mDateStatus = false;
                    mDateLayout.setError("Mohon isikan tanggal yang benar");
                } else {
                    mDateStatus = true;
                    mDateLayout.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mTime = (EditText) findViewById(R.id.time);
        mTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new TimePickerDialog(AddBabyActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            mTime.setText(TimeConverter.fromCalendarTimeToLocal(hourOfDay + ":" + minute));
                        }
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
                }
            }
        });
        mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AddBabyActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mTime.setText(TimeConverter.fromCalendarTimeToLocal(hourOfDay + ":" + minute));
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        });
        mTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    mTimeStatus = false;
                    mTimeLayout.setError("Waktu tidak boleh kosong");
                } else {
                    mTimeStatus = true;
                    mTimeLayout.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mBoy = (RadioButton) findViewById(R.id.boy);
        mGirl = (RadioButton) findViewById(R.id.girl);
        mPhoto = (CircleImageView) findViewById(R.id.photo);
        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        mAddBaby = (Button) findViewById(R.id.add_baby);

        Realm.init(this);
        final Realm realm = Realm.getDefaultInstance();

        mAddBaby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBabyNameStatus && mDateStatus && mTimeStatus && mPhotoStatus) {
                    realm.beginTransaction();
                    RealmResults<Baby> realmResults = realm.where(Baby.class).findAllSorted("id");
                    Baby baby = realm.createObject(Baby.class);
                    if (realmResults.size() == 0)
                        baby.setId(1);
                    else
                        baby.setId(realmResults.last().getId() + 1);
                    baby.setBabyName(mBabyName.getText().toString());
                    baby.setDateOfBirth(mDate.getText().toString() + " " + mTime.getText().toString());
                    if (mBoy.isChecked())
                        baby.setGender("Laki-Laki");
                    else
                        baby.setGender("Perempuan");
                    baby.setPhoto(mPhotoPath);
                    realm.commitTransaction();
                    if (mIsBeginning) {
                        PreferencesManager.setBabyId(AddBabyActivity.this, baby.getId());
                        setAlarm();
                        startActivity(new Intent(AddBabyActivity.this, MainActivity.class));
                        finish();
                    } else {
                        setResult(RESULT_OK);
                        finish();
                    }

                } else {
                    Toast.makeText(AddBabyActivity.this, "Mohon masukkan semua data dengan benar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void showDialog() {
        new MaterialDialog.Builder(AddBabyActivity.this).title("Choose file")
                .items(sChooseFile)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {

                        if (i == 0) {
                            if (ContextCompat.checkSelfPermission(AddBabyActivity.this, Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED
                                    && ContextCompat.checkSelfPermission(AddBabyActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(AddBabyActivity.this,
                                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        1);
                            } else {


                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                                mOutputUri = FileManager.getOutputMediaFileUri(FileManager.CAMERA_IMAGE_CODE);

                                intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutputUri);

                                startActivityForResult(intent, FileManager.CAMERA_IMAGE_CODE);
                            }
                        } else {
                            if (ContextCompat.checkSelfPermission(AddBabyActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(AddBabyActivity.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                        2);
                            } else {
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                startActivityForResult(intent, FileManager.FILE_MANAGER_CODE);
                            }
                        }
                    }
                }).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==1){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                mOutputUri = FileManager.getOutputMediaFileUri(FileManager.CAMERA_IMAGE_CODE);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutputUri);

                startActivityForResult(intent, FileManager.CAMERA_IMAGE_CODE);
            }
        }else if (requestCode==2){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, FileManager.FILE_MANAGER_CODE);
            }
        }else{

        }

    }

    void setAlarm() {
        ComponentName receiver = new ComponentName(this, AlarmReceiver.class);
        PackageManager pm = getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Schedule> schedules = realm.where(Schedule.class).findAll();
        RealmResults<Baby> babies = realm.where(Baby.class).equalTo("id", PreferencesManager.getBabyId(this)).findAll();

        Baby baby = babies.first();
        Date babyBirthDate = TimeConverter.fromLocalStringToDate(baby.getDateOfBirth());
        Intent intent = new Intent(this, AlarmReceiver.class);
        for (int i = 0; i < schedules.size(); i++) {
            Schedule schedule = schedules.get(i);
            intent.putExtra("VaccineName", schedule.getVaccineName());
            intent.putExtra("BabyName", baby.getBabyName());
            RealmResults<Vaccine> vaccines = realm.where(Vaccine.class).equalTo("id", schedule.getVaccineId()).findAll();
            intent.putExtra("VaccineDescription", vaccines.first().getDescription());
            Date vaccineTime = TimeConverter.getVaccineDate(baby.getDateOfBirth(), schedule.getTime(), schedule.getTimeFormat());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, schedule.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (schedule.isRepeated()) {
                if (schedule.getTimeFormat().equals("tahun")) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(babyBirthDate);
                    calendar.add(Calendar.YEAR, schedule.getTime());
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, vaccineTime.getTime() + (vaccineTime.getTime() < System.currentTimeMillis() ? calendar.getTimeInMillis() : 0), calendar.getTimeInMillis(), pendingIntent);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == FileManager.CAMERA_IMAGE_CODE) {
                //Log.d("PATH", data.getData().getPath());
                mPhotoStatus = true;
                mPhoto.setBorderColor(ContextCompat.getColor(this, android.R.color.white));
                //mProfpicBitmap = BitmapFactory.decodeFile(mOutputUri.getPath());
                //mProfpic.setImageBitmap(mProfpicBitmap);
                mPhotoPath = FileManager.getPath(this, mOutputUri);
                Picasso.with(this).load(mOutputUri).fit().into(mPhoto);
                //mProfpic.setImageURI(mOutputUri);
                //mFile = Compressor.getDefault(this).compressToFile(new File(FileManager.getPath(this, mOutputUri)));
            } else {
                mPhotoStatus = true;
                mPhoto.setBorderColor(ContextCompat.getColor(this, android.R.color.white));
                mOutputUri = data.getData();
                mPhotoPath = FileManager.getPath(this, mOutputUri);
                Picasso.with(this).load(data.getData()).fit().into(mPhoto);
                //mProfpic.setImageURI(data.getData());
                //mFile = Compressor.getDefault(this).compressToFile(new File(FileManager.getPath(this, data.getData())));
            }
        }
    }
}
