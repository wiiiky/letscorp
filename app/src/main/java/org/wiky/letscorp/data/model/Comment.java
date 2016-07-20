package org.wiky.letscorp.data.model;

import org.json.JSONObject;

/**
 * Created by wiky on 7/20/16.
 */
public class Comment {
    public String id;
    public String username;
    public String avatar;
    public String datetime;
    public String content;
    public ReplyComment reply;

    public Comment(String id, String username, String avatar, String datetime, String content) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
        this.datetime = datetime;
        this.content = content;
    }

    public Comment(String id, String username, String avatar, String datetime, String content, ReplyComment replyComment) {
        this(id, username, avatar, datetime, content);
        this.reply = replyComment;
    }

    public static Comment fromJSON(JSONObject obj) {
        if (obj == null) {
            return null;
        }
        try {
            String id = obj.getString("id");
            String username = obj.getString("username");
            String avatar = obj.getString("avatar");
            String datetime = obj.getString("datetime");
            String content = obj.getString("content");
            ReplyComment reply = null;
            try {
                JSONObject rc = obj.getJSONObject("reply");
                if (rc != null) {
                    reply = new ReplyComment(rc.getString("id"), rc.getString("username"), rc.getString("content"));
                }
            } catch (Exception ignored) {

            }
            return new Comment(id, username, avatar, datetime, content, reply);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject toJSON() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);
            obj.put("username", username);
            obj.put("avatar", avatar);
            obj.put("datetime", datetime);
            obj.put("content", content);
            obj.put("reply", null);
            if (reply != null) {
                JSONObject rc = new JSONObject();
                rc.put("id", reply.id);
                rc.put("username", reply.username);
                rc.put("content", reply.content);
                obj.put("reply", rc);
            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class ReplyComment {
        public String id;
        public String username;
        public String content;

        public ReplyComment(String id, String name, String content) {
            this.id = id;
            this.username = name;
            this.content = content;
        }
    }
}
