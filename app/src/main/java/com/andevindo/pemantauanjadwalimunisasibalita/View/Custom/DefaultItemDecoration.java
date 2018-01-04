package com.andevindo.pemantauanjadwalimunisasibalita.View.Custom;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by heendher on 8/9/2016.
 */
public class DefaultItemDecoration extends RecyclerView.ItemDecoration {

    private int mMargin;

    public DefaultItemDecoration(int margin) {
        mMargin = margin;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.left = mMargin;
        outRect.right = mMargin;
        outRect.top = mMargin;
        outRect.bottom = mMargin;

    }
}
