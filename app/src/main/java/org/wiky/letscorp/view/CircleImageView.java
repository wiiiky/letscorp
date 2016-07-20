package org.wiky.letscorp.view;

import android.content.Context;
import android.util.AttributeSet;

import com.squareup.picasso.Picasso;

import org.wiky.letscorp.Application;

/**
 * Created by wiky on 7/20/16.
 */
public class CircleImageView extends de.hdodenhof.circleimageview.CircleImageView {
    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setUrl(String url) {
        Picasso.with(Application.getApplication())
                .load(url)
                .into(this);
    }
}
