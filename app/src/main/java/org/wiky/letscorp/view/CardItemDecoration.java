package org.wiky.letscorp.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by wiky on 6/11/16.
 */
public class CardItemDecoration extends RecyclerView.ItemDecoration {

    private final int mVerticalSpaceHeight;

    public CardItemDecoration(int mVerticalSpaceHeight) {
        this.mVerticalSpaceHeight = mVerticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.bottom = mVerticalSpaceHeight;
        outRect.left = 24;
        outRect.right = 24;
    }
}