package org.wiky.letscorp.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import org.wiky.letscorp.Application;
import org.wiky.letscorp.data.model.PostItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wiky on 7/13/16.
 */
public class PostItemHelper implements BaseColumns {

    public static final String TABLE_NAME = "post_item";
    public static final String COLUMN_NAME_ITEM_ID = "id";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_HREF = "href";
    public static final String COLUMN_NAME_IMG = "img";
    public static final String COLUMN_NAME_CONTENT = "content";
    public static final String COLUMN_NAME_COMMENT_COUNT = "comment_count";
    public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
    public static final String COLUMN_NAME_CATEGORY = "category";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_NAME_ITEM_ID + " INTEGER," +
            COLUMN_NAME_TITLE + " TEXT," +
            COLUMN_NAME_HREF + " TEXT," +
            COLUMN_NAME_IMG + " TEXT," +
            COLUMN_NAME_CONTENT + " TEXT," +
            COLUMN_NAME_COMMENT_COUNT + " INTEGER," +
            COLUMN_NAME_TIMESTAMP + " INTEGER," +
            COLUMN_NAME_CATEGORY + " INTEGER," +
            "PRIMARY KEY(" + COLUMN_NAME_ITEM_ID + "," + COLUMN_NAME_CATEGORY + ")" +
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
        values.put(COLUMN_NAME_TIMESTAMP, item.timestamp);
        values.put(COLUMN_NAME_CATEGORY, item.category);
        return values;
    }

    public static PostItem getPostItem(Cursor c) {
        int id = c.getInt(c.getColumnIndex(COLUMN_NAME_ITEM_ID));
        String title = c.getString(c.getColumnIndex(COLUMN_NAME_TITLE));
        String href = c.getString(c.getColumnIndex(COLUMN_NAME_HREF));
        String img = c.getString(c.getColumnIndex(COLUMN_NAME_IMG));
        String content = c.getString(c.getColumnIndex(COLUMN_NAME_CONTENT));
        int commentCount = c.getInt(c.getColumnIndex(COLUMN_NAME_COMMENT_COUNT));
        long timestamp = c.getLong(c.getColumnIndex(COLUMN_NAME_TIMESTAMP));
        int category = c.getInt(c.getColumnIndex(COLUMN_NAME_CATEGORY));
        PostItem p = new PostItem(id, title, href, img, content, commentCount, timestamp, category);
        if (c.getColumnIndex("readn") >= 0) {
            p.readn = c.getInt(c.getColumnIndex("readn")) != 0;
        }
        return p;
    }

    public static void deletePostItems() {
        SQLiteDatabase db = Application.getDBHelper().getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    public static List<PostItem> getPostItems(int category, int page, int count) {
        String sql = String.format("SELECT *, IFNULL((SELECT 1 FROM %s AS `B` WHERE `A`.`%s`=`B`.`%s`), 0) AS `readn` FROM %s AS `A` WHERE `A`.`%s`=%s ORDER BY %s DESC, %s DESC LIMIT %d OFFSET %d",
                PostHelper.TABLE_NAME, COLUMN_NAME_HREF, PostHelper.COLUMN_NAME_HREF, TABLE_NAME, COLUMN_NAME_CATEGORY, category, COLUMN_NAME_TIMESTAMP, COLUMN_NAME_ITEM_ID, count, (page - 1) * count);
        return getPostItems(sql);
    }

    private static List<PostItem> getPostItems(String sql) {
        SQLiteDatabase db = Application.getDBHelper().getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        ArrayList<PostItem> items = new ArrayList<>();
        while (c.moveToNext()) {
            PostItem item=getPostItem(c);
            items.add(item);
        }
        c.close();
        return items;
    }

    public static boolean checkPostItem(int category, int id) {
        SQLiteDatabase db = Application.getDBHelper().getReadableDatabase();
        Cursor c = db.query(TABLE_NAME, null, COLUMN_NAME_ITEM_ID + "=? AND " + COLUMN_NAME_CATEGORY + "=?",
                new String[]{String.valueOf(id),String.valueOf(category)}, null, null, null);
        boolean exists = c.moveToNext();
        c.close();
        return exists;
    }

    public static void savePostItem(PostItem item) {
        SQLiteDatabase db = Application.getDBHelper().getWritableDatabase();
        ContentValues values = getContentValues(item);
        if (checkPostItem(item.category,item.id)) { /* 已存在 */
            db.update(TABLE_NAME, values, COLUMN_NAME_ITEM_ID + "=? & " + COLUMN_NAME_CATEGORY + "=?",
                    new String[]{String.valueOf(item.id),String.valueOf(item.category)});
        } else {
            db.insert(TABLE_NAME, null, values);
        }
    }

    /* 更新评论数和发布时间 */
    public static void updatePostItem(int id, int ccount, long timestamp) {
        SQLiteDatabase db = Application.getDBHelper().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_COMMENT_COUNT, ccount);
        values.put(COLUMN_NAME_TIMESTAMP, timestamp);
        db.update(TABLE_NAME, values, String.format("%s=?", COLUMN_NAME_ITEM_ID), new String[]{String.valueOf(id)});
    }

    public static void savePostItems(List<PostItem> items) {
        for (PostItem item : items) {
            savePostItem(item);
        }
    }
}
