package org.wiky.letscorp.list;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import org.wiky.letscorp.api.Api;
import org.wiky.letscorp.data.model.PostItem;

import java.util.List;

/**
 * Created by wiky on 7/23/16.
 * 搜索结果列表
 */
public class SearchListView extends BasePostListVIew {

    private String mQuery = "";
    private boolean mEnd = false;

    public SearchListView(Context context) {
        super(context);
    }

    public SearchListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void search(String query) {
        mQuery = query;
        mPage = 1;
        mEnd = false;
        onRefresh(true);
        Api.searchPost(mQuery, mPage, new Api.ApiHandler<List<PostItem>>() {
            @Override
            public void onSuccess(List<PostItem> items) {
                mAdapter.resetItems(items);
            }

            @Override
            public void onFinally() {
                onRefresh(false);
            }
        });
    }

    @Override
    public void loadMore() {
        if (mEnd) {
            return;
        }
        mAdapter.setLoading();
        Api.searchPost(mQuery, ++mPage, new Api.ApiHandler<List<PostItem>>() {
            @Override
            public void onSuccess(List<PostItem> items) {
                mEnd = items.size() == 0;
                mAdapter.addItems(items);
            }

            @Override
            public void onFinally() {
                mAdapter.setLoaded();
            }
        });
    }

}
