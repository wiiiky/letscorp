package org.wiky.letscorp.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wiky on 6/11/16.
 */
public final class Const {
    public final static String LETSCORP_HOST = "https://m.letscorp.net";

    public final static int LETSCORP_CATEGORY_ALL = 0;
    public final static int LETSCORP_CATEGORY_ECONOMICS = 1;
    public final static int LETSCORP_CATEGORY_NEWS = 2;

    private static Map<Integer, String> mCategoryURL = new HashMap<>();

    static {
        mCategoryURL.put(LETSCORP_CATEGORY_ALL, LETSCORP_HOST + "/page/");
        mCategoryURL.put(LETSCORP_CATEGORY_ECONOMICS, getCategoryURL(LETSCORP_CATEGORY_ECONOMICS));
        mCategoryURL.put(LETSCORP_CATEGORY_NEWS, getCategoryURL(LETSCORP_CATEGORY_NEWS));
    }

    public static String getCategoryName(int category) {
        switch (category) {
            case LETSCORP_CATEGORY_ECONOMICS:
                return "economics";
            case LETSCORP_CATEGORY_NEWS:
                return "news";
        }
        return "";
    }

    private static String getCategoryURL(int category) {
        String name = getCategoryName(category);
        return LETSCORP_HOST + "/archives/category/" + name + "/page/";
    }

    public static String getPostListUrl(int cagegory, int page) {
        return mCategoryURL.get(cagegory) + page;
    }

}
