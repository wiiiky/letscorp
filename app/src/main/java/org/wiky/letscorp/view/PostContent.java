package org.wiky.letscorp.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.wiky.letscorp.R;
import org.wiky.letscorp.util.Util;

import java.util.Objects;

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
        tv.setTextColor(getResources().getColor(R.color.colorPrimaryText));
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

    private View createImage(String url) {
        PhotoView photoView = new PhotoView(getContext());

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = (int) Util.dp2px(4);
        photoView.setLayoutParams(layoutParams);
        photoView.setZoomable(false);
        photoView.setUrl(url, true);
        photoView.setTransitionName("image");
        photoView.setOnClickListener(mOnImageClickListener);

        return photoView;
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
            if (Objects.equals(tag, "p")) {
                Elements imgs = e.select(">a>img");
                if (!imgs.isEmpty()) {
                    for (Element img : imgs) {
                        addImage(img.attr("data-original"));
                    }
                } else {
                    addText(e.ownText(), tag);
                }
            } else if (Objects.equals(tag, "blockquote")) {
                for (Element p : e.select(">p")) {
                    addText(p.ownText(), tag);
                }
            } else {
                Toast.makeText(getContext(), String.format("unknown tag %s", tag), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setOnImageClickListener(OnClickListener listener) {
        mOnImageClickListener = listener;
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            if (v instanceof PhotoView) {
                v.setOnClickListener(mOnImageClickListener);
            }
        }
    }
}
