package org.wiky.letscorp.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;

import org.wiky.letscorp.Application;
import org.wiky.letscorp.R;
import org.wiky.letscorp.pref.GeneralPreferences;
import org.wiky.letscorp.pref.StylePreferences;
import org.wiky.letscorp.util.Util;

import java.util.ArrayList;
import java.util.List;


public class BaseActivity extends AppCompatActivity {
    protected Toolbar mToolBar;
    protected AppBarLayout mAppBar;
    protected StylePreferences mStylePref;
    protected GeneralPreferences mGeneralPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStylePref = Application.getStylePreferences();
        mGeneralPref = Application.getGeneralPreferences();
        getTheme().applyStyle(mStylePref.getListFontStyle().resid(), true);
        getTheme().applyStyle(mStylePref.getPostFontStyle().resid(), true);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        bindViews();
    }

    protected void bindViews() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mAppBar = (AppBarLayout) findViewById(R.id.appbar);
        setupToolbar();
    }

    protected void setupToolbar() {
        if (mToolBar != null) {
            setSupportActionBar(mToolBar);
        }
    }

    protected void startToolbarAnimation() {
        float actionbarSize = Util.dp2px(56);
        mToolBar.setTranslationY(-actionbarSize);
        mToolBar.animate().translationY(0)
                .setDuration(300)
                .setStartDelay(300);
    }

    protected Pair[] makeSceneTransitionPairs(View... views) {
        List<Pair<View, String>> pairs = new ArrayList<>();

        View statusBar = findViewById(android.R.id.statusBarBackground);
        View navigationBar = findViewById(android.R.id.navigationBarBackground);

        if (statusBar != null) {
            pairs.add(Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME));
        }
        if (navigationBar != null) {
            pairs.add(Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME));
        }
        for (View v : views) {
            if (v != null) {
                pairs.add(Pair.create(v, v.getTransitionName()));
            }
        }
        return pairs.toArray(new Pair[pairs.size()]);
    }

}
