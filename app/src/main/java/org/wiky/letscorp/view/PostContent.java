package org.wiky.letscorp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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

    private View createText(String text) {
        TextView tv = new TextView(getContext());
        tv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        tv.setTextColor(Color.BLACK);
        tv.setText("\u3000\u3000" + text);
        return tv;
    }

    private void addText(String text) {
        addView(createText(text));
    }

    private View createImage(String url) {
        ImageViewer imageViewer = new ImageViewer(getContext());

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        imageViewer.setLayoutParams(layoutParams);
        imageViewer.setURL(url);
        imageViewer.setTransitionName("image");
        imageViewer.setOnClickListener(mOnImageClickListener);
        return imageViewer;
    }

    private void addImage(String url) {
        if (url.isEmpty()) {
            return;
        }
        addView(createImage(url));
    }

    public void setContent(String content) {
        removeAllViews();
        mContent = content;
        Document doc = Jsoup.parseBodyFragment(mContent);
        for (Element e : doc.body().children()) {
            String tag = e.tagName();
            String text = e.text();
            if (!text.isEmpty()) {
                addText(text);
            } else {
                for (Element img : e.select("img")) {
                    addImage(img.attr("src"));
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
