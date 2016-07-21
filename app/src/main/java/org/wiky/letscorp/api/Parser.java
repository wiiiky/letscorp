package org.wiky.letscorp.api;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.wiky.letscorp.data.model.Comment;
import org.wiky.letscorp.data.model.Post;
import org.wiky.letscorp.data.model.PostItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by wiky on 6/11/16.
 * 从HTML中解析文章信息，如果HTML结构变化只需要更改这里
 */
public class Parser {
    /* 解析列表中的文章信息 */
    public static PostItem parsePostItem(Element e, int category) {
        Element titleElement = e.select("div.entry-title a").first();
        Element imgELement = e.select("img").first();
        Element contentElement = e.select("div.entry-content").first();
        Element commentElement = e.select("footer div.comments-link a").first();
        Element dateElement = e.select("footer time.entry-date").first();
        if (titleElement == null) {
            return null;
        } else if (contentElement == null) {
            if ((contentElement = e.select("div.entry-summary").first()) == null) {
                return null;
            }
        }
        String id, title, href, content, img = "", commentCount = "", date = "";
        id = e.id();
        title = titleElement.text();
        href = titleElement.attr("href");
        if (imgELement != null && !imgELement.attr("data-original").isEmpty()) {
            img = imgELement.attr("data-original");
        }
        for (Element t : contentElement.getAllElements()) { /* 删除多余的标签 */
            if (Objects.equals(t.tagName(), "a") || t.text().isEmpty()) {
                t.remove();
            }
        }
        content = contentElement.html();
        if (commentElement != null) {
            commentCount = commentElement.text();
            if (!commentCount.endsWith("条评论")) {
                commentCount = "没有评论";
            }
        }
        if (dateElement != null) {
            date = dateElement.text();
        }

        if (id.startsWith("post-")) {
            id = id.substring(5);
        }

        return new PostItem(Integer.parseInt(id), title, href, img, content, commentCount, date, category);
    }

    public static List<PostItem> parsePostItems(Document doc, int category) {
        List<PostItem> items = new ArrayList<>();
        for (Element post : doc.select("article.post")) {
            PostItem item = parsePostItem(post, category);
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }

    /* 解析评论的引用 */
    public static Comment.CommentCite parseCommentCite(Element e) {
        try {
            String id = e.attr("cite").substring(1);
            String username = e.select("p:first-child strong a").first().ownText();

            e = e.clone();
            e.select("p:first-child strong").remove();
            e.select("p:first-child br").remove();
            String content = e.select("p").outerHtml();
            return new Comment.CommentCite(id, username, content);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /* 解析评论 */
    public static Comment parseComment(Element e) {
        try {
            Element article = e.select(">article").first();
            String id = article.attr("id").substring(4);
            String avatar = article.select("div.vcard > img").first().attr("data-original");
            String username = article.select("div.vcard > b.fn").first().text();
            String datetime = article.select("time").first().text();
            String content = article.select("div.comment-content > p").outerHtml();
            Comment.CommentCite commentCite = null;
            Element blockquote = article.select("blockquote").first();
            if (blockquote != null) {
                commentCite = parseCommentCite(blockquote);
            }
            return new Comment(id, username, avatar, datetime, content, commentCite);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /* 解析文章详情 */
    public static Post parsePost(Element doc, String href) {
        Element p = doc.select("article.post").first();
        String title = p.select("div.entry-title > h2").text();
        String content = p.select("div.entry-content").html();
        ArrayList<String> tags = new ArrayList<>();
        for (Element t : p.select("p.tags > a")) {
            tags.add(t.text());
        }
        ArrayList<String> categories = new ArrayList<>();
        for (Element t : p.select("p.categories > a")) {
            categories.add(t.text());
        }
        String date = p.select("p.date time.entry-date").text();
        String author = p.select("p.date a[rel=author]").text();

        List<Comment> comments = new ArrayList<>();

        for (Element c : doc.select("div.comments-area > ol.comment-list > li.comment")) {
            Comment comment = parseComment(c);
            if (comment != null) {
                comments.add(comment);
            }
        }
        return new Post(href, title, content, tags, categories, date, author, comments);
    }
}
