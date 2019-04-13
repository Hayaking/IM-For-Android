package com.example.haya.im.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MySQLite extends SQLiteOpenHelper {

    public static MySQLite getInstance(Context context) {
        if (instance == null)
            instance = new MySQLite(context);
        return instance;
    }

    public static MySQLite getInstance() {
        if (null == instance) {
            try {
                throw new Throwable("必须先调用getInstance(Context context)方法");
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return instance;
    }
    private static MySQLite instance;

    private MySQLite(Context context) {
        super(context, "haya.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String history = "create table history("
                + " _id varchar(40), "
                + "type varchar(20),"
                + "flag varchar(10),"
                + "count integer,"
                + "primary key(_id,type))";

        String message = "CREATE TABLE message (" +
                "_id integer NOT NULL PRIMARY KEY AUTOINCREMENT,"
                + "contact TEXT,"
                + "type TEXT,"
                + "body TEXT)";
        db.execSQL(history);
        db.execSQL(message);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
