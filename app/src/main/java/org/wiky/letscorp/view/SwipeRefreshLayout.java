package org.wiky.letscorp.view;

import android.content.Context;
import android.util.AttributeSet;

import org.wiky.letscorp.R;

/**
 * Created by wiky on 7/23/16.
 */
public class SwipeRefreshLayout extends android.support.v4.widget.SwipeRefreshLayout {
    public SwipeRefreshLayout(Context context) {
        super(context);
        initialize(context);
    }

    public SwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    private void initialize(Context context) {
        setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
    }
}
