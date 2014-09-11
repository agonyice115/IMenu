package com.huassit.imenu.android.biz;

import android.content.Context;
import android.os.Handler;
import com.baidu.location.BDLocation;
import com.huassit.imenu.android.http.BaseInvoker;
import com.huassit.imenu.android.model.API;
import com.huassit.imenu.android.model.BaseModel;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.GetFrontPageParser;
import com.huassit.imenu.android.util.LocationUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Sylar on 14-7-15.
 */
public class GetFrontPageResp extends BaseInvoker {
    private GetFrontPageParser pageParser;

    public GetFrontPageResp(Context ctx, Handler handler) {
        super(ctx, handler);
        pageParser = new GetFrontPageParser();
    }

    @Override
    public List<NameValuePair> getParameters() {
        List<NameValuePair> nvp = new ArrayList<NameValuePair>();
        nvp.add(new BasicNameValuePair("route", API.GET_FRONT_PAGE));
        nvp.add(new BasicNameValuePair("token", ""));
        nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
        return nvp;
    }

    private String makeJsonText() {
        JSONObject json = new JSONObject();
        try {
            BDLocation location = LocationUtils.getInstance(ctx).getLocation();
            if (location != null) {
                json.put("longitude_num", String.valueOf(location.getLongitude()));
                json.put("latitude_num", String.valueOf(location.getLatitude()));
            } else {
                json.put("longitude_num", "1");
                json.put("latitude_num", "1");
            }
            json.put("request_date", String.valueOf(System.currentTimeMillis() / 1000));
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
            Map<String, BaseModel> result = pageParser.doParse(response);
            if (pageParser.getResponseCode() == BaseParser.SUCCESS && result != null && result.size() > 0) {
                sendMessage(ON_SUCCESS, result);
            } else {
                sendMessage(ON_FAILURE, null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            sendMessage(ON_FAILURE, null);
        }
    }
}
