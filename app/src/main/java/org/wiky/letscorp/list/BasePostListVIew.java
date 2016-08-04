package org.wiky.letscorp.list;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import org.wiky.letscorp.api.Const;
import org.wiky.letscorp.data.model.Post;
import org.wiky.letscorp.list.adapter.PostItemAdapter;
import org.wiky.letscorp.list.anim.PostAnimator;


public abstract class BasePostListVIew extends RecyclerView {
    protected final int mPageCount = 15;    /* 这是网页上显示的文章数量，用于取数据库时限制 */
    protected PostItemAdapter mAdapter;
    protected LinearLayoutManager mLayoutManager;
    protected int mCategory = Const.LETSCORP_CATEGORY_ALL;
    protected int mPage;
    protected boolean mLocal;
    protected boolean mReseting;
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

    public BasePostListVIew(Context context) {
        super(context);
        initialize(context);
    }

    public BasePostListVIew(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public BasePostListVIew(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    private void initialize(Context context) {
        mAdapter = new PostItemAdapter();
        mLayoutManager = new LinearLayoutManager(context);
        mPage = 1;
        mReseting = false;

        setAdapter(mAdapter);
        setLayoutManager(mLayoutManager);
        setItemAnimator(new PostAnimator());
        addItemDecoration(new CardItemDecoration(10));

        addOnScrollListener(mOnScrollListener);
    }

    public void setOnItemClickListener(PostItemAdapter.OnItemClickListener listener) {
        mAdapter.setOnItemClickListener(listener);
    }

    public void updateItem(Post post) {
        mAdapter.updateItem(post.href, post.commentCount(), post.date);
    }

    public void setCategory(int category) {
        mCategory = category;
    }

    public abstract void loadMore();

    @Override
    public boolean isInEditMode() {
        return true;
    }

    protected void onRefresh(boolean r) {
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
