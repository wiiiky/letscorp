package org.wiky.letscorp.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.wiky.letscorp.R;
import org.wiky.letscorp.util.Util;

import java.util.Objects;

/**
 * Created by wiky on 7/15/16.
 */
public class PostContent extends LinearLayout {

    private String mContent;
    private OnClickListener mOnImageClickListener;

    public PostContent(Context context) {
        super(context);
        initialize(context);
    }

    public PostContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public PostContent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    public PostContent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context);
    }

    private void initialize(Context context) {
        setOrientation(VERTICAL);
    }

    private View createText(String text, String tag) {
        TextView tv = new TextView(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = (int) Util.dp2px(4);
        tv.setLayoutParams(layoutParams);
        tv.setText(text);
        if (Objects.equals(tag, "blockquote")) {
            tv.setPadding((int) Util.dp2px(20), getPaddingTop(), getPaddingRight(), getPaddingBottom());
            tv.setBackgroundResource(R.drawable.blockquote);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            tv.setTypeface(null, Typeface.ITALIC);
        }
        return tv;
    }

    private void addText(String text, String tag) {
        addView(createText(text, tag));
    }

    private View createImage(String url, String tag) {
        ImageViewer imageViewer = new ImageViewer(getContext());

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        layoutParams.topMargin = (int) Util.dp2px(4);
        imageViewer.setLayoutParams(layoutParams);
        imageViewer.setURL(url);
        imageViewer.setTransitionName("image");
        imageViewer.setOnClickListener(mOnImageClickListener);
        return imageViewer;
    }

    private void addImage(String url, String tag) {
        if (url.isEmpty()) {
            return;
        }
        addView(createImage(url, tag));
    }

    public void setContent(String content) {
        removeAllViews();
        mContent = content;
        Document doc = Jsoup.parseBodyFragment(mContent);
        for (Element e : doc.body().children()) {
            String tag = e.tagName();
            String text = e.text();
            if (!text.isEmpty()) {
                addText(text, tag);
            } else {
                for (Element img : e.select("img")) {
                    addImage(img.attr("src"), tag);
                }
            }
        }
    }

    public void setOnImageClickListener(OnClickListener listener) {
        mOnImageClickListener = listener;
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            if (v instanceof ImageViewer) {
                v.setOnClickListener(mOnImageClickListener);
            }
        }
    }
}
