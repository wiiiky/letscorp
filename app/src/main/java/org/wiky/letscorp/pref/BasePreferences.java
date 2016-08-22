package org.wiky.letscorp.pref;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by wiky on 8/22/16.
 */
public class BasePreferences {

    private final static String PREF = "pref";
    protected final Context context;

    public BasePreferences(Context context) {
        this.context = context;
    }

    protected SharedPreferences open() {
        return context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    protected SharedPreferences.Editor edit() {
        return open().edit();
    }
}
