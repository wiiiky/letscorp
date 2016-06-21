package org.wiky.letscorp.data.model;

import java.util.ArrayList;

/**
 * Created by wiky on 6/21/16.
 */
public class Post {
    public String href;
    public String title;
    public String content;
    public ArrayList<String> tags;
    public ArrayList<String> categories;
    public String date;
    public String author;

    public Post(String href, String title, String content, ArrayList<String> tags,
                ArrayList<String> categories, String date, String author) {
        this.href = href;
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.categories = categories;
        this.date = date;
        this.author = author;
    }
}
