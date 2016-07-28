package org.wiky.letscorp.util;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.InputMethodManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.wiky.letscorp.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wiky on 6/15/16.
 */
public class Util {

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

    public static void openURL(String url) {
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

    public static void hideInputKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) Application.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /* 解析数字 */
    public static int parseInt(String str) {
        StringBuilder v = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                v.append(str.charAt(i));
            } else {
                break;
            }
        }
        try {
            return Integer.parseInt(v.toString());
        } catch (Exception e) {
            return 0;
        }
    }

    /* 创建reveal动画，cx和cy分别是圆心相对于屏幕的坐标 */
    public static Animator createCircularReveal(View view, int cx, int cy, boolean show) {
        int[] pos = Util.getScreenLocation(view);
        cx -= pos[0];
        cy -= pos[1];
        float r = (float) Math.hypot(view.getWidth(), view.getHeight());
        float startRadius = 0;
        float endRadius = r;
        if (!show) {
            startRadius = r;
            endRadius = 0;
        }
        return ViewAnimationUtils.createCircularReveal(view, cx, cy, startRadius, endRadius);
    }
}
