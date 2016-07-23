package org.wiky.letscorp.list;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import org.wiky.letscorp.api.Api;
import org.wiky.letscorp.data.model.PostItem;

import java.util.List;

/**
 * Created by wiky on 6/13/16.
 * 文章列表控件，包含下滑载入和对Adapter方法的封装
 */
public class PostListView extends BasePostListVIew {

    public PostListView(Context context) {
        super(context);
    }

    public PostListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PostListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void setOnItemClickListener(PostListAdapter.OnItemClickListener listener) {
        mAdapter.setOnItemClickListener(listener);
    }

    public void setItemReadn(String href) {
        mAdapter.setItemReadn(href);
    }

    public void setCategory(int category) {
        mCategory = category;
    }

    /* 从数据库载入数据，载入成功返回true，否则返回false */
    public boolean loadLocal(int page, int count) {
        mPage = page;
        List<PostItem> items = Api.loadPostItems(mCategory, page, count);
        mAdapter.resetItems(items);
        mLocal = items.size() > 0;
        return mLocal;
    }

    public boolean loadLocal() {
        return loadLocal(1, mPageCount);
    }

    /* 重置页面 */
    public void resetItems() {
        mPage = 1;
        mReseting = true;
        mLocal = false;
        onRefresh(true);

        Api.fetchPostItems(mCategory, mPage, new Api.ApiHandler<List<PostItem>>() {
            @Override
            public void onSuccess(List<PostItem> items) {
                mAdapter.resetItems(items);
                mReseting = false;
            }

            @Override
            public void onFinally() {
                onRefresh(false);
            }
        });
    }

    /* 载入下一页 */
    public void loadMore() {
        if (mLocal) {
            List<PostItem> items = Api.loadPostItems(mCategory, ++mPage, mPageCount);
            mAdapter.addItems(items);
        } else {
            mAdapter.setLoading();
            Api.fetchPostItems(mCategory, ++mPage, new Api.ApiHandler<List<PostItem>>() {
                @Override
                public void onSuccess(List<PostItem> items) {
                    mAdapter.setLoaded();
                    mAdapter.addItems(items);
                }

                @Override
                public void onFinally() {
                }
            });
        }
    }

}
