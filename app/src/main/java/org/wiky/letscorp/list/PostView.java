package org.wiky.letscorp.list;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import org.wiky.letscorp.data.model.Post;
import org.wiky.letscorp.data.model.PostItem;
import org.wiky.letscorp.list.adapter.PostAdapter;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.RecyclerViewOverScrollDecorAdapter;

/**
 * Created by wiky on 8/9/16.
 * 文章详情页面
 */
public class PostView extends RecyclerView {

    private PostAdapter mAdapter;

    public PostView(Context context) {
        super(context);
        initialize(context);
    }

    public PostView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public PostView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    private void initialize(Context context) {
        setLayoutManager(new LinearLayoutManager(context));
        new VerticalOverScrollBounceEffectDecorator(new RecyclerViewOverScrollDecorAdapter(this));
    }

    public void setPost(Post data, OnClickListener listener) {
        if (mAdapter == null) {
            mAdapter = new PostAdapter(getContext(), data.title, data.tags, data.categories, data.author,
                    data.getDatetime(), data.content, listener);
            setAdapter(mAdapter);
        } else {
            mAdapter.update(data.title, data.tags, data.categories, data.author, data.getDatetime(), data.content);
        }
    }

    public void setItem(PostItem data, OnClickListener listener) {
        if (mAdapter == null) {
            mAdapter = new PostAdapter(getContext(), data.title, new ArrayList<String>(), new ArrayList<String>(),
                    "", data.getDatetime(), "", listener);
            setAdapter(mAdapter);
        }
    }

    public List<PostAdapter.Segment> getSegments() {
        return mAdapter.getSegments();
    }
}
