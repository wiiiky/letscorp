package org.wiky.letscorp.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import org.wiky.letscorp.adapter.PostListAdapter;
import org.wiky.letscorp.animator.PostItemAnimator;
import org.wiky.letscorp.api.API;
import org.wiky.letscorp.data.model.PostItem;

import java.util.List;

/**
 * Created by wiky on 6/13/16.
 */
public class PostListView extends RecyclerView {
    private PostListAdapter mAdapter;

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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        setAdapter(mAdapter);
        setLayoutManager(linearLayoutManager);
        setItemAnimator(new PostItemAnimator());
        addItemDecoration(new CardItemDecoration(10));
    }

    public void setPostPage(int page) {
        API.getPostList(page, new API.ApiResponseHandler() {
            @Override
            public void onSuccess(Object data) {
                List<PostItem> posts = (List<PostItem>) data;
                mAdapter.addPosts(posts);
            }
        });
    }

    @Override
    public boolean isInEditMode() {
        return true;
    }

}
