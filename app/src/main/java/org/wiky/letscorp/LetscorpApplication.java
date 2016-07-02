package org.wiky.letscorp;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by wiky on 6/11/16.
 */
public class LetscorpApplication extends android.app.Application {

    private static LetscorpApplication mApplication = null;
    private static Handler mUIHandler = null;

    public static LetscorpApplication getApplication() {
        return mApplication;
    }

    public static Handler getUIHandler() {
        if (mUIHandler == null) {
            mUIHandler = new Handler(mApplication.getMainLooper());
        }
        return mUIHandler;
    }

    public static int getScreenHeight() {
        WindowManager wm = (WindowManager) mApplication.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    public static int getScreenWidth() {
        WindowManager wm = (WindowManager) mApplication.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }
}
