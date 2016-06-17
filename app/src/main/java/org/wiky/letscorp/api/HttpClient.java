package org.wiky.letscorp.api;


import android.os.Handler;

import org.wiky.letscorp.LetscorpApplication;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wiky on 6/11/16.
 */
public class HttpClient {
    private static OkHttpClient mClient = new OkHttpClient();

    public static void get(String url, HttpResponseHandler hander) {
        Request request = new Request.Builder()
                .addHeader("User-Agent", "Mozilla/5.0 (X11; Fedora; Linux x86_64; rv:47.0) Gecko/20100101 Firefox/47.0")
                .url(url)
                .build();
        mClient.newCall(request).enqueue(new CallbackWrapper(hander));
    }

    public interface HttpResponseHandler {
        void onError(IOException e);     /* 网络错误 */

        void onFailure(int statusCode);   /* HTTP请求错误，statusCode是错误码 */

        void onSuccess(byte[] body);      /* 成功 */
    }

    private static class CallbackWrapper implements Callback {

        private HttpResponseHandler mHandler;
        private Handler mLoopHandler;

        public CallbackWrapper(HttpResponseHandler hander) {
            mHandler = hander;
            mLoopHandler = new Handler(LetscorpApplication.getApplication().getMainLooper());
        }

        @Override
        public void onFailure(Call call, final IOException e) {
            mLoopHandler.post(new Runnable() {
                @Override
                public void run() {
                    mHandler.onError(e);
                }
            });

        }

        @Override
        public void onResponse(Call call, final Response response) {
            mLoopHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!response.isSuccessful()) {
                            mHandler.onFailure(response.code());
                        } else {
                            mHandler.onSuccess(response.body().bytes());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
