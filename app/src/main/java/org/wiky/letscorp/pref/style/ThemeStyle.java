package org.wiky.letscorp.pref.style;

import org.wiky.letscorp.Application;
import org.wiky.letscorp.R;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by wiky on 8/23/16.
 */
public enum ThemeStyle {
    LIGHT(R.style.ThemeStyle_Light, 0),
    DARK(R.style.ThemeStyle_Dark, 1);

    private int resId;
    private int index;

    ThemeStyle(int resId, int index) {
        this.resId = resId;
        this.index = index;
    }

    public static Collection<String> items() {
        Collection<String> items = new ArrayList<>();
        for (ThemeStyle style : ThemeStyle.values()) {
            items.add(style.title());
        }
        return items;
    }

    public int resid() {
        return resId;
    }

    public String title() {
        switch (resId) {
            case R.style.ThemeStyle_Light:
                return Application.getApplication().getString(R.string.light);
            case R.style.ThemeStyle_Dark:
                return Application.getApplication().getString(R.string.dark);
        }
        return "Unknown";
    }

    public int index() {
        return index;
    }
}
