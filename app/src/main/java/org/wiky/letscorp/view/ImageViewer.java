package org.wiky.letscorp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.wiky.letscorp.Application;

/**
 * Created by wiky on 6/11/16.
 */
public class ImageViewer extends ImageView {

    protected String mURL;
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

    public String getURL() {
        return mURL;
    }

    public void setURL(String url) {
        mURL = url;
        if (mURL.isEmpty()) {
            this.setImageDrawable(null);
            return;
        }
        Picasso.with(Application.getApplication()).load(mURL).into(this);
    }
}
