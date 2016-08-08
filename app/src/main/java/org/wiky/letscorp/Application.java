package org.wiky.letscorp;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.view.Display;
import android.view.WindowManager;

import org.wiky.letscorp.data.db.SQLHelper;

/**
 * Created by wiky on 6/11/16.
 */
public class Application extends android.app.Application {

    private static Application mApplication = null;
    private static Handler muiHandler = null;
    private static SQLHelper mdbHelper = null;

    public static Application getApplication() {
        return mApplication;
    }

    public static Handler getUIHandler() {
        if (muiHandler == null) {
            muiHandler = new Handler(mApplication.getMainLooper());
        }
        return muiHandler;
    }

    public static SQLHelper getDBHelper() {
        if (mdbHelper == null) {
            mdbHelper = new SQLHelper(mApplication);
        }
        return mdbHelper;
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
