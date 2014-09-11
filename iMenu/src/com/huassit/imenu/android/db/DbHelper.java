package com.huassit.imenu.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by sylar on 14-3-12.
 */
public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, DbConstants.DB_NAME, null, DbConstants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL(DbConstants.CREATE_HISTORY_TABLE_SQL.toString());
            db.execSQL(DbConstants.CREATE_REGION_TABLE_SQL.toString());
            db.execSQL(DbConstants.CREATE_CATEGORY_TABLE_SQL.toString());
            db.execSQL(DbConstants.CREATE_SERVICE_TABLE_SQL.toString());
            db.execSQL(DbConstants.CREATE_FEEDBACK_TABLE_SQL.toString());
            db.execSQL(DbConstants.CREATE_ENVIRONMENT_TABLE_SQL.toString());
            db.execSQL(DbConstants.CREATE_MENU_UNIT_TABLE_SQL.toString());
            db.execSQL(DbConstants.CREATE_MENU_TASTE_TABLE_SQL.toString());
            db.execSQL(DbConstants.CREATE_AREA_CODE_TABLE_SQL.toString());
            db.execSQL(DbConstants.CREATE_SHARE_MENU_TABLE_SQL.toString());
            db.execSQL(DbConstants.CREATE_SHARE_STORE_TABLE_SQL.toString());
            db.execSQL(DbConstants.CREATE_SHARE_MEMBER_TABLE_SQL.toString());
            db.execSQL(DbConstants.CREATE_SHARE_DYNAMIC_TABLE_SQL.toString());
            db.execSQL(DbConstants.CREATE_CLIENT_SKIN_TABLE_SQL.toString());
            db.execSQL(DbConstants.CREATE_ABOUT_INFO_TABLE_SQL.toString());
            db.execSQL(DbConstants.CREATE_RETURN_REASON_TABLE_SQL.toString());
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(this.getClass().getName(), e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
