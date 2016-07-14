package org.wiky.letscorp.util;

import android.content.res.Resources;
import android.util.TypedValue;

import org.json.JSONArray;
import org.json.JSONException;

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

}
