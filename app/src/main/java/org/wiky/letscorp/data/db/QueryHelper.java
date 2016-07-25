package org.wiky.letscorp.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import org.wiky.letscorp.Application;
import org.wiky.letscorp.data.model.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wiky on 7/24/16.
 */
public class QueryHelper implements BaseColumns {

    public static final String TABLE_NAME = "query";
    public static final String COLUMN_NAME_QUERY = "query";
    public static final String COLUMN_NAME_TIMESTAMP = "timestamp";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_NAME_QUERY + " TEXT PRIMARY KEY," +
            COLUMN_NAME_TIMESTAMP + " INT" +
            ")";
    public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static ContentValues getContentValues(Query query) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_QUERY, query.query);
        values.put(COLUMN_NAME_TIMESTAMP, query.timestamp);
        return values;
    }

    public static Query getQuery(Cursor c) {
        String query = c.getString(c.getColumnIndex(COLUMN_NAME_QUERY));
        long timestamp = c.getLong(c.getColumnIndex(COLUMN_NAME_TIMESTAMP));
        return new Query(query, timestamp);
    }

    public static List<Query> getQueries(String query, int count) {
        SQLiteDatabase db= Application.getDBHelper().getReadableDatabase();
        Cursor c = db.query(TABLE_NAME, null, String.format("%s like ?", COLUMN_NAME_QUERY), new String[]{query + "%"}, null, null, String.format("%s DESC", COLUMN_NAME_TIMESTAMP), String.valueOf(count));

        List<Query> queries=new ArrayList<>();
        while(c.moveToNext()){
            queries.add(getQuery(c));
        }
        c.close();
        return queries;
    }

    public static boolean checkQuery(String query) {
        SQLiteDatabase db = Application.getDBHelper().getReadableDatabase();
        Cursor c = db.query(TABLE_NAME, null, String.format("%s=?", COLUMN_NAME_QUERY),
                new String[]{query}, null, null, null);
        boolean exists = c.moveToNext();
        c.close();
        return exists;
    }

    public static void saveQuery(Query query){
        SQLiteDatabase db = Application.getDBHelper().getWritableDatabase();
        ContentValues values=getContentValues(query);
        if(checkQuery(query.query)){
            db.update(TABLE_NAME, values, String.format("%s=?", COLUMN_NAME_QUERY), new String[]{query.query});
        }else{
            db.insert(TABLE_NAME, null, values);
        }
    }

    public static void clearQueries(int count){
        SQLiteDatabase db = Application.getDBHelper().getWritableDatabase();
        String sql=String.format("DELETE FROM %s WHERE %s NOT IN (SELECT %s FROM %s ORDER BY %s DESC LIMIT %s)",
                TABLE_NAME, COLUMN_NAME_QUERY, COLUMN_NAME_QUERY, TABLE_NAME, COLUMN_NAME_TIMESTAMP, count);
        db.execSQL(sql);
    }

    public static void deleteQueries() {
        SQLiteDatabase db = Application.getDBHelper().getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    public static void deleteQuery(String query) {
        SQLiteDatabase db = Application.getDBHelper().getWritableDatabase();
        db.delete(TABLE_NAME, String.format("%s = ?", COLUMN_NAME_QUERY), new String[]{query});
    }
}
