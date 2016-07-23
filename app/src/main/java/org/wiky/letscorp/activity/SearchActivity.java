package org.wiky.letscorp.activity;

import android.os.Bundle;
import android.widget.Toast;

import org.wiky.letscorp.R;
import org.wiky.letscorp.view.SearchView;
import org.wiky.letscorp.view.SwipeRefreshLayout;

public class SearchActivity extends BaseActivity implements SearchView.OnSearchListener {

    private SearchView mSearchView;
    private int mSearchCX;
    private int mSearchCY;
    private SwipeRefreshLayout mRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSearchCX = getIntent().getIntExtra("cx", 0);
        mSearchCY= getIntent().getIntExtra("cy", 0);

        mSearchView = (SearchView) findViewById(R.id.search_search_view);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.search_swipe_refresh);

        mSearchView.setOnSearchListener(this);
        mSearchView.post(new Runnable() {
            @Override
            public void run() {
                mSearchView.show(mSearchCX, mSearchCY);
            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        mSearchView.hide(mSearchCX, mSearchCY);
    }

    @Override
    public void onSearch(String query) {
        mRefreshLayout.setRefreshing(true);
        Toast.makeText(this, String.format("%d,%d", mRefreshLayout.getWidth(), mRefreshLayout.getHeight()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onQueryChanged(String query) {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onSearchBack() {
        onBackPressed();
    }
}
