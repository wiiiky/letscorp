package org.wiky.letscorp.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import org.wiky.letscorp.Application;
import org.wiky.letscorp.data.model.Post;
import org.wiky.letscorp.util.Util;

import java.util.List;

/**
 * Created by wiky on 7/13/16.
 */
public class PostHelper implements BaseColumns {
    public static final String TABLE_NAME = "post";
    public static final String COLUMN_NAME_HREF = "href";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_CONTENT = "content";
    public static final String COLUMN_NAME_TAGS = "tags";
    public static final String COLUMN_NAME_CATEGORIES = "categories";
    public static final String COLUMN_NAME_DATE = "date";
    public static final String COLUMN_NAME_AUTHOR = "author";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_NAME_HREF + " TEXT PRIMARY KEY, " +
            COLUMN_NAME_TITLE + " TEXT," +
            COLUMN_NAME_CONTENT + " TEXT," +
            COLUMN_NAME_TAGS + " TEXT," +
            COLUMN_NAME_CATEGORIES + " TEXT," +
            COLUMN_NAME_DATE + " TEXT," +
            COLUMN_NAME_AUTHOR + " TEXT" +
            ")";
    public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static ContentValues getContentValues(Post post) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_HREF, post.href);
        values.put(COLUMN_NAME_TITLE, post.title);
        values.put(COLUMN_NAME_CONTENT, post.content);
        values.put(COLUMN_NAME_TAGS, Util.serializeStringList(post.tags));
        values.put(COLUMN_NAME_CATEGORIES, Util.serializeStringList(post.categories));
        values.put(COLUMN_NAME_DATE, post.date);
        values.put(COLUMN_NAME_AUTHOR, post.author);
        return values;
    }

    public static boolean checkPost(String href) {
        SQLiteDatabase db = Application.getDBHelper().getReadableDatabase();
        Cursor c = db.query(TABLE_NAME, null, COLUMN_NAME_HREF + "=?", new String[]{href}, null, null, null);
        return c.moveToNext();
    }

    public static void savePost(Post post) {
        SQLiteDatabase db = Application.getDBHelper().getWritableDatabase();
        ContentValues values = getContentValues(post);
        if (checkPost(post.href)) {
            db.update(TABLE_NAME, values, COLUMN_NAME_HREF + "=?", new String[]{post.href});
        } else {
            db.insert(TABLE_NAME, null, values);
        }
    }

    private static Post getPost(Cursor c) {
        String href = c.getString(c.getColumnIndex(COLUMN_NAME_HREF));
        String title = c.getString(c.getColumnIndex(COLUMN_NAME_TITLE));
        String content = c.getString(c.getColumnIndex(COLUMN_NAME_CONTENT));
        List<String> tags = Util.deserializeStringList(c.getString(c.getColumnIndex(COLUMN_NAME_TAGS)));
        List<String> categories = Util.deserializeStringList(c.getString(c.getColumnIndex(COLUMN_NAME_CATEGORIES)));
        String date = c.getString(c.getColumnIndex(COLUMN_NAME_DATE));
        String author = c.getString(c.getColumnIndex(COLUMN_NAME_AUTHOR));
        return new Post(href, title, content, tags, categories, date, author);
    }

    public static Post getPost(String href) {
        SQLiteDatabase db = Application.getDBHelper().getReadableDatabase();
        Cursor c = db.query(TABLE_NAME, null, COLUMN_NAME_HREF + "=?", new String[]{href}, null, null, null);
        if (c.moveToNext()) {
            return getPost(c);
        }
        return null;
    }
}
