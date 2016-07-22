package org.wiky.letscorp.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import org.wiky.letscorp.R;
import org.wiky.letscorp.util.Util;

/**
 * Created by wiky on 7/22/16.
 */
public class SearchView extends FrameLayout implements View.OnClickListener, TextWatcher, View.OnKeyListener {

    private Activity mActivity;
    private View mAttachedView;
    private ImageView mBackImage;
    private ImageView mClearImage;
    private EditText mSearchBox;
    private OnSearchListener mOnSearchListener = null;

    public SearchView(Context context) {
        super(context);
        initialize(context);
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }


    public SearchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context);
    }

    private void initialize(Context context) {
        inflate(context, R.layout.search_view, this);

        mSearchBox = (EditText) findViewById(R.id.search_view_edit);
        mBackImage = (ImageView) findViewById(R.id.search_view_back);
        mClearImage = (ImageView) findViewById(R.id.search_view_clear);
        mBackImage.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        mClearImage.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

        mSearchBox.addTextChangedListener(this);
        mSearchBox.setOnKeyListener(this);
        mBackImage.setOnClickListener(this);
        mClearImage.setOnClickListener(this);

        setTransitionName("transition-search-view");
    }

    @Override
    public boolean dispatchKeyEvent(final KeyEvent event) {
        if (mActivity != null && event.getAction() == KeyEvent.ACTION_UP &&
                event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            hide();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    public void hide() {
        if (mActivity == null || getVisibility() != VISIBLE) {
            return;
        }
        int[] viewPos = Util.getViewCenterOnScreen(mAttachedView);
        int[] pos = Util.getScreenLocation(this);
        int cx = viewPos[0] - pos[0];
        int cy = viewPos[1] - pos[1];
        Animator animator = ViewAnimationUtils.createCircularReveal(this, cx, cy, (float) Math.hypot(getWidth(), getHeight()), 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setVisibility(View.GONE);
            }

        });
        animator.setDuration(300);
        animator.start();
    }

    public void show() {
        if (mActivity == null || getVisibility() == VISIBLE) {
            return;
        }
        int[] viewPos = Util.getViewCenterOnScreen(mAttachedView);
        int[] pos = Util.getScreenLocation(this);
        int cx = viewPos[0] - pos[0];
        int cy = viewPos[1] - pos[1];
        setVisibility(VISIBLE);
        Animator animator = ViewAnimationUtils.createCircularReveal(this, cx, cy, 0, (float) Math.hypot(getWidth(), getHeight()));
        animator.setDuration(300);
        animator.start();
    }

    /* 将SearchView绑定到Activity */
    public void attach(Activity activity, View view) {
        if (mActivity != null) {
            return;
        }
        mActivity = activity;
        mAttachedView = view;
        Rect rect = new Rect();
        Window window = mActivity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                rect.right /* This ensures we don't go under the navigation bar in landscape */,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_PANEL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = statusBarHeight;

        WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(this, params);
        setVisibility(GONE);
    }

    public void setQuery(String query) {
        mSearchBox.setText(query);
        mSearchBox.setSelection(query.length());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.search_view_back) {
            hide();
        } else if (id == R.id.search_view_clear) {
            mSearchBox.setText("");
            mClearImage.setVisibility(INVISIBLE);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (mOnSearchListener != null) {
            mOnSearchListener.onQueryChanged(charSequence.toString());
        }
        mClearImage.setVisibility(TextUtils.isEmpty(charSequence) ? INVISIBLE : VISIBLE);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public String getSearchQuery() {
        Editable editable = mSearchBox.getText();
        if (editable == null) {
            return "";
        }
        return editable.toString();
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if ((keyEvent.getAction() == KeyEvent.ACTION_UP) &&
                (keyCode == KeyEvent.KEYCODE_ENTER ||
                        keyCode == KeyEvent.KEYCODE_SEARCH)) {
            final String query = getSearchQuery();
            if (!TextUtils.isEmpty(query) && mOnSearchListener != null) {
                mOnSearchListener.onSearch(query);
            }
            return true;
        }
        return false;
    }

    public void setOnSearchListener(OnSearchListener listener) {
        mOnSearchListener = listener;
    }

    @Override
    public boolean isInEditMode() {
        return true;
    }

    public interface OnSearchListener {
        void onSearch(String query);

        void onQueryChanged(String query);
    }
}
