package com.example.chh85.exercise_elderly;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

import static java.lang.Boolean.TRUE;

public class UserDB  extends SQLiteOpenHelper {
    SQLiteDatabase sqldb;

    public UserDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user_info (name varchar(20) PRIMARY KEY, age varchar(3), gender varchar(3),height varchar(3), weight varchar(3), disease varchar(15));");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user_info");
        onCreate(db);
    }

    public ArrayList<String> readDB(UserDB userdb){
        ArrayList<String> info_list = new  ArrayList<String>();

        sqldb = userdb.getReadableDatabase();
        Cursor cursor;
        cursor = sqldb.rawQuery("SELECT * FROM user_info",null);

        while (cursor.moveToNext()){
            for(int i=0;i<6;i++){
                info_list.add(cursor.getString(i));
            }

        }
        sqldb.close();
        return info_list;
    }



    public Boolean exist_db (UserDB userdb){
        sqldb = userdb.getReadableDatabase();
        Cursor cursor;
        cursor = sqldb.rawQuery("SELECT * FROM user_info",null);
        cursor.moveToFirst();
        if(cursor.getCount()>0){
            sqldb.close();
            return true;
        }else{
            sqldb.close();
            return false;
        }
    }


}