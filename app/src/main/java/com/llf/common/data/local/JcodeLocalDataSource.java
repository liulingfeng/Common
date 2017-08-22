package com.llf.common.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.llf.basemodel.utils.LogUtil;
import com.llf.common.data.JcodeDataSource;
import com.llf.common.entity.JcodeEntity;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by llf on 2017/3/22.
 * 发现数据库操作工具类
 */

public class JcodeLocalDataSource implements JcodeDataSource {
    private DbHelper mSqliteOpenHelp;
    private static JcodeLocalDataSource INSTANCE;

    private JcodeLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        mSqliteOpenHelp = new DbHelper(context);
    }

    public static JcodeLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new JcodeLocalDataSource(context);
        }
        return INSTANCE;
    }

    /**
     * 增加事务，保证数据的安全性，两步操作有一步不对可以回滚
     *
     * @param entity
     * @return
     */
    @Override
    public long saveJcode(@NonNull JcodeEntity entity) {
        long rowId = 0;
        checkNotNull(entity);
        SQLiteDatabase sqLiteDatabase = mSqliteOpenHelp.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("imgUrl", entity.getImgUrl());
            values.put("title", entity.getTitle());
            values.put("detailUrl", entity.getDetailUrl());
            values.put("content", entity.getContent());
            values.put("author", entity.getAuthor());
            values.put("authorImg", entity.getAuthorImg());
            values.put("watch", entity.getWatch());
            values.put("comments", entity.getComments());
            values.put("like", entity.getLike());
            rowId = sqLiteDatabase.insert("jcode", "imgUrl", values);
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            LogUtil.d("插入数据库出错");
        } finally {
            sqLiteDatabase.endTransaction();
            sqLiteDatabase.close();
        }
        return rowId;
    }

    /**
     * 删除一条记录
     * 返回值是受影响的行数，-1表示失败
     */

    @Override
    public int deleteJcode(@NonNull String title) {
        checkNotNull(title);
        SQLiteDatabase sqLiteDatabase = mSqliteOpenHelp.getWritableDatabase();
        int deleteResult = sqLiteDatabase.delete("jcode", "title=?", new String[]{title});
        sqLiteDatabase.close();
        return deleteResult;
    }

    @Override
    public void deleteAllJcodes() {
        SQLiteDatabase db = mSqliteOpenHelp.getWritableDatabase();
        db.delete("jcode", null, null);
        db.close();
    }


    @Override
    public void getJcodes(@NonNull LoadJcodesCallback callback) {
        List<JcodeEntity> datas = new ArrayList<>();
        SQLiteDatabase readableDatabase = mSqliteOpenHelp.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select * from jcode", null);
        if (cursor != null && cursor.getCount() > 0) {
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
        }
        if (cursor != null) {
            cursor.close();
        }
        readableDatabase.close();
        if (datas.isEmpty()) {
            callback.onDataNotAvailable();
        } else {
            callback.onTasksLoaded(datas);
        }
    }

    @Override
    public void getJcode(@NonNull String title, @NonNull GetJcodeCallback callback) {
        JcodeEntity entity = null;
        SQLiteDatabase readableDatabase = mSqliteOpenHelp.getReadableDatabase();
        Cursor cursor = readableDatabase.query("jcode", new String[]{"title", "detailUrl", "content"}, "title=?", new String[]{title}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToNext()) {
                entity = new JcodeEntity();
                entity.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                entity.setDetailUrl(cursor.getString(cursor.getColumnIndex("detailUrl")));
                entity.setContent(cursor.getString(cursor.getColumnIndex("content")));
            }
        }

        if (cursor != null) {
            cursor.close();
        }
        readableDatabase.close();
        if (entity != null) {
            callback.onTaskLoaded(entity);
        } else {
            callback.onDataNotAvailable();
        }
    }


    @Override
    public void refreshJcodes(@NonNull JcodeEntity entity) {
        checkNotNull(entity);
        SQLiteDatabase sqLiteDatabase = mSqliteOpenHelp.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("imgUrl", entity.getImgUrl());
        values.put("title", entity.getTitle());
        values.put("detailUrl", entity.getDetailUrl());
        values.put("content", entity.getContent());
        values.put("author", entity.getAuthor());
        values.put("authorImg", entity.getAuthorImg());
        values.put("watch", entity.getWatch());
        values.put("comments", entity.getComments());
        values.put("like", entity.getLike());
        sqLiteDatabase.update("jcode", values, "id=?", new String[]{String.valueOf(id)});
        sqLiteDatabase.close();
    }
}
