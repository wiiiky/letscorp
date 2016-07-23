package org.wiky.letscorp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
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

import org.wiky.letscorp.Application;
import org.wiky.letscorp.R;
import org.wiky.letscorp.api.Const;
import org.wiky.letscorp.data.model.Post;
import org.wiky.letscorp.data.model.PostItem;
import org.wiky.letscorp.list.PostListAdapter;
import org.wiky.letscorp.list.PostListView;
import org.wiky.letscorp.signal.Signal;
import org.wiky.letscorp.util.Util;
import org.wiky.letscorp.view.AboutDialogHelper;
import org.wiky.letscorp.view.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements TabLayout.OnTabSelectedListener{

    private PageAdapter mPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_name);

        mPagerAdapter = new PageAdapter(this, getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.addOnTabSelectedListener(this);

        startToolbarAnimation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            onSearch();
        } else if (id == R.id.action_browser) {
            Util.openBrowser(Const.LETSCORP_HOST);
        } else if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_about) {
            AboutDialogHelper.showDialog(this);
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        PostListFragment page = (PostListFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.container + ":" + mViewPager.getCurrentItem());
        if (page != null) {
            page.scrollToTop();
        }
    }


    public void onSearch() {
        Intent intent = new Intent(this, SearchActivity.class);
        int[] pos=Util.getViewCenterOnScreen(findViewById(R.id.action_search));
        intent.putExtra("cx", pos[0]);
        intent.putExtra("cy", pos[1]);

        View statusBar = findViewById(android.R.id.statusBarBackground);
        View navigationBar = findViewById(android.R.id.navigationBarBackground);

        List<Pair<View, String>> pairs = new ArrayList<>();
        pairs.add(Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME));
        pairs.add(Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME));
        pairs.add(Pair.create((View) mAppBar, mAppBar.getTransitionName()));

        Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                pairs.toArray(new Pair[pairs.size()])).toBundle();
        startActivity(intent, options);
    }

    public static class PostListFragment extends Fragment implements PostListAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, PostListView.OnRefreshListener, Signal.SignalListener {
        private static final String ARG_CATEGORY = "category";
        private PostListView mPostList;
        private SwipeRefreshLayout mRefreshLayout;
        private int mCategory;

        public PostListFragment() {
        }

        public static PostListFragment newInstance(int sectionNumber) {
            PostListFragment fragment = new PostListFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_CATEGORY, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public void scrollToTop() {
            mPostList.smoothScrollToPosition(0);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            mCategory = getArguments().getInt(ARG_CATEGORY);
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            mPostList = (PostListView) rootView.findViewById(R.id.main_post_list);
            mRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.main_swipe_refresh);

            mPostList.setCategory(mCategory);
            mPostList.setOnItemClickListener(this);
            mPostList.setOnRefreshListener(this);
            if (!mPostList.loadLocal()) {
                Application.getUIHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPostList.resetItems();
                    }
                }, 150);
            }

            mRefreshLayout.setOnRefreshListener(this);

            Signal.register(Signal.SIGINT_ITEM_READN, this);
            return rootView;
        }

        @Override
        public void onDestroy() {
            Signal.unregister(Signal.SIGINT_ITEM_READN, this);
            super.onDestroy();
        }

        @Override
        public void onItemClick(PostListAdapter.PostItemHolder holder, PostItem data) {
            PostActivity.startPostActivity(getActivity(), data);
        }

        @Override
        public void onRefresh() {
            mPostList.resetItems();
        }

        @Override
        public void onRefresh(boolean r) {
            mRefreshLayout.setRefreshing(r);
        }

        @Override
        public void onSignal(int signal, Object data) {
            if (signal == Signal.SIGINT_ITEM_READN) {
                Post post = (Post) data;
                mPostList.setItemReadn(post.href);
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private static class PageAdapter extends FragmentPagerAdapter {

        private List<CategoryInfo> mCategories = new ArrayList<>();

        public PageAdapter(Context context, FragmentManager fm) {
            super(fm);
            mCategories.add(new CategoryInfo(Const.LETSCORP_CATEGORY_ALL, context.getString(R.string.category_all)));
            mCategories.add(new CategoryInfo(Const.LETSCORP_CATEGORY_NEWS, context.getString(R.string.category_news)));
            mCategories.add(new CategoryInfo(Const.LETSCORP_CATEGORY_VIEW, context.getString(R.string.category_view)));
            mCategories.add(new CategoryInfo(Const.LETSCORP_CATEGORY_POLITICS, context.getString(R.string.category_politics)));
            mCategories.add(new CategoryInfo(Const.LETSCORP_CATEGORY_ECONOMICS, context.getString(R.string.category_economics)));
            mCategories.add(new CategoryInfo(Const.LETSCORP_CATEGORY_RUMOR, context.getString(R.string.category_rumor)));
            mCategories.add(new CategoryInfo(Const.LETSCORP_CATEGORY_TECH, context.getString(R.string.category_tech)));
            mCategories.add(new CategoryInfo(Const.LETSCORP_CATEGORY_HISTORY, context.getString(R.string.category_history)));
            mCategories.add(new CategoryInfo(Const.LETSCORP_CATEGORY_GALLERY, context.getString(R.string.category_gallery)));
        }

        @Override
        public Fragment getItem(int position) {
            return PostListFragment.newInstance(getCategory(position));
        }

        @Override
        public int getCount() {
            return mCategories.size();
        }

        private int getCategory(int position) {
            CategoryInfo info = mCategories.get(position);
            return info.category;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            CategoryInfo info = mCategories.get(position);
            return info.name;
        }

        private class CategoryInfo {
            public int category;
            public String name;

            public CategoryInfo(int c, String n) {
                category = c;
                name = n;
            }
        }
    }
}
