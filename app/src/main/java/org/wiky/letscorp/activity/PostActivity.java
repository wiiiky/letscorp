package org.wiky.letscorp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.wiky.letscorp.R;
import org.wiky.letscorp.api.API;
import org.wiky.letscorp.data.db.PostHelper;
import org.wiky.letscorp.data.model.Post;
import org.wiky.letscorp.data.model.PostItem;
import org.wiky.letscorp.signal.Signal;
import org.wiky.letscorp.view.PostContent;
import org.wiky.materialprogressbar.MaterialProgressBar;

public class PostActivity extends BaseActivity {

    private Post mPostData;
    private TextView mTitle;
    private TextView mAuthor;
    private MaterialProgressBar mProgressBar;
    private PostContent mContent;
    private RelativeLayout mScrollViewLayout;
    private API.ApiResponseHandler mApiHandler = new API.ApiResponseHandler() {
        @Override
        public void onSuccess(Object data) {
            updatePost((Post) data, true);
        }
    };

    private void updatePost(Post data, boolean animated) {
        mPostData = data;
        mAuthor.setText(mPostData.author + "发表于" + mPostData.date);
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
        Post post = PostHelper.getPost(postItem.href);
        setContentView(R.layout.activity_post);
        setTitle(postItem.title);

        mTitle = (TextView) findViewById(R.id.post_title);
        mAuthor = (TextView) findViewById(R.id.post_author);
        mProgressBar = (MaterialProgressBar) findViewById(R.id.post_loading);
        mScrollViewLayout = (RelativeLayout) findViewById(R.id.post_scrollview_layout);
        mContent = (PostContent) findViewById(R.id.post_content);


        mTitle.setText(postItem.title);

        if (post != null) {
            mProgressBar.setVisibility(View.INVISIBLE);
            updatePost(post, false);
        } else {
            getPostDetail(postItem.href);
        }
    }


    @Override
    public void onBackPressed() {
        if (mPostData != null) {    /* 如果已经下载了文章，则将文章ID返回，用户设置文章已读 */
            Signal.trigger(Signal.SIGNAL_POST_READN, mPostData.href);
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    private void getPostDetail(String href) {
        mProgressBar.setVisibility(View.VISIBLE);
        API.getPostDetail(href, mApiHandler, new API.HttpFinalHandler() {
            @Override
            public void onFinally() {
                mProgressBar.animate().alpha(0.0f).setDuration(250).start();
            }
        });
    }

}
