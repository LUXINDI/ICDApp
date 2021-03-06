package cn.edu.tsinghua.sicd.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lxdbu on 2017/4/22.
 */

public class QuickSearchHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "QuickSearch.db"; //数据库名称
    private static final int version = 1; //数据库版本

    public QuickSearchHelper(Context context) {
        super(context, DB_NAME, null, version);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql1 = "create table tbldiagnosis_301(term varchar(100) not null," +
                "icd_code varchar(255),"+
                "Star_code varchar(255),"+
                "py varchar(255),"+
                "data text"+
                " );";
        db.execSQL(sql1);

        String sql2 = "create table tbldiagnosis_bj(term varchar(100) not null," +
                "icd_code varchar(255),"+
                "Star_code varchar(255),"+
                "py varchar(255),"+
                "data text"+
                " );";
        db.execSQL(sql2);

        String sql3 = "create table tbldiagnosis_wsb(term varchar(100) not null," +
                "icd_code varchar(255),"+
                "Star_code varchar(255),"+
                "py varchar(255),"+
                "data text"+
                " );";
        db.execSQL(sql3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }
}
