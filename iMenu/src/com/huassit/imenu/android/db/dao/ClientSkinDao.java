package com.huassit.imenu.android.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.huassit.imenu.android.db.DbConstants;
import com.huassit.imenu.android.model.ClientSkin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sylar on 14-7-2.
 */
public class ClientSkinDao extends BaseDao<ClientSkin> {
    public ClientSkinDao(Context context) {
        super(context);
    }

    public void save(ClientSkin skin) {
        ContentValues values = new ContentValues();
        values.put(DbConstants.COLUMN_CLIENT_SKIN.CLIENT_SKIN_ID, skin.client_skin_id);
        values.put(DbConstants.COLUMN_CLIENT_SKIN.CLIENT_SKIN_NAME, skin.client_skin_name);
        values.put(DbConstants.COLUMN_CLIENT_SKIN.CLIENT_SKIN_VALUE, skin.client_skin_value);
        values.put(DbConstants.COLUMN_CLIENT_SKIN.SEX, skin.sex);
        values.put(DbConstants.COLUMN_CLIENT_SKIN.DEFAULT_VALUE, skin.defaultValue);
        values.put(DbConstants.COLUMN_CLIENT_SKIN.SORT_ORDER, skin.sort_order);
        database.insert(DbConstants.TABLE.TABLE_CLIENT_SKIN, null, values);
    }

    public void update(ClientSkin skin) {
        ContentValues values = new ContentValues();
        values.put(DbConstants.COLUMN_CLIENT_SKIN.CLIENT_SKIN_ID, skin.client_skin_id);
        values.put(DbConstants.COLUMN_CLIENT_SKIN.CLIENT_SKIN_NAME, skin.client_skin_name);
        values.put(DbConstants.COLUMN_CLIENT_SKIN.CLIENT_SKIN_VALUE, skin.client_skin_value);
        values.put(DbConstants.COLUMN_CLIENT_SKIN.SEX, skin.sex);
        values.put(DbConstants.COLUMN_CLIENT_SKIN.DEFAULT_VALUE, skin.defaultValue);
        values.put(DbConstants.COLUMN_CLIENT_SKIN.SORT_ORDER, skin.sort_order);
        database.update(DbConstants.TABLE.TABLE_CLIENT_SKIN, values, DbConstants.COLUMN_CLIENT_SKIN.CLIENT_SKIN_ID + "=?", new String[]{skin.client_skin_id});
    }

    public boolean isExits(ClientSkin skin) {
        boolean result = false;
        Cursor cursor = null;
        try {
            final String sql = "select count(*) from " + DbConstants.TABLE.TABLE_CLIENT_SKIN + " where " + DbConstants.COLUMN_CLIENT_SKIN.CLIENT_SKIN_ID + " =?";
            cursor = database.rawQuery(sql, new String[]{skin.client_skin_id});
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

    public void saveOrUpdate(ClientSkin skin) {
        if (isExits(skin)) {
            update(skin);
        } else {
            save(skin);
        }
    }

    public List<ClientSkin> getAllSkin() {
        List<ClientSkin> result = null;
        Cursor cursor = null;
        try {
            final String sql = "select * from " + DbConstants.TABLE.TABLE_CLIENT_SKIN;
            cursor = database.rawQuery(sql, null);
            if (cursor != null) {
                result = new ArrayList<ClientSkin>();
                while (cursor.moveToNext()) {
                    ClientSkin skin = new ClientSkin();
                    skin.client_skin_name = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_CLIENT_SKIN.CLIENT_SKIN_NAME));
                    skin.client_skin_value = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_CLIENT_SKIN.CLIENT_SKIN_VALUE));
                    skin.client_skin_id = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_CLIENT_SKIN.CLIENT_SKIN_ID));
                    skin.defaultValue = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_CLIENT_SKIN.DEFAULT_VALUE));
                    skin.sex = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_CLIENT_SKIN.SEX));
                    skin.sort_order = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_CLIENT_SKIN.SORT_ORDER));
                    result.add(skin);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    public ClientSkin getDefaultSkin(String defaultValue) {
        ClientSkin skin = null;
        Cursor cursor = null;
        try {
            final String sql = "select * from " + DbConstants.TABLE.TABLE_CLIENT_SKIN + " where " + DbConstants.COLUMN_CLIENT_SKIN.DEFAULT_VALUE + "=?";
            cursor = database.rawQuery(sql, new String[]{defaultValue});
            if (cursor != null && cursor.moveToFirst()) {
                skin = new ClientSkin();
                skin.client_skin_name = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_CLIENT_SKIN.CLIENT_SKIN_NAME));
                skin.client_skin_value = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_CLIENT_SKIN.CLIENT_SKIN_VALUE));
                skin.client_skin_id = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_CLIENT_SKIN.CLIENT_SKIN_ID));
                skin.defaultValue = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_CLIENT_SKIN.DEFAULT_VALUE));
                skin.sex = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_CLIENT_SKIN.SEX));
                skin.sort_order = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_CLIENT_SKIN.SORT_ORDER));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return skin;
    }
}
