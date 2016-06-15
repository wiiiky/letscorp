package org.wiky.letscorp.activity;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wiky.letscorp.R;

/**
 * Created by wiky on 6/15/16.
 */
public class BaseDrawerActivity extends BaseActivity implements View.OnClickListener {
    protected DrawerLayout mDrawerLayout;
    protected NavigationView mNavigation;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_drawer);
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.flContentRoot);
        LayoutInflater.from(this).inflate(layoutResID, viewGroup, true);
        bindViews();
    }

    @Override
    protected void bindViews() {
        super.bindViews();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigation = (NavigationView) findViewById(R.id.vNavigation);

        if (mToolBar != null) {
            mToolBar.setNavigationOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

}
