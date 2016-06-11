package org.wiky.letscorp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by wiky on 6/11/16.
 */
public class ImageViewer extends ImageView {
    public ImageViewer(Context context) {
        super(context);
    }

    public ImageViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ImageViewer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setURL(String url) {
        if (url.isEmpty()) {
            this.setImageDrawable(null);
            return;
        }
        Picasso.with(this.getContext()).load(url).into(this);
    }
}
