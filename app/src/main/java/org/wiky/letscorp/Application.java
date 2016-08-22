package org.wiky.letscorp;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.view.Display;
import android.view.WindowManager;

import org.wiky.letscorp.data.db.SQLHelper;
import org.wiky.letscorp.pref.GeneralPreferences;
import org.wiky.letscorp.pref.StylePreferences;

/**
 * Created by wiky on 6/11/16.
 * 应用示例
 */
public class Application extends android.app.Application {

    private static Application mApplication = null;
    private Handler mUiHandler = null;
    private SQLHelper mSqlHelper = null;
    private StylePreferences mStylePref = null;
    private GeneralPreferences mGeneralPref = null;

    public static Application getApplication() {
        return mApplication;
    }

    public static Handler getUIHandler() {
        return mApplication.mUiHandler;
    }

    public static SQLHelper getDBHelper() {
        return mApplication.mSqlHelper;
    }

    public static StylePreferences getStylePreferences() {
        return mApplication.mStylePref;
    }

    public static GeneralPreferences getGeneralPreferences() {
        return mApplication.mGeneralPref;
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
        mUiHandler = new Handler(getMainLooper());
        mSqlHelper = new SQLHelper(this);
        mStylePref = new StylePreferences(this);
        mGeneralPref = new GeneralPreferences(this);
        mApplication = this;
    }
}
