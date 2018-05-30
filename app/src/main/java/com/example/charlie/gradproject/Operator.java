package com.example.charlie.gradproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Operator {
    final static String TAG = "Operator";
    private Context context;
    private DbHelper dbHelper;
    private final String[] ORDER_COLUMNS = new String[]{"Id", "Name", "Date"};

    private String d;
    private String[] searchColumns;
    private int version;

    public Operator(Context context,int version) {
        this.version=version;
        this.context = context;
        dbHelper = new DbHelper(context,version,null,null);
    }

    /**
     * 判断表中是否有数据
     */
    public boolean isDataExist() {
        int count = 0;

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();
            // select count(Id) from Orders
            cursor = db.query(DbHelper.TABLE_NAME2, new String[]{"COUNT(Id)"}, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            if (count > 0) return true;
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
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
    public void initTable() {
        SQLiteDatabase db = null;

        try {
            db = dbHelper.getWritableDatabase();
            db.beginTransaction();

            db.execSQL("insert into " + DbHelper.TABLE_NAME1 + " (Id, Name, Date) values (1, 'a','到课')");
            db.execSQL("insert into " + DbHelper.TABLE_NAME1 + " (Id, Name, Date) values (2, 'b','到课')");
            db.execSQL("insert into " + DbHelper.TABLE_NAME1 + " (Id, Name, Date) values (3, 'c','到课')");
            db.execSQL("insert into " + DbHelper.TABLE_NAME1 + " (Id, Name, Date) values (4, 'd','缺席')");
            db.execSQL("insert into " + DbHelper.TABLE_NAME1 + " (Id, Name, Date) values (5, 'e','到课')");
            db.execSQL("insert into " + DbHelper.TABLE_NAME1 + " (Id, Name, Date) values (6, 'f','请假')");

            db.execSQL("insert into " + DbHelper.TABLE_NAME2 + " (Id, Name, Date) values (7, 'g','到课')");
            db.execSQL("insert into " + DbHelper.TABLE_NAME2 + " (Id, Name, Date) values (8, 'h','到课')");
            db.execSQL("insert into " + DbHelper.TABLE_NAME2 + " (Id, Name, Date) values (9, 'i','到课')");
            db.execSQL("insert into " + DbHelper.TABLE_NAME2 + " (Id, Name, Date) values (10, 'j','缺席')");
            db.execSQL("insert into " + DbHelper.TABLE_NAME2 + " (Id, Name, Date) values (11, 'k','到课')");
            db.execSQL("insert into " + DbHelper.TABLE_NAME2 + " (Id, Name, Date) values (12, 'l','请假')");

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public String[] getAllColumn(String tableName) {
        Cursor c = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + tableName + " WHERE 0", null);
        try {
            String[] columnNames = c.getColumnNames();
            //           Log.w(TAG,columnNames[3]);
            for (int i = 0; i < columnNames.length; i++) {
                Log.w(TAG, columnNames[i]);
            }
            return columnNames;
        } finally {
            c.close();
        }
    }

    /**
     * 查询数据库中所有数据
     */
    public List<Order> getAllDate(String name,String column) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String[] columnArr=new String[]{"Id", "Name",column};

        try {
            db = dbHelper.getReadableDatabase();
            // select * from Orders
            cursor = db.query(name, columnArr, null, null, null, null, null);

            if (cursor.getCount() > 0) {
                Log.w(TAG, "data cursored");
                List<Order> orderList = new ArrayList<Order>(cursor.getCount());
                while (cursor.moveToNext()) {
                    orderList.add(parseOrder(cursor,column));
                }
                Log.e(TAG,orderList.size()+"");
                return orderList;
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
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
    private Order parseOrder(Cursor cursor,String column) {
        Order order = new Order();
        order.id = (cursor.getInt(cursor.getColumnIndex("Id")));
        order.name = (cursor.getString(cursor.getColumnIndex("Name")));
        order.date = (cursor.getString(cursor.getColumnIndex(column)));
        return order;
    }

    public List<String> getName() {//获得数据表的名称，以list返回
        List<String> names = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select name from sqlite_master where type='table' order by name", null);
        while (cursor.moveToNext()) {
            //遍历出表名
            String name = cursor.getString(0);
            Log.w("System.out", name);
            names.add(name);
        }
        names.remove(0);    //list【0】要筛选掉 meta
        return names;
    }

    public void changeStatus(String tableName,String column ,String date, int Id) {  //设定点名情况
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("update " + tableName + " set "+column+"=? where Id=?",
                new Object[]{date, Id});
    }

    //搜索指定列
   /* public List<Order> searchColumn(String tableName, String d) {
        this.d = d;
        searchColumns = new String[]{"Id", "Name", "date2018_05_20"};
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();
            // select * from Orders
            cursor = db.query(tableName, searchColumns, null, null, null, null, null);

            if (cursor.getCount() > 0) {
                //Log.w(TAG, "added something");
                List<Order> orderList = new ArrayList<Order>(cursor.getCount());
                while (cursor.moveToNext()) {
                    orderList.add(parseOrder(cursor));
                }
                return orderList;
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return null;
    }*/

    public boolean isRowExists(String tableName, String rowDate) {
        /*boolean result = false ;
        Cursor cursor = null ;
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        try{
            cursor = db.rawQuery( "select * from sqlite_master where name = ? and sql like ?"
                    , new String[]{tableName , "%" + rowDate + "%"} );
            result = null != cursor && cursor.moveToFirst() ;
        }catch (Exception e){
            Log.e(TAG,"checkColumnExists..." + e.getMessage()) ;
        }finally{
            if(null != cursor && !cursor.isClosed()){
                cursor.close() ;
            }
        }
        return result;*/
        boolean result = false;
        Cursor cursor = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            //查询一行
            cursor = db.rawQuery("SELECT * FROM " + tableName + " LIMIT 0"
                    , null);
            result = cursor != null && cursor.getColumnIndex(rowDate) != -1;
        } catch (Exception e) {
            Log.e(TAG, "checkColumnExists1..." + e.getMessage());
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }

    //添加新列,
    public void addNewColumn(String tableName, String[] newColumnArr) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        dbHelper.sendName(newColumnArr, tableName);
        dbHelper=new DbHelper(context,version+1,newColumnArr, tableName);
    }

}
