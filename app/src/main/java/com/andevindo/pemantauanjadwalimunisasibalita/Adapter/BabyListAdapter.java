package com.andevindo.pemantauanjadwalimunisasibalita.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andevindo.pemantauanjadwalimunisasibalita.Helper.PreferencesManager;
import com.andevindo.pemantauanjadwalimunisasibalita.Model.Baby;
import com.andevindo.pemantauanjadwalimunisasibalita.R;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.RealmObject;

/**
 * Created by heendher on 10/22/2016.
 */

public class BabyListAdapter extends BaseRealmAdapter<Baby> {

    public BabyListAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRealmViewHolder bindData(View view) {
        return new BaseRealmViewHolder<Baby>(view) {
            @Override
            public void bindData(Baby baby) {
                CircleImageView photo = (CircleImageView)itemView.findViewById(R.id.photo);
                TextView babyName = (TextView)itemView.findViewById(R.id.baby_name);
                ImageView isSelected = (ImageView)itemView.findViewById(R.id.is_selected);
                File file = new File(baby.getPhoto());
                Picasso.with(getContext()).load(file).fit().into(photo);
                babyName.setText(baby.getBabyName());
                if (PreferencesManager.getBabyId(getContext())==baby.getId())
                    isSelected.setVisibility(ImageView.VISIBLE);
                else
                    isSelected.setVisibility(ImageView.GONE);
            }
        };
    }

    @Override
    protected int itemViewId(int viewType) {
        return R.layout.recycler_baby_list;
    }
}
