package com.example.haya.im.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.haya.im.bean.Historical;
import com.example.haya.im.client.Message;

import java.util.ArrayList;

public class DBUtils {

    public static void deleteHistory(Historical item) {
        MySQLite instance = MySQLite.getInstance();
        SQLiteDatabase db = instance.getWritableDatabase();
        String sql = String.format("delete from history where _id= '%s' and type ='%s'", item.getName(), item.getType());
        Log.i("sql", sql);
        try {
            db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public static ArrayList<Historical> getHistory() {
        ArrayList<Historical> list = new ArrayList<>();
        MySQLite instance = MySQLite.getInstance();
        SQLiteDatabase db = instance.getWritableDatabase();
        String sql = "select * from history ";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String type = cursor.getString(1);
            String flag = cursor.getString(2);
            int count = cursor.getInt(3);
            list.add(new Historical(id, type, flag, count));
        }
        cursor.close();
        db.close();
        return list;
    }

    public static ArrayList<Message> getMessage(String toName) {
        ArrayList<Message> list = new ArrayList<>();
        MySQLite instance = MySQLite.getInstance();
        SQLiteDatabase db = instance.getWritableDatabase();
        String sql = String.format("select * from message where contact = '%s'", toName);
        Log.i("sql", sql);
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String contact = cursor.getString(1);
            String type = cursor.getString(2);
            String body = cursor.getString(3);
            list.add(new Message(contact, type, body));
        }
        cursor.close();
        db.close();
        return list;
    }

    public static void insertHistory(Historical item) {
        MySQLite instance = MySQLite.getInstance();
        SQLiteDatabase db = instance.getWritableDatabase();
        String sql = String.format("insert into history values('%s','%s','%s',%s)",
                item.getName(),
                item.getType(),
                item.isFlag(),
                item.getCount());
        Log.i("sql", sql);
        try {
            db.execSQL(sql);
        } catch (SQLiteConstraintException e) {
            try {
                throw new Exception("重复");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } finally {
            db.close();
        }
    }

    public static void insertMessage(Message message) {
        MySQLite instance = MySQLite.getInstance();
        SQLiteDatabase db = instance.getWritableDatabase();
        String sql = String.format("insert into message (contact,type,body)values('%s','%s','%s')",
                message.getTo(),
                message.getType(),
                message.getText());
        Log.i("sql", sql);
        try {
            db.execSQL(sql);
        } catch (SQLiteConstraintException e) {
            try {
                throw new Exception("重复");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } finally {
            db.close();
        }
    }

    public static void updateMessageFlag(String contact, boolean flag) {
        MySQLite instance = MySQLite.getInstance();
        SQLiteDatabase db = instance.getWritableDatabase();
        String sql = String.format("update history set flag = '%s' where _id = '%s'", flag, contact);
        Log.i("sql", sql);
        try {
            db.execSQL(sql);
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }
}
