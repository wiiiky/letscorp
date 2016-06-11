package org.wiky.letscorp.api;

/**
 * Created by wiky on 6/11/16.
 */
public class Const {
    public static String LETSCOR_HOST = "https://m.letscorp.net";

    public static String getPostListUrl(int page) {
        return LETSCOR_HOST + "/page/" + page;
    }
}
