package com.llf.common.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.llf.basemodel.db.DBHelper;
import com.llf.common.entity.NewsEntity;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by llf on 2017/3/22.
 * 新闻操作工具类
 */

public class NewsDao {
    private DBHelper mSqliteOpenHelp;

    public NewsDao(Context context) {
        mSqliteOpenHelp = new DBHelper(context);
    }

    /**
     * 增加一条记录
     */
    public long insertRecord(NewsEntity newsEntity) {
        SQLiteDatabase sqLiteDatabase = mSqliteOpenHelp.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("newsId", newsEntity.getDocid());
        values.put("title", newsEntity.getTitle());
        //允许title为null
        long rowId = sqLiteDatabase.insert("news", "title", values);
        sqLiteDatabase.close();
        return rowId;
    }

    /**
     * 删除一条记录
     * 返回值是受影响的行数，0表示失败
     */
    public int deleteRecord(String id) {
        SQLiteDatabase sqLiteDatabase = mSqliteOpenHelp.getWritableDatabase();
        int deleteResult = sqLiteDatabase.delete("news", "newsId=?", new String[]{String.valueOf(id)});
        sqLiteDatabase.close();
        return deleteResult;
    }
    /**
     * 查询
     */
    public List<NewsEntity> queryRecords() {
        List<NewsEntity> datas = new ArrayList<>();
        SQLiteDatabase readableDatabase = mSqliteOpenHelp.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select * from news order by id DESC", null);
        while (cursor.moveToNext()) {
            NewsEntity entity = new NewsEntity();
            entity.setDocid(cursor.getString(cursor.getColumnIndex("newsId")));
            entity.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            datas.add(entity);
        }
        cursor.close();
        readableDatabase.close();
        return datas;
    }
    /**
     * 修改记录
     */
    public int updateRecord(NewsEntity newsEntity, String id) {
        SQLiteDatabase sqLiteDatabase = mSqliteOpenHelp.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("newsId", newsEntity.getDocid());
        values.put("title", newsEntity.getTitle());
        int updateResult = sqLiteDatabase.update("news", values, "newsId=?", new String[]{String.valueOf(id)});
        sqLiteDatabase.close();
        return updateResult;
    }
}
