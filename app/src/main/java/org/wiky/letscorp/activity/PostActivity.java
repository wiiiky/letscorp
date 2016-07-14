package org.wiky.letscorp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.wiky.letscorp.R;
import org.wiky.letscorp.api.API;
import org.wiky.letscorp.data.db.PostHelper;
import org.wiky.letscorp.data.model.Post;
import org.wiky.letscorp.data.model.PostItem;
import org.wiky.materialprogressbar.MaterialProgressBar;

public class PostActivity extends BaseActivity {

    private Post mPostData;
    private PostItem mItemData;
    private TextView mTitle;
    private TextView mAuthor;
    private MaterialProgressBar mProgressBar;
    private LinearLayout mContentLayout;
    private TextView mContent;
    private RelativeLayout mScrollViewLayout;
    private API.ApiResponseHandler mApiHandler = new API.ApiResponseHandler() {
        @Override
        public void onSuccess(Object data) {
            updatePost((Post) data);
        }
    };

    private void updatePost(Post data) {
        mPostData = data;
        mAuthor.setText(mPostData.author + "发表于" + mPostData.date);
        mContent.setText(Html.fromHtml(mPostData.content));
        mContent.setAlpha(0.0f);
        mContent.animate().alpha(1.0f).setDuration(300).start();
        Log.d("content", mPostData.content);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setEnterTransition();

        mItemData = getIntent().getParcelableExtra("data");
        setContentView(R.layout.activity_post);
        setTitle(mItemData.title);

        mTitle = (TextView) findViewById(R.id.post_title);
        mAuthor = (TextView) findViewById(R.id.post_author);
        mProgressBar = (MaterialProgressBar) findViewById(R.id.post_loading);
        mScrollViewLayout = (RelativeLayout) findViewById(R.id.post_scrollview_layout);
        mContentLayout = (LinearLayout) findViewById(R.id.post_content_layout);
        mContent = (TextView) findViewById(R.id.post_content);


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
        mScrollViewLayout.setAlpha(0.0f);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mScrollViewLayout.animate().alpha(1.0f).setDuration(300).start();
                getPostDetail();
            }
        }, 300);
    }

    private void onExit() {
        mScrollViewLayout.animate().alpha(0.0f).start();
    }

    @Override
    public void onResume() {
        onEnter();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        onExit();
        if (mPostData != null) {    /* 如果已经下载了文章，则将文章ID返回，用户设置文章已读 */
            setResult(mItemData.id);
        } else {
            setResult(0);
        }
        super.onBackPressed();
    }

    private void getPostDetail() {
        Post data = PostHelper.getPost(mItemData.href);
        if (data == null) {
            mProgressBar.setVisibility(View.VISIBLE);
            API.getPostDetail(mItemData.href, mApiHandler, new API.HttpFinalHandler() {
                @Override
                public void onFinally() {
                    mProgressBar.animate().alpha(0.0f).setDuration(250).start();
                }
            });
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            updatePost(data);
        }
    }

}
