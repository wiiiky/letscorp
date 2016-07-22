package org.wiky.letscorp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.target.Target;

import org.wiky.letscorp.Application;
import org.wiky.letscorp.R;
import org.wiky.letscorp.util.Util;

public class PhotoView extends uk.co.senab.photoview.PhotoView {

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
        setScaleType(ScaleType.FIT_CENTER);
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        setUrl(url, false);
    }

    public void setUrl(String url, boolean loading) {
        mUrl = url;
        if (mUrl.isEmpty()) {
            return;
        }

        Log.d("url", mUrl);
        GlideUrl glideUrl = new GlideUrl(mUrl, new LazyHeaders.Builder()
                .addHeader("User-Agent", Util.HTTP_USER_AGENT)
                .build());
        BitmapTypeRequest req = Glide.with(Application.getApplication()).load(glideUrl).asBitmap();
        if (loading) {
            req.placeholder(R.mipmap.ic_photo_black);
        }
        req.error(R.mipmap.ic_photo_black)
                .fitCenter()
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(this);
    }
}