package org.wiky.letscorp.api;

import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.wiky.letscorp.Application;
import org.wiky.letscorp.R;
import org.wiky.letscorp.data.db.PostHelper;
import org.wiky.letscorp.data.db.PostItemHelper;
import org.wiky.letscorp.data.model.Post;
import org.wiky.letscorp.data.model.PostItem;

import java.io.IOException;
import java.util.List;

/**
 * Created by wiky on 6/11/16.
 * 并不是真正的API，下载HTML并解析文章信息
 */
public class API {

    private static int mApiID = 0;

    public static List<PostItem> loadPostItems(int category, int page, int count) {
        mApiID++;
        return PostItemHelper.getPostItems(category, page, count);
    }

    public static Post loadPostDetail(String href) {
        return PostHelper.getPost(href);
    }

    /* 获取文章列表 */
    public static void fetchPostItems(final int category, int page, final ApiResponseHandler handler, HttpFinalHandler finalHandler) {
        String url = Const.getPostListUrl(category, page);
        HttpClient.get(url, new HttpResponseHandlerWrapper(++mApiID, finalHandler) {
            @Override
            public boolean isLive(int _id) {
                return _id == mApiID;
            }

            @Override
            public void onSuccess(String body) throws Exception {
                Document doc = Jsoup.parse(body);
                final List<PostItem> items = Parser.parsePostItems(doc, category);
                PostItemHelper.savePostItems(items);
                for (PostItem item : items) {
                    item.readn = PostHelper.checkPost(item.href);
                }
                Application.getUIHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        handler.onSuccess(items);
                    }
                });
            }
        });
    }

    /*
     * 获取文章详细内容
     */
    public static void fetchPostDetail(final String url, final ApiResponseHandler handler, HttpFinalHandler finalHandler) {
        final int id = ++mApiID;
        HttpClient.get(url, new HttpResponseHandlerWrapper(id, finalHandler) {
            @Override
            public boolean isLive(int _id) {
                return mApiID == _id;
            }

            @Override
            public void onSuccess(String body) throws Exception {
                Document doc = Jsoup.parse(body);
                final Post post = Parser.parsePost(doc, url);
                PostHelper.savePost(post);
                Application.getUIHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        handler.onSuccess(post);
                    }
                });
            }
        });
    }

    public interface ApiResponseHandler {
        void onSuccess(Object data);
    }

    public interface HttpFinalHandler {
        void onFinally();
    }

    private static abstract class HttpResponseHandlerWrapper implements HttpClient.HttpResponseHandler {

        private HttpFinalHandler mFinalHandler;
        private int mID;

        public HttpResponseHandlerWrapper(int id, HttpFinalHandler finalHandler) {
            mFinalHandler = finalHandler;
            mID = id;
        }

        private void onFinally() {
            if (mFinalHandler != null) {
                mFinalHandler.onFinally();
            }
        }

        @Override
        public void onError(final IOException e) {
            if (!isLive(mID)) {
                return;
            }
            Application.getUIHandler().post(new Runnable() {
                @Override
                public void run() {
                    onFinally();
                    android.app.Application app = Application.getApplication();
                    Toast.makeText(app, app.getString(R.string.api_error_message) + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void onFailure(final int statusCode) {
            if (!isLive(mID)) {
                return;
            }
            Application.getUIHandler().post(new Runnable() {
                @Override
                public void run() {
                    onFinally();
                    android.app.Application app = Application.getApplication();
                    Toast.makeText(app, app.getString(R.string.api_failure_message) + statusCode, Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onSuccess(byte[] body) {
            if (!isLive(mID)) {
                return;
            }
            try {
                this.onSuccess(new String(body));
            } catch (Exception e) {
                this.onFailure(-1);
                e.printStackTrace();
            } finally {
                Application.getUIHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        onFinally();
                    }
                });
            }
        }

        public abstract boolean isLive(int _id);

        public abstract void onSuccess(String body) throws Exception;
    }
}
