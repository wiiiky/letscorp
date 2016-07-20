package org.wiky.letscorp.list;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wiky.letscorp.R;
import org.wiky.letscorp.data.model.Comment;
import org.wiky.letscorp.view.CircleImageView;

import java.util.List;

/**
 * Created by wiky on 7/20/16.
 */
public class CommentListAdapter extends RecyclerView.Adapter {

    private List<Comment> mData;

    public CommentListAdapter(List<Comment> comments) {
        mData = comments;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Comment data = mData.get(position);
        viewHolder.avatar.setUrl(data.avatar, R.drawable.none);
        viewHolder.username.setText(data.username);
        viewHolder.datetime.setText(data.datetime);
        viewHolder.content.setText(Html.fromHtml(data.content));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setComments(List<Comment> comments) {
        mData = comments;
        notifyDataSetChanged();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        public TextView content;
        public TextView username;
        public TextView datetime;
        public CircleImageView avatar;

        public ViewHolder(View itemView) {
            super(itemView);
            avatar = (CircleImageView) itemView.findViewById(R.id.comment_avatar);
            username = (TextView) itemView.findViewById(R.id.comment_username);
            datetime = (TextView) itemView.findViewById(R.id.comment_datetime);
            content = (TextView) itemView.findViewById(R.id.comment_content);
        }
    }
}
