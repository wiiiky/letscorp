package org.wiky.letscorp.api;

import org.jsoup.nodes.Element;
import org.wiky.letscorp.data.model.PostItem;

/**
 * Created by wiky on 6/11/16.
 */
public class Parser {
    public static PostItem parsePostItem(Element post) {
        Element _a = post.select("div.entry-title a").first();
        Element _img = post.select("img").first();
        Element _content = post.select("div.entry-content").first();
        Element _comment = post.select("footer div.comments-link a").first();
        Element _date = post.select("footer time.entry-date").first();
        if (_a == null || _content == null) {
            return null;
        }
        String id, title, href, content, img = "", commentCount = "", date = "";
        id = post.id();
        title = _a.text();
        href = _a.attr("href");
        if (_img != null && !_img.attr("data-original").isEmpty()) {
            img = _img.attr("data-original");
        }
        for (Element e : _content.getAllElements()) { /* 删除多余的标签 */
            if (e.tagName() == "a" || e.text().isEmpty()) {
                e.remove();
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
}
