package org.wiky.letscorp.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
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
import org.wiky.letscorp.data.db.QueryHelper;
import org.wiky.letscorp.data.model.Query;
import org.wiky.letscorp.util.Util;

/**
 * Created by wiky on 7/22/16.
 */
public class SearchBox extends FrameLayout implements View.OnClickListener, TextWatcher, View.OnKeyListener {

    private ImageView mBackImage;
    private ImageView mClearImage;
    private EditText mQueryBox;
    private RecyclerView mCompleteList;
    private OnSearchListener mOnSearchListener = null;
    private Activity mActivity;

    private static final int AUTOCOMPLETE_COUNT=5;

    public SearchBox(Context context) {
        super(context);
        initialize(context);
    }

    public SearchBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public SearchBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }


    public SearchBox(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context);
    }

    private void initialize(Context context) {
        inflate(context, R.layout.search_view, this);

        mCompleteList= (RecyclerView) findViewById(R.id.search_box_auto_complete);
        mQueryBox = (EditText) findViewById(R.id.search_box_edit);
        mBackImage = (ImageView) findViewById(R.id.search_box_back);
        mClearImage = (ImageView) findViewById(R.id.search_box_clear);
        mBackImage.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        mClearImage.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

        mQueryBox.addTextChangedListener(this);
        mQueryBox.setOnKeyListener(this);
        mBackImage.setOnClickListener(this);
        mClearImage.setOnClickListener(this);
    }



    public void show(int cx, int cy) {
        int[] pos = Util.getScreenLocation(this);
        cx -= pos[0];
        cy -= pos[1];
        Animator animator = ViewAnimationUtils.createCircularReveal(this, cx, cy, 0, (float) Math.hypot(getWidth(), getHeight()));
        animator.setDuration(300);
        setVisibility(VISIBLE);
        animator.start();
    }

    public void hide(int cx, int cy) {
        int[] pos = Util.getScreenLocation(this);
        cx -= pos[0];
        cy -= pos[1];
        Animator animator = ViewAnimationUtils.createCircularReveal(this, cx, cy, (float) Math.hypot(getWidth(), getHeight()), 0);
        animator.setDuration(300);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setVisibility(INVISIBLE);
            }
        });
        animator.start();
    }

    public void detach(){
        if(mActivity==null){
            return;
        }
        WindowManager windowManager=(WindowManager)mActivity.getSystemService(Context.WINDOW_SERVICE);
        windowManager.removeViewImmediate(this);
    }

    public void attach(Activity activity){
        if(mActivity!=null){
            return;
        }
        mActivity=activity;
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
        WindowManager windowManager=(WindowManager)mActivity.getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(this, params);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.search_box_back) {
            if (mOnSearchListener != null) {
                mOnSearchListener.onSearchBack();
            }
        } else if (id == R.id.search_box_clear) {
            mQueryBox.setText("");
            mClearImage.setVisibility(INVISIBLE);
            if (mOnSearchListener != null) {
                mOnSearchListener.onQueryChanged("");
            }
        }
    }

    @Override
    public boolean dispatchKeyEvent(final KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP &&
                event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if(mOnSearchListener!=null){
                mOnSearchListener.onSearchBack();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
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
        Editable editable = mQueryBox.getText();
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
            String query = getSearchQuery();
            if (!TextUtils.isEmpty(query) && mOnSearchListener != null) {
                mOnSearchListener.onSearch(query);
            }
            QueryHelper.saveQuery(new Query(query));
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

        void onSearchBack();
    }
}
