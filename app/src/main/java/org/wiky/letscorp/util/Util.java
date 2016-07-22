package org.wiky.letscorp.util;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.util.TypedValue;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.wiky.letscorp.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wiky on 6/15/16.
 */
public class Util {

    public static final String HTTP_USER_AGENT = "Mozilla/5.0 (X11; Fedora; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36";
    public static float dp2px(float dp) {
        Resources r = Resources.getSystem();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    /* 将字符串数组序列化为JSON数组，用于数据库保存 */
    public static String serializeStringList(List<String> list) {
        JSONArray array = new JSONArray();
        for (String s : list) {
            array.put(s);
        }
        return array.toString();
    }

    /* 将JSON数组的解析为字符串数组 */
    public static List<String> deserializeStringList(String data) {
        ArrayList<String> list = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(data);
            for (int i = 0; i < array.length(); i++) {
                list.add(array.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String joinString(List<String> strings) {
        StringBuilder s = new StringBuilder();
        int size = strings.size();
        for (int i = 0; i < size; i++) {
            if (i < size - 1) {
                s.append(strings.get(i));
                s.append(",");
            } else {
                s.append(strings.get(i));
            }
        }
        return s.toString();
    }

    public static void openBrowser(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent();
        intent.setData(uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Application.getApplication().startActivity(intent);
    }

    public static CharSequence trim(CharSequence s) {
        return trim(s, 0, s.length());
    }

    public static CharSequence trim(CharSequence s, int start, int end) {
        while (start < end && Character.isWhitespace(s.charAt(start))) {
            start++;
        }

        while (end > start && Character.isWhitespace(s.charAt(end - 1))) {
            end--;
        }

        return s.subSequence(start, end);
    }

    public static int[] getScreenLocation(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        return location;
    }

    public static int[] getViewCenterOnScreen(View v) {
        int[] location = getScreenLocation(v);
        location[0] += v.getWidth() / 2;
        location[1] += v.getHeight() / 2;
        return location;
    }
}
