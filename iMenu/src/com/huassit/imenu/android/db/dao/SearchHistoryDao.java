package com.huassit.imenu.android.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.huassit.imenu.android.db.DbConstants;
import com.huassit.imenu.android.model.SearchHistory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sylar on 14-6-30.
 */
public class SearchHistoryDao extends BaseDao<SearchHistory> {

    public SearchHistoryDao(Context context) {
        super(context);
    }

    public void deleteAll() {
        database.delete(DbConstants.TABLE.TABLE_SEARCH_HISTORY, null, null);
    }

    public void saveOrUpdate(SearchHistory searchHistory) {
        if (isExits(searchHistory)) {
            update(searchHistory);
        } else {
            save(searchHistory);
        }
    }

    public void save(SearchHistory searchHistory) {
        ContentValues values = new ContentValues();
        values.put(DbConstants.COLUMN_HISTORY.KEYWORD, searchHistory.keyword);
        values.put(DbConstants.COLUMN_HISTORY.LAST_UPDATED, searchHistory.lastUpdated);
        database.insert(DbConstants.TABLE.TABLE_SEARCH_HISTORY, null, values);
    }

    public void update(SearchHistory searchHistory) {
        ContentValues values = new ContentValues();
        values.put(DbConstants.COLUMN_HISTORY.KEYWORD, searchHistory.keyword);
        values.put(DbConstants.COLUMN_HISTORY.LAST_UPDATED, searchHistory.lastUpdated);
        database.update(DbConstants.TABLE.TABLE_SEARCH_HISTORY, values, DbConstants.COLUMN_HISTORY.KEYWORD + "=?", new String[]{searchHistory.keyword});
    }

    public boolean isExits(SearchHistory searchHistory) {
        boolean result = false;
        Cursor cursor = null;
        try {
            final String sql = "select count(*) from " + DbConstants.TABLE.TABLE_SEARCH_HISTORY + " where " + DbConstants.COLUMN_HISTORY.KEYWORD + " =?";
            cursor = database.rawQuery(sql, new String[]{searchHistory.keyword});
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

    public List<SearchHistory> getSearchHistoryList() {
        List<SearchHistory> result = null;
        Cursor cursor = null;
        try {
            cursor = database.query(DbConstants.TABLE.TABLE_SEARCH_HISTORY, new String[]{DbConstants.COLUMN_HISTORY.KEYWORD}, null, null, null, null, DbConstants.COLUMN_HISTORY.LAST_UPDATED + " desc");
            if (cursor != null) {
                result = new ArrayList<SearchHistory>();
                while (cursor.moveToNext()) {
                    SearchHistory history = new SearchHistory();
                    history.keyword = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_HISTORY.KEYWORD));
                    result.add(history);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

}
