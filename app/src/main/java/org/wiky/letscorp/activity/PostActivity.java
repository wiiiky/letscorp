package org.wiky.letscorp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.wiky.letscorp.R;
import org.wiky.letscorp.api.API;
import org.wiky.letscorp.data.model.Post;
import org.wiky.letscorp.data.model.PostItem;
import org.wiky.materialprogressbar.MaterialProgressBar;

public class PostActivity extends BaseActivity {

    private Post mPostData;
    private PostItem mItemData;
    private TextView mTitle;
    private MaterialProgressBar mProgressBar;
    private LinearLayout mContentLayout;
    private TextView mContent;
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

        Transition fade = new Fade();
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        Transition slide = new Slide(Gravity.TOP);
        slide.addTarget(R.id.appbar);

        TransitionSet enter = new TransitionSet();
        enter.addTransition(fade).addTransition(slide);

//        getWindow().setExitTransition(enter);
        getWindow().setEnterTransition(enter);


        mItemData = getIntent().getParcelableExtra("data");
        setContentView(R.layout.activity_post);
        setTitle(mItemData.title);

        mTitle = (TextView) findViewById(R.id.post_title);
        mProgressBar = (MaterialProgressBar) findViewById(R.id.post_loading);
        mContentLayout = (LinearLayout) findViewById(R.id.post_content_layout);
        mContent = (TextView) findViewById(R.id.post_content);

        mTitle.setText(mItemData.title);
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
