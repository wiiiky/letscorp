package org.wiky.letscorp.util;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by wiky on 6/15/16.
 */
public class Util {
    public static float dp2px(float dp) {
        Resources r = Resources.getSystem();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

}
