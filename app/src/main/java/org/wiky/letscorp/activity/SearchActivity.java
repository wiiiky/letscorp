package org.wiky.letscorp.activity;

import android.os.Bundle;

import org.wiky.letscorp.R;
import org.wiky.letscorp.view.SearchView;

/**
 * Created by wiky on 7/23/16.
 */
public class SearchActivity extends BaseActivity {

    private SearchView mSearchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        String query = getIntent().getStringExtra("query");

        mSearchView = (SearchView) findViewById(R.id.search_search_view);
        mSearchView.setQuery(query);
    }
}
