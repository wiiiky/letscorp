package org.wiky.letscorp.data.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 文章评论
 */
public class Comment {
    public int id;
    public String username;
    public String avatar;
    public String datetime;
    public String content;
    public CommentCite cite;
    public List<Comment> children;

    public Comment(int id, String username, String avatar, String datetime, String content) {
        this(id, username, avatar, datetime, content, null, new ArrayList<Comment>());
    }

    public Comment(int id, String username, String avatar, String datetime, String content, CommentCite cite,
                   List<Comment> children) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
        this.datetime = datetime;
        this.content = content;
        this.cite = cite;
        this.children = children;
    }

    public static int getChildrenSize(List<Comment> children) {
        int size = 0;
        for (Comment c : children) {
            size += c.size();
        }
        return size;
    }

    public int size() {
        return 1 + getChildrenSize(children);
    }

    /* 评论的引用 */
    public static class CommentCite {
        public String id;
        public String username;
        public String content;

        public CommentCite(String id, String name, String content) {
            this.id = id;
            this.username = name;
            this.content = content;
        }
    }
}
