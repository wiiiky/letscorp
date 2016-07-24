package org.wiky.letscorp.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import org.wiky.letscorp.R;
import org.wiky.letscorp.data.db.QueryHelper;
import org.wiky.letscorp.data.model.Query;
import org.wiky.letscorp.util.Util;


public class SearchBox extends FrameLayout implements View.OnClickListener, TextWatcher, View.OnKeyListener {

    //    private View mAttachedView;
    private ImageView mBackImage;
    private ImageView mClearImage;
    private EditText mQueryBox;
    private OnSearchListener mOnSearchListener = null;

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
        inflate(context, R.layout.search_box, this);

        mQueryBox = (EditText) findViewById(R.id.search_box_edit);
        mBackImage = (ImageView) findViewById(R.id.search_box_back);
        mClearImage = (ImageView) findViewById(R.id.search_box_clear);
        if (mQueryBox == null) {
            return;
        }
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

    public void setQuery(String query) {
        mQueryBox.removeTextChangedListener(this);
        mQueryBox.setText(query);
        mQueryBox.setSelection(query.length());
        mQueryBox.addTextChangedListener(this);
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
