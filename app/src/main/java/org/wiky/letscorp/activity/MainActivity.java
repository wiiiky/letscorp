package org.wiky.letscorp.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import org.wiky.letscorp.R;
import org.wiky.letscorp.api.API;
import org.wiky.letscorp.util.Util;
import org.wiky.letscorp.view.PostListView;

public class MainActivity extends BaseDrawerActivity implements SwipeRefreshLayout.OnRefreshListener {

    private PostListView mPostListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPostListView = (PostListView) findViewById(R.id.post_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        startAnimation();
    }

    private void startAnimation() {
        float actionbarSize = Util.dp2px(56);
        mToolBar.setTranslationY(-actionbarSize);
        mToolBar.animate().translationY(0)
                .setDuration(300)
                .setStartDelay(300);
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
