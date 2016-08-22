package org.wiky.letscorp.pref;

import android.content.Context;

import org.wiky.letscorp.R;

/**
 * Created by wiky on 8/22/16.
 * 通用设置
 */
public class GeneralPreferences extends BasePreferences {

    private final static String RANDOM_USERNAME = "RANDOM_USERNAME";
    private final static String USERNAME = "USERNAME";

    public GeneralPreferences(Context context) {
        super(context);
    }

    public boolean isRandomUsername() {
        return open().getBoolean(RANDOM_USERNAME, true);
    }

    public void setRandomUsername(boolean randomUsername) {
        edit().putBoolean(RANDOM_USERNAME, randomUsername).commit();
    }

    public String getUsername() {
        return open().getString(USERNAME, context.getString(R.string.anonymous));
    }

    public void setUsername(String username) {
        edit().putString(USERNAME, username).commit();
    }
}
