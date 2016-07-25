package org.wiky.letscorp.adapter;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.wiky.letscorp.Application;
import org.wiky.letscorp.R;
import org.wiky.letscorp.data.db.QueryHelper;
import org.wiky.letscorp.data.model.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wiky on 7/25/16.
 */
public class QueryAdapter extends BaseAdapter {

    private static final int AUTOCOMPLETE_COUNT = 5;

    private List<Query> mData;
    private String mQuery = "";
    private OnItemClickListener mOnItemClickListener = null;

    public QueryAdapter() {
        mData = QueryHelper.getQueries(mQuery, AUTOCOMPLETE_COUNT);
    }

    public void update(String query) {
        mQuery = query;
        update();
    }

    public void clear() {
        notifyDataSetInvalidated();
        mData = new ArrayList<>();
        notifyDataSetChanged();
    }

    public QueryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_query, parent, false);
        return new QueryHolder(v);
    }

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
                mData.remove(query);
                QueryHelper.deleteQuery(query.query);
                update();
            }
        });
    }

    private void update() {
        mData = QueryHelper.getQueries(mQuery, AUTOCOMPLETE_COUNT);
        if (mData.isEmpty()) {
            notifyDataSetInvalidated();
        } else {
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            QueryHolder holder = onCreateViewHolder(viewGroup, 0);
            view = holder.itemView;
            view.setTag(holder);
        }
        QueryHolder holder = (QueryHolder) view.getTag();
        onBindViewHolder(holder, i);
        return view;
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
