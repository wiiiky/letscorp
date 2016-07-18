package org.wiky.letscorp.activity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import org.wiky.letscorp.R;
import org.wiky.letscorp.api.Api;
import org.wiky.letscorp.data.model.Post;
import org.wiky.letscorp.data.model.PostItem;
import org.wiky.letscorp.signal.Signal;
import org.wiky.letscorp.util.Util;
import org.wiky.letscorp.view.ImageViewer;
import org.wiky.letscorp.view.PostContent;
import org.wiky.materialprogressbar.MaterialProgressBar;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends BaseActivity implements View.OnClickListener {

    private Post mPostData;
    private TextView mTitle;
    private TextView mAuthor;
    private TextView mCategory;
    private MaterialProgressBar mProgressBar;
    private PostContent mContent;

    private void updatePost(Post data, boolean animated) {
        mPostData = data;
        mAuthor.setText(mPostData.author + "发表于" + mPostData.date);
        mCategory.setText(Util.joinString(mPostData.categories));
        mContent.setContent(mPostData.content);
        if (animated) {
            mContent.setAlpha(0.0f);
            mContent.animate().alpha(1.0f).setDuration(300).start();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PostItem postItem = getIntent().getParcelableExtra("data");
        Post post = Api.loadPostDetail(postItem.href);
        setContentView(R.layout.activity_post);
        setTitle(postItem.title);

        mTitle = (TextView) findViewById(R.id.post_title);
        mAuthor = (TextView) findViewById(R.id.post_author);
        mCategory = (TextView) findViewById(R.id.post_category);
        mProgressBar = (MaterialProgressBar) findViewById(R.id.post_loading);
        mContent = (PostContent) findViewById(R.id.post_content);

        mTitle.setText(postItem.title);

        mContent.setOnImageClickListener(this);

        if (post != null) {
            mProgressBar.setVisibility(View.GONE);
            updatePost(post, false);
        } else {
            getPostDetail(postItem.href);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    private void getPostDetail(String href) {
        mProgressBar.setVisibility(View.VISIBLE);
        Api.fetchPostDetail(href, new Api.ApiHandler() {
            @Override
            public void onSuccess(Object data) {
                updatePost((Post) data, true);
                Signal.trigger(Signal.SIGNAL_POST_READN, mPostData.href);
            }

            @Override
            public void onFinally() {
                hideProgressBar();
            }
        });
    }

    private void hideProgressBar() {
        mProgressBar.animate()
                .alpha(0.0f)
                .setDuration(250)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();
    }

    @Override
    public void onClick(View v) {
        if (v instanceof ImageViewer) {
            ImageViewer imageViewer = (ImageViewer) v;
            Intent intent = new Intent(this, ImageActivity.class);
            intent.putExtra("url", imageViewer.getURL());
            intent.putExtra("width", imageViewer.getDrawable().getIntrinsicWidth());
            intent.putExtra("height", imageViewer.getDrawable().getIntrinsicHeight());
            intent.putExtra("title", getTitle());

            View statusBar = findViewById(android.R.id.statusBarBackground);
            View navigationBar = findViewById(android.R.id.navigationBarBackground);

            List<Pair<View, String>> pairs = new ArrayList<>();
            pairs.add(Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME));
            pairs.add(Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME));
            pairs.add(Pair.create((View) imageViewer, imageViewer.getTransitionName()));
            pairs.add(Pair.create((View) mAppBar, mAppBar.getTransitionName()));

            Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                    pairs.toArray(new Pair[pairs.size()])).toBundle();
            startActivity(intent, options);
        }
    }
}
