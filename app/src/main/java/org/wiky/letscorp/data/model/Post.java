package org.wiky.letscorp.data.model;

import java.util.List;

/**
 * Created by wiky on 6/21/16.
 */
public class Post {
    public String href;
    public String title;
    public String content;
    public List<String> tags;
    public List<String> categories;
    public String date;
    public String author;

    public Post(String href, String title, String content, List<String> tags,
                List<String> categories, String date, String author) {
        this.href = href;
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.categories = categories;
        this.date = date;
        this.author = author;
    }
}
