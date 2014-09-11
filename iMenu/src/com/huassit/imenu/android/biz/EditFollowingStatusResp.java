package com.huassit.imenu.android.biz;

import android.app.Activity;
import android.os.Handler;
import com.huassit.imenu.android.http.BaseInvoker;
import com.huassit.imenu.android.model.API;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sylar on 14-6-28.
 */
public class EditFollowingStatusResp extends BaseInvoker {
    private final String token;
    private final String memberId;
    private final String followingMemberId;
    private final String followingStoreId;
    private final String followingStatus;

    public EditFollowingStatusResp(Activity ctx, Handler handler, String token, String memberId, String followingMemberId, String followingStoreId, String followingStatus) {
        super(ctx, handler);
        this.token = token;
        this.memberId = memberId;
        this.followingMemberId = followingMemberId;
        this.followingStoreId = followingStoreId;
        this.followingStatus = followingStatus;
    }


    @Override
    public List<NameValuePair> getParameters() {
        List<NameValuePair> nvp = new ArrayList<NameValuePair>();
        nvp.add(new BasicNameValuePair("route", API.CHECK_CLIENT_CONFIG));
        nvp.add(new BasicNameValuePair("token", token));
        nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
        return nvp;
    }

    private String makeJsonText() {
        JSONObject json = new JSONObject();
        try {
            json.put("region", memberId);
            json.put("category", followingMemberId);
            json.put("environment", followingStoreId);
            json.put("service", followingStatus);
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

    }
}
