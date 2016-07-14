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
        mCategoryURL.put(LETSCORP_CATEGORY_ECONOMICS, getCategoryURL("economics"));
        mCategoryURL.put(LETSCORP_CATEGORY_NEWS, getCategoryURL("news"));
    }

    private static String getCategoryURL(String name) {
        return LETSCORP_HOST + "/archives/category/" + name + "/page/";
    }

    public static String getPostListUrl(int cagegory, int page) {
        return mCategoryURL.get(cagegory) + page;
    }

}
