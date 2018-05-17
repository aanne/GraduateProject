package com.example.charlie.gradproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Operator {
    final static String TAG="Operator";
    private Context context;
    private DbHelper dbHelper;
    private final String[] ORDER_COLUMNS = new String[] {"Id", "Name","Date"};

    public Operator(Context context) {
        this.context = context;
        dbHelper = new DbHelper(context);
    }

    /**
     * 判断表中是否有数据
     */
    public boolean isDataExist(){
        int count = 0;

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();
            // select count(Id) from Orders
            cursor = db.query(DbHelper.TABLE_NAME, new String[]{"COUNT(Id)"}, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            if (count > 0) return true;
        }
        catch (Exception e) {
            Log.e(TAG, "", e);
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return false;
    }

    /**
     * 初始化数据
     */
    public void initTable(){
        SQLiteDatabase db = null;

        try {
            db = dbHelper.getWritableDatabase();
            db.beginTransaction();

            db.execSQL("insert into " + DbHelper.TABLE_NAME + " (Id, Name, Date) values (1, 'a','到')");
            db.execSQL("insert into " + DbHelper.TABLE_NAME + " (Id, Name, Date) values (2, 'b','到')");
            db.execSQL("insert into " + DbHelper.TABLE_NAME + " (Id, Name, Date) values (3, 'c','到')");
            db.execSQL("insert into " + DbHelper.TABLE_NAME + " (Id, Name, Date) values (4, 'd','缺席')");
            db.execSQL("insert into " + DbHelper.TABLE_NAME + " (Id, Name, Date) values (5, 'e','到')");
            db.execSQL("insert into " + DbHelper.TABLE_NAME + " (Id, Name, Date) values (6, 'f','请假')");

            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.e(TAG, "", e);
        }finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    /**
     * 查询数据库中所有数据
     */
    public List<Order> getAllDate(){
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();
            // select * from Orders
            cursor = db.query(DbHelper.TABLE_NAME, ORDER_COLUMNS, null, null, null, null, null);

            if (cursor.getCount() > 0) {
                List<Order> orderList = new ArrayList<Order>(cursor.getCount());
                while (cursor.moveToNext()) {
                    orderList.add(parseOrder(cursor));
                }
                return orderList;
            }
        }
        catch (Exception e) {
            Log.e(TAG, "", e);
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }


    /**
     * 将查找到的数据转换成Order类
     */
    private Order parseOrder(Cursor cursor){
        Order order = new Order();
        order.id = (cursor.getInt(cursor.getColumnIndex("Id")));
        order.name = (cursor.getString(cursor.getColumnIndex("Name")));
        order.date = (cursor.getString(cursor.getColumnIndex("Date")));
        return order;
    }

}
