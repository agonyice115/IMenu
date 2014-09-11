package com.huassit.imenu.android.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.huassit.imenu.android.db.DbConstants;
import com.huassit.imenu.android.model.Share;

/**
 * Created by Sylar on 14-7-2.
 */
public class ShareMenuDao extends BaseDao<Share> {
    public ShareMenuDao(Context context) {
        super(context);
    }

    public void save(Share share) {
        ContentValues values = new ContentValues();
        values.put(DbConstants.COLUMN_SHARE.TITLE, share.title);
        values.put(DbConstants.COLUMN_SHARE.DESCRIPTION, share.description);
        values.put(DbConstants.COLUMN_SHARE.CONTENT, share.content);
        values.put(DbConstants.COLUMN_SHARE.LINK, share.link);
        database.insert(DbConstants.TABLE.TABLE_SHARE_MENU, null, values);
    }

    public void update(Share share) {
        ContentValues values = new ContentValues();
        values.put(DbConstants.COLUMN_SHARE.TITLE, share.title);
        values.put(DbConstants.COLUMN_SHARE.DESCRIPTION, share.description);
        values.put(DbConstants.COLUMN_SHARE.CONTENT, share.content);
        values.put(DbConstants.COLUMN_SHARE.LINK, share.link);
        database.update(DbConstants.TABLE.TABLE_SHARE_MENU, values, DbConstants.COLUMN_SHARE.TITLE + "=?", new String[]{share.title});
    }

    public boolean isExits(Share share) {
        boolean result = false;
        Cursor cursor = null;
        try {
            final String sql = "select count(*) from " + DbConstants.TABLE.TABLE_SHARE_MENU + " where " + DbConstants.COLUMN_SHARE.TITLE + " =?";
            cursor = database.rawQuery(sql, new String[]{share.title});
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getLong(0) > 0;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    public void saveOrUpdate(Share share) {
        if (isExits(share)) {
            update(share);
        } else {
            save(share);
        }
    }

    public Share getShareMenu() {
        String sql = "select * from " + DbConstants.TABLE.TABLE_SHARE_MENU;
        Cursor cursor = null;
        Share share = null;
        try {
            cursor = database.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                share = new Share();
                share.title = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_SHARE.TITLE));
                share.description = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_SHARE.DESCRIPTION));
                share.content = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_SHARE.CONTENT));
                share.link = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_SHARE.LINK));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return share;
    }
}
