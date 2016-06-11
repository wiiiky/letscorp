package org.wiky.letscorp;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by wiky on 6/11/16.
 */
public class Application extends android.app.Application {

    private static Application mApplication;

    public static Application getApplication() {
        return mApplication;
    }

    public static float dp2px(float dp) {
        Resources r = mApplication.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return px;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }
}
