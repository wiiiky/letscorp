package org.wiky.letscorp.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.wiky.letscorp.Application;
import org.wiky.letscorp.R;
import org.wiky.letscorp.style.StylePreferences;
import org.wiky.letscorp.util.Util;


public class BaseActivity extends AppCompatActivity {
    protected Toolbar mToolBar;
    protected AppBarLayout mAppBar;
    protected StylePreferences mStylePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStylePref = Application.getStylePreferences();
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

}
