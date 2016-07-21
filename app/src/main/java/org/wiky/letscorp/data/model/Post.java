package org.wiky.letscorp.data.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
    public List<Comment> comments;

    public Post(String href, String title, String content, List<String> tags,
                List<String> categories, String date, String author) {
        this(href, title, content, tags, categories, date, author, new ArrayList<Comment>());
    }

    public Post(String href, String title, String content, List<String> tags,
                List<String> categories, String date, String author, List<Comment> comments) {
        this.href = href;
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.categories = categories;
        this.date = date;
        this.author = author;
        this.comments = comments;
    }

    public static List<Comment> parseComments(String data) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Comment>>() {
        }.getType();
        return gson.fromJson(data, listType);
    }

    public String comments() {
        Gson gson = new Gson();
        return gson.toJson(comments);
    }
}
