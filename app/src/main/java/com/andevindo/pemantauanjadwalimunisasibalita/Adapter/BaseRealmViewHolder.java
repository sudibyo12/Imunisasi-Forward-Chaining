package com.andevindo.pemantauanjadwalimunisasibalita.Adapter;

import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import io.realm.RealmObject;

/**
 * Created by heendher on 8/7/2016.
 */
public abstract class BaseRealmViewHolder<T extends RealmObject> extends RecyclerView.ViewHolder {

    public BaseRealmViewHolder(View itemView) {
        super(itemView);
    }

    public void setBasePresenter(final T t, final BaseRealmAdapter.BaseRealmAdapterPresenter<T> presenter){
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onClick(t);
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                presenter.onLongClick(t);
                return true;
            }
        });
    }

    public abstract void bindData(final T t);
}
