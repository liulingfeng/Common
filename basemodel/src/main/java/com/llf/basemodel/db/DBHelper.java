package com.llf.basemodel.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by llf on 2017/3/1.
 * 数据库创建工具类
 * 修改表需要更新数据库版本
 */

public class DBHelper extends SQLiteOpenHelper{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "common.db";

    private String CREATE_TABLE = "create table news(id integer primary key autoincrement,newsId integer,title text)";
    private String CREATE_TEMP_TABLE = "alter table news rename to temp_news";
    private String CREATE_NEW_TABLE = "create table news(id integer primary key autoincrement,newsId integer,title text,category varchar(50))";
    private String LEAD_IN = "insert into news select *,' ' from temp_news";
    private String DROP_TEMP = "drop table temp_news";
    private String ADD_ROW = "alter table broadcast add column category varchar(50)";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (newVersion){
            case 2:
                /**
                 * 增加字段
                 * 1.将表名改为临时表
                 * 2.创建新表
                 * 3.导入数据
                 * 4.删除临时表
                 */
                db.execSQL(CREATE_TEMP_TABLE);
                db.execSQL(CREATE_NEW_TABLE);
                db.execSQL(LEAD_IN);
                db.execSQL(DROP_TEMP);
                break;
            case 3:
                break;
            case 4:
                break;
        }
    }
}
