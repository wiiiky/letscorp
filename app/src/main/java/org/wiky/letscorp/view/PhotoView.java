package org.wiky.letscorp.view;

import android.content.Context;
import android.util.AttributeSet;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.wiky.letscorp.Application;
import org.wiky.photoview.PhotoViewAttacher;

public class PhotoView extends org.wiky.photoview.PhotoView {

    private PhotoViewAttacher mAttacher;
    private String mUrl;

    public PhotoView(Context context, AttributeSet attr) {
        super(context, attr);
        initialize();
    }

    public PhotoView(Context context) {
        super(context);
        initialize();
    }

    private void initialize() {
        mAttacher = new PhotoViewAttacher(this);
        mAttacher.setScaleType(ScaleType.FIT_CENTER);
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
        if (mUrl.isEmpty()) {
            return;
        }
        Picasso.with(Application.getApplication())
                .load(url)
                .into(this, new Callback() {
                    @Override
                    public void onSuccess() {
                        mAttacher.update();
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    @Override
    public void setZoomable(boolean zoomable) {
        mAttacher.setZoomable(zoomable);
    }
}