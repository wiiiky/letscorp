package org.wiky.letscorp.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.wiky.letscorp.util.Util;

/**
 * Created by wiky on 6/11/16.
 */
public class CardItemDecoration extends RecyclerView.ItemDecoration {

    private final float mVerticalSpaceHeight;

    public CardItemDecoration(int mVerticalSpaceHeight) {
        this.mVerticalSpaceHeight = Util.dp2px(mVerticalSpaceHeight);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.bottom = (int) (mVerticalSpaceHeight / 2);
        outRect.top = (int) (mVerticalSpaceHeight / 2);
        outRect.left = 24;
        outRect.right = 24;
    }
}