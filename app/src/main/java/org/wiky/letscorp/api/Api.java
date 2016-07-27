package org.wiky.letscorp.api;

import android.widget.Toast;

import org.jsoup.nodes.Document;
import org.wiky.letscorp.Application;
import org.wiky.letscorp.R;
import org.wiky.letscorp.data.db.PostHelper;
import org.wiky.letscorp.data.db.PostItemHelper;
import org.wiky.letscorp.data.model.Post;
import org.wiky.letscorp.data.model.PostItem;
import org.wiky.letscorp.signal.Signal;

import java.util.List;

import okhttp3.Call;


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
                apiHandler.onError(e);
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
                PostItemHelper.updatePostItem(post.href, post.commentCount(), post.date);
                Signal.trigger(Signal.SIGINT_ITEM_READN, post);
                apiHandler.onSuccess(post);
                apiHandler.onFinally();
            }

            @Override
            public void onCancelled() {
                apiHandler.onFinally();
            }

            @Override
            public void onError(Exception e) {
                apiHandler.onError(e);
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
                    Post post = PostHelper.getPost(item.href);
                    if (post != null) {
                        item.readn = true;
                        item.commentCount = post.commentCount();
                        item.date = post.date;
                    }
                }
                apiHandler.onSuccess(items);
                apiHandler.onFinally();
            }

            @Override
            public void onCancelled() {
            }

            @Override
            public void onError(Exception e) {
                apiHandler.onError(e);
            }
        });
    }

    public static abstract class ApiHandler<T> {
        public abstract void onSuccess(T data);

        public abstract void onFinally();

        public void onError(Exception e) {
            Toast.makeText(Application.getApplication(), String.format(Application.getApplication().getString(R.string.network_error), e != null ? e.getMessage() : ""), Toast.LENGTH_SHORT).show();
            onFinally();
        }
    }
}

