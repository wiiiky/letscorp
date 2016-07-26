package org.wiky.letscorp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.wiky.letscorp.Application;
import org.wiky.letscorp.R;
import org.wiky.letscorp.api.Const;

/*
 * 文章列表页的一项
 */
public class PostItem implements Parcelable {
    public static final Creator<PostItem> CREATOR = new Creator<PostItem>() {
        @Override
        public PostItem createFromParcel(Parcel in) {
            return new PostItem(in);
        }

        @Override
        public PostItem[] newArray(int size) {
            return new PostItem[size];
        }
    };
    public int id;
    public String title;
    public String href;
    public String img;
    public String content;
    public int commentCount;
    public String date;
    public boolean readn;
    public int category;


    public PostItem(int id, String title, String href, String img, String content, int ccount, String date) {
        this.id = id;
        this.title = title;
        this.href = href;
        this.img = img;
        this.content = content;
        this.commentCount = ccount;
        this.date = date;
        readn = false;
        category = Const.LETSCORP_CATEGORY_ALL;
    }

    public PostItem(int id, String title, String href, String img, String content, int ccount, String date, int cate) {
        this(id, title, href, img, content, ccount, date);
        category = cate;
    }

    protected PostItem(Parcel in) {
        id = in.readInt();
        title = in.readString();
        href = in.readString();
        img = in.readString();
        content = in.readString();
        commentCount = in.readInt();
        date = in.readString();
        readn = in.readInt() > 0;
        category = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(href);
        dest.writeString(img);
        dest.writeString(content);
        dest.writeInt(commentCount);
        dest.writeString(date);
        dest.writeInt(readn ? 1 : 0);
        dest.writeInt(category);
    }

    public String commentCount() {
        if (commentCount == 0) {
            return "";
        }
        return String.format(Application.getApplication().getString(R.string.comment_count), commentCount);
    }
}
