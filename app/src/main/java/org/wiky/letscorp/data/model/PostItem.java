package org.wiky.letscorp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wiky on 6/11/16.
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
    public String commentCount;
    public String date;
    public boolean readn;


    public PostItem(int id, String title, String href, String img, String content, String ccount, String date) {
        this.id = id;
        this.title = title;
        this.href = href;
        this.img = img;
        this.content = content;
        this.commentCount = ccount;
        this.date = date;
        readn = false;
    }

    protected PostItem(Parcel in) {
        id = in.readInt();
        title = in.readString();
        href = in.readString();
        img = in.readString();
        content = in.readString();
        commentCount = in.readString();
        date = in.readString();
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
        dest.writeString(commentCount);
        dest.writeString(date);
    }
}
