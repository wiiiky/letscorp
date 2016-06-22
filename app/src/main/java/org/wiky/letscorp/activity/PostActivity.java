package org.wiky.letscorp.activity;

import android.os.Bundle;
import android.text.Html;
import android.transition.Explode;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.wiky.letscorp.R;
import org.wiky.letscorp.api.API;
import org.wiky.letscorp.data.model.Post;
import org.wiky.letscorp.data.model.PostItem;

public class PostActivity extends BaseActivity {

    private Post mData;
    private TextView mTitle;
    private ProgressBar mProgressBar;
    private LinearLayout mContentLayout;
    private TextView mContent;
    private API.ApiResponseHandler mApiHandler = new API.ApiResponseHandler() {
        @Override
        public void onSuccess(Object data) {
            mData = (Post) data;
            mContent.setText(Html.fromHtml(mData.content));
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setDuration(150);
            mContent.startAnimation(alphaAnimation);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setEnterTransition(new Explode());

        PostItem data = getIntent().getParcelableExtra("data");
        setContentView(R.layout.activity_post);

        setProgressBarIndeterminateVisibility(true);
        setProgressBarIndeterminate(true);
        setProgressBarVisibility(true);

        setTitle(getString(R.string.post_activity_title));
        mTitle = (TextView) findViewById(R.id.post_title);
        mProgressBar = (ProgressBar) findViewById(R.id.post_loading);
        mContentLayout = (LinearLayout) findViewById(R.id.post_content_layout);
        mContent = (TextView) findViewById(R.id.post_content);

        mTitle.setText(data.title);
        API.getPostDetail(data.href, mApiHandler, new API.HttpFinalHandler() {
            @Override
            public void onFinally() {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
