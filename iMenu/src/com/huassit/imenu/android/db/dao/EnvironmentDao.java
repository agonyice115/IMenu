package com.huassit.imenu.android.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.huassit.imenu.android.db.DbConstants;
import com.huassit.imenu.android.model.Environment;
import com.huassit.imenu.android.model.Service;

/**
 * Created by Sylar on 14-7-2.
 */
public class EnvironmentDao extends BaseDao<Environment> {
    public EnvironmentDao(Context context) {
        super(context);
    }


    public void save(Environment environment) {
        ContentValues values = new ContentValues();
        values.put(DbConstants.COLUMN_ENVIRONMENT.ENVIRONMENT_ID, environment.environment_id);
        values.put(DbConstants.COLUMN_ENVIRONMENT.ENVIRONMENT_NAME, environment.environment_name);
        values.put(DbConstants.COLUMN_ENVIRONMENT.ENVIRONMENT_IMAGE, environment.environment_image);
        values.put(DbConstants.COLUMN_ENVIRONMENT.THUMB_IMAGE, environment.thumb_image);
        values.put(DbConstants.COLUMN_ENVIRONMENT.SORT_ORDER, environment.sort_order);
        database.insert(DbConstants.TABLE.TABLE_ENVIRONMENT, null, values);
    }

    public void update(Environment environment) {
        ContentValues values = new ContentValues();
        values.put(DbConstants.COLUMN_ENVIRONMENT.ENVIRONMENT_ID, environment.environment_id);
        values.put(DbConstants.COLUMN_ENVIRONMENT.ENVIRONMENT_NAME, environment.environment_name);
        values.put(DbConstants.COLUMN_ENVIRONMENT.ENVIRONMENT_IMAGE, environment.environment_image);
        values.put(DbConstants.COLUMN_ENVIRONMENT.THUMB_IMAGE, environment.thumb_image);
        values.put(DbConstants.COLUMN_ENVIRONMENT.SORT_ORDER, environment.sort_order);
        database.update(DbConstants.TABLE.TABLE_ENVIRONMENT, values, DbConstants.COLUMN_ENVIRONMENT.ENVIRONMENT_ID + "=?", new String[]{environment.environment_id});
    }

    public boolean isExits(Environment environment) {
        boolean result = false;
        Cursor cursor = null;
        try {
            final String sql = "select count(*) from " + DbConstants.TABLE.TABLE_ENVIRONMENT + " where " + DbConstants.COLUMN_ENVIRONMENT.ENVIRONMENT_ID + " =?";
            cursor = database.rawQuery(sql, new String[]{environment.environment_id});
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

    public void saveOrUpdate(Environment environment) {
        if (isExits(environment)) {
            update(environment);
        } else {
            save(environment);
        }
    }
    
    public Environment getEnvironmentById(String environmentId){
    	Environment environment =null;
    	Cursor cursor =null;
    	String sql ="select * from "+DbConstants.TABLE.TABLE_ENVIRONMENT+" where "
    			+DbConstants.COLUMN_ENVIRONMENT.ENVIRONMENT_ID+" = "+environmentId;
    	cursor =database.rawQuery(sql, null);
    	if(cursor!=null){
    		environment =new Environment();
    		while(cursor.moveToNext()){
    			environment.environment_id =cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_ENVIRONMENT.ENVIRONMENT_ID));
    			environment.environment_name =cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_ENVIRONMENT.ENVIRONMENT_NAME));
    			environment.environment_image =cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_ENVIRONMENT.ENVIRONMENT_IMAGE));
    			environment.thumb_image =cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_ENVIRONMENT.THUMB_IMAGE));
    			environment.sort_order =cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_ENVIRONMENT.SORT_ORDER));
    		}
    	}
		return environment;
    	
    }
}
