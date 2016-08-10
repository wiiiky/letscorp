package org.wiky.letscorp.style;

import org.wiky.letscorp.Application;
import org.wiky.letscorp.R;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by wiky on 8/9/16.
 */
public enum ListFontStyle {
    Small(R.style.ListFontStyle_Small, 0),
    Medium(R.style.ListFontStyle_Medium, 1),
    Large(R.style.ListFontStyle_Large, 2);

    private int resId;
    private int index;

    ListFontStyle(int resId, int index) {
        this.resId = resId;
        this.index = index;
    }

    public static Collection<String> items() {
        Collection<String> items = new ArrayList<>();
        for (ListFontStyle style : ListFontStyle.values()) {
            items.add(style.title());
        }
        return items;
    }

    public int resid() {
        return resId;
    }

    public String title() {
        switch (resId) {
            case R.style.ListFontStyle_Small:
                return Application.getApplication().getString(R.string.small);
            case R.style.ListFontStyle_Medium:
                return Application.getApplication().getString(R.string.medium);
            case R.style.ListFontStyle_Large:
                return Application.getApplication().getString(R.string.large);
        }
        return "Unknown";
    }

    public int index() {
        return index;
    }
}
