package com.huassit.imenu.android.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.huassit.imenu.android.db.DbConstants;
import com.huassit.imenu.android.model.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sylar on 14-7-2.
 */
public class ServiceDao extends BaseDao<Service> {
    public ServiceDao(Context context) {
        super(context);
    }

    public void save(Service service) {
        ContentValues values = new ContentValues();
        values.put(DbConstants.COLUMN_SERVICE.SERVICE_ID, service.serviceId);
        values.put(DbConstants.COLUMN_SERVICE.SERVICE_NAME, service.serviceName);
        values.put(DbConstants.COLUMN_SERVICE.SERVICE_IMAGE, service.service_image);
        values.put(DbConstants.COLUMN_SERVICE.THUMB_IMAGE, service.thumb_image);
        values.put(DbConstants.COLUMN_SERVICE.SORT_ORDER, service.sort_order);
        database.insert(DbConstants.TABLE.TABLE_SERVICE, null, values);
    }

    public void update(Service service) {
        ContentValues values = new ContentValues();
        values.put(DbConstants.COLUMN_SERVICE.SERVICE_ID, service.serviceId);
        values.put(DbConstants.COLUMN_SERVICE.SERVICE_NAME, service.serviceName);
        values.put(DbConstants.COLUMN_SERVICE.SERVICE_IMAGE, service.service_image);
        values.put(DbConstants.COLUMN_SERVICE.THUMB_IMAGE, service.thumb_image);
        values.put(DbConstants.COLUMN_SERVICE.SORT_ORDER, service.sort_order);
        database.update(DbConstants.TABLE.TABLE_SERVICE, values, DbConstants.COLUMN_SERVICE.SERVICE_ID + "=?", new String[]{service.serviceId});
    }

    public boolean isExits(Service service) {
        boolean result = false;
        Cursor cursor = null;
        try {
            final String sql = "select count(*) from " + DbConstants.TABLE.TABLE_SERVICE + " where " + DbConstants.COLUMN_SERVICE.SERVICE_ID + " =?";
            cursor = database.rawQuery(sql, new String[]{service.serviceId});
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

    public void saveOrUpdate(Service service) {
        if (isExits(service)) {
            update(service);
        } else {
            save(service);
        }
    }

    public Service getServiceById(String serviceId) {
        Service service = null;
        Cursor cursor = null;
        String sql = "select * from " + DbConstants.TABLE.TABLE_SERVICE + " where "
                + DbConstants.COLUMN_SERVICE.SERVICE_ID + " = " + serviceId;
        cursor = database.rawQuery(sql, null);
        if (cursor != null && cursor.moveToFirst()) {
            service = new Service();
            service.serviceId = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_SERVICE.SERVICE_ID));
            service.serviceName = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_SERVICE.SERVICE_NAME));
            service.service_image = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_SERVICE.SERVICE_IMAGE));
            service.thumb_image = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_SERVICE.THUMB_IMAGE));
            service.sort_order = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_SERVICE.SORT_ORDER));
        }
        return service;
    }

    public List<Service> getServiceList() {
        List<Service> serviceList = null;
        Cursor cursor = null;
        String sql = "select * from " + DbConstants.TABLE.TABLE_SERVICE;
        cursor = database.rawQuery(sql, null);
        if (cursor != null) {
            serviceList = new ArrayList<Service>();
            while (cursor.moveToNext()) {
                Service service = new Service();
                service.serviceId = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_SERVICE.SERVICE_ID));
                service.serviceName = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_SERVICE.SERVICE_NAME));
                service.service_image = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_SERVICE.SERVICE_IMAGE));
                service.thumb_image = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_SERVICE.THUMB_IMAGE));
                service.sort_order = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_SERVICE.SORT_ORDER));
                serviceList.add(service);
            }
        }
        return serviceList;
    }
}
