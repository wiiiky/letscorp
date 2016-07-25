package org.wiky.letscorp.activity;

import android.os.Bundle;
import android.support.v7.widget.ListPopupWindow;
import android.text.TextUtils;
import android.transition.Transition;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import org.wiky.letscorp.R;
import org.wiky.letscorp.adapter.PostListAdapter;
import org.wiky.letscorp.adapter.QueryAdapter;
import org.wiky.letscorp.data.db.QueryHelper;
import org.wiky.letscorp.data.model.Post;
import org.wiky.letscorp.data.model.PostItem;
import org.wiky.letscorp.data.model.Query;
import org.wiky.letscorp.list.BasePostListVIew;
import org.wiky.letscorp.list.SearchListView;
import org.wiky.letscorp.signal.Signal;
import org.wiky.letscorp.util.Util;
import org.wiky.letscorp.view.SearchBox;
import org.wiky.letscorp.view.SwipeRefreshLayout;

public class SearchActivity extends BaseActivity implements SearchBox.OnSearchListener,
        BasePostListVIew.OnRefreshListener,
        android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener,
        PostListAdapter.OnItemClickListener, QueryAdapter.OnItemClickListener,
        Transition.TransitionListener, Signal.SignalListener {

    private SearchBox mSearchBox;
    private ListPopupWindow mQueryPopupWindow;
    private QueryAdapter mQueryAdapter;
    private int mSearchCX;
    private int mSearchCY;
    private SwipeRefreshLayout mRefreshLayout;
    private SearchListView mSearchList;
    private String mQuery = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSearchCX = getIntent().getIntExtra("cx", 0);
        mSearchCY= getIntent().getIntExtra("cy", 0);

        mSearchBox = (SearchBox) findViewById(R.id.search_search_box);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.search_swipe_refresh);
        mSearchList = (SearchListView) findViewById(R.id.search_post_list);

        mRefreshLayout.setOnRefreshListener(this);
        mSearchList.setOnRefreshListener(this);
        mSearchList.setOnItemClickListener(this);
        mSearchBox.setOnSearchListener(this);

        mSearchBox.post(new Runnable() {
            @Override
            public void run() {
                mSearchBox.show(mSearchCX, mSearchCY);
            }
        });

        getWindow().getSharedElementEnterTransition().addListener(this);
        Signal.register(Signal.SIGINT_ITEM_READN, this);
    }


    @Override
    public void onBackPressed(){
        getWindow().getSharedElementEnterTransition().removeListener(this);
        super.onBackPressed();
        mSearchBox.hide(mSearchCX, mSearchCY);
        if (mQueryPopupWindow != null) {
            mQueryPopupWindow.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }


    @Override
    public void onSearch(String query) {
        mQuery = query;
        mSearchList.search(mQuery);
        Util.hideInputKeyboard(mSearchBox);
        Toast.makeText(this, R.string.search_patient, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onQueryChanged(String query) {
        if (mQueryPopupWindow == null) {
            return;
        }
        mQueryAdapter.update(query);
        if (!mQueryPopupWindow.isShowing()) {
            mQueryPopupWindow.show();
        }
    }

    @Override
    public void onSearchBack() {
        onBackPressed();
    }

    @Override
    public void onRefresh(boolean r) {
        mRefreshLayout.setRefreshing(r);
    }

    @Override
    public void onRefresh() {
        if (TextUtils.isEmpty(mQuery)) {
            mRefreshLayout.setRefreshing(false);
        } else {
            onSearch(mQuery);
        }
    }

    @Override
    public void onItemClick(PostListAdapter.PostItemHolder holder, PostItem data) {
        PostActivity.startPostActivity(this, data);
    }

    @Override
    public void onItemClick(View v, String query) {
        mQueryPopupWindow.dismiss();
        mSearchBox.setQuery(query);
        QueryHelper.saveQuery(new Query(query));
        onSearch(query);
    }

    @Override
    public void onTransitionStart(Transition transition) {

    }

    @Override
    public void onTransitionEnd(Transition transition) {
        mQueryPopupWindow = new ListPopupWindow(this);
        mQueryAdapter = new QueryAdapter();
        mQueryAdapter.setOnItemClickListener(this);
        mQueryPopupWindow.setAnchorView(mSearchBox);
        mQueryPopupWindow.setAdapter(mQueryAdapter);
        mQueryPopupWindow.setVerticalOffset((int) Util.dp2px(2));
        mQueryPopupWindow.show();
    }

    @Override
    public void onTransitionCancel(Transition transition) {

    }

    @Override
    public void onTransitionPause(Transition transition) {

    }

    @Override
    public void onTransitionResume(Transition transition) {

    }

    @Override
    public void onSignal(int signal, Object data) {
        if (signal == Signal.SIGINT_ITEM_READN) {
            Post post = (Post) data;
            mSearchList.setItemReadn(post.href);
        }
    }
}
