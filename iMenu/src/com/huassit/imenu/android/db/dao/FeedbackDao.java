package com.huassit.imenu.android.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.huassit.imenu.android.db.DbConstants;
import com.huassit.imenu.android.model.Feedback;

public class FeedbackDao extends BaseDao<Feedback> {
    public FeedbackDao(Context context) {
        super(context);
    }

    public void save(Feedback feedback) {
        ContentValues values = new ContentValues();
        values.put(DbConstants.COLUMN_FEEDBACK.FEEDBACK_ID, feedback.feedback_id);
        values.put(DbConstants.COLUMN_FEEDBACK.FEEDBACK_NAME, feedback.feedback_name);
        values.put(DbConstants.COLUMN_FEEDBACK.FEEDBACK_TYPE, feedback.feedback_type);
        values.put(DbConstants.COLUMN_FEEDBACK.PARENT_ID, feedback.parent_id);
        values.put(DbConstants.COLUMN_FEEDBACK.LEVEL, feedback.level);
        values.put(DbConstants.COLUMN_FEEDBACK.SORT_ORDER, feedback.sort_order);
        database.insert(DbConstants.TABLE.TABLE_FEEDBACK, null, values);
    }

    public void update(Feedback feedback) {
        ContentValues values = new ContentValues();
        values.put(DbConstants.COLUMN_FEEDBACK.FEEDBACK_ID, feedback.feedback_id);
        values.put(DbConstants.COLUMN_FEEDBACK.FEEDBACK_NAME, feedback.feedback_name);
        values.put(DbConstants.COLUMN_FEEDBACK.FEEDBACK_TYPE, feedback.feedback_type);
        values.put(DbConstants.COLUMN_FEEDBACK.PARENT_ID, feedback.parent_id);
        values.put(DbConstants.COLUMN_FEEDBACK.LEVEL, feedback.level);
        values.put(DbConstants.COLUMN_FEEDBACK.SORT_ORDER, feedback.sort_order);
        database.update(DbConstants.TABLE.TABLE_FEEDBACK, values, DbConstants.COLUMN_FEEDBACK.FEEDBACK_ID + "=?", new String[]{feedback.feedback_id});
    }

    public boolean isExits(Feedback feedback) {
        boolean result = false;
        Cursor cursor = null;
        try {
            final String sql = "select count(*) from " + DbConstants.TABLE.TABLE_FEEDBACK + " where " + DbConstants.COLUMN_FEEDBACK.FEEDBACK_ID + " =?";
            cursor = database.rawQuery(sql, new String[]{feedback.feedback_id});
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
    
    public List<Feedback> getFeedBackList() {
        List<Feedback> result = null;
        Cursor cursor = null;
        try {
            final String sql = "select * from " + DbConstants.TABLE.TABLE_FEEDBACK + " order by " + DbConstants.COLUMN_FEEDBACK.SORT_ORDER;
            cursor = database.rawQuery(sql, null);
            if (cursor != null) {
                result = new ArrayList<Feedback>();
                while (cursor.moveToNext()) {
                	Feedback feedback = new Feedback();
                	feedback.feedback_id = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_FEEDBACK.FEEDBACK_ID));
                	feedback.feedback_name = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_FEEDBACK.FEEDBACK_NAME));
                	feedback.feedback_type = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_FEEDBACK.FEEDBACK_TYPE));
                	feedback.level = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_FEEDBACK.LEVEL));
                	feedback.sort_order = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_FEEDBACK.SORT_ORDER));
                	feedback.parent_id = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_FEEDBACK.PARENT_ID));
                    result.add(feedback);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    public void saveOrUpdate(Feedback feedback) {
        if (isExits(feedback)) {
            update(feedback);
        } else {
            save(feedback);
        }
    }
}
