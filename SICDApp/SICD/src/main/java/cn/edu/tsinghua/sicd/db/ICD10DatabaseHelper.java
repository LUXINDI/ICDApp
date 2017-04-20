package cn.edu.tsinghua.sicd.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ICD10DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "ICD10.db"; //数据库名称
    private static final int version = 1; //数据库版本

    public ICD10DatabaseHelper(Context context) {
        super(context, DB_NAME, null, version);
        // TODO Auto-generated constructor stub
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table icd10(cause varchar(100) not null, icd10 varchar(255),data text" +

                " );";
        db.execSQL(sql);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
 
    }
 
}
