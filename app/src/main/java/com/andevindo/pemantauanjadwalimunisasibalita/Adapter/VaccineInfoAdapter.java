package com.andevindo.pemantauanjadwalimunisasibalita.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.andevindo.pemantauanjadwalimunisasibalita.Model.Vaccine;
import com.andevindo.pemantauanjadwalimunisasibalita.R;

import io.realm.RealmObject;

/**
 * Created by heendher on 10/20/2016.
 */

public class VaccineInfoAdapter extends BaseRealmAdapter<Vaccine> {

    public VaccineInfoAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRealmViewHolder bindData(View view) {
        return new BaseRealmViewHolder<Vaccine>(view) {
            @Override
            public void bindData(Vaccine vaccine) {
                TextView vaccineName = (TextView)itemView.findViewById(R.id.vaccine_name);
                vaccineName.setText(vaccine.getName());
            }
        };
    }

    @Override
    protected int itemViewId(int viewType) {
        return R.layout.recycler_vaccine_info;
    }
}
