package com.huassit.imenu.android.biz;

import android.app.Activity;
import android.os.Handler;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.http.BaseInvoker;
import com.huassit.imenu.android.model.API;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.DefaultParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sylar on 14-7-11.
 */
public class LogoutResp extends BaseInvoker {
    private String token;
    private String memberId;

    public LogoutResp(Activity ctx, Handler handler, String token, String memberId) {
        super(ctx, handler);
        this.token = token;
        this.memberId = memberId;
    }


    @Override
    public List<NameValuePair> getParameters() {
        List<NameValuePair> nvp = new ArrayList<NameValuePair>();
        nvp.add(new BasicNameValuePair("route", API.LOGOUT));
        nvp.add(new BasicNameValuePair("token", token));
        nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
        return nvp;
    }

    private String makeJsonText() {
        JSONObject member = new JSONObject();
        try {
            member.put("memberId", memberId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return member.toString();

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
        DefaultParser defaultParser = new DefaultParser();
        try {
            defaultParser.doParse(response);
            if (defaultParser.getResponseCode() == BaseParser.SUCCESS) {
                sendMessage(ON_SUCCESS, null);
            } else {
                sendMessage(ON_FAILURE, defaultParser.getResponseMessage());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            sendMessage(ON_FAILURE, ctx.getString(R.string.data_error));
        }
    }
}
