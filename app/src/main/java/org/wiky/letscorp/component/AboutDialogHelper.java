package org.wiky.letscorp.component;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import org.wiky.letscorp.R;
import org.wiky.letscorp.util.Util;

public class AboutDialogHelper {

    private static final String TWITTER_URL = "https://twitter.com/letscorp";
    private static final String GOOGLE_PLUS_URL = "https://plus.google.com/+LetscorpNet";
    private static final String GITHUB_URL = "https://github.com/hitoshii/letscorp";
    private static View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.about_twitter) {
                Util.openURL(TWITTER_URL);
            } else if (view.getId() == R.id.about_gplus) {
                Util.openURL(GOOGLE_PLUS_URL);
            } else if (view.getId() == R.id.about_github) {
                Util.openURL(GITHUB_URL);
            }
        }
    };

    public static void showDialog(Context context) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(R.string.action_about)
                .customView(R.layout.dialog_about, true)
                .positiveText(R.string.ok)
                .show();

        View twitter = dialog.findViewById(R.id.about_twitter);
        View gplus = dialog.findViewById(R.id.about_gplus);
        View github = dialog.findViewById(R.id.about_github);
        TextView version = (TextView) dialog.findViewById(R.id.about_version);

        try {
            assert version != null;
            version.setText(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        assert twitter != null;
        twitter.setOnClickListener(mOnClickListener);
        assert gplus != null;
        gplus.setOnClickListener(mOnClickListener);
        assert github != null;
        github.setOnClickListener(mOnClickListener);
    }
}