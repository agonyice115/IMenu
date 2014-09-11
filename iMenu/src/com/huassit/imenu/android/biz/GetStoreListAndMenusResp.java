package com.huassit.imenu.android.biz;

import android.app.Activity;
import android.os.Handler;
import com.huassit.imenu.android.http.BaseInvoker;
import com.huassit.imenu.android.model.API;
import com.huassit.imenu.android.model.Store;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.StoreListParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetStoreListAndMenusResp extends BaseInvoker {

    private String category_id;
    private String region_id;
    private String sort_type;
    private String order_type;
    private int store_count;
    private int page;
    private int menu_count;
    private String longitude_num;
    private String latitude_num;
    private String serviceId;
    private StoreListParser storeParser;

    public GetStoreListAndMenusResp(Activity ctx, Handler handler,
                                    String category_id, String region_id, String sort_type, String order_type, int store_count,
                                    int page, int menu_count, String longitude_num, String latitude_num, String serviceId) {
        super(ctx, handler);
        this.category_id = category_id;
        this.region_id = region_id;
        this.sort_type = sort_type;
        this.order_type = order_type;
        this.store_count = store_count;
        this.page = page;
        this.menu_count = menu_count;
        this.latitude_num = latitude_num;
        this.longitude_num = longitude_num;
        this.serviceId = serviceId;
        storeParser = new StoreListParser();
    }

    @Override
    public List<NameValuePair> getParameters() {
        List<NameValuePair> nvp = new ArrayList<NameValuePair>();
        nvp.add(new BasicNameValuePair("route", API.GET_STORE_LIST_AND_MENUS));
        nvp.add(new BasicNameValuePair("token", ""));
        nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
        return nvp;
    }

    private String makeJsonText() {
        JSONObject json = new JSONObject();
        try {
            json.put("category_id", category_id);
            json.put("region_id", region_id);
            json.put("sort_type", sort_type);
            json.put("order_type", order_type);
            json.put("store_count", store_count);
            json.put("page", page);
            json.put("menu_count", menu_count);
            json.put("longitude_num", longitude_num);
            json.put("latitude_num", latitude_num);
            json.put("service_id", serviceId);
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
            System.out.println("GetStoreList" + response);
            ArrayList<Store> storeList = storeParser.doParse(response);
            if (storeParser.getResponseCode() == BaseParser.SUCCESS && storeList != null) {
                sendMessage(ON_SUCCESS, storeList);
            } else {
                sendMessage(ON_FAILURE, null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
