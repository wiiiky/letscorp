package org.wiky.letscorp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import org.wiky.letscorp.R;
import org.wiky.letscorp.adapter.PostListAdapter;
import org.wiky.letscorp.api.API;
import org.wiky.letscorp.data.model.PostItem;
import org.wiky.letscorp.view.PostListView;

public class MainActivity extends BaseDrawerActivity implements SwipeRefreshLayout.OnRefreshListener {

    private PostListView mPostListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private PostListAdapter.OnItemClickListener mOnItemClickListener = new PostListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(final PostListAdapter.PostItemHolder viewHolder, final PostItem data) {
            Intent intent = new Intent(MainActivity.this, PostActivity.class);
            intent.putExtra("data", data);


            View shared = viewHolder.itemView;
            shared.setTransitionName(getResources().getString(R.string.transition_shared));
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(MainActivity.this,
                            Pair.create(shared, shared.getTransitionName())
                    );
            startActivity(intent, options.toBundle());
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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        }, 200);
    }

    @Override
    public void onRefresh() {
        mPostListView.resetPage(new API.HttpFinalHandler() {
            @Override
            public void onFinally() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

}
