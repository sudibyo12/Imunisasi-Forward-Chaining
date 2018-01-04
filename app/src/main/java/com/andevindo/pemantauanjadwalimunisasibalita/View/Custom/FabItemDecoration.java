package com.andevindo.pemantauanjadwalimunisasibalita.View.Custom;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.andevindo.pemantauanjadwalimunisasibalita.R;

/**
 * Created by heendher on 8/9/2016.
 */
public class FabItemDecoration extends RecyclerView.ItemDecoration {

    private int mMargin;

    public FabItemDecoration(Context context) {
        mMargin = (int)context.getResources().getDimension(R.dimen.list_with_fab);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int itemCount = parent.getAdapter().getItemCount() - 1;

        if(parent.getChildAdapterPosition(view) == itemCount)
            outRect.bottom = mMargin;

    }
}
