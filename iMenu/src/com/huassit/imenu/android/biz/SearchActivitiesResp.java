package com.huassit.imenu.android.biz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.huassit.imenu.android.R;
import com.huassit.imenu.android.http.BaseInvoker;
import com.huassit.imenu.android.model.API;
import com.huassit.imenu.android.model.Dynamic;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.DynamicListParser;

/**
 * Created by Sylar on 14-6-28.
 */
public class SearchActivitiesResp extends BaseInvoker {
    private final String token;
    private final String keyword;
    private final String count;
    private final String page;
    private DynamicListParser parser;

    public SearchActivitiesResp(Activity ctx, Handler handler, String token, String keyword, String count, String page) {
        super(ctx, handler);
        this.token = token;
        this.keyword = keyword;
        this.count = count;
        this.page = page;
        parser = new DynamicListParser();
    }

    @Override
    public List<NameValuePair> getParameters() {
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("route", API.SEARCH_ACTIVITIES));
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
            List<Dynamic> dynamicList = parser.doParse(response);
            if (parser.getResponseCode() == BaseParser.SUCCESS && dynamicList != null && dynamicList.size() > 0) {
                sendMessage(ON_SUCCESS, dynamicList);
            } else {
                sendMessage(ON_FAILURE, ctx.getString(R.string.no_result_try_again));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            sendMessage(ON_FAILURE, ctx.getString(R.string.data_error));
        }
    }
}
