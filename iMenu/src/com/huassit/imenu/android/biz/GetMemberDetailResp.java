package com.huassit.imenu.android.biz;

import android.app.Activity;
import android.os.Handler;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.http.BaseInvoker;
import com.huassit.imenu.android.model.API;
import com.huassit.imenu.android.model.MemberDetail;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.MemberDetailParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sylar on 14-7-10.
 */
public class GetMemberDetailResp extends BaseInvoker {

    private final String memberId;
    private final String viewMemberId;
    private final long lastDate;
    private final String token;
    private MemberDetailParser parser;

    public GetMemberDetailResp(Activity ctx, Handler handler, String memberId, String viewMemberId, long lastDate, String token) {
        super(ctx, handler);
        this.memberId = memberId;
        this.viewMemberId = viewMemberId;
        this.lastDate = lastDate;
        parser = new MemberDetailParser();
        this.token = token;
    }

    @Override
    public List<NameValuePair> getParameters() {
        List<NameValuePair> nvp = new ArrayList<NameValuePair>();
        nvp.add(new BasicNameValuePair("route", API.MEMBER_DETAIL));
        nvp.add(new BasicNameValuePair("token", token));
        nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
        return nvp;
    }

    private String makeJsonText() {
        JSONObject json = new JSONObject();
        try {
            json.put("member_id", memberId);
            json.put("view_member_id", viewMemberId);
            json.put("last_date", lastDate);
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
            MemberDetail memberDetail = parser.doParse(response);
            if (parser.getResponseCode() == BaseParser.SUCCESS) {
                if (memberDetail != null) {
                    sendMessage(ON_SUCCESS, memberDetail);
                } else {
                    sendMessage(ON_FAILURE, ctx.getString(R.string.data_error));
                }
            } else {
                sendMessage(ON_FAILURE, parser.getResponseMessage());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            sendMessage(ON_FAILURE, ctx.getString(R.string.data_error));
        }
    }
}
