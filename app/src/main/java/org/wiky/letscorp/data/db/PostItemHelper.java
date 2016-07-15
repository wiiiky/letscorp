package org.wiky.letscorp.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import org.wiky.letscorp.Application;
import org.wiky.letscorp.api.Const;
import org.wiky.letscorp.data.model.PostItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wiky on 7/13/16.
 */
public class PostItemHelper implements BaseColumns {

    public static final String TABLE_NAME = "post_item";
    public static final String COLUMN_NAME_ITEM_ID = "itemid";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_HREF = "href";
    public static final String COLUMN_NAME_IMG = "img";
    public static final String COLUMN_NAME_CONTENT = "content";
    public static final String COLUMN_NAME_COMMENT_COUNT = "comment_count";
    public static final String COLUMN_NAME_DATE = "date";
    public static final String COLUMN_NAME_CATEGORY = "category";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_NAME_ITEM_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_NAME_TITLE + " TEXT," +
            COLUMN_NAME_HREF + " TEXT," +
            COLUMN_NAME_IMG + " TEXT," +
            COLUMN_NAME_CONTENT + " TEXT," +
            COLUMN_NAME_COMMENT_COUNT + " TEXT," +
            COLUMN_NAME_DATE + " TEXT," +
            COLUMN_NAME_CATEGORY + " INTEGER" +
            ")";
    public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static ContentValues getContentValues(PostItem item) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_ITEM_ID, item.id);
        values.put(COLUMN_NAME_TITLE, item.title);
        values.put(COLUMN_NAME_HREF, item.href);
        values.put(COLUMN_NAME_IMG, item.img);
        values.put(COLUMN_NAME_CONTENT, item.content);
        values.put(COLUMN_NAME_COMMENT_COUNT, item.commentCount);
        values.put(COLUMN_NAME_DATE, item.date);
        values.put(COLUMN_NAME_CATEGORY, item.category);
        return values;
    }

    public static PostItem getPostItem(Cursor c) {
        int id = c.getInt(c.getColumnIndex(COLUMN_NAME_ITEM_ID));
        String title = c.getString(c.getColumnIndex(COLUMN_NAME_TITLE));
        String href = c.getString(c.getColumnIndex(COLUMN_NAME_HREF));
        String img = c.getString(c.getColumnIndex(COLUMN_NAME_IMG));
        String content = c.getString(c.getColumnIndex(COLUMN_NAME_CONTENT));
        String commentCount = c.getString(c.getColumnIndex(COLUMN_NAME_COMMENT_COUNT));
        String date = c.getString(c.getColumnIndex(COLUMN_NAME_DATE));
        int category = c.getInt(c.getColumnIndex(COLUMN_NAME_CATEGORY));
        PostItem p = new PostItem(id, title, href, img, content, commentCount, date, category);
        if (c.getColumnIndex("readn") >= 0) {
            p.readn = c.getInt(c.getColumnIndex("readn")) != 0;
        }
        return p;
    }

    public static PostItem getPostItem(int id) {
        SQLiteDatabase db = Application.getDBHelper().getReadableDatabase();
        Cursor c = db.query(TABLE_NAME, null, COLUMN_NAME_ITEM_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (c.moveToNext()) {
            return getPostItem(c);
        }
        return null;
    }

    public static List<PostItem> getPostItems(int page, int count) {
        String sql = String.format("SELECT *, IFNULL((SELECT 1 FROM %s AS `B` WHERE `A`.`%s`=`B`.`%s`), 0) AS `readn` FROM %s AS `A` ORDER BY %s DESC LIMIT %d OFFSET %d",
                PostHelper.TABLE_NAME, COLUMN_NAME_HREF, PostHelper.COLUMN_NAME_HREF, TABLE_NAME, COLUMN_NAME_ITEM_ID, count, (page - 1) * count);
        return getPostItems(sql);
    }

    public static List<PostItem> getPostItems(int category, int page, int count) {
        if (category == Const.LETSCORP_CATEGORY_ALL) {
            return getPostItems(page, count);
        }
        String sql = String.format("SELECT *, IFNULL((SELECT 1 FROM %s AS `B` WHERE `A`.`%s`=`B`.`%s`), 0) AS `readn` FROM %s AS `A` WHERE `A`.`%s`=%s ORDER BY %s DESC LIMIT %d OFFSET %d",
                PostHelper.TABLE_NAME, COLUMN_NAME_HREF, PostHelper.COLUMN_NAME_HREF, TABLE_NAME, COLUMN_NAME_CATEGORY, category, COLUMN_NAME_ITEM_ID, count, (page - 1) * count);
        return getPostItems(sql);
    }

    private static List<PostItem> getPostItems(String sql) {
        SQLiteDatabase db = Application.getDBHelper().getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        ArrayList<PostItem> items = new ArrayList<>();
        while (c.moveToNext()) {
            items.add(getPostItem(c));
        }
        return items;
    }

    public static boolean checkPostItem(int id) {
        SQLiteDatabase db = Application.getDBHelper().getReadableDatabase();
        Cursor c = db.query(TABLE_NAME, null, COLUMN_NAME_ITEM_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        return c.moveToNext();
    }

    public static void savePostItem(PostItem item) {
        SQLiteDatabase db = Application.getDBHelper().getWritableDatabase();
        ContentValues values = getContentValues(item);
        if (checkPostItem(item.id)) { /* 已存在 */
            db.update(TABLE_NAME, values, COLUMN_NAME_ITEM_ID + "=?", new String[]{String.valueOf(item.id)});
        } else {
            db.insert(TABLE_NAME, null, values);
        }
    }

    public static void savePostItems(List<PostItem> items) {
        for (PostItem item : items) {
            savePostItem(item);
        }
    }
}
