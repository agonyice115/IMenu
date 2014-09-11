package com.huassit.imenu.android.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.huassit.imenu.android.db.DbConstants;
import com.huassit.imenu.android.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sylar on 14-6-30.
 */
public class CategoryDao extends BaseDao<Category> {
    public CategoryDao(Context context) {
        super(context);
    }

    public void deleteAll() {
        database.delete(DbConstants.TABLE.TABLE_CATEGORY, null, null);
    }

    public void saveOrUpdate(Category category) {
        if (isExits(category)) {
            update(category);
        } else {
            save(category);
        }
    }

    public void save(Category category) {
        ContentValues values = new ContentValues();
        values.put(DbConstants.COLUMN_CATEGORY.CATEGORY_ID, category.category_id);
        values.put(DbConstants.COLUMN_CATEGORY.CATEGORY_NAME, category.category_name);
        values.put(DbConstants.COLUMN_CATEGORY.CATEGORY_IMAGE, category.category_image);
        values.put(DbConstants.COLUMN_CATEGORY.ALIASES_NAME, category.aliases_name);
        values.put(DbConstants.COLUMN_CATEGORY.LEVEL, category.level);
        values.put(DbConstants.COLUMN_CATEGORY.PARENT_ID, category.parent_id);
        values.put(DbConstants.COLUMN_CATEGORY.SORT_ORDER, category.sort_order);
        database.insert(DbConstants.TABLE.TABLE_CATEGORY, null, values);
    }

    public void update(Category category) {
        ContentValues values = new ContentValues();
        values.put(DbConstants.COLUMN_CATEGORY.CATEGORY_ID, category.category_id);
        values.put(DbConstants.COLUMN_CATEGORY.CATEGORY_NAME, category.category_name);
        values.put(DbConstants.COLUMN_CATEGORY.CATEGORY_IMAGE, category.category_image);
        values.put(DbConstants.COLUMN_CATEGORY.ALIASES_NAME, category.aliases_name);
        values.put(DbConstants.COLUMN_CATEGORY.LEVEL, category.level);
        values.put(DbConstants.COLUMN_CATEGORY.PARENT_ID, category.parent_id);
        values.put(DbConstants.COLUMN_CATEGORY.SORT_ORDER, category.sort_order);
        database.update(DbConstants.TABLE.TABLE_CATEGORY, values, DbConstants.COLUMN_CATEGORY.CATEGORY_ID + "=?", new String[]{category.category_id});
    }

    public boolean isExits(Category category) {
        boolean result = false;
        Cursor cursor = null;
        try {
            final String sql = "select count(*) from " + DbConstants.TABLE.TABLE_CATEGORY + " where " + DbConstants.COLUMN_CATEGORY.CATEGORY_ID + " =?";
            cursor = database.rawQuery(sql, new String[]{category.category_id});
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

    public List<Category> getCategoryList() {
        List<Category> result = null;
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("select * from " + DbConstants.TABLE.TABLE_CATEGORY + " order by " + DbConstants.COLUMN_CATEGORY.SORT_ORDER, null);
            if (cursor != null) {
                result = new ArrayList<Category>();
                while (cursor.moveToNext()) {
                    Category category = new Category();
                    category.category_id = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_CATEGORY.CATEGORY_ID));
                    category.category_name = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_CATEGORY.CATEGORY_NAME));
                    category.category_image = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_CATEGORY.CATEGORY_IMAGE));
                    category.parent_id = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_CATEGORY.PARENT_ID));
                    category.aliases_name = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_CATEGORY.ALIASES_NAME));
                    category.level = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_CATEGORY.LEVEL));
                    category.sort_order = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_CATEGORY.SORT_ORDER));
                    result.add(category);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    public Category getCategoryById(String id) {
        Category category = null;
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("select * from " + DbConstants.TABLE.TABLE_CATEGORY +
                    " where " + DbConstants.COLUMN_CATEGORY.CATEGORY_ID + "=?", new String[]{id});
            if (cursor != null && cursor.moveToNext()) {
                category = new Category();
                category.category_id = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_CATEGORY.CATEGORY_ID));
                category.category_name = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_CATEGORY.CATEGORY_NAME));
                category.category_image = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_CATEGORY.CATEGORY_IMAGE));
                category.parent_id = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_CATEGORY.PARENT_ID));
                category.aliases_name = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_CATEGORY.ALIASES_NAME));
                category.level = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_CATEGORY.LEVEL));
                category.sort_order = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_CATEGORY.SORT_ORDER));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return category;
    }
}
