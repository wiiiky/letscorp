package org.wiky.letscorp.view;

/**
 * Created by wiky on 7/3/16.
 */

import android.animation.Animator;
import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class OverScrollView extends NestedScrollView {

    private int mLastEventY;
    private boolean mScrollable = true;
    private Animator.AnimatorListener mAnimationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            mScrollable = false;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mScrollable = true;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            mScrollable = true;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };
    private OverScrollListener listener;

    public OverScrollView(Context context) {
        super(context);
    }

    public OverScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OverScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mScrollable) {
            return super.onTouchEvent(event);
        }
        final int eventY = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                int yDistance = (int) getTranslationY();
                if (yDistance != 0) {
                    if (listener == null || !listener.onOverScroll(yDistance, true)) { //only do this if listener returns false
                        animate().translationY(0)
                                .setDuration(600)
                                .setInterpolator(new DecelerateInterpolator(6))
                                .setListener(mAnimationListener)
                                .start();
                    }
                }
                break;
            case MotionEvent.ACTION_DOWN:
                mLastEventY = eventY;
                break;
            case MotionEvent.ACTION_MOVE:
                if (getScrollY() == 0) {
                    handleOverscroll(event, false);
                } else {
                    View view = getChildAt(getChildCount() - 1);
                    if (view.getHeight() <= (getHeight() + getScrollY())) {
                        handleOverscroll(event, true);
                    }
                }
                break;
        }

        if (getTranslationY() != 0) {
            return true;
        }
        return super.onTouchEvent(event);

    }

    public void setOverScrollListener(OverScrollListener listener) {
        this.listener = listener;
    }

    private void handleOverscroll(MotionEvent ev, boolean isBottom) {
        int pointerCount = ev.getHistorySize();
        for (int p = 0; p < pointerCount; p++) {
            int historicalY = (int) ev.getHistoricalY(p);
            int yDistance = (historicalY - mLastEventY) / 6;

            if ((isBottom && yDistance < 0) || (!isBottom && yDistance > 0)) {
                setTranslationY(yDistance);
                if (listener != null) listener.onOverScroll(yDistance, false);
            }
        }
    }

    public interface OverScrollListener {
        boolean onOverScroll(int yDistance, boolean isReleased);
    }
}
