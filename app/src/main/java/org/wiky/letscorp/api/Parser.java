package org.wiky.letscorp.api;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
    public static PostItem parsePostItem(Element e) {
        Element _a = e.select("div.entry-title a").first();
        Element _img = e.select("img").first();
        Element _content = e.select("div.entry-content").first();
        Element _comment = e.select("footer div.comments-link a").first();
        Element _date = e.select("footer time.entry-date").first();
        if (_a == null || _content == null) {
            return null;
        }
        String id, title, href, content, img = "", commentCount = "", date = "";
        id = e.id();
        title = _a.text();
        href = _a.attr("href");
        if (_img != null && !_img.attr("data-original").isEmpty()) {
            img = _img.attr("data-original");
        }
        for (Element t : _content.getAllElements()) { /* 删除多余的标签 */
            if (Objects.equals(t.tagName(), "a") || t.text().isEmpty()) {
                t.remove();
            }
        }
        content = _content.html();
        if (_comment != null) {
            commentCount = _comment.text();
            if (!commentCount.endsWith("条评论")) {
                commentCount = "没有评论";
            }
        }
        if (_date != null) {
            date = _date.text();
        }

        if (id.startsWith("post-")) {
            id = id.substring(5);
        }

        return new PostItem(Integer.parseInt(id), title, href, img, content, commentCount, date);
    }

    public static List<PostItem> parsePostItems(Document doc) {
        List<PostItem> items = new ArrayList<PostItem>();
        for (Element post : doc.select("article.post")) {
            PostItem item = parsePostItem(post);
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }

    /* 解析文章详情 */
    public static Post parsePost(Element doc, String href) {
        Element e = doc.select("article.post").first();
        String title = e.select("div.entry-title > h2").text();
        String content = e.select("div.entry-content").html();
        ArrayList<String> tags = new ArrayList<>();
        for (Element t : e.select("p.tags > a")) {
            tags.add(t.text());
        }
        ArrayList<String> categories = new ArrayList<>();
        for (Element t : e.select("p.categories > a")) {
            categories.add(t.text());
        }
        String date = e.select("p.date time.entry-date").text();
        String author = e.select("p.date a[rel=author]").text();
        return new Post(href, title, content, tags, categories, date, author);
    }
}
