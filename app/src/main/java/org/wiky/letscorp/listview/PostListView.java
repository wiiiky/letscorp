package org.wiky.letscorp.listview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import org.wiky.letscorp.api.API;
import org.wiky.letscorp.api.Const;
import org.wiky.letscorp.data.model.PostItem;
import org.wiky.letscorp.signal.Signal;
import org.wiky.letscorp.signal.SignalHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by wiky on 6/13/16.
 * 文章列表控件，包含下滑载入和对Adapter方法的封装
 */
public class PostListView extends RecyclerView implements SignalHandler {
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
                mAdapter.setLoading();
                loadMore();
            }
        }
    };

    private API.HttpFinalHandler mAPIFinalHandler = new API.HttpFinalHandler() {
        @Override
        public void onFinally() {
            Signal.trigger(Signal.SIGNAL_POST_RESET_END);
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
        addItemDecoration(new PostItemDecoration(10));

        addOnScrollListener(mOnScrollListener);
        Signal.register(Signal.SIGNAL_CATEGORY_CHANGE, this);
    }

    public void setOnItemClickListener(PostListAdapter.OnItemClickListener listener) {
        mAdapter.setOnItemClickListener(listener);
    }

    /* 从数据库载入数据，载入成功返回true，否则返回false */
    public boolean loadLocal(int page, int count) {
        mPage = page;
        List<PostItem> items = API.loadPostItems(mCategory, page, count);
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
        Signal.trigger(Signal.SIGNAL_POST_RESET_START);

        API.fetchPostItems(mCategory, mPage, new API.ApiResponseHandler() {
            @Override
            public void onSuccess(Object data) {
                List<PostItem> items = (List<PostItem>) data;
                mAdapter.resetItems(items);
                mReseting = false;
            }
        }, mAPIFinalHandler);
    }

    /* 载入下一页 */
    public void loadMore(API.HttpFinalHandler finalHandler) {
        if (mLocal) {
            List<PostItem> items = API.loadPostItems(mCategory, ++mPage, mPageCount);
            mAdapter.setLoaded();
            mAdapter.addItems(items);
        } else {
            API.fetchPostItems(mCategory, ++mPage, new API.ApiResponseHandler() {
                @Override
                public void onSuccess(Object data) {
                    List<PostItem> items = (List<PostItem>) data;
                    mAdapter.setLoaded();
                    mAdapter.addItems(items);
                }
            }, finalHandler);
        }
    }

    public void loadMore() {
        loadMore(null);
    }

    @Override
    public boolean isInEditMode() {
        return true;
    }

    @Override
    public void handleSignal(String signal, Object data) {
        if (Objects.equals(signal, Signal.SIGNAL_CATEGORY_CHANGE)) {
            mCategory = (int) data;
            if (!loadLocal()) {
                mAdapter.resetItems(new ArrayList<PostItem>());
                resetItems();
            } else {
                Signal.trigger(Signal.SIGNAL_POST_RESET_END);
            }
        }
    }
}
