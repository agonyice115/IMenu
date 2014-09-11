package com.huassit.imenu.android.biz;

import android.app.Activity;
import android.os.Handler;
import com.huassit.imenu.android.http.BaseInvoker;
import com.huassit.imenu.android.model.API;
import com.huassit.imenu.android.model.BaseModel;
import com.huassit.imenu.android.model.ClientConfig;
import com.huassit.imenu.android.model.KeyValuePair;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.ConfigListParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CheckClientConfigResp extends BaseInvoker {


    private ConfigListParser configParser;
    private ClientConfig config;

    public CheckClientConfigResp(Activity ctx, Handler handler, ClientConfig config) {
        super(ctx, handler);
        this.config = config;
        configParser = new ConfigListParser();
    }

    @Override
    public List<NameValuePair> getParameters() {
        List<NameValuePair> nvp = new ArrayList<NameValuePair>();
        nvp.add(new BasicNameValuePair("route", API.CHECK_CLIENT_CONFIG));
        nvp.add(new BasicNameValuePair("token", ""));
        nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
        return nvp;
    }

    private String makeJsonText() {
        JSONObject json = new JSONObject();
        try {
            json.put("region", config.region);
            json.put("category", config.category);
            json.put("environment", config.environment);
            json.put("service", config.service);
            json.put("menu_unit", config.menu_unit);
            json.put("menu_taste", config.menu_taste);
            json.put("area_code", config.area_code);
            json.put("share_menu", config.share_menu);
            json.put("share_store", config.share_store);
            json.put("share_member", config.share_member);
            json.put("share_dynamic", config.share_dynamic);
            json.put("client_skin", config.client_skin);
            json.put("feedback", config.feed_back);
            json.put("device_type", config.device_type);
            json.put("version_android", config.version_android);
            json.put("mobile_token", config.mobile_token);
            json.put("about_info", config.about_info);
            json.put("return_reason", config.return_reason);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @Override
    public String getRequestUrl() {

        return API.server;
    }

    @Override
    public HTTP_METHOD getRequestMethod() {

        return HTTP_METHOD.METHOD_POST;
    }

    @Override
    protected void handleResponse(String response) {
        try {
            Map<KeyValuePair, List<? extends BaseModel>> configList = configParser.doParse(response);
            //存入数据库
            if (configParser.getResponseCode()==BaseParser.SUCCESS && configList != null) {
                sendMessage(ON_SUCCESS, configList);
            } else {
                sendMessage(ON_FAILURE, null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
