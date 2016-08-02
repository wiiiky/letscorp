package org.wiky.letscorp.list;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import org.wiky.letscorp.data.model.Comment;
import org.wiky.letscorp.list.adapter.CommentReplyAdapter;

import java.util.List;

/*
 * 评论回复列表
 */
public class CommentReplyListView extends RecyclerView {
    private CommentReplyAdapter mAdapter;

    public CommentReplyListView(Context context) {
        super(context);
        initialize(context);
    }

    public CommentReplyListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public CommentReplyListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    private void initialize(Context context) {
        LayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setAutoMeasureEnabled(true);
        setLayoutManager(layoutManager);
        mAdapter = new CommentReplyAdapter();
        setAdapter(mAdapter);
    }

    public void setComments(List<Comment> comments) {
        mAdapter.setComments(comments);
    }
}
