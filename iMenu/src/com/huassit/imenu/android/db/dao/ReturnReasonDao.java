package com.huassit.imenu.android.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.huassit.imenu.android.db.DbConstants;
import com.huassit.imenu.android.model.ReturnReason;
import com.huassit.imenu.android.model.Service;

/**
 * Created by Sylar on 14-7-2.
 */
public class ReturnReasonDao extends BaseDao<ReturnReason> {
    public ReturnReasonDao(Context context) {
        super(context);
    }

    public void save(ReturnReason reason) {
        ContentValues values = new ContentValues();
        values.put(DbConstants.COLUMN_RETURN_REASON.RETURN_REASON_ID, reason.return_reason_id);
        values.put(DbConstants.COLUMN_RETURN_REASON.RETURN_REASON_NAME, reason.return_reason_name);
        database.insert(DbConstants.TABLE.TABLE_RETURN_REASON, null, values);
    }

    public void update(ReturnReason reason) {
        ContentValues values = new ContentValues();
        values.put(DbConstants.COLUMN_RETURN_REASON.RETURN_REASON_ID, reason.return_reason_id);
        values.put(DbConstants.COLUMN_RETURN_REASON.RETURN_REASON_NAME, reason.return_reason_name);
        database.update(DbConstants.TABLE.TABLE_RETURN_REASON, values, DbConstants.COLUMN_RETURN_REASON.RETURN_REASON_ID + "=?", new String[]{reason.return_reason_id});
    }
    
    public List<ReturnReason> getReturnReasonList() {
        List<ReturnReason> returnReasonList = null;
        Cursor cursor = null;
        String sql = "select * from " + DbConstants.TABLE.TABLE_RETURN_REASON;
        cursor = database.rawQuery(sql, null);
        if (cursor != null) {
        	returnReasonList = new ArrayList<ReturnReason>();
            while (cursor.moveToNext()) {
            	ReturnReason reason = new ReturnReason();
            	reason.return_reason_id = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_RETURN_REASON.RETURN_REASON_ID));
            	reason.return_reason_name = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_RETURN_REASON.RETURN_REASON_NAME));
            	returnReasonList.add(reason);
            }
        }
        return returnReasonList;
    }

    public boolean isExits(ReturnReason reason) {
        boolean result = false;
        Cursor cursor = null;
        try {
            final String sql = "select count(*) from " + DbConstants.TABLE.TABLE_RETURN_REASON + " where " + DbConstants.COLUMN_RETURN_REASON.RETURN_REASON_ID + " =?";
            cursor = database.rawQuery(sql, new String[]{reason.return_reason_id});
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

    public void saveOrUpdate(ReturnReason reason) {
        if (isExits(reason)) {
            update(reason);
        } else {
            save(reason);
        }
    }
}
