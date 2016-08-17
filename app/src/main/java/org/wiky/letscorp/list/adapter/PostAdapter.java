package org.wiky.letscorp.list.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.wiky.letscorp.R;
import org.wiky.letscorp.util.Util;
import org.wiky.letscorp.view.ImageViewer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Created by wiky on 8/9/16.
 */
public class PostAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_SEGMENT = 1;
    private String mTitle;
    private List<String> mTags, mCategories;
    private String mAuthor;
    private String mDatetime;
    private List<Segment> mContent;
    private Context mContext;
    private View.OnClickListener mOnImageClickListener;

    public PostAdapter(Context context, String title, List<String> tags, List<String> categories, String author,
                       String datetime, String content, View.OnClickListener listener) {
        mContext = context;
        mTitle = title;
        mTags = tags;
        mCategories = categories;
        mAuthor = author;
        mDatetime = datetime;
        mContent = parseContent(content);
        mOnImageClickListener = listener;
    }

    /* 将文章内容解析成不同成分 TODO */
    private List<Segment> parseContent(String content) {
        ArrayList<Segment> segments = new ArrayList<>();
        Document doc = Jsoup.parseBodyFragment(content);
        for (Element e : doc.body().children()) {
            String tag = e.tagName();
            if (Objects.equals(tag, "p")) {
                Elements imgs = e.select(">a>img");
                if (!imgs.isEmpty()) {
                    for (Element img : imgs) {
                        String url = img.attr("data-original");
                        Glide.with(mContext)
                                .load(url)
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .preload();
                        segments.add(new Segment(SegmentType.IMAGE, url));
                    }
                } else {
                    segments.add(new Segment(Objects.equals(tag, "blockquote") ? SegmentType.QUOTE : SegmentType.PLAIN, e.html()));
                }
            } else if (Objects.equals(tag, "blockquote")) {
                for (Element p : e.select(">p")) {
                    segments.add(new Segment(Objects.equals(tag, "blockquote") ? SegmentType.QUOTE : SegmentType.PLAIN, p.html()));
                }
            } else {
                Toast.makeText(mContext, String.format("unknown tag %s", tag), Toast.LENGTH_SHORT).show();
            }
        }
        return segments;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_HEADER;
        }
        return VIEW_TYPE_SEGMENT;
    }

    public void update(String title, List<String> tags, List<String> categories, String author, String datetime, String content) {
        mTitle = title;
        mTags = tags;
        mCategories = categories;
        mAuthor = author;
        mDatetime = datetime;
        if (mContent.isEmpty()) {
            mContent = parseContent(content);
            notifyDataSetChanged();
        } else {
            notifyItemChanged(0);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == VIEW_TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_header, parent, false);
            holder = new HeaderHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_segment, parent, false);
            holder = new SegmentHolder(v, mOnImageClickListener);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderHolder) {
            HeaderHolder headerHolder = (HeaderHolder) holder;
            headerHolder.title.setText(mTitle);
            headerHolder.author.setText(String.format("%s %s %s", mAuthor, mContext.getString(R.string.published_on), mDatetime));
            if (!mCategories.isEmpty()) {
                headerHolder.category.setText(String.format("分类：%s", Util.joinString(mCategories)));
            } else {
                headerHolder.category.setText("");
            }
            if (!mTags.isEmpty()) {
                headerHolder.tag.setText(String.format("标签：%s", Util.joinString(mTags)));
            } else {
                headerHolder.tag.setText("");
            }
        } else {
            SegmentHolder segmentHolder = (SegmentHolder) holder;
            Segment segment = getSegment(position);
            if (segment.type == SegmentType.IMAGE) {
                segmentHolder.image.setVisibility(View.VISIBLE);
                segmentHolder.text.setVisibility(View.GONE);
                segmentHolder.quote.setVisibility(View.GONE);
                segmentHolder.image.setUrl(segment.data, true);
            } else if (segment.type == SegmentType.PLAIN) {
                segmentHolder.image.setVisibility(View.GONE);
                segmentHolder.text.setVisibility(View.VISIBLE);
                segmentHolder.quote.setVisibility(View.GONE);
                segmentHolder.text.setText(Html.fromHtml(segment.data));
            } else if (segment.type == SegmentType.QUOTE) {
                segmentHolder.image.setVisibility(View.GONE);
                segmentHolder.text.setVisibility(View.GONE);
                segmentHolder.quote.setVisibility(View.VISIBLE);
                segmentHolder.quote.setText(Html.fromHtml(segment.data));
            }
        }
    }

    private Segment getSegment(int position) {
        return mContent.get(position - 1);
    }

    @Override
    public int getItemCount() {
        return 1 + mContent.size();
    }

    private enum SegmentType {
        PLAIN,
        QUOTE,
        IMAGE
    }

    private static class Segment {
        public SegmentType type;
        public String data;

        public Segment(SegmentType t, String d) {
            type = t;
            data = d;
        }
    }

    private static class SegmentHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public TextView quote;
        public ImageViewer image;

        public SegmentHolder(View root, View.OnClickListener listener) {
            super(root);
            text = (TextView) root.findViewById(R.id.post_content_text);
            image = (ImageViewer) root.findViewById(R.id.post_content_image);
            quote = (TextView) root.findViewById(R.id.post_content_quote);
            image.setTransitionName("image");
            image.setOnClickListener(listener);
        }
    }

    private static class HeaderHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView category;
        public TextView tag;
        public TextView author;

        public HeaderHolder(View root) {
            super(root);
            title = (TextView) root.findViewById(R.id.post_header_title);
            category = (TextView) root.findViewById(R.id.post_header_category);
            tag = (TextView) root.findViewById(R.id.post_header_tag);
            author = (TextView) root.findViewById(R.id.post_header_author);
        }
    }
}
