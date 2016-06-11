package org.wiky.letscorp.data.model;

/**
 * Created by wiky on 6/11/16.
 */
public class PostItem {
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

}
