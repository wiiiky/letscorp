package org.wiky.letscorp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import org.wiky.letscorp.R;
import org.wiky.letscorp.data.db.PostHelper;
import org.wiky.letscorp.data.db.PostItemHelper;

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
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);

            findPreference("about").setOnPreferenceClickListener(this);
            findPreference("clear_cache").setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            String key = preference.getKey();
            if (Objects.equals(key, "about")) {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
            } else if (Objects.equals(key, "clear_cache")) {
                PostHelper.deletePosts();
                PostItemHelper.deletePostItems();
                Toast.makeText(getActivity(), R.string.toast_cache_deleted, Toast.LENGTH_SHORT).show();
            } else {
                return false;
            }
            return true;
        }
    }
}
