package org.wiky.letscorp.api;

import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.wiky.letscorp.Application;
import org.wiky.letscorp.R;
import org.wiky.letscorp.data.model.PostItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wiky on 6/11/16.
 */
public class API {

    public static RecyclerView getPostList(int page, final ApiResponseHandler hander) {
        String url = Const.getPostListUrl(page);
        HttpClient.get(url, new HttpResponseHandlerWrapper() {
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
        return null;
    }

    public interface ApiResponseHandler {
        void onSuccess(Object data);
    }

    private static abstract class HttpResponseHandlerWrapper implements HttpClient.HttpResponseHandler {

        @Override
        public void onError(IOException e) {
            android.app.Application app = Application.getApplication();
            Toast.makeText(app, app.getString(R.string.api_error_message) + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(int statusCode) {
            android.app.Application app = Application.getApplication();
            Toast.makeText(app, app.getString(R.string.api_failure_message) + statusCode, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSuccess(byte[] body) {
            try {
                this.onSuccess(new String(body));
            } catch (Exception e) {
                this.onFailure(-1);
            }
        }

        public abstract void onSuccess(String body) throws Exception;
    }
}
