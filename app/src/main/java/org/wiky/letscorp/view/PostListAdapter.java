package org.wiky.letscorp.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wiky.letscorp.R;
import org.wiky.letscorp.data.model.PostItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wiky on 6/11/16.
 */
public class PostListAdapter extends RecyclerView.Adapter {

    private List<PostItem> mData;

    public PostListAdapter(List<PostItem> data) {
        mData = data;
    }

    public PostListAdapter() {
        mData = new ArrayList<>();
    }

    public void setData(List<PostItem> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostItemHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PostItem data = mData.get(position);
        PostItemHolder viewHolder = (PostItemHolder) holder;
        viewHolder.mTitle.setText(data.Title);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private class PostItemHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;

        public PostItemHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
