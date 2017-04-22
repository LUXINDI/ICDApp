package cn.edu.tsinghua.sicd.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lxdbu on 2017/4/22.
 */

public class QuickSearchHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Cancer.db"; //数据库名称
    private static final int version = 1; //数据库版本

    public QuickSearchHelper(Context context) {
        super(context, DB_NAME, null, version);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table tbldiagnosis_301(keyword varchar(100) not null, [Primary] varchar(255)," +
                "secondary varchar(255),"+
                "insitu varchar(255),"+
                "benign varchar(255),"+
                "tumor varchar(255),"+
                "remark varchar(255),"+
                "data text"+
                " );";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }
}
