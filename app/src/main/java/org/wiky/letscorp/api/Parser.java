package org.wiky.letscorp.api;

import org.jsoup.nodes.Element;
import org.wiky.letscorp.data.model.Post;
import org.wiky.letscorp.data.model.PostItem;

import java.util.ArrayList;

/**
 * Created by wiky on 6/11/16.
 */
public class Parser {
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
            if (t.tagName() == "a" || t.text().isEmpty()) {
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

        return new PostItem(id, title, href, img, content, commentCount, date);
    }

    public static Post parsePost(Element e, String href) {
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
