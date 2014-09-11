package com.huassit.imenu.android.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.huassit.imenu.android.db.DbConstants;
import com.huassit.imenu.android.model.Region;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sylar on 14-6-30.
 */
public class RegionDao extends BaseDao<Region> {
    public RegionDao(Context context) {
        super(context);
    }

    public void deleteAll() {
        database.delete(DbConstants.TABLE.TABLE_REGION, null, null);
    }

    public void saveOrUpdate(Region region) {
        if (isExits(region)) {
            update(region);
        } else {
            save(region);
        }
    }

    public void save(Region region) {
        ContentValues values = new ContentValues();
        values.put(DbConstants.COLUMN_REGION.REGION_ID, region.region_id);
        values.put(DbConstants.COLUMN_REGION.REGION_NAME, region.region_name);
        values.put(DbConstants.COLUMN_REGION.REGION_IMAGE, region.region_image);
        values.put(DbConstants.COLUMN_REGION.ALIASES_NAME, region.aliases_name);
        values.put(DbConstants.COLUMN_REGION.IS_OPEN, region.is_open);
        values.put(DbConstants.COLUMN_REGION.LEVEL, region.level);
        values.put(DbConstants.COLUMN_REGION.PARENT_ID, region.parent_id);
        values.put(DbConstants.COLUMN_REGION.SORT_ORDER, region.sort_order);
        database.insert(DbConstants.TABLE.TABLE_REGION, null, values);
    }

    public void update(Region region) {
        ContentValues values = new ContentValues();
        values.put(DbConstants.COLUMN_REGION.REGION_ID, region.region_id);
        values.put(DbConstants.COLUMN_REGION.REGION_NAME, region.region_name);
        values.put(DbConstants.COLUMN_REGION.REGION_IMAGE, region.region_image);
        values.put(DbConstants.COLUMN_REGION.ALIASES_NAME, region.aliases_name);
        values.put(DbConstants.COLUMN_REGION.IS_OPEN, region.is_open);
        values.put(DbConstants.COLUMN_REGION.LEVEL, region.level);
        values.put(DbConstants.COLUMN_REGION.PARENT_ID, region.parent_id);
        values.put(DbConstants.COLUMN_REGION.SORT_ORDER, region.sort_order);
        database.update(DbConstants.TABLE.TABLE_REGION, values, DbConstants.COLUMN_REGION.REGION_ID + "=?", new String[]{region.region_id});
    }

    public boolean isExits(Region region) {
        boolean result = false;
        Cursor cursor = null;
        try {
            final String sql = "select count(*) from " + DbConstants.TABLE.TABLE_REGION + " where " + DbConstants.COLUMN_REGION.REGION_ID + " =?";
            cursor = database.rawQuery(sql, new String[]{region.region_id});
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

 
    
    public List<Region> getRegionList() {
        List<Region> result = null;
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("select * from " + DbConstants.TABLE.TABLE_REGION + " order by " + DbConstants.COLUMN_REGION.SORT_ORDER, null);
            if (cursor != null) {
                result = new ArrayList<Region>();
                while (cursor.moveToNext()) {
                    Region region = new Region();
                    region.region_id = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.REGION_ID));
                    region.region_name = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.REGION_NAME));
                    region.region_image = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.REGION_IMAGE));
                    region.parent_id = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.PARENT_ID));
                    region.aliases_name = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.ALIASES_NAME));
                    region.is_open = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.IS_OPEN));
                    region.level = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.LEVEL));
                    region.sort_order = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.SORT_ORDER));
                    result.add(region);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    public List<Region> getSectionAndAreaList(String regionId) {
        List<Region> result = null;
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("select * from " + DbConstants.TABLE.TABLE_REGION + " where " + DbConstants.COLUMN_REGION.PARENT_ID + " ='" + regionId + "' order by " + DbConstants.COLUMN_REGION.SORT_ORDER, null);
            if (cursor != null) {
                result = new ArrayList<Region>();
                while (cursor.moveToNext()) {
                    Region region = new Region();
                    region.region_id = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.REGION_ID));
                    region.region_name = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.REGION_NAME));
                    region.region_image = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.REGION_IMAGE));
                    region.parent_id = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.PARENT_ID));
                    region.aliases_name = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.ALIASES_NAME));
                    region.is_open = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.IS_OPEN));
                    region.level = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.LEVEL));
                    region.sort_order = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.SORT_ORDER));
                    result.add(region);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    public List<Region> getProvinceList() {
        List<Region> result = null;
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("select * from " + DbConstants.TABLE.TABLE_REGION + " where level='1' order by " + DbConstants.COLUMN_REGION.SORT_ORDER, null);
            if (cursor != null) {
                result = new ArrayList<Region>();
                while (cursor.moveToNext()) {
                    Region region = new Region();
                    region.region_id = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.REGION_ID));
                    region.region_name = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.REGION_NAME));
                    region.region_image = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.REGION_IMAGE));
                    region.parent_id = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.PARENT_ID));
                    region.aliases_name = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.ALIASES_NAME));
                    region.is_open = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.IS_OPEN));
                    region.level = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.LEVEL));
                    region.sort_order = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.SORT_ORDER));
                    result.add(region);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    public List<Region> getCityList(String regionId) {
        List<Region> result = null;
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("select * from " + DbConstants.TABLE.TABLE_REGION + " where level='2' and " + DbConstants.COLUMN_REGION.PARENT_ID + "='" + regionId + "' order by " + DbConstants.COLUMN_REGION.SORT_ORDER, null);
            if (cursor != null) {
                result = new ArrayList<Region>();
                while (cursor.moveToNext()) {
                    Region region = new Region();
                    region.region_id = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.REGION_ID));
                    region.region_name = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.REGION_NAME));
                    region.region_image = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.REGION_IMAGE));
                    region.parent_id = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.PARENT_ID));
                    region.aliases_name = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.ALIASES_NAME));
                    region.is_open = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.IS_OPEN));
                    region.level = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.LEVEL));
                    region.sort_order = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.SORT_ORDER));
                    result.add(region);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    public Region getRegionByName(String name) {
        Region region = null;
        Cursor cursor = null;
        try {
        	String sql ="select * from " + DbConstants.TABLE.TABLE_REGION + " where " + DbConstants.COLUMN_REGION.REGION_NAME + "='" + name + "'";
            cursor = database.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                region = new Region();
                region.region_id = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.REGION_ID));
                region.region_name = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.REGION_NAME));
                region.region_image = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.REGION_IMAGE));
                region.parent_id = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.PARENT_ID));
                region.aliases_name = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.ALIASES_NAME));
                region.is_open = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.IS_OPEN));
                region.level = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.LEVEL));
                region.sort_order = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.SORT_ORDER));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return region;
    }
    
    public Region getRegionById(String id) {
        Region region = null;
        Cursor cursor = null;
        try {
        	String sql ="select * from " + DbConstants.TABLE.TABLE_REGION + " where " + DbConstants.COLUMN_REGION.REGION_ID + "='" + id + "'";
            cursor = database.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                region = new Region();
                region.region_id = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.REGION_ID));
                region.region_name = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.REGION_NAME));
                region.region_image = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.REGION_IMAGE));
                region.parent_id = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.PARENT_ID));
                region.aliases_name = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.ALIASES_NAME));
                region.is_open = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.IS_OPEN));
                region.level = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.LEVEL));
                region.sort_order = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_REGION.SORT_ORDER));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return region;
    }
}
