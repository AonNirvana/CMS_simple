package com.egco428.cms_simple;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by HP on 1/12/2558.
 */
public class ArticleHelper {
    private MySQLiteHelper helper;

    public ArticleHelper(Context context) {helper = new MySQLiteHelper(context);}

    /**
     * Select all articles.
     * This method open database just before read and close database just after read to prevent the
     * need of onPause() and onResume() override.
     * @param articles The article Vector in ViewActivity.
     */
    public void cLArticle(Vector<Map<String,String>> articles) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(MySQLiteHelper.TB_NAME,new String[] {ViewFragment.ARG_KEY,ViewFragment.ARG_COL},null,null,null,null,null);
        try {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                HashMap<String, String> article = new HashMap<String, String>();
                article.put(ViewFragment.ARG_KEY, cursor.getString(0));
                article.put(ViewFragment.ARG_COL, cursor.getString(1));
                articles.add(article);
            }
        }
        catch(CursorIndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
        finally {
            cursor.close();
            db.close();
        }
    }

    /**
     * Insert new article content.
     * This method open database just before write and close database just after write to prevent the
     * need of onPause() and onResume() override.
     * @param key The name of such article.
     * @param col The content of such article.
     */
    public void i1Article(String key,String col) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ViewFragment.ARG_KEY,key);
        values.put(ViewFragment.ARG_COL,col);
        db.insert(MySQLiteHelper.TB_NAME,null,values);
        db.close();
    }

    /**
     * Update existed article content.
     * This method open database just before write and close database just after write to prevent the
     * need of onPause() and onResume() override.
     * @param key The name of such article.
     * @param col The content of such article.
     */
    public void u1Article(String key,String col) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ViewFragment.ARG_COL,col);
        db.update(MySQLiteHelper.TB_NAME, values, ViewFragment.ARG_KEY + "=?", new String[]{key});
        db.close();
    }

    /**
     * Delete existed article content.
     * This method open database just before write and close database just after write to prevent the
     * need of onPause() and onResume() override.
     * @param key The name of such article.
     */
    public void d1Article(String key) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(MySQLiteHelper.TB_NAME, ViewFragment.ARG_KEY + "=?", new String[] {key});
        db.close();
    }
}
