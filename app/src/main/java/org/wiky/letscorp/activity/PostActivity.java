package org.wiky.letscorp.activity;

import android.os.Bundle;
import android.text.Html;
import android.transition.Explode;
import android.widget.TextView;

import org.wiky.letscorp.R;
import org.wiky.letscorp.data.model.PostItem;

public class PostActivity extends BaseActivity {

    private PostItem mData;
    private TextView mTitle;
    private TextView mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setEnterTransition(new Explode());

        mData = getIntent().getParcelableExtra("data");
        setContentView(R.layout.activity_post);

        setTitle(getString(R.string.post_activity_title));
        mTitle = (TextView) findViewById(R.id.post_title);
        mContent = (TextView) findViewById(R.id.post_content);

        mTitle.setText(mData.Title);
        mContent.setText(Html.fromHtml(mData.Content));
    }
}
