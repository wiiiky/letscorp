package org.wiky.letscorp.api;

import android.widget.Toast;

import org.jsoup.nodes.Document;
import org.wiky.letscorp.Application;
import org.wiky.letscorp.data.db.PostHelper;
import org.wiky.letscorp.data.db.PostItemHelper;
import org.wiky.letscorp.data.model.Post;
import org.wiky.letscorp.data.model.PostItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by wiky on 7/18/16.
 */
public class Api {


    private static final int CALL_POST_ITEMS = 1;
    private static final int CALL_POST_DETAIL = 2;
    private static Map<Integer, Call> mCalls = new HashMap<>();

    public static List<PostItem> loadPostItems(int category, int page, int count) {
        Call call = mCalls.get(CALL_POST_ITEMS);
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
        mCalls.put(CALL_POST_ITEMS, null);
        return PostItemHelper.getPostItems(category, page, count);
    }

    public static Post loadPostDetail(String href) {
        return PostHelper.getPost(href);
    }

    /* 获取文章列表 */
    public static void fetchPostItems(final int category, int page, final ApiHandler apiHandler) {
        String url = Const.getPostListUrl(category, page);
        Call call = mCalls.get(CALL_POST_ITEMS);
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
        call = Request.get(url, new Request.Callback() {
            @Override
            public void onSuccess(Document doc) {
                List<PostItem> items = Parser.parsePostItems(doc, category);
                PostItemHelper.savePostItems(items);
                for (PostItem item : items) {
                    item.readn = PostHelper.checkPost(item.href);
                }
                apiHandler.onSuccess(items);
                apiHandler.onFinally();
            }

            @Override
            public void onCancelled() {
                apiHandler.onFinally();
            }

            @Override
            public void onError(Exception e) {
                if (e != null) {
                    Toast.makeText(Application.getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                apiHandler.onFinally();
            }
        });
        mCalls.put(CALL_POST_ITEMS, call);
    }

    public static void fetchPostDetail(final String url, final ApiHandler apiHandler) {
        Call call = mCalls.get(CALL_POST_DETAIL);
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
        call = Request.get(url, new Request.Callback() {
            @Override
            public void onSuccess(Document doc) {
                Post post = Parser.parsePost(doc, url);
                PostHelper.savePost(post);
                apiHandler.onSuccess(post);
                apiHandler.onFinally();
            }

            @Override
            public void onCancelled() {
                apiHandler.onFinally();
            }

            @Override
            public void onError(Exception e) {
                if (e != null) {
                    Toast.makeText(Application.getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                apiHandler.onFinally();
            }
        });
        mCalls.put(CALL_POST_DETAIL, call);
    }

    public interface ApiHandler {
        void onSuccess(Object data);

        void onFinally();
    }
}

