package com.huassit.imenu.android.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Sylar on 14-6-30.
 */
public abstract class BaseModel<T> implements Serializable {

    public abstract T parse(JSONObject jsonObject);
}
