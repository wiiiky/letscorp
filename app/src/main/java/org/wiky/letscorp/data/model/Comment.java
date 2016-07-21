package org.wiky.letscorp.data.model;

import java.util.List;

/**
 * Created by wiky on 7/20/16.
 */
public class Comment {
    public String id;
    public String username;
    public String avatar;
    public String datetime;
    public String content;
    public CommentCite cite;
    public List<Comment> children;

    public Comment(String id, String username, String avatar, String datetime, String content) {
        this(id, username, avatar, datetime, content, null);
    }

    public Comment(String id, String username, String avatar, String datetime, String content, CommentCite cite) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
        this.datetime = datetime;
        this.content = content;
        this.cite = cite;
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
