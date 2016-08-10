package org.wiky.letscorp;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.view.Display;
import android.view.WindowManager;

import org.wiky.letscorp.data.db.SQLHelper;
import org.wiky.letscorp.style.StylePreferences;

/**
 * Created by wiky on 6/11/16.
 */
public class Application extends android.app.Application {

    private static Application mApplication = null;
    private Handler muiHandler = null;
    private SQLHelper msqlHelper = null;
    private StylePreferences mstylePref = null;

    public static Application getApplication() {
        return mApplication;
    }

    public static Handler getUIHandler() {
        return mApplication.muiHandler;
    }

    public static SQLHelper getDBHelper() {
        return mApplication.msqlHelper;
    }

    public static StylePreferences getStylePreferences() {
        return mApplication.mstylePref;
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
        muiHandler = new Handler(getMainLooper());
        msqlHelper = new SQLHelper(this);
        mstylePref = new StylePreferences(this);
        mApplication = this;
    }
}
