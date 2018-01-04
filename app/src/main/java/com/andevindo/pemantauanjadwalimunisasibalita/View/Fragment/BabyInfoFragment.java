package com.andevindo.pemantauanjadwalimunisasibalita.View.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andevindo.pemantauanjadwalimunisasibalita.Helper.PreferencesManager;
import com.andevindo.pemantauanjadwalimunisasibalita.Model.Baby;
import com.andevindo.pemantauanjadwalimunisasibalita.R;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class BabyInfoFragment extends Fragment {

    private CircleImageView mPhoto;
    private TextView mBabyName, mDateBirth, mGender;

    public BabyInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_baby_info, container, false);

        mPhoto = (CircleImageView)view.findViewById(R.id.photo);
        mBabyName = (TextView)view.findViewById(R.id.baby_name);
        mDateBirth = (TextView)view.findViewById(R.id.date_birth);
        mGender = (TextView)view.findViewById(R.id.gender);

        Realm.init(getContext());
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Baby> realmResults = realm.where(Baby.class).equalTo("id", PreferencesManager.getBabyId(getContext())).findAll();
        if (realmResults.size()>0){
            Baby baby = realmResults.first();
            File file = new File(baby.getPhoto());
            Picasso.with(getContext()).load(file).fit().into(mPhoto);
            mBabyName.setText(baby.getBabyName());
            mDateBirth.setText(baby.getDateOfBirth());
            mGender.setText(baby.getGender());
        }

        return view;
    }

}
