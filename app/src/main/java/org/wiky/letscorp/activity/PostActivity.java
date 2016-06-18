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
    }
}
