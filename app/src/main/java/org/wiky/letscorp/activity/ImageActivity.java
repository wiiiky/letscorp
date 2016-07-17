package org.wiky.letscorp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

import org.wiky.letscorp.Application;
import org.wiky.letscorp.R;
import org.wiky.letscorp.view.PhotoView;


public class ImageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String title = intent.getStringExtra("title");
        int width = intent.getIntExtra("width", 1);
        int height = intent.getIntExtra("height", 0);

        int totalWidth = Application.getScreenWidth();
        height = (int) (height * 1.0f / width * totalWidth);

        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
        photoView.getLayoutParams().height = height;
        photoView.setURL(url);
        setTitle(title);

        // finished its layout.
        // http://stackoverflow.com/questions/26600263/how-do-i-prevent-the-status-bar-and-navigation-bar-from-animating-during-an-acti
        postponeEnterTransition();

        final View decor = getWindow().getDecorView();
        decor.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                decor.getViewTreeObserver().removeOnPreDrawListener(this);
                startPostponedEnterTransition();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
