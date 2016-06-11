package org.wiky.letscorp.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.wiky.letscorp.R;
import org.wiky.letscorp.api.API;
import org.wiky.letscorp.data.model.PostItem;
import org.wiky.letscorp.view.CardItemDecoration;
import org.wiky.letscorp.view.PostListAdapter;

import java.util.List;

public class MainActivity extends BaseActivity {

    private RecyclerView mPostListView;
    private PostListAdapter mPostListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPostListAdapter = new PostListAdapter();
        mPostListView = (RecyclerView) findViewById(R.id.post_list);
        mPostListView.setLayoutManager(new LinearLayoutManager(this));
        mPostListView.addItemDecoration(new CardItemDecoration(20));
        mPostListView.setAdapter(mPostListAdapter);

        API.getPostList(1, new API.ApiResponseHandler() {
            @Override
            public void onSuccess(Object data) {
                List<PostItem> posts = (List<PostItem>) data;
                mPostListAdapter.setData(posts);
                for (PostItem post : posts) {
                    Log.d("POST", post.Id + ":" + post.Title + ":" + post.Href);
                }
            }
        });
    }
}
