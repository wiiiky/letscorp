package org.wiky.letscorp.api;

import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.wiky.letscorp.LetscorpApplication;
import org.wiky.letscorp.R;
import org.wiky.letscorp.data.model.Post;
import org.wiky.letscorp.data.model.PostItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wiky on 6/11/16.
 */
public class API {

    public static void getPostList(int page, final ApiResponseHandler hander, HttpFinalHandler finalHandler) {
        String url = Const.getPostListUrl(page);
        HttpClient.get(url, new HttpResponseHandlerWrapper(finalHandler) {
            @Override
            public void onSuccess(String body) throws Exception {
                Document doc = Jsoup.parse(body);
                Elements posts = doc.select("article.post");
                List<PostItem> results = new ArrayList<PostItem>();
                for (Element post : posts) {
                    PostItem item = Parser.parsePostItem(post);
                    if (item != null) {
                        results.add(item);
                    }
                }
                hander.onSuccess(results);
            }
        });
    }

    public static void getPostDetail(final String url, final ApiResponseHandler handler, HttpFinalHandler finalHandler) {
        HttpClient.get(url, new HttpResponseHandlerWrapper(finalHandler) {
            @Override
            public void onSuccess(String body) throws Exception {
                Document doc = Jsoup.parse(body);
                Element e = doc.select("article.post").first();
                Post post = Parser.parsePost(e, url);
                handler.onSuccess(post);
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

        public HttpResponseHandlerWrapper() {

        }

        private void onFinally() {
            if (mFinalHandler != null) {
                mFinalHandler.onFinally();
            }
        }

        @Override
        public void onError(IOException e) {
            onFinally();
            android.app.Application app = LetscorpApplication.getApplication();
            Toast.makeText(app, app.getString(R.string.api_error_message) + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        @Override
        public void onFailure(int statusCode) {
            onFinally();
            android.app.Application app = LetscorpApplication.getApplication();
            Toast.makeText(app, app.getString(R.string.api_failure_message) + statusCode, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSuccess(byte[] body) {
            try {
                this.onSuccess(new String(body));
            } catch (Exception e) {
                this.onFailure(-1);
            } finally {
                onFinally();
            }
        }

        public abstract void onSuccess(String body) throws Exception;
    }
}
