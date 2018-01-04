package com.andevindo.pemantauanjadwalimunisasibalita.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andevindo.pemantauanjadwalimunisasibalita.Model.ArrangeSchedule;
import com.andevindo.pemantauanjadwalimunisasibalita.R;

import io.realm.RealmObject;

/**
 * Created by heendher on 10/28/2016.
 */

public class ArrangeScheduleAdapter extends BaseRealmAdapter<ArrangeSchedule> {

    public ArrangeScheduleAdapter(Context context) {
        super(context);
    }

    public void updateDate(ArrangeSchedule arrangeSchedule){
        int index = getData().indexOf(arrangeSchedule);
        notifyItemChanged(index);
    }

    @Override
    protected BaseRealmViewHolder bindData(View view) {
        return new BaseRealmViewHolder<ArrangeSchedule>(view) {
            @Override
            public void bindData(ArrangeSchedule arrangeSchedule) {
                TextView vaccineName = (TextView)itemView.findViewById(R.id.vaccine_name);
                TextView dueDate = (TextView)itemView.findViewById(R.id.dueDate);
                TextView givenDate = (TextView)itemView.findViewById(R.id.givenDate);
                ImageView check = (ImageView)itemView.findViewById(R.id.check);
                vaccineName.setText(arrangeSchedule.getVaccine().getName());
                dueDate.setText(arrangeSchedule.getDueDate());
                givenDate.setText(arrangeSchedule.getGivenDate());
                if (arrangeSchedule.getGivenDate()!=null){
                    check.setVisibility(ImageView.VISIBLE);
                }else{
                    check.setVisibility(ImageView.INVISIBLE);
                }
            }
        };
    }

    @Override
    protected int itemViewId(int viewType) {
        return R.layout.recycler_arrange_schedule;
    }
}
