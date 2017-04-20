package cn.edu.tsinghua.sicd.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DiseaseInjuryDatabaseHelper extends SQLiteOpenHelper {
 
    private static final String DB_NAME = "DiseaseInjury.db"; //数据库名称
    private static final int version = 1; //数据库版本
     
    public DiseaseInjuryDatabaseHelper(Context context) {
        super(context, DB_NAME, null, version);
        // TODO Auto-generated constructor stub
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table diseaseinjury(DiagnosisTerm varchar(100) not null, General varchar(255)," +
             "Congenital varchar(255),"+
             "PerinatalNeonatal varchar(255),"+
                "Pregnancy varchar(255),"+
                "Childbirth varchar(255),"+
                "Postpartum varchar(255),"+
                "data text"+
                " );";
        db.execSQL(sql);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
 
    }
 
}
