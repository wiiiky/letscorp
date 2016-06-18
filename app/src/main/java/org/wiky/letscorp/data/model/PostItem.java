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
    public String Id;
    public String Title;
    public String Href;
    public String Img;
    public String Content;
    public String CommentCount;
    public String Date;

    public PostItem(String id, String title, String href, String img, String content, String ccount, String date) {
        Id = id;
        Title = title;
        Href = href;
        Img = img;
        Content = content;
        CommentCount = ccount;
        Date = date;
    }

    protected PostItem(Parcel in) {
        Id = in.readString();
        Title = in.readString();
        Href = in.readString();
        Img = in.readString();
        Content = in.readString();
        CommentCount = in.readString();
        Date = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(Title);
        dest.writeString(Href);
        dest.writeString(Img);
        dest.writeString(Content);
        dest.writeString(CommentCount);
        dest.writeString(Date);
    }
}
