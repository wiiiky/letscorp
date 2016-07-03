package org.wiky.letscorp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.transition.Fade;
import android.transition.Transition;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.wiky.letscorp.R;
import org.wiky.letscorp.api.API;
import org.wiky.letscorp.data.model.Post;
import org.wiky.letscorp.data.model.PostItem;
import org.wiky.letscorp.view.OverScrollView;
import org.wiky.materialprogressbar.MaterialProgressBar;

public class PostActivity extends BaseActivity {

    private Post mPostData;
    private PostItem mItemData;
    private TextView mTitle;
    private MaterialProgressBar mProgressBar;
    private LinearLayout mContentLayout;
    private TextView mContent;
    private OverScrollView mScrollView;
    private API.ApiResponseHandler mApiHandler = new API.ApiResponseHandler() {
        @Override
        public void onSuccess(Object data) {
            mPostData = (Post) data;
            mContent.setText(Html.fromHtml(mPostData.content));
            mContent.setAlpha(0.0f);
            mContent.animate().alpha(1.0f).setDuration(300).start();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setEnterTransition();

        mItemData = getIntent().getParcelableExtra("data");
        setContentView(R.layout.activity_post);
        setTitle(mItemData.title);

        mTitle = (TextView) findViewById(R.id.post_title);
        mProgressBar = (MaterialProgressBar) findViewById(R.id.post_loading);
        mScrollView = (OverScrollView) findViewById(R.id.post_scrollview);
        mContentLayout = (LinearLayout) findViewById(R.id.post_content_layout);
        mContent = (TextView) findViewById(R.id.post_content);

        mScrollView.setOverScrollListener(new OverScrollView.OverScrollListener() {
            int translationThreshold = 200;

            @Override
            public boolean onOverScroll(int yDistance, boolean isReleased) {
                if (Math.abs(yDistance) > translationThreshold) { //passed threshold
                    if (isReleased) {
//                        onBackPressed();
                        return false;
                    }
                }
                return false;
            }
        });

        mTitle.setText(mItemData.title);
    }

    private void setEnterTransition() {
        Transition fade = new Fade();
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        fade.excludeTarget(R.id.appbar, true);
        getWindow().setEnterTransition(fade);
    }

    private void onEnter() {
        mTitle.setAlpha(0.0f);
        mContentLayout.setAlpha(0.0f);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mTitle.animate().alpha(1.0f).setDuration(300).start();
                mContentLayout.animate().alpha(1.0f).setDuration(300).start();
                getPostDetail();
            }
        }, 300);
    }

    private void onExit() {
        mTitle.animate().alpha(0.0f).start();
        mContentLayout.animate().alpha(0.0f).start();
    }

    @Override
    public void onResume() {
        onEnter();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        onExit();
        super.onBackPressed();
    }

    private void getPostDetail() {
        mProgressBar.setVisibility(View.VISIBLE);
        API.getPostDetail(mItemData.href, mApiHandler, new API.HttpFinalHandler() {
            @Override
            public void onFinally() {
                mProgressBar.animate().alpha(0.0f).setDuration(250).start();
            }
        });
    }

}
