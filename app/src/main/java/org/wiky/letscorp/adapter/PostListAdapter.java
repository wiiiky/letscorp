package org.wiky.letscorp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
    private boolean mLoading;

    public PostListAdapter() {
        mData = new ArrayList<>();
        mLoading = false;
    }

    public void addPosts(List<PostItem> data) {
        int start = mData.size();
        mData.addAll(data);
        notifyItemRangeInserted(start, data.size());
    }

    public void setLoading() {
        mLoading = true;
        notifyDataSetChanged();
    }

    public void setLoaded() {
        mLoading = false;
        notifyItemRemoved(mData.size());
    }

    public boolean isLoading() {
        return mLoading;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == VIEW_TYPE_DEFAULT) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
            holder = new PostItemHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_loader, parent, false);
            holder = new LoaderHolder(v);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PostItemHolder) {
            PostItemHolder viewHolder = (PostItemHolder) holder;
            PostItem data = mData.get(position);
            viewHolder.mTitle.setText(data.Title);
            if (!data.Img.isEmpty()) {
                viewHolder.mImage.setVisibility(View.VISIBLE);
                viewHolder.mImage.setURL(data.Img);
            } else {
                viewHolder.mImage.setVisibility(View.GONE);
            }
        } else if (holder instanceof LoaderHolder) {
            LoaderHolder viewHolder = (LoaderHolder) holder;
            viewHolder.mLoader.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mData.size()) {
            return VIEW_TYPE_DEFAULT;
        }
        return VIEW_TYPE_LOADER;
    }

    @Override
    public int getItemCount() {
        return mData.size() + (mLoading ? 1 : 0);
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

    public class LoaderHolder extends RecyclerView.ViewHolder {

        public ProgressBar mLoader;

        public LoaderHolder(View itemView) {
            super(itemView);
            mLoader = (ProgressBar) itemView.findViewById(R.id.loader);
        }
    }

}