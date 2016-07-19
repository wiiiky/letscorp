package org.wiky.letscorp.activity;

import android.animation.Animator;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import org.wiky.letscorp.R;
import org.wiky.letscorp.api.Api;
import org.wiky.letscorp.data.model.Post;
import org.wiky.letscorp.data.model.PostItem;
import org.wiky.letscorp.signal.Signal;
import org.wiky.letscorp.util.Util;
import org.wiky.letscorp.view.PhotoView;
import org.wiky.letscorp.view.PostContent;
import org.wiky.materialprogressbar.MaterialProgressBar;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class PostActivity extends BaseActivity {

    private PageAdapter mPagerAdapter;
    private MaterialProgressBar mProgressBar;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_post2);

        PostItem postItem = getIntent().getParcelableExtra("data");
        setTitle(postItem.title);

        mPagerAdapter = new PageAdapter(getSupportFragmentManager(), postItem);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        OverScrollDecoratorHelper.setUpOverScroll(mViewPager);
        mProgressBar = (MaterialProgressBar) findViewById(R.id.post_loading);
        mViewPager.setAdapter(mPagerAdapter);

        Post post = Api.loadPostDetail(postItem.href);

        if (post != null) {
            mProgressBar.setVisibility(View.GONE);
            mPagerAdapter.update(post);
        } else {
            getPostDetail(postItem.href);
        }
    }

    private void getPostDetail(String href) {
        mProgressBar.setVisibility(View.VISIBLE);
        Api.fetchPostDetail(href, new Api.ApiHandler<Post>() {
            @Override
            public void onSuccess(Post post) {
                mPagerAdapter.update(post);
                Signal.trigger(Signal.SIGINT_ITEM_READN, post);
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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_right);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PostFragment extends Fragment implements View.OnClickListener {

        private static final String ARG_POST_ITEM = "item";

        private Post mPost;
        private TextView mTitle;
        private TextView mAuthor;
        private TextView mCategory;
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

            mContent.setOnImageClickListener(this);

            PostItem item = getArguments().getParcelable(ARG_POST_ITEM);

            assert item != null;
            mTitle.setText(item.title);
            if (mPost != null) {
                update(mPost, false);
            }
            return rootView;
        }

        public void updateData(Post post) {
            if (!isAdded() || mAuthor == null) {
                mPost = post;
            } else {
                update(post, true);
            }
        }

        private void update(Post post, boolean animated) {
            mAuthor.setText(String.format("%s %s %s", post.author, getString(R.string.published_on), post.date));
            mCategory.setText(Util.joinString(post.categories));
            mContent.setContent(post.content);
            if (animated) {
                mContent.setAlpha(0.0f);
                mContent.animate().alpha(1.0f).setDuration(250).start();
            }
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

    public static class PostCommentFragment extends Fragment {
        private static final String ARG_COMMENTS = "comments";

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
            return rootView;
        }

        public void updateData() {
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
            mCommentFragment.updateData();
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
