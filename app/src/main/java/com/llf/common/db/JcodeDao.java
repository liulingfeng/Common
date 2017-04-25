package com.llf.common.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.llf.common.entity.JcodeEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by llf on 2017/3/22.
 * 发现操作工具类
 */

public class JcodeDao {
    private DBHelper mSqliteOpenHelp;

    public JcodeDao(Context context) {
        mSqliteOpenHelp = new DBHelper(context);
    }

    /**
     * 增加一条记录
     */
    public long insertRecord(JcodeEntity jcodeEntity) {
        if (queryById(jcodeEntity.getTitle()) != null) {
            return -1;
        }
        SQLiteDatabase sqLiteDatabase = mSqliteOpenHelp.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("imgUrl", jcodeEntity.getImgUrl());
        values.put("title", jcodeEntity.getTitle());
        values.put("detailUrl", jcodeEntity.getDetailUrl());
        values.put("content", jcodeEntity.getContent());
        values.put("author", jcodeEntity.getAuthor());
        values.put("authorImg", jcodeEntity.getAuthorImg());
        values.put("watch", jcodeEntity.getWatch());
        values.put("comments", jcodeEntity.getComments());
        values.put("like", jcodeEntity.getLike());
        long rowId = sqLiteDatabase.insert("jcode", "imgUrl", values);
        sqLiteDatabase.close();
        return rowId;
    }

    /**
     * 删除一条记录
     * 返回值是受影响的行数，-1表示失败
     */
    public int deleteRecord(String title) {
        SQLiteDatabase sqLiteDatabase = mSqliteOpenHelp.getWritableDatabase();
        int deleteResult = sqLiteDatabase.delete("jcode", "title=?", new String[]{title});
        sqLiteDatabase.close();
        return deleteResult;
    }

    /**
     * 查询
     */
    public List<JcodeEntity> queryRecords() {
        List<JcodeEntity> datas = new ArrayList<>();
        SQLiteDatabase readableDatabase = mSqliteOpenHelp.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select * from jcode", null);
        while (cursor.moveToNext()) {
            JcodeEntity entity = new JcodeEntity();
            entity.setImgUrl(cursor.getString(cursor.getColumnIndex("imgUrl")));
            entity.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            entity.setDetailUrl(cursor.getString(cursor.getColumnIndex("detailUrl")));
            entity.setContent(cursor.getString(cursor.getColumnIndex("content")));
            entity.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
            entity.setAuthorImg(cursor.getString(cursor.getColumnIndex("authorImg")));
            entity.setWatch(cursor.getString(cursor.getColumnIndex("watch")));
            entity.setComments(cursor.getString(cursor.getColumnIndex("comments")));
            entity.setLike(cursor.getString(cursor.getColumnIndex("like")));
            datas.add(entity);
        }
        cursor.close();
        readableDatabase.close();
        return datas;
    }

    public JcodeEntity queryById(String title) {
        JcodeEntity entity = null;
        SQLiteDatabase readableDatabase = mSqliteOpenHelp.getReadableDatabase();
        Cursor cursor = readableDatabase.query("jcode", new String[]{"title", "detailUrl", "content"}, "title=?", new String[]{title}, null, null, null);
        if (cursor.moveToNext()) {
            entity = new JcodeEntity();
            entity.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            entity.setDetailUrl(cursor.getString(cursor.getColumnIndex("detailUrl")));
            entity.setContent(cursor.getString(cursor.getColumnIndex("content")));
        }
        return entity;
    }

    /**
     * 修改记录
     */
    public int updateRecord(JcodeEntity jcodeEntity, String id) {
        SQLiteDatabase sqLiteDatabase = mSqliteOpenHelp.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("imgUrl", jcodeEntity.getImgUrl());
        values.put("title", jcodeEntity.getTitle());
        values.put("detailUrl", jcodeEntity.getDetailUrl());
        values.put("content", jcodeEntity.getContent());
        values.put("author", jcodeEntity.getAuthor());
        values.put("authorImg", jcodeEntity.getAuthorImg());
        values.put("watch", jcodeEntity.getWatch());
        values.put("comments", jcodeEntity.getComments());
        values.put("like", jcodeEntity.getLike());
        int updateResult = sqLiteDatabase.update("jcode", values, "id=?", new String[]{String.valueOf(id)});
        sqLiteDatabase.close();
        return updateResult;
    }
}
