package com.example.simon.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Simon on 2018/1/1.
 */

public class foodDB extends SQLiteOpenHelper {
    private static final String database = "food.db";
    private static final int version=1;

    public foodDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }

    public foodDB(Context context){
        this(context,database,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE foodTable(_id integer primary key autoincrement,"
                +"food_name text not null,"
                +"food_amount text not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int version) {
        db.execSQL("DROP TABLE IF EXISTS foodTable");
        onCreate(db);
    }
}
