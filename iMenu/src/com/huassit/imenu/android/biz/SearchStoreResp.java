package com.huassit.imenu.android.biz;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import com.huassit.imenu.android.R;
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

/**
 * Created by Sylar on 14-6-28.
 */
public class SearchStoreResp extends BaseInvoker {
    private final String keyword;
    private final String count;
    private final String page;
    private final String latitude;
    private final String longitude;
    private final String token;

    private StoreListParser storeParser;

    public SearchStoreResp(Activity ctx, Handler handler, String token, String keyword, String count, String page, String latitude, String longitude) {
        super(ctx, handler);
        this.keyword = keyword;
        this.count = count;
        this.page = page;
        this.latitude = latitude;
        this.longitude = longitude;
        this.token = token;
        storeParser = new StoreListParser();
    }

    @Override
    public List<NameValuePair> getParameters() {
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("route", API.SEARCH_STORE));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("jsonText", getJsonText()));
        return params;
    }

    private String getJsonText() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("filter_data", keyword);
            jsonObject.put("count", count);
            jsonObject.put("page", page);
            jsonObject.put("latitude_num", latitude);
            jsonObject.put("longitude_num", longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
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
        Log.e("TAG", response);
        try {
            List<Store> storeList = storeParser.doParse(response);
            if (storeParser.getResponseCode() == BaseParser.SUCCESS && storeList != null && storeList.size() > 0) {
                sendMessage(ON_SUCCESS, storeList);
            } else {
                sendMessage(ON_FAILURE, ctx.getString(R.string.no_result_try_again));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            sendMessage(ON_FAILURE, ctx.getString(R.string.data_error));
        }
    }
}
