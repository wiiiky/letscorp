package org.wiky.letscorp.activity;

import android.os.Bundle;

import org.wiky.letscorp.R;
import org.wiky.letscorp.data.model.PostItem;

public class PostActivity extends BaseActivity {

    private PostItem mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mData = getIntent().getParcelableExtra("data");
        setContentView(R.layout.activity_post);

        setTitle(mData.Title);

//        Slide slide = new Slide(Gravity.LEFT);
//        slide.setDuration(300);
//        getWindow().setEnterTransition(slide);
//
//        slide = new Slide(Gravity.RIGHT);
//        slide.setDuration(300);
//        getWindow().setReturnTransition(slide);
    }
}
