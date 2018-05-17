package com.example.charlie.gradproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    final static String TABLE_NAME="class1";
    final static String name="gradProj.db";
    private static final int VERSION = 1;

    public DbHelper(Context context) {
        super(context, name, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //数据库创建
        String sql = "create table if not exists " + TABLE_NAME + " (Id integer primary key, Name text, Date text)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}


