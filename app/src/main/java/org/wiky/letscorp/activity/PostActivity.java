package org.wiky.letscorp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.wiky.letscorp.R;
import org.wiky.letscorp.api.API;
import org.wiky.letscorp.data.model.Post;
import org.wiky.letscorp.data.model.PostItem;
import org.wiky.materialprogressbar.MaterialProgressBar;

public class PostActivity extends BaseActivity {

    private Post mData;
    private TextView mTitle;
    private MaterialProgressBar mProgressBar;
    private LinearLayout mContentLayout;
    private TextView mContent;
    private API.ApiResponseHandler mApiHandler = new API.ApiResponseHandler() {
        @Override
        public void onSuccess(final Object data) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mData = (Post) data;
                    mContent.setText(Html.fromHtml(mData.content));
                    mContent.setAlpha(0.0f);
                    mContent.animate().alpha(1.0f).setDuration(150).start();
                }
            }, 250);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PostItem data = getIntent().getParcelableExtra("data");
        setContentView(R.layout.activity_post);

        setProgressBarIndeterminateVisibility(true);
        setProgressBarIndeterminate(true);
        setProgressBarVisibility(true);

        setTitle(data.title);
        mTitle = (TextView) findViewById(R.id.post_title);
        mProgressBar = (MaterialProgressBar) findViewById(R.id.post_loading);
        mContentLayout = (LinearLayout) findViewById(R.id.post_content_layout);
        mContent = (TextView) findViewById(R.id.post_content);

        mTitle.setText(data.title);
        getPostDetail(data);
    }

    private void getPostDetail(PostItem data) {
        API.getPostDetail(data.href, mApiHandler, new API.HttpFinalHandler() {
            @Override
            public void onFinally() {
                mProgressBar.animate().alpha(0.0f).setDuration(250).start();
            }
        });
    }
}
