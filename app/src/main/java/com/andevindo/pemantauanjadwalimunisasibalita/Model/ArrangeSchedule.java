package com.andevindo.pemantauanjadwalimunisasibalita.Model;

import io.realm.RealmObject;

/**
 * Created by heendher on 10/28/2016.
 */

public class ArrangeSchedule extends RealmObject {

    private int id;
    private Baby baby;
    private Vaccine vaccine;
    private String dueDate;
    private String givenDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Baby getBaby() {
        return baby;
    }

    public void setBaby(Baby baby) {
        this.baby = baby;
    }

    public Vaccine getVaccine() {
        return vaccine;
    }

    public void setVaccine(Vaccine vaccine) {
        this.vaccine = vaccine;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getGivenDate() {
        return givenDate;
    }

    public void setGivenDate(String givenDate) {
        this.givenDate = givenDate;
    }
}
