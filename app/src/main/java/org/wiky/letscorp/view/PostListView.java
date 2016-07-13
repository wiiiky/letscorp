package org.wiky.letscorp.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import org.wiky.letscorp.adapter.PostListAdapter;
import org.wiky.letscorp.animator.PostItemAnimator;
import org.wiky.letscorp.api.API;
import org.wiky.letscorp.data.db.PostItemHelper;
import org.wiky.letscorp.data.model.PostItem;

import java.util.List;

/**
 * Created by wiky on 6/13/16.
 */
public class PostListView extends RecyclerView {
    private PostListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private boolean mReseting;
    private int mPage;
    /* 滚动事件监听 */
    private OnScrollListener mOnScrollListener = new OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (mReseting) {
                return;
            }

            int totalItemCount = mLayoutManager.getItemCount();
            int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            int visibleThreshold = mLayoutManager.findLastVisibleItemPosition() - mLayoutManager.findFirstVisibleItemPosition();

            if (!mAdapter.isLoading() && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                mAdapter.setLoading();
                loadPage(++mPage);
            }
        }
    };

    public PostListView(Context context) {
        super(context);
        init(context);
    }

    public PostListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PostListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mAdapter = new PostListAdapter();
        mLayoutManager = new LinearLayoutManager(context);
        mPage = 1;
        mReseting = false;

        setAdapter(mAdapter);
        setLayoutManager(mLayoutManager);
        setItemAnimator(new PostItemAnimator());
        addItemDecoration(new CardItemDecoration(10));

        addOnScrollListener(mOnScrollListener);
        loadLocal();
    }

    public void setOnItemClickListener(PostListAdapter.OnItemClickListener listener) {
        mAdapter.setOnItemClickListener(listener);
    }

    public void loadLocal(int page, int count) {
        mPage = 1;
        List<PostItem> items = PostItemHelper.getPostItems(page, count);
        mAdapter.resetPosts(items);
    }

    public void loadLocal() {
        loadLocal(1, 15);
    }

    public void resetPage(API.HttpFinalHandler finalHandler) {
        mPage = 1;
        mReseting = true;
        API.getPostList(mPage, new API.ApiResponseHandler() {
            @Override
            public void onSuccess(Object data) {
                List<PostItem> items = (List<PostItem>) data;
                mAdapter.resetPosts(items);
                mReseting = false;
            }
        }, finalHandler);
    }

    public void loadPage(int page, API.HttpFinalHandler finalHandler) {
        API.getPostList(page, new API.ApiResponseHandler() {
            @Override
            public void onSuccess(Object data) {
                List<PostItem> items = (List<PostItem>) data;
                mAdapter.setLoaded();
                mAdapter.addPosts(items);
            }
        }, finalHandler);
    }

    public void loadPage(int page) {
        loadPage(page, null);
    }

    @Override
    public boolean isInEditMode() {
        return true;
    }
}
