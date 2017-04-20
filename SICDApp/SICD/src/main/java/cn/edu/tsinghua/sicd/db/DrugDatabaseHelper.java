package cn.edu.tsinghua.sicd.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DrugDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Drug.db"; //数据库名称
    private static final int version = 1; //数据库版本

    public DrugDatabaseHelper(Context context) {
        super(context, DB_NAME, null, version);
        // TODO Auto-generated constructor stub
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table drug(drugorchemicalproduct varchar(100) not null, poisoning varchar(255)," +
             "accident varchar(255),"+
             "intentionalselfharm varchar(255),"+
                "unknownintent varchar(255),"+
                "advesreEffect varchar(255),"+
                "data text"+
                " );";
        db.execSQL(sql);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
 
    }
 
}
