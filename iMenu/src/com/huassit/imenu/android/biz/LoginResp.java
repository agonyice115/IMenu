package com.huassit.imenu.android.biz;

import android.app.Activity;
import android.os.Handler;
import com.huassit.imenu.android.MD5;
import com.huassit.imenu.android.http.BaseInvoker;
import com.huassit.imenu.android.model.API;
import com.huassit.imenu.android.model.Member;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.UserInfoParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginResp extends BaseInvoker {

    private UserInfoParser userParser;
    private Member user;
    private String loginType;

    public LoginResp(Activity ctx, Handler handler, Member user, String loginType) {
        super(ctx, handler);
        this.user = user;
        this.loginType = loginType;
        userParser = new UserInfoParser();
    }

    @Override
    public List<NameValuePair> getParameters() {
        List<NameValuePair> nvp = new ArrayList<NameValuePair>();
        nvp.add(new BasicNameValuePair("route", API.LOGIN_API));
        nvp.add(new BasicNameValuePair("token", ""));
        nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
        return nvp;
    }

    private String makeJsonText() {
        JSONObject member = new JSONObject();
        String loginName = "";
        try {
            if ("1".equals(loginType)) {
                loginName = user.getMobile();
            } else {
                loginName = user.getEmail();
            }
            member.put("loginName", loginName);
            member.put("password", user.getPassWord());
            member.put("type", loginType);
            member.put("code", MD5.MD5Encode(loginName + "siyo_imenu"));

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
        try {
            Member user = userParser.doParse(response);
            if (userParser.getResponseCode() == BaseParser.SUCCESS && user != null) {
                sendMessage(ON_SUCCESS, user);
            } else {
                sendMessage(ON_FAILURE, userParser.getResponseMessage());
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
