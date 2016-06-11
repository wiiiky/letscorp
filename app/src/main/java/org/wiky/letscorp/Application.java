package org.wiky.letscorp;

/**
 * Created by wiky on 6/11/16.
 */
public class Application extends android.app.Application {

    private static Application mApplication;

    public static Application getApplication() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }
}
