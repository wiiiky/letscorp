package org.wiky.letscorp.api;

import android.widget.Toast;

import org.jsoup.nodes.Document;
import org.wiky.letscorp.Application;
import org.wiky.letscorp.data.db.PostHelper;
import org.wiky.letscorp.data.db.PostItemHelper;
import org.wiky.letscorp.data.model.Post;
import org.wiky.letscorp.data.model.PostItem;

import java.util.List;

import okhttp3.Call;

/**
 * Created by wiky on 7/18/16.
 */
public class Api {

    /* 搜索请求同时只能有一个 */
    private static Call mCurrentSearchCall = null;

    public static List<PostItem> loadPostItems(int category, int page, int count) {
        return PostItemHelper.getPostItems(category, page, count);
    }

    public static Post loadPostDetail(String href) {
        return PostHelper.getPost(href);
    }

    /* 获取文章列表 */
    public static void fetchPostItems(final int category, int page, final ApiHandler<List<PostItem>> apiHandler) {
        String url = Const.getPostListUrl(category, page);
        Request.get(url, new Request.Callback() {
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
    }

    /* 获取文章详细内容 */
    public static void fetchPostDetail(final String url, final ApiHandler<Post> apiHandler) {
        Request.get(url, new Request.Callback() {
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
    }

    /* 搜索 */
    public static void searchPost(String query, int page, final ApiHandler<List<PostItem>> apiHandler) {
        String url = Const.getSearchUrl(query, page);
        if (mCurrentSearchCall != null) {
            mCurrentSearchCall.cancel();
        }
        mCurrentSearchCall = Request.get(url, new Request.Callback() {
            @Override
            public void onSuccess(Document doc) {
                List<PostItem> items = Parser.parseSearchItems(doc);
                for (PostItem item : items) {
                    item.readn = PostHelper.checkPost(item.href);
                }
                apiHandler.onSuccess(items);
                apiHandler.onFinally();
            }

            @Override
            public void onCancelled() {
            }

            @Override
            public void onError(Exception e) {
                if (e != null) {
                    Toast.makeText(Application.getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                apiHandler.onFinally();
            }
        });
    }

    public interface ApiHandler<T> {
        void onSuccess(T data);

        void onFinally();
    }
}

