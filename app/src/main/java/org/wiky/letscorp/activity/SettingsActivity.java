package org.wiky.letscorp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.wiky.letscorp.Application;
import org.wiky.letscorp.R;
import org.wiky.letscorp.data.db.PostHelper;
import org.wiky.letscorp.data.db.PostItemHelper;
import org.wiky.letscorp.data.db.QueryHelper;
import org.wiky.letscorp.pref.style.ListFontStyle;
import org.wiky.letscorp.pref.style.PostFontStyle;

import java.util.Objects;

public class SettingsActivity extends BaseActivity {

    private ListFontStyle mPrevListFontStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mPrevListFontStyle = mStylePref.getListFontStyle();

        setTitle(R.string.settings);
        getFragmentManager().beginTransaction().replace(R.id.settings_frame, new SettingsFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /* 如果列表页字体大小设置变化，则更新主页 */
        if (mPrevListFontStyle != mStylePref.getListFontStyle()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public static class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            Preference pref;
            CheckBoxPreference checkPref;

            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);

            checkPref = (CheckBoxPreference) findPreference(getString(R.string.pref_key_comment_username));
            checkPref.setChecked(Application.getGeneralPreferences().isRandomUsername());
            checkPref.setOnPreferenceChangeListener(this);

            pref = findPreference(getString(R.string.pref_key_list_font));
            pref.setSummary(getListFontStyle().title());
            pref.setOnPreferenceClickListener(this);

            pref = findPreference(getString(R.string.pref_key_post_font));
            pref.setSummary(getPostFontStyle().title());
            pref.setOnPreferenceClickListener(this);

            findPreference(getString(R.string.pref_key_clear_cache)).setOnPreferenceClickListener(this);
            findPreference(getString(R.string.pref_key_clear_query)).setOnPreferenceClickListener(this);
        }


        private ListFontStyle getListFontStyle() {
            return Application.getStylePreferences().getListFontStyle();
        }

        private void setListFontStyle(int index) {
            ListFontStyle[] styles = ListFontStyle.values();
            for (int i = 0; i < styles.length; i++) {
                if (i == index) {
                    findPreference(getString(R.string.pref_key_list_font)).setSummary(styles[i].title());
                    Application.getStylePreferences().setListFontStyle(styles[i]);
                    break;
                }
            }
        }

        private PostFontStyle getPostFontStyle() {
            return Application.getStylePreferences().getPostFontStyle();
        }

        private void setPostFontStyle(int index) {
            PostFontStyle[] styles = PostFontStyle.values();
            for (int i = 0; i < styles.length; i++) {
                if (i == index) {
                    findPreference(getString(R.string.pref_key_post_font)).setSummary(styles[i].title());
                    Application.getStylePreferences().setPostFontStyle(styles[i]);
                    break;
                }
            }
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            String key = preference.getKey();
            if (Objects.equals(key, getString(R.string.pref_key_clear_cache))) {
                clearCache();
            } else if (Objects.equals(key, getString(R.string.pref_key_clear_query))) {
                clearQuery();
            } else if (Objects.equals(key, getString(R.string.pref_key_list_font))) {
                changeListFontStyle();
            } else if (Objects.equals(key, getString(R.string.pref_key_post_font))) {
                changePostFontStyle();
            } else {
                return false;
            }
            return true;
        }

        /* 更改文章页面字体大小 */
        private void changePostFontStyle() {
            new MaterialDialog.Builder(getActivity())
                    .title(R.string.post_font_size)
                    .items(PostFontStyle.items())
                    .autoDismiss(false)
                    .positiveText(R.string.ok)
                    .itemsCallbackSingleChoice(getPostFontStyle().index(), new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                            return true;
                        }
                    })
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull final MaterialDialog dialog, @NonNull DialogAction which) {
                            setPostFontStyle(dialog.getSelectedIndex());
                            Application.getUIHandler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                }
                            }, 250);
                        }
                    })
                    .show();
        }

        /* 更改列表页字体大小 */
        private void changeListFontStyle() {
            new MaterialDialog.Builder(getActivity())
                    .title(R.string.list_font_size)
                    .items(ListFontStyle.items())
                    .autoDismiss(false)
                    .positiveText(R.string.ok)
                    .itemsCallbackSingleChoice(getListFontStyle().index(), new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                            return true;
                        }
                    })
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull final MaterialDialog dialog, @NonNull DialogAction which) {
                            setListFontStyle(dialog.getSelectedIndex());
                            Application.getUIHandler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                }
                            }, 250);
                        }
                    })
                    .show();
        }

        /* 删除缓存 */
        private void clearCache() {
            PostHelper.clear();
            PostItemHelper.clear();
            Toast.makeText(getActivity(), R.string.cache_deleted, Toast.LENGTH_SHORT).show();
        }

        private void clearQuery() {
            QueryHelper.clear();
            Toast.makeText(getActivity(), R.string.search_history_deleted, Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            Application.getGeneralPreferences().setRandomUsername((Boolean) o);
            return true;
        }
    }
}
