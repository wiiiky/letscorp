package org.wiky.letscorp.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import org.wiky.letscorp.R;
import org.wiky.letscorp.data.db.QueryHelper;
import org.wiky.letscorp.data.model.Query;
import org.wiky.letscorp.list.QueryListView;
import org.wiky.letscorp.list.adapter.QueryAdapter;
import org.wiky.letscorp.util.Util;


public class SearchBox extends FrameLayout implements View.OnClickListener, TextWatcher, View.OnKeyListener, View.OnFocusChangeListener, QueryAdapter.OnItemClickListener {

    private ViewGroup mRoot;
    private CardView mCard;
    private ImageView mBackImage;
    private ImageView mClearImage;
    private EditText mQueryEdit;
    private QueryListView mQueryList;
    //    private QueryAdapter mQueryAdapter;
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

        mRoot = (ViewGroup) findViewById(R.id.search_box_root);
        mCard = (CardView) findViewById(R.id.search_box_card);
        mQueryEdit = (EditText) findViewById(R.id.search_box_edit);
        mBackImage = (ImageView) findViewById(R.id.search_box_back);
        mClearImage = (ImageView) findViewById(R.id.search_box_clear);
        mQueryList = (QueryListView) findViewById(R.id.search_box_query_list);
        if (mQueryEdit == null) {
            return;
        }
        mBackImage.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        mClearImage.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

        mQueryEdit.addTextChangedListener(this);
        mQueryEdit.setOnFocusChangeListener(this);
        mQueryEdit.setOnKeyListener(this);
        mBackImage.setOnClickListener(this);
        mClearImage.setOnClickListener(this);
        mRoot.setOnClickListener(this);

        mQueryList.setOnItemClickListener(this);

        setVisibility(INVISIBLE);
    }


    public void show(int cx, int cy) {
        setVisibility(VISIBLE);
        Animator animator = Util.createCircularReveal(mCard, cx, cy, true);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mQueryEdit.requestFocus();
            }
        });
        animator.setDuration(300);
        animator.start();
    }

    public void hide(int cx, int cy) {
        Animator animator = Util.createCircularReveal(mCard, cx, cy, false);
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
        mQueryEdit.removeTextChangedListener(this);
        mQueryEdit.setText(query);
        mQueryEdit.setSelection(query.length());
        mClearImage.setVisibility(TextUtils.isEmpty(query) ? INVISIBLE : VISIBLE);
        mQueryEdit.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.search_box_back) {
            if (mOnSearchListener != null) {
                mOnSearchListener.onSearchBack();
            }
        } else if (id == R.id.search_box_clear) {
            mQueryEdit.setText("");
            mClearImage.setVisibility(INVISIBLE);
            mQueryEdit.requestFocus();
        } else if (id == R.id.search_box_root) {
            mQueryEdit.clearFocus();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mQueryList.update(charSequence.toString());
        mClearImage.setVisibility(TextUtils.isEmpty(charSequence) ? INVISIBLE : VISIBLE);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public String getSearchQuery() {
        Editable editable = mQueryEdit.getText();
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
            if (!TextUtils.isEmpty(query)) {
                onSearch(query);
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

    @Override
    public void onFocusChange(View view, boolean b) {
        if (b) {
            mQueryList.update(getSearchQuery());
            mRoot.setClickable(true);
            Util.showInputKeyboard(view);
        } else {
            mQueryList.clear();
            mRoot.setClickable(false);
            Util.hideInputKeyboard(view);
        }
    }

    public void clearFocus() {
        mQueryEdit.clearFocus();
    }

    @Override
    public void onItemClick(View v, String query) {
        onSearch(query);
    }

    private void onSearch(String query) {
        if (mOnSearchListener != null) {
            QueryHelper.saveQuery(new Query(query));
            setQuery(query);
            mOnSearchListener.onSearch(query);
        }
    }

    public interface OnSearchListener {
        void onSearch(String query);

        void onSearchBack();
    }
}
