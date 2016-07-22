package org.wiky.letscorp.list;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.wiky.letscorp.Application;
import org.wiky.letscorp.R;
import org.wiky.letscorp.data.model.PostItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by wiky on 6/14/16.
 * 文章列表适配器
 */
public class PostListAdapter extends RecyclerView.Adapter {

    public static final int VIEW_TYPE_DEFAULT = 1;
    public static final int VIEW_TYPE_LOADER = 2;

    private List<PostItem> mItems;
    private boolean mLoading;
    private OnItemClickListener mOnItemClickListener;

    public PostListAdapter() {
        mItems = new ArrayList<>();
        mLoading = false;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    /* 添加数据 */
    public void addItems(List<PostItem> items) {
        int start = mItems.size();
        mItems.addAll(items);
        notifyItemRangeInserted(start, items.size());
    }

    /* 重置数据 */
    public void resetItems(final List<PostItem> data) {
        int count = getItemCount();
        mLoading = false;
        mItems.clear();
        notifyItemRangeRemoved(0, count);
        Application.getUIHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mItems.addAll(data);
                notifyItemRangeInserted(0, mItems.size());
            }
        }, 250);
    }

    /* 设置当前状态为载入更多 */
    public void setLoading() {
        mLoading = true;
        notifyDataSetChanged();
    }

    public void setLoaded() {
        mLoading = false;
        notifyItemRemoved(mItems.size());
    }

    /* 判断当前是否在载入 */
    public boolean isLoading() {
        return mLoading;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == VIEW_TYPE_DEFAULT) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
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
            final PostItemHolder viewHolder = (PostItemHolder) holder;
            final PostItem data = mItems.get(position);
            viewHolder.mTitle.setText(data.title);
            viewHolder.mContent.setText(Html.fromHtml(data.content));
            viewHolder.mComment.setText(data.commentCount);
            viewHolder.mDate.setText(data.date);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(viewHolder, data);
                }
            });
            if (data.readn) { /* 文章已经阅读过 */
                viewHolder.mTitle.setTextColor(Color.GRAY);
            } else {
                viewHolder.mTitle.setTextColor(Color.BLACK);
            }
        }
    }

    /*  将指定ID的文章设置成已读 */
    public void setItemReadn(String href) {
        for (int i = 0; i < mItems.size(); i++) {
            PostItem item = mItems.get(i);
            if (Objects.equals(item.href, href)) {
                if (!item.readn) {
                    item.readn = true;
                    notifyItemChanged(i);
                }
                break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mItems.size()) {
            return VIEW_TYPE_DEFAULT;
        }
        return VIEW_TYPE_LOADER;
    }

    @Override
    public int getItemCount() {
        return mItems.size() + (mLoading ? 1 : 0);
    }

    public interface OnItemClickListener {
        void onItemClick(PostItemHolder holder, PostItem data);
    }

    public class PostItemHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;
        public TextView mContent;
        public TextView mComment;
        public TextView mDate;

        public PostItemHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.item_title);
            mContent = (TextView) itemView.findViewById(R.id.item_content);
            mComment = (TextView) itemView.findViewById(R.id.item_comment);
            mDate = (TextView) itemView.findViewById(R.id.item_date);
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