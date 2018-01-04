package com.andevindo.pemantauanjadwalimunisasibalita.Model;

import io.realm.RealmObject;

/**
 * Created by heendher on 10/20/2016.
 */

public class Vaccine extends RealmObject {

    private int id;
    private String name;
    private String description;

    @Override
    public String toString() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
