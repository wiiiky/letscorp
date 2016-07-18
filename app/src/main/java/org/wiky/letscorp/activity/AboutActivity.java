package org.wiky.letscorp.activity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        View twitter = findViewById(R.id.about_twitter);
        View gplus = findViewById(R.id.about_gplus);
        View github = findViewById(R.id.about_github);
        TextView power = (TextView) findViewById(R.id.about_power);

        power.setText(Html.fromHtml("Unofficial! Powered By <a href=\"https://m.letscorp.net\">m.letscorp.net</a>"));
        power.setMovementMethod(LinkMovementMethod.getInstance());
        twitter.setOnClickListener(this);
        gplus.setOnClickListener(this);
        github.setOnClickListener(this);

        setTitle(R.string.about);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.about_twitter) {
            Util.openBrowser("https://twitter.com/letscorp");
        } else if (view.getId() == R.id.about_gplus) {
            Util.openBrowser("https://plus.google.com/+LetscorpNet");
        } else if (view.getId() == R.id.about_github) {
            Util.openBrowser("https://github.com/hitoshii/letscorp");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
