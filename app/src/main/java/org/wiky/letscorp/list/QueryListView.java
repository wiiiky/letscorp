package org.wiky.letscorp.list;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import org.wiky.letscorp.list.adapter.QueryAdapter;

/**
 * 搜索历史记录的列表
 */
public class QueryListView extends RecyclerView {
    private QueryAdapter mAdapter;

    public QueryListView(Context context) {
        super(context);
        initialize(context);
    }

    public QueryListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public QueryListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    private void initialize(Context context) {
        mAdapter = new QueryAdapter();
        setAdapter(mAdapter);
        setLayoutManager(new LinearLayoutManager(context));
        getItemAnimator().setChangeDuration(0);
        getItemAnimator().setRemoveDuration(100);
        getItemAnimator().setAddDuration(100);
    }

    public void setOnItemClickListener(QueryAdapter.OnItemClickListener listener) {
        mAdapter.setOnItemClickListener(listener);
    }

    public void update(String query) {
        mAdapter.update(query);
    }

    public void clear() {
        mAdapter.clear();
    }
}
