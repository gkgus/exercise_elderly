package com.example.chh85.exercise_elderly;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class RecordDB extends SQLiteOpenHelper {
    SQLiteDatabase sqldb;

    public RecordDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE record_exercise (ex_date String, ex_type String,ex_time String);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS record_exercise");
        onCreate(db);
    }

    public ArrayList<String> readDB(RecordDB recordDB) {
        ArrayList<String> info_list = new ArrayList<String>();

        sqldb = recordDB.getReadableDatabase();
        Cursor cursor;
        cursor = sqldb.rawQuery("SELECT * FROM record_exercise", null);

        while (cursor.moveToNext()) {
            for (int i = 0; i < 3; i++) {
                info_list.add(cursor.getString(i));
            }

        }
        sqldb.close();
        return info_list;
    }

    public void insertDB(RecordDB recordDB, String date, String ex_type, String ex_time) {
        sqldb = recordDB.getWritableDatabase();
        //recordDB.onUpgrade(sqldb,1,2);
        sqldb.execSQL("INSERT INTO record_exercise VALUES ( '" + date + "','"
                + ex_type + "','"
                + ex_time + "');");
        sqldb.close();
    }

    public void backupDB(RecordDB recordDB) {
        sqldb = recordDB.getReadableDatabase();
        Cursor cursor;
        cursor = sqldb.rawQuery("SELECT * FROM record_exercise", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            recordDB.onUpgrade(sqldb, 1, 2);
            sqldb.close();
            //return true;
        } else {
            //recordDB.onUpgrade(sqldb,1,2);
            sqldb.close();
            //return false;
        }
    }


    public Boolean exist_db (RecordDB recordDB){
        sqldb = recordDB.getReadableDatabase();
        Cursor cursor;
        cursor = sqldb.rawQuery("SELECT * FROM record_exercise",null);
        cursor.moveToFirst();
        if(cursor.getCount()>0){
            sqldb.close();
            return true;
        }else{
            sqldb.close();
            return false;
        }
    }



    public ArrayList<String> date_exercise(RecordDB recordDB, String date) {
        ArrayList<String> exercise_list = new ArrayList<String>();
        sqldb = recordDB.getReadableDatabase();
        Cursor cursor;
        cursor = sqldb.rawQuery("SELECT ex_type,ex_time FROM record_exercise" + " WHERE " + "ex_date= '" + date + "';", null);

        while (cursor.moveToNext()) {

                for (int i = 0; i < 2; i++) {
                    exercise_list.add(cursor.getString(i));
            }

        }
        sqldb.close();
        return exercise_list;
    }

    public HashSet<String> set_of_date(RecordDB recordDB){
        HashSet<String> date_set = new HashSet<>();
        sqldb = recordDB.getReadableDatabase();
        Cursor cursor;
        cursor = sqldb.rawQuery("SELECT ex_date FROM record_exercise;", null);

        while (cursor.moveToNext()) {
                date_set.add(cursor.getString(0));
        }
        sqldb.close();
        return date_set;
    }
}


