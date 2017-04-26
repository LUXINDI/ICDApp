package cn.edu.tsinghua.sicd.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LandDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Land.db"; //数据库名称
    private static final int version = 1; //数据库版本

    public LandDatabaseHelper(Context context) {
        super(context, DB_NAME, null, version);
        // TODO Auto-generated constructor stub
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table land(keyword1 varchar(100) not null, keyword2 varchar(255),code varchar(255),data text" +

                " );";
        db.execSQL(sql);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
 
    }
 
}
