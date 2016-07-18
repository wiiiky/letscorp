package org.wiky.letscorp.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import org.wiky.letscorp.R;
import org.wiky.letscorp.util.Util;

/*
 * 关于界面
 */
public class AboutActivity extends BaseActivity implements View.OnClickListener {

    private static final String TWITTER_URL = "https://twitter.com/letscorp";
    private static final String GOOGLE_PLUS_URL = "https://plus.google.com/+LetscorpNet";
    private static final String GITHUB_URL = "https://github.com/hitoshii/letscorp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        View twitter = findViewById(R.id.about_twitter);
        View gplus = findViewById(R.id.about_gplus);
        View github = findViewById(R.id.about_github);
        TextView power = (TextView) findViewById(R.id.about_power);
        TextView version = (TextView) findViewById(R.id.about_version);
        TextView develop = (TextView) findViewById(R.id.about_developer);

        try {
            version.setText(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        power.setText(Html.fromHtml(getString(R.string.app_statement)));
        develop.setText(Html.fromHtml(getString(R.string.developer_statement)));
        power.setMovementMethod(LinkMovementMethod.getInstance());
        develop.setMovementMethod(LinkMovementMethod.getInstance());
        twitter.setOnClickListener(this);
        gplus.setOnClickListener(this);
        github.setOnClickListener(this);

        setTitle(R.string.about);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.about_twitter) {
            Util.openBrowser(TWITTER_URL);
        } else if (view.getId() == R.id.about_gplus) {
            Util.openBrowser(GOOGLE_PLUS_URL);
        } else if (view.getId() == R.id.about_github) {
            Util.openBrowser(GITHUB_URL);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
