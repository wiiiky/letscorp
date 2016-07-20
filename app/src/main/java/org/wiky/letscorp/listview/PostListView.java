package org.wiky.letscorp.listview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import org.wiky.letscorp.api.Api;
import org.wiky.letscorp.api.Const;
import org.wiky.letscorp.data.model.PostItem;

import java.util.List;

/**
 * Created by wiky on 6/13/16.
 * 文章列表控件，包含下滑载入和对Adapter方法的封装
 */
public class PostListView extends RecyclerView {
    private final int mPageCount = 15;
    private PostListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private boolean mReseting;
    private int mCategory = Const.LETSCORP_CATEGORY_ALL;
    private int mPage;
    private boolean mLocal;
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
                loadMore();
            }
        }
    };
    private OnRefreshListener mOnRefreshListener = null;

    public PostListView(Context context) {
        super(context);
        initialize(context);
    }

    public PostListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public PostListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    private void initialize(Context context) {
        mAdapter = new PostListAdapter();
        mLayoutManager = new LinearLayoutManager(context);
        mPage = 1;
        mReseting = false;

        setAdapter(mAdapter);
        setLayoutManager(mLayoutManager);
        setItemAnimator(new PostItemAnimator());
        addItemDecoration(new PostItemDecoration(10));

        addOnScrollListener(mOnScrollListener);
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
        mAdapter.setLoading();
        if (mLocal) {
            List<PostItem> items = Api.loadPostItems(mCategory, ++mPage, mPageCount);
            mAdapter.setLoaded();
            mAdapter.addItems(items);
        } else {
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

    @Override
    public boolean isInEditMode() {
        return true;
    }

    private void onRefresh(boolean r) {
        if (mOnRefreshListener != null) {
            mOnRefreshListener.onRefresh(r);
        }
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefreshListener = listener;
    }

    public interface OnRefreshListener {
        void onRefresh(boolean r);
    }
}
