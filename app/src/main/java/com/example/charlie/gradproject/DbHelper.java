package com.example.charlie.gradproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import java.util.Arrays;

public class DbHelper extends SQLiteOpenHelper {

    final static String TABLE_NAME1 ="class1";
    final static String TABLE_NAME2 ="class2";
    final static String name="gradProj.db";
    final static String TAG="DbHelper";
//    private static final int VERSION = 3;
    String[] newColumnArr;
    String oldTableName;


    public DbHelper(Context context,int version,String[] newColumnArr,String oldTableName) {
        super(context, name, null, version);
        this.newColumnArr=newColumnArr;
        this.oldTableName=oldTableName;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //数据库创建
        String sql = "create table if not exists " + TABLE_NAME1 + " (Id integer primary key, Name text, Date text)";
        sqLiteDatabase.execSQL(sql);
        sql="create table if not exists " + TABLE_NAME2 + " (Id integer primary key, Name text, Date text)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.e("DbHelper","onUpgrade");
        addColumn(sqLiteDatabase,newColumnArr,oldTableName);
        /*sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME1);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME2);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ "_temp_class1");*/
        onCreate(sqLiteDatabase);
    }

    /*public void sendName(String[] newColumnArr,String oldTableName){
        this.newColumnArr=newColumnArr;
        this.oldTableName=oldTableName;
    }*/

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    /**
     * 在表中添加字段方法 操作步骤： 1、先更改表名 2、创建新表,表名为原来的表名 3、复制数据 4、删除旧表
     *
     * @param db 数据库名
     * @param newColumnArr 添加的新字段的表名数组
     * @param oldTableName 旧表名，在方法内部将旧表名修改为 _temp_+oldTableName
     */
    private void addColumn(SQLiteDatabase db, String[] newColumnArr,
                           String oldTableName) {
        Log.w("DbHelper","addrow()");

        if (db == null || newColumnArr == null || newColumnArr.length < 1
                || TextUtils.isEmpty(oldTableName)) {
            Log.e(TAG,"db is null"+(db == null));
            Log.e(TAG,"newColumnArr is null"+(newColumnArr == null));
           // Log.e(TAG,"db is null"+(db == null));
            Log.e(TAG,"table is null"+(TextUtils.isEmpty(oldTableName)));
            // 数据库为空，新字段个数为0，添加字段后的字段数组个数为0，旧表名为空
            return;
        }

        // 拿到旧表中所有的数据
        Cursor cursor = db.rawQuery("select * from " + oldTableName, null);
        if (cursor == null) {
            // 如果游标为空，说明旧表中没有数据，如果是这种情况那就可以直接改表的字段，不需要转移数据了，代码后面也有，省略掉复制数据的操作就好
            return;
        }
        // 拿到原来的表中所有的字段名
        String[] oldColumnNames = cursor.getColumnNames();

        // 更改原表名为临时表
        String tempTableName = "_temp_" + oldTableName;
        db.execSQL(
                "alter table " + oldTableName + " rename to " + tempTableName);

        // 创建新表
        if (oldColumnNames.length < 1) {
            // 如果原来的表中字段个数为0
            return;
        }

        // 创建一个线程安全的字符串缓冲对象，防止用conn多线程访问数据库时造成线程安全问题
        StringBuffer createNewTableStr = new StringBuffer();
        createNewTableStr
                .append("create table if not exists " + oldTableName + "(");
        for (int i = 0; i < oldColumnNames.length; i++) {
            if (i == 0) {
                createNewTableStr.append(oldColumnNames[i]
                        + " integer primary key autoincrement,");
            } else {
                createNewTableStr.append(oldColumnNames[i] + ",");
            }
        }

        for (int i = 0; i < newColumnArr.length; i++) {
            if (i == newColumnArr.length - 1) {
                // 最后一个
                createNewTableStr.append(newColumnArr[i] + ")");
            } else {
                // 不是最后一个
                createNewTableStr.append(newColumnArr[i] + ",");
            }
        }

        db.execSQL(createNewTableStr.toString());
        Log.e("sql 语句：",createNewTableStr.toString());
        Cursor cursor01 = db.rawQuery("select name from sqlite_master where type='table' order by name", null);
        while (cursor01.moveToNext()) {
            //遍历出表名
            String name = cursor01.getString(0);
            Log.e("旧表未删除", name);
        }

        Cursor c = db.rawQuery("SELECT * FROM " + "class2" + " WHERE 0", null);
        try {
            String[] columnNames = c.getColumnNames();
            //           Log.w(TAG,columnNames[3]);
            for (int i = 0; i < columnNames.length; i++) {
                Log.w("新表字段", columnNames[i]);
            }
        } finally {
            c.close();
        }

        // 复制旧表数据到新表
        StringBuffer copySQLStr = new StringBuffer();
        copySQLStr.append("insert into " + oldTableName + " select *,");
        // 有多少个新的字段，就要预留多少个' '空值给新字段
        for (int i = 0; i < newColumnArr.length; i++) {
            if (i == newColumnArr.length - 1) {
                // 最后一个
                copySQLStr.append("' ' from " + tempTableName);
            } else {
                // 不是最后一个
                copySQLStr.append("' ',");
            }
        }

        db.execSQL(copySQLStr.toString());

        // 删除旧表
        db.execSQL("drop table " + tempTableName);

        // 关闭游标
        cursor.close();
    }
}


