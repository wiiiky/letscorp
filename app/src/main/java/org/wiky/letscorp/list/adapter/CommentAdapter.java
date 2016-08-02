package org.wiky.letscorp.list.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wiky.letscorp.R;
import org.wiky.letscorp.data.model.Comment;
import org.wiky.letscorp.list.CommentReplyListView;
import org.wiky.letscorp.util.Util;
import org.wiky.letscorp.view.CircleImageView;

import java.util.List;


public class CommentAdapter extends RecyclerView.Adapter {

    private List<Comment> mData;

    public CommentAdapter(List<Comment> comments) {
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
        final Comment data = mData.get(position);
        viewHolder.avatar.setImageResource(R.mipmap.ic_face_black);
        viewHolder.username.setText(data.username);
        viewHolder.datetime.setText(data.datetime);
        viewHolder.content.setText(Util.trim(Html.fromHtml(data.content)));
        if (data.cite != null) {
            viewHolder.citeLayout.setVisibility(View.VISIBLE);
            viewHolder.citeUsername.setText(data.cite.username);
            viewHolder.citeContent.setText(Util.trim(Html.fromHtml(data.cite.content)));
        } else {
            viewHolder.citeLayout.setVisibility(View.GONE);
        }
        if (!data.children.isEmpty()) {
            viewHolder.replyContainer.setVisibility(View.VISIBLE);
            viewHolder.reply.setComments(data.children);
        } else {
            viewHolder.replyContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setComments(final List<Comment> comments) {
        List<Comment> oldData = mData;
        mData = comments;
        if (mData.size() > oldData.size()) {
            notifyItemRangeChanged(0, oldData.size());
            notifyItemRangeInserted(oldData.size(), mData.size() - oldData.size());
        } else {
            notifyItemRangeChanged(0, mData.size());
            notifyItemRangeRemoved(mData.size(), oldData.size() - mData.size());
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        public TextView content;
        public TextView username;
        public TextView datetime;
        public CircleImageView avatar;
        public ViewGroup citeLayout;
        public TextView citeUsername;
        public TextView citeContent;
        public ViewGroup replyContainer;
        public CommentReplyListView reply;

        public ViewHolder(View itemView) {
            super(itemView);
            avatar = (CircleImageView) itemView.findViewById(R.id.comment_avatar);
            username = (TextView) itemView.findViewById(R.id.comment_username);
            datetime = (TextView) itemView.findViewById(R.id.comment_datetime);
            content = (TextView) itemView.findViewById(R.id.comment_content);
            citeLayout = (ViewGroup) itemView.findViewById(R.id.comment_cite);
            citeUsername = (TextView) itemView.findViewById(R.id.comment_cite_username);
            citeContent = (TextView) itemView.findViewById(R.id.comment_cite_content);
            replyContainer = (ViewGroup) itemView.findViewById(R.id.comment_reply_container);
            reply = (CommentReplyListView) itemView.findViewById(R.id.comment_reply);

            content.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}
