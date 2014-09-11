package com.huassit.imenu.android.db.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.huassit.imenu.android.db.SQLiteManager;
import com.huassit.imenu.android.model.BaseModel;

import java.util.List;

/**
 * Created by Sylar on 14-6-30.
 */
public abstract class BaseDao<T extends BaseModel> {
    protected SQLiteDatabase database;
    protected Context mContext;

    public BaseDao(Context context) {
        this.mContext = context;
        database = SQLiteManager.getInstance(mContext).getReadableDatabase();
    }

    public abstract void save(T t);

    public abstract void saveOrUpdate(T t);

    public abstract void update(T t);

    public boolean isExits(T t) {
        return false;
    }


    public void bulkSave(List<T> list) {
        database.beginTransaction();
        try {
            for (T t : list) {
                save(t);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public void bulkSaveOrUpdate(List<T> list) {
        database.beginTransaction();
        try {
            for (T t : list) {
                saveOrUpdate(t);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }
}
