package org.wiky.letscorp.activity;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.wiky.letscorp.R;

/**
 * Created by wiky on 6/11/16.
 */
public class BaseActivity extends AppCompatActivity {
    @Nullable
    protected Toolbar mToolBar;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        bindViews();
    }

    protected void bindViews() {
        setupToolbar();
    }

    protected void setupToolbar() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolBar != null) {
            setSupportActionBar(mToolBar);
            mToolBar.setNavigationIcon(R.mipmap.ic_menu_white);
        }
    }

    public Toolbar getToolbar() {
        return mToolBar;
    }
}
