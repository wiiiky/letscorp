package org.wiky.letscorp.activity;

import android.os.Bundle;
import android.support.v7.widget.ListPopupWindow;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import org.wiky.letscorp.R;
import org.wiky.letscorp.adapter.PostListAdapter;
import org.wiky.letscorp.adapter.QueryAdapter;
import org.wiky.letscorp.data.model.PostItem;
import org.wiky.letscorp.list.BasePostListVIew;
import org.wiky.letscorp.list.SearchListView;
import org.wiky.letscorp.util.Util;
import org.wiky.letscorp.view.SearchBox;
import org.wiky.letscorp.view.SwipeRefreshLayout;

public class SearchActivity extends BaseActivity implements SearchBox.OnSearchListener, BasePostListVIew.OnRefreshListener, android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener, PostListAdapter.OnItemClickListener, QueryAdapter.OnItemClickListener {

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
    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        mSearchBox.hide(mSearchCX, mSearchCY);
        mQueryPopupWindow.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
            mQueryPopupWindow = new ListPopupWindow(this);
            mQueryAdapter = new QueryAdapter();
            mQueryAdapter.setOnItemClickListener(this);
            mQueryPopupWindow.setAnchorView(mSearchBox);
            mQueryPopupWindow.setAdapter(mQueryAdapter);
        }
        mQueryAdapter.update(query);
        mQueryPopupWindow.show();
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
        onSearch(query);
    }
}
