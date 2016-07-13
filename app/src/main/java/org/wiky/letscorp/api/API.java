package org.wiky.letscorp.api;

import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.wiky.letscorp.LetscorpApplication;
import org.wiky.letscorp.R;
import org.wiky.letscorp.data.db.PostHelper;
import org.wiky.letscorp.data.db.PostItemHelper;
import org.wiky.letscorp.data.model.Post;
import org.wiky.letscorp.data.model.PostItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wiky on 6/11/16.
 */
public class API {

    public static void getPostList(int page, final ApiResponseHandler handler, HttpFinalHandler finalHandler) {
        String url = Const.getPostListUrl(page);
        HttpClient.get(url, new HttpResponseHandlerWrapper(finalHandler) {
            @Override
            public void onSuccess(String body) throws Exception {
                Document doc = Jsoup.parse(body);
                Elements posts = doc.select("article.post");
                final List<PostItem> results = new ArrayList<PostItem>();
                for (Element post : posts) {
                    PostItem item = Parser.parsePostItem(post);
                    if (item != null) {
                        results.add(item);
                    }
                }
                PostItemHelper.savePostItems(results);
                for (PostItem item : results) {
                    item.readn = PostHelper.checkPost(item.href);
                }
                LetscorpApplication.getUIHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        handler.onSuccess(results);
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
                Element e = doc.select("article.post").first();
                final Post post = Parser.parsePost(e, url);
                PostHelper.savePost(post);
                LetscorpApplication.getUIHandler().post(new Runnable() {
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
            LetscorpApplication.getUIHandler().post(new Runnable() {
                @Override
                public void run() {
                    onFinally();
                    android.app.Application app = LetscorpApplication.getApplication();
                    Toast.makeText(app, app.getString(R.string.api_error_message) + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void onFailure(final int statusCode) {
            LetscorpApplication.getUIHandler().post(new Runnable() {
                @Override
                public void run() {
                    onFinally();
                    android.app.Application app = LetscorpApplication.getApplication();
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
                LetscorpApplication.getUIHandler().post(new Runnable() {
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
