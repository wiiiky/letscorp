package org.wiky.letscorp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wiky.letscorp.R;
import org.wiky.letscorp.data.model.PostItem;
import org.wiky.letscorp.view.ImageViewer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wiky on 6/14/16.
 */
public class PostListAdapter extends RecyclerView.Adapter {

    public static final int VIEW_TYPE_DEFAULT = 1;
    public static final int VIEW_TYPE_LOADER = 2;

    private List<PostItem> mData;

    public PostListAdapter() {
        mData = new ArrayList<>();
    }

    public void addPosts(List<PostItem> data) {
        int start = mData.size() - 1;
        mData = data;
        notifyItemRangeInserted(start, mData.size());
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
        viewHolder.mImage.setURL(data.Img);
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_DEFAULT;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class PostItemHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;
        public ImageViewer mImage;

        public PostItemHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mImage = (ImageViewer) itemView.findViewById(R.id.img);
        }
    }

}