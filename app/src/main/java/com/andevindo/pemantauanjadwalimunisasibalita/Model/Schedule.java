package com.andevindo.pemantauanjadwalimunisasibalita.Model;

import io.realm.RealmObject;

/**
 * Created by heendher on 10/22/2016.
 */

public class Schedule extends RealmObject {

    private int id;
    private int vaccineId;
    private String vaccineName;
    private int time;
    private String timeFormat;
    private boolean isRepeated;

    public int getVaccineId() {
        return vaccineId;
    }

    public void setVaccineId(int vaccineId) {
        this.vaccineId = vaccineId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }

    public boolean isRepeated() {
        return isRepeated;
    }

    public void setRepeated(boolean repeated) {
        isRepeated = repeated;
    }
}
