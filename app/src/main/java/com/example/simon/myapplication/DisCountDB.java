package com.example.simon.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Simon on 2018/1/2.
 */

public class DisCountDB extends SQLiteOpenHelper {
    private static final String database = "discount.db";
    private static final int version=1;

    public DisCountDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }

    public DisCountDB(Context context){
        this(context,database,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE DiscountTable(_id integer primary key autoincrement,"
                +"username text not null,"
                +"coin text not null,"
                +"ninety text not null,"
                +"eighty text not null,"
                +"fifty text not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int version) {
        db.execSQL("DROP TABLE IF EXISTS DiscountTable");
        onCreate(db);
    }
}
