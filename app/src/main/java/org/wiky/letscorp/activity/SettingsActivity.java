package org.wiky.letscorp.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.wiky.letscorp.Application;
import org.wiky.letscorp.R;
import org.wiky.letscorp.data.db.PostHelper;
import org.wiky.letscorp.data.db.PostItemHelper;
import org.wiky.letscorp.util.Util;

import java.util.Objects;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setTitle(R.string.settings);
        getFragmentManager().beginTransaction().replace(R.id.settings_frame, new SettingsFragment()).commit();
    }


    public static class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

        private AboutDialog mAboutDialog;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);

            findPreference(getString(R.string.key_clear_cache)).setOnPreferenceClickListener(this);
            findPreference(getString(R.string.key_about)).setOnPreferenceClickListener(this);

            mAboutDialog = new AboutDialog(getActivity());
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            String key = preference.getKey();
            if (Objects.equals(key, getString(R.string.key_about))) {
                mAboutDialog.show();
            } else if (Objects.equals(key, getString(R.string.key_clear_cache))) {
                PostHelper.deletePosts();
                PostItemHelper.deletePostItems();
                Toast.makeText(getActivity(), R.string.toast_cache_deleted, Toast.LENGTH_SHORT).show();
            } else {
                return false;
            }
            return true;
        }

        /*
         * 关于对话框
         */
        private static class AboutDialog extends AlertDialog implements View.OnClickListener {

            private static final String TWITTER_URL = "https://twitter.com/letscorp";
            private static final String GOOGLE_PLUS_URL = "https://plus.google.com/+LetscorpNet";
            private static final String GITHUB_URL = "https://github.com/hitoshii/letscorp";

            private View mOK;


            protected AboutDialog(@NonNull Context context) {
                super(context);
            }

            @Override
            public void show() {
                super.show();
                setContentView(R.layout.dialog_about);
                Context context = getContext();
                View twitter = findViewById(R.id.about_twitter);
                View gplus = findViewById(R.id.about_gplus);
                View github = findViewById(R.id.about_github);
                mOK = findViewById(R.id.about_ok);
                TextView version = (TextView) findViewById(R.id.about_version);
                TextView develop = (TextView) findViewById(R.id.about_developer);

                try {
                    version.setText(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                develop.setText(Html.fromHtml(context.getString(R.string.developer_statement)));
                develop.setMovementMethod(LinkMovementMethod.getInstance());

                twitter.setOnClickListener(this);
                gplus.setOnClickListener(this);
                github.setOnClickListener(this);
                mOK.setOnClickListener(this);
            }


            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.about_twitter) {
                    Util.openBrowser(TWITTER_URL);
                } else if (view.getId() == R.id.about_gplus) {
                    Util.openBrowser(GOOGLE_PLUS_URL);
                } else if (view.getId() == R.id.about_github) {
                    Util.openBrowser(GITHUB_URL);
                } else if (view.getId() == R.id.about_ok) {
                    Application.getUIHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dismiss();
                        }
                    }, 250);
                    mOK.setOnClickListener(null);
                }
            }
        }

    }
}
