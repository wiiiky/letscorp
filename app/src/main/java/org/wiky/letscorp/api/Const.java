package org.wiky.letscorp.api;

/**
 * Created by wiky on 6/11/16.
 */
public final class Const {
    public static String LETSCORP_HOST = "https://m.letscorp.net";

    public static String getPostListUrl(int page) {
        return LETSCORP_HOST + "/page/" + page;
    }
}
