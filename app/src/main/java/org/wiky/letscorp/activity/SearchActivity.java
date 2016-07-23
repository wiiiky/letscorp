package org.wiky.letscorp.activity;

import android.animation.Animator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;

import org.wiky.letscorp.R;
import org.wiky.letscorp.util.Util;
import org.wiky.letscorp.view.SearchView;

/**
 * Created by wiky on 7/23/16.
 */
public class SearchActivity extends BaseActivity {

    private SearchView mSearchView;
    private int mSearchCX;
    private int mSearchCY;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSearchCX = getIntent().getIntExtra("cx", 0);
        mSearchCY= getIntent().getIntExtra("cy", 0);

        mSearchView = (SearchView) findViewById(R.id.search_search_view);
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
}
