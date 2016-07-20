package org.wiky.letscorp.data.model;

import org.json.JSONArray;
import org.json.JSONException;

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
        List<Comment> comments = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(data);
            for (int i = 0; i < array.length(); i++) {
                Comment comment = Comment.fromJSON(array.getJSONObject(i));
                if (comment != null) {
                    comments.add(comment);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return comments;
    }

    public String comments() {
        JSONArray array = new JSONArray();
        for (Comment c : comments) {
            array.put(c.toJSON());
        }
        return array.toString();
    }
}
