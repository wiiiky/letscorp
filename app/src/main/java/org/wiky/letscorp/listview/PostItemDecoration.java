package org.wiky.letscorp.listview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.wiky.letscorp.util.Util;

/**
 * Created by wiky on 6/11/16.
 * PostListView的装饰器，为每个item流出边缘
 */
public class PostItemDecoration extends RecyclerView.ItemDecoration {

    private final float mVerticalSpaceHeight;

    public PostItemDecoration(int mVerticalSpaceHeight) {
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