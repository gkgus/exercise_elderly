package com.example.chh85.exercise_elderly;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class RecordDB extends SQLiteOpenHelper {
    SQLiteDatabase sqldb;

    public RecordDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE record_exercise (startdate date, ex_type String,ex_time String);");
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
    public void insertDB(RecordDB recordDB, String date, String ex_type, String ex_time){
        sqldb = recordDB.getWritableDatabase();
        //recordDB.onUpgrade(sqldb,1,2);
        sqldb.execSQL("INSERT INTO record_exercise VALUES ( '" +date +"','"
                +ex_type+"','"
                +ex_time+"');");
        sqldb.close();
    }

    public Boolean existDB(RecordDB recordDB) {
        sqldb = recordDB.getReadableDatabase();
        Cursor cursor;
        cursor = sqldb.rawQuery("SELECT * FROM record_exercise", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            sqldb.close();
            return true;
        } else {
            sqldb.close();
            return false;
        }
    }


}