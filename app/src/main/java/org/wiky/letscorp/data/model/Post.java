package org.wiky.letscorp.data.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

/**
 * 文章详情
 */
public class Post {
    public int id;
    public String href;
    public String title;
    public String content;
    public List<String> tags;
    public List<String> categories;
    public long timestamp;
    public String author;
    public List<Comment> comments;

    public Post(int id, String href, String title, String content, List<String> tags,
                List<String> categories, long timestamp, String author, List<Comment> comments) {
        this.id = id;
        this.href = href;
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.categories = categories;
        this.timestamp = timestamp;
        this.author = author;
        this.comments = comments;
    }

    public static List<Comment> parseComments(String data) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Comment>>() {
        }.getType();
        return gson.fromJson(data, listType);
    }

    public String getDatetime() {
        if (timestamp <= 0) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(timestamp);
    }

    public int commentCount() {
        int size = 0;
        for (Comment c : comments) {
            size += c.size();
        }
        return size;
    }

    public String getComments() {
        Gson gson = new Gson();
        return gson.toJson(comments);
    }
}
