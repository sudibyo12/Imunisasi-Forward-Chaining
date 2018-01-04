package com.andevindo.pemantauanjadwalimunisasibalita.View.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.andevindo.pemantauanjadwalimunisasibalita.Adapter.BaseRealmAdapter;
import com.andevindo.pemantauanjadwalimunisasibalita.Adapter.VaccineInfoAdapter;
import com.andevindo.pemantauanjadwalimunisasibalita.Model.Vaccine;
import com.andevindo.pemantauanjadwalimunisasibalita.R;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class VaccineInfoFragment extends Fragment implements BaseRealmAdapter.BaseRealmAdapterPresenter<Vaccine>{


    public VaccineInfoFragment() {
        // Required empty public constructor
    }

    private RecyclerView mRecyclerView;
    private VaccineInfoAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vaccine_info, container, false);

        Realm.init(getContext());

        Realm realm = Realm.getDefaultInstance();

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler);
        mAdapter = new VaccineInfoAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter.setBasePresenter(this);
        RealmResults<Vaccine> realmResults = realm.where(Vaccine.class).findAll();
        RealmList<Vaccine> realmList = new RealmList<>();
        realmList.addAll(realmResults.subList(0, realmResults.size()));
        mAdapter.setData(realmList);
        return view;
    }

    @Override
    public void onClick(Vaccine vaccine) {
        showDetailInfo(vaccine.getDescription());
    }

    void showDetailInfo(String description){
        new MaterialDialog.Builder(getContext())
                .content(description)
                .negativeText("Tutup")
                .build().show();
    }

    @Override
    public void onLongClick(Vaccine vaccine) {

    }
}
