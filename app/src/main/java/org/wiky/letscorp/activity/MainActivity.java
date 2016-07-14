package org.wiky.letscorp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import org.wiky.letscorp.Application;
import org.wiky.letscorp.R;
import org.wiky.letscorp.data.model.PostItem;
import org.wiky.letscorp.listview.PostListAdapter;
import org.wiky.letscorp.listview.PostListView;
import org.wiky.letscorp.signal.Signal;
import org.wiky.letscorp.signal.SignalHandler;

import java.util.Objects;

public class MainActivity extends BaseDrawerActivity implements SwipeRefreshLayout.OnRefreshListener, SignalHandler {

    private PostListView mPostListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private PostListAdapter.OnItemClickListener mOnItemClickListener = new PostListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(final PostListAdapter.PostItemHolder viewHolder, final PostItem data) {
            Intent intent = new Intent(MainActivity.this, PostActivity.class);
            intent.putExtra("data", data);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        mPostListView = (PostListView) findViewById(R.id.post_list);
        mPostListView.setOnItemClickListener(mOnItemClickListener);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        startToolbarAnimation();
        Signal.register(Signal.SIGNAL_POST_LIST_RESET_END, this);

        if (!mPostListView.loadLocal()) {
            Application.getUIHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                    onRefresh();
                }
            }, 200);
        }
    }

    @Override
    public void onRefresh() {
        mPostListView.resetItems();
    }

    @Override
    public void handleSignal(String signal, Object data) {
        if (Objects.equals(signal, Signal.SIGNAL_POST_LIST_RESET_END)) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
