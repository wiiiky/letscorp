package org.wiky.letscorp.pref.style;

import org.wiky.letscorp.Application;
import org.wiky.letscorp.R;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by wiky on 8/9/16.
 */
public enum PostFontStyle {
    Small(R.style.PostFontStyle_Small, 0),
    Medium(R.style.PostFontStyle_Medium, 1),
    Large(R.style.PostFontStyle_Large, 2);

    private int resId;
    private int index;

    PostFontStyle(int resId, int index) {
        this.resId = resId;
        this.index = index;
    }

    public static Collection<String> items() {
        Collection<String> items = new ArrayList<>();
        for (PostFontStyle style : PostFontStyle.values()) {
            items.add(style.title());
        }
        return items;
    }

    public int resid() {
        return resId;
    }

    public String title() {
        switch (resId) {
            case R.style.PostFontStyle_Small:
                return Application.getApplication().getString(R.string.small);
            case R.style.PostFontStyle_Medium:
                return Application.getApplication().getString(R.string.medium);
            case R.style.PostFontStyle_Large:
                return Application.getApplication().getString(R.string.large);
        }
        return "Unknown";
    }

    public int index() {
        return index;
    }
}
