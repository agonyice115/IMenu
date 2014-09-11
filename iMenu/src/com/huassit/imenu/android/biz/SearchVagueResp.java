package com.huassit.imenu.android.biz;

import android.content.Context;
import android.os.Handler;
import com.huassit.imenu.android.http.BaseInvoker;
import com.huassit.imenu.android.model.API;
import com.huassit.imenu.android.model.SearchVague;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.SearchVagueParser;
import com.huassit.imenu.android.util.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sylar on 14-7-15.
 */
public class SearchVagueResp extends BaseInvoker {
    private String keyword;
    private int searchType;
    private String latitude;
    private String longitude;
    private int count = 20;
    private SearchVagueParser parser;

    public SearchVagueResp(Context ctx, Handler handler, String keyword, int searchType, String lat, String lng) {
        super(ctx, handler);
        this.keyword = keyword;
        this.searchType = searchType;
        this.latitude = lat;
        this.longitude = lng;
        parser = new SearchVagueParser();
    }

    @Override
    public List<NameValuePair> getParameters() {
        List<NameValuePair> params = new ArrayList<NameValuePair>(3);
        params.add(new BasicNameValuePair("route", API.SEARCH_VAGUE));
        params.add(new BasicNameValuePair("token", ""));
        params.add(new BasicNameValuePair("jsonText", makeJSON()));
        return params;
    }

    private String makeJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("filter_data", keyword);
            jsonObject.put("count", count);
            jsonObject.put("search_type", searchType);
            if (!StringUtils.isBlank(latitude)) {
                jsonObject.put("latitude_num", latitude);
            }
            if (!StringUtils.isBlank(longitude)) {
                jsonObject.put("longitude_num", longitude);
            }
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
        try {
            List<SearchVague> vagueList = parser.doParse(response);
            if (parser.getResponseCode() == BaseParser.SUCCESS) {
                sendMessage(ON_SUCCESS, vagueList);
            } else {
                sendMessage(ON_FAILURE, parser.getResponseMessage());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
