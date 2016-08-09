package org.wiky.letscorp.style;

import org.wiky.letscorp.R;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by wiky on 8/9/16.
 */
public enum PostFontStyle {
    Small(R.style.PostFontStyle_Small, "Small", 0),
    Medium(R.style.PostFontStyle_Medium, "Medium", 1),
    Large(R.style.PostFontStyle_Large, "Large", 2);

    private int resId;
    private String title;
    private int index;

    PostFontStyle(int resId, String title, int index) {
        this.resId = resId;
        this.title = title;
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
        return title;
    }

    public int index() {
        return index;
    }
}
