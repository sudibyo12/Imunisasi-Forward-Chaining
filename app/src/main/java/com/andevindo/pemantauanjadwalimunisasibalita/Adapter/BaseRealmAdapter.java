package com.andevindo.pemantauanjadwalimunisasibalita.Adapter;

import android.content.Context;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by heendher on 8/7/2016.
 */
public abstract class BaseRealmAdapter<T extends RealmObject> extends RecyclerView.Adapter<BaseRealmViewHolder<T>> {

    private RealmList<T> mList;
    private Context mContext;
    private BaseRealmAdapterPresenter<T> mPresenter = new BaseRealmAdapterPresenter<T>() {
        @Override
        public void onClick(T t) {
            Log.d("Adapter", "OnClick");
        }

        @Override
        public void onLongClick(T t) {
            Log.d("Adapter", "OnLongClick");
        }
    };

    public BaseRealmAdapter(Context context) {
        mContext = context;
    }

    public void setBasePresenter(BaseRealmAdapterPresenter<T> presenter){
        mPresenter = presenter;
    }

    public Context getContext(){
        return mContext;
    }

    @Override
    public BaseRealmViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(itemViewId(viewType), parent, false);
        return bindData(view);
    }

    protected abstract BaseRealmViewHolder bindData(View view);

    protected abstract int itemViewId(int viewType);

    protected BaseRealmAdapterPresenter<T> getBaseAdapter(){
        return mPresenter;
    }

    public interface BaseRealmAdapterPresenter<T>{
        void onClick(T t);
        void onLongClick(T t);
    }

    @Override
    public void onBindViewHolder(BaseRealmViewHolder<T> holder, int position) {
        holder.bindData(mList.get(position));
        holder.setBasePresenter(mList.get(position), mPresenter);
    }

    public void setData(RealmList<T> list){
        mList = list;
        notifyDataSetChanged();
    }

    public RealmList<T> getData(){
        return mList;
    }

    public void refresh(){
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mList == null)
            return 0;
        else
            return mList.size();
    }
}
