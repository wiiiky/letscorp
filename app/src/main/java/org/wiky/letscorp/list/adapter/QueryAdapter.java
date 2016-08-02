package org.wiky.letscorp.list.adapter;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.wiky.letscorp.Application;
import org.wiky.letscorp.R;
import org.wiky.letscorp.data.db.QueryHelper;
import org.wiky.letscorp.data.model.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by wiky on 7/25/16.
 * 搜索历史记录的适配器
 */
public class QueryAdapter extends RecyclerView.Adapter {

    private static final int AUTOCOMPLETE_COUNT = 5;

    private List<Query> mData;
    private String mQuery = "";
    private OnItemClickListener mOnItemClickListener = null;

    public QueryAdapter() {
        mData = new ArrayList<>();
    }

    public void update(String query) {
        mQuery = query;
        update();
    }

    public void clear() {
        List<Query> oldData = mData;
        mData = new ArrayList<>();
        notifyItemRangeRemoved(0, oldData.size());
    }

    @Override
    public QueryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_query, parent, false);
        return new QueryHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Query query = mData.get(position);
        final QueryHolder viewHolder = (QueryHolder) holder;
        viewHolder.query.setText(query.query);
        viewHolder.query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    Application.getUIHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mOnItemClickListener.onItemClick(viewHolder.itemView, query.query);
                        }
                    }, 250);
                }
            }
        });
        viewHolder.clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QueryHelper.deleteQuery(query.query);
                update();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private void update() {
        List<Query> oldData = mData;
        mData = QueryHelper.getQueries(mQuery, AUTOCOMPLETE_COUNT);
        if (mData.size() >= oldData.size()) {
            for (int i = 0; i < oldData.size(); i++) {
                if (!Objects.equals(oldData.get(i).query, mData.get(i).query)) {
                    notifyItemChanged(i);
                }
            }
            notifyItemRangeInserted(oldData.size(), mData.size() - oldData.size());
        } else {
            for (int i = 0; i < mData.size(); i++) {
                if (!Objects.equals(oldData.get(i).query, mData.get(i).query)) {
                    notifyItemChanged(i);
                }
            }
            notifyItemRangeRemoved(mData.size(), oldData.size() - mData.size());
        }
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, String query);
    }

    private class QueryHolder extends RecyclerView.ViewHolder {

        private TextView query;
        private ImageView clear;

        public QueryHolder(View itemView) {
            super(itemView);
            query = (TextView) itemView.findViewById(R.id.query_query);
            clear = (ImageView) itemView.findViewById(R.id.query_clear);
            clear.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        }
    }
}
