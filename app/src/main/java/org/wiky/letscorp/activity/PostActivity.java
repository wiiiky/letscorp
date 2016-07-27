package org.wiky.letscorp.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import org.wiky.letscorp.R;
import org.wiky.letscorp.api.Api;
import org.wiky.letscorp.data.model.Comment;
import org.wiky.letscorp.data.model.Post;
import org.wiky.letscorp.data.model.PostItem;
import org.wiky.letscorp.list.CommentListView;
import org.wiky.letscorp.util.Util;
import org.wiky.letscorp.view.PhotoView;
import org.wiky.letscorp.view.PostContent;

import java.util.ArrayList;
import java.util.List;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class PostActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private PageAdapter mPagerAdapter;
    private MaterialProgressBar mProgressBar;
    private ViewPager mViewPager;
    private PostItem mPostItem;
    private Post mPost;
    /* 接口回调处理 */
    private Api.ApiHandler<Post> mPostDetailHandler = new Api.ApiHandler<Post>() {

        @Override
        public void onSuccess(Post post) {
            mPost = post;
            mPagerAdapter.update(mPost);
        }

        @Override
        public void onFinally() {
            hideProgressBar();
        }

        @Override
        public void onError(Exception e) {
            if (mPost != null) {
                Toast.makeText(PostActivity.this, String.format(getString(R.string.reload_post_failure), e != null ? e.getMessage() : ""), Toast.LENGTH_SHORT).show();
            } else {
                super.onError(e);
            }
        }
    };

    public static void startPostActivity(Activity activity, PostItem data) {
        Intent intent = new Intent(activity, PostActivity.class);
        intent.putExtra("data", data);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_post);

        mPostItem = getIntent().getParcelableExtra("data");
        setTitle(mPostItem.title);

        mPagerAdapter = new PageAdapter(getSupportFragmentManager(), mPostItem);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mProgressBar = (MaterialProgressBar) findViewById(R.id.post_loading);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(this);

        mPost = Api.loadPostDetail(mPostItem.href);

        if (mPost != null) {
            /* 有缓存数据时不在显示载入进度条 */
            mProgressBar.setVisibility(View.GONE);
            mPagerAdapter.update(mPost);
            if (mPost.commentCount() == mPostItem.commentCount) {   /* 当评论数量没有改变时不更新 */
                return;
            }
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        /* 即使已经有缓存也要请求网络，因为评论可能会变 */
        Api.fetchPostDetail(mPostItem.href, mPostDetailHandler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_browser) {
            Util.openURL(mPostItem.href);
        }
        return true;
    }

    private void hideProgressBar() {
        if (mProgressBar.getVisibility() != View.VISIBLE) {
            return;
        }
        mProgressBar.animate()
                .alpha(0.0f)
                .setDuration(250)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        mProgressBar.setVisibility(View.GONE);
                    }
                })
                .start();
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 1) {
            mViewPager.setCurrentItem(0, true);
        } else {
            super.onBackPressed();
            overridePendingTransition(0, R.anim.slide_out_right);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position == 0 && positionOffset < 0.5f) {
            setTitle(mPostItem.title);
            mToolBar.setAlpha((0.5f - positionOffset) * 2);
        } else if (position == 0 && positionOffset >= 0.5f) {
            setTitle(R.string.title_comment);
            mToolBar.setAlpha((positionOffset - 0.5f) * 2);
        } else if (position == 1) {
            setTitle(R.string.title_comment);
            mToolBar.setAlpha(1.0f);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 文章详情页面
     */
    public static class PostFragment extends Fragment implements View.OnClickListener {

        private static final String ARG_POST_ITEM = "item";

        private Post mData;
        private TextView mTitle;
        private TextView mAuthor;
        private TextView mCategory;
        private TextView mTag;
        private PostContent mContent;

        public PostFragment() {
        }


        public static PostFragment newInstance(PostItem item) {
            PostFragment fragment = new PostFragment();
            Bundle args = new Bundle();
            args.putParcelable(ARG_POST_ITEM, item);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_post, container, false);
            mTitle = (TextView) rootView.findViewById(R.id.post_title);
            mAuthor = (TextView) rootView.findViewById(R.id.post_author);
            mCategory = (TextView) rootView.findViewById(R.id.post_category);
            mContent = (PostContent) rootView.findViewById(R.id.post_content);
            mTag = (TextView) rootView.findViewById(R.id.post_tag);

            mContent.setOnImageClickListener(this);

            PostItem item = getArguments().getParcelable(ARG_POST_ITEM);

            assert item != null;
            mTitle.setText(item.title);
            if (mData != null) {
                update(mData);
            }
            return rootView;
        }

        public void updateData(Post post) {
            if (!isAdded() || mAuthor == null) {
                mData = post;
            } else {
                update(post);
            }
        }

        private void update(Post post) {
            mAuthor.setText(String.format("%s %s %s", post.author, getString(R.string.published_on), post.date));
            if (!post.categories.isEmpty()) {
                mCategory.setText(String.format("分类：%s", Util.joinString(post.categories)));
            } else {
                mCategory.setVisibility(View.GONE);
            }
            if (!post.tags.isEmpty()) {
                mTag.setText(String.format("标签：%s", Util.joinString(post.tags)));
            } else {
                mTag.setVisibility(View.GONE);
            }
            mContent.setContent(post.content);
        }

        @Override
        public void onClick(View v) {
            if (v instanceof PhotoView) {
                PhotoView photoView = (PhotoView) v;
                Activity activity = getActivity();
                Intent intent = new Intent(activity, ImageActivity.class);
                intent.putExtra("url", photoView.getUrl());
                intent.putExtra("title", activity.getTitle());

                View statusBar = activity.findViewById(android.R.id.statusBarBackground);
                View navigationBar = activity.findViewById(android.R.id.navigationBarBackground);
                View appBar = activity.findViewById(R.id.appbar);

                List<Pair<View, String>> pairs = new ArrayList<>();
                pairs.add(Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME));
                pairs.add(Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME));
                pairs.add(Pair.create((View) photoView, photoView.getTransitionName()));
                pairs.add(Pair.create(appBar, appBar.getTransitionName()));

                Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                        pairs.toArray(new Pair[pairs.size()])).toBundle();
                startActivity(intent, options);
            }
        }
    }

    /* 文章评论页面 */
    public static class PostCommentFragment extends Fragment {
        private static final String ARG_COMMENTS = "comments";

        private CommentListView mCommentList;
        private List<Comment> mData;

        public PostCommentFragment() {
        }

        public static PostCommentFragment newInstance(int sectionNumber) {
            PostCommentFragment fragment = new PostCommentFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_COMMENTS, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_comment, container, false);
            mCommentList = (CommentListView) rootView.findViewById(R.id.post_comment_list);
            if (mData != null) {
                update(mData);
            }
            return rootView;
        }

        public void updateData(List<Comment> comments) {
            if (isAdded() && mCommentList != null) {
                update(comments);
            } else {
                mData = comments;
            }
        }

        private void update(List<Comment> comments) {
            mCommentList.setComments(comments);
        }
    }

    public class PageAdapter extends FragmentPagerAdapter {

        private PostFragment mPostFragment;
        private PostCommentFragment mCommentFragment;

        public PageAdapter(FragmentManager fm, PostItem item) {
            super(fm);
            mPostFragment = PostFragment.newInstance(item);
            mCommentFragment = PostCommentFragment.newInstance(0);
        }

        public void update(Post post) {
            mPostFragment.updateData(post);
            mCommentFragment.updateData(post.comments);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return mPostFragment;
            } else {
                return mCommentFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "POST";
                case 1:
                    return "COMMENT";
            }
            return null;
        }
    }
}
