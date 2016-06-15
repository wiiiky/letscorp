package org.wiky.letscorp;

/**
 * Created by wiky on 6/11/16.
 */
public class LetscorpApplication extends android.app.Application {

    private static LetscorpApplication mApplication = null;

    public static LetscorpApplication getApplication() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }
}
