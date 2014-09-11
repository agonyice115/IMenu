package com.huassit.imenu.android.biz;

import android.app.Activity;
import android.os.Handler;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.http.BaseInvoker;
import com.huassit.imenu.android.model.API;
import com.huassit.imenu.android.model.Menu;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.MenuParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sylar on 14-6-28.
 */
public class SearchMenuResp extends BaseInvoker {

    private String keyword;
    private String count;
    private String page;
    private String token;

    public SearchMenuResp(Activity ctx, Handler handler, String token, String keyword, String count, String page) {
        super(ctx, handler);
        this.keyword = keyword;
        this.count = count;
        this.page = page;
        this.token = token;
    }

    @Override
    public List<NameValuePair> getParameters() {
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("route", API.SEARCH_MENU));
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
        MenuParser menuParser = new MenuParser();
        try {
            List<Menu> menuList = menuParser.doParse(response);
            if (menuParser.getResponseCode() == BaseParser.SUCCESS && menuList != null && menuList.size() > 0) {
                sendMessage(ON_SUCCESS, menuList);
            } else {
                sendMessage(ON_FAILURE, ctx.getString(R.string.no_result_try_again));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            sendMessage(ON_FAILURE, ctx.getString(R.string.data_error));
        }

    }
}
