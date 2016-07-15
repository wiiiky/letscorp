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

    /* 获取文章列表 */
    public static void getPostList(final int category, int page, final ApiResponseHandler handler, HttpFinalHandler finalHandler) {
        String url = Const.getPostListUrl(category, page);
        HttpClient.get(url, new HttpResponseHandlerWrapper(finalHandler) {
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
    public static void getPostDetail(final String url, final ApiResponseHandler handler, HttpFinalHandler finalHandler) {
        HttpClient.get(url, new HttpResponseHandlerWrapper(finalHandler) {
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

        public HttpResponseHandlerWrapper(HttpFinalHandler finalHandler) {
            mFinalHandler = finalHandler;
        }

        private void onFinally() {
            if (mFinalHandler != null) {
                mFinalHandler.onFinally();
            }
        }

        @Override
        public void onError(final IOException e) {
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

        public abstract void onSuccess(String body) throws Exception;
    }
}
