package org.wiky.letscorp.activity;

import android.os.Bundle;

import org.wiky.letscorp.R;
import org.wiky.letscorp.util.Util;
import org.wiky.letscorp.view.PostListView;

public class MainActivity extends BaseDrawerActivity {

    private PostListView mPostListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPostListView = (PostListView) findViewById(R.id.post_list);
        mPostListView.loadPage(1);

        startAnimation();
    }

    private void startAnimation() {
        float actionbarSize = Util.dp2px(56);
        mToolBar.setTranslationY(-actionbarSize);
        mToolBar.animate().translationY(0)
                .setDuration(300)
                .setStartDelay(300);
    }
}
