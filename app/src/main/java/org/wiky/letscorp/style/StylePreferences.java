package org.wiky.letscorp.style;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by wiky on 8/9/16.
 * 样式设置
 */
public class StylePreferences {
    private final static String LIST_FONT_STYLE = "LIST_FONT_STYLE";
    private final static String POST_FONT_STYLE = "POST_FONT_STYLE";

    private final Context context;

    public StylePreferences(Context context) {
        this.context = context;
    }

    protected SharedPreferences open() {
        return context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }

    protected SharedPreferences.Editor edit() {
        return open().edit();
    }

    public ListFontStyle getListFontStyle() {
        return ListFontStyle.valueOf(open().getString(LIST_FONT_STYLE,
                ListFontStyle.Medium.name()));
    }

    public void setListFontStyle(ListFontStyle style) {
        edit().putString(LIST_FONT_STYLE, style.name()).commit();
    }

    public PostFontStyle getPostFontStyle() {
        return PostFontStyle.valueOf(open().getString(POST_FONT_STYLE, PostFontStyle.Medium.name()));
    }

    public void setPostFontStyle(PostFontStyle style) {
        edit().putString(POST_FONT_STYLE, style.name()).commit();
    }
}