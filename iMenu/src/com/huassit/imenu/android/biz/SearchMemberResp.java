package com.huassit.imenu.android.biz;

import android.app.Activity;
import android.os.Handler;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.http.BaseInvoker;
import com.huassit.imenu.android.model.API;
import com.huassit.imenu.android.model.Member;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.MemberListParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sylar on 14-6-28.
 */
public class SearchMemberResp extends BaseInvoker {
    private final String token;
    private final String keyword;
    private final String memberId;
    private final String count;
    private final String page;

    public SearchMemberResp(Activity ctx, Handler handler, String token, String keyword, String memberId, String count, String page) {
        super(ctx, handler);
        this.token = token;
        this.keyword = keyword;
        this.memberId = memberId;
        this.count = count;
        this.page = page;
    }

    @Override
    public List<NameValuePair> getParameters() {
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("route", API.SEARCH_MEMBER));
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
            jsonObject.put("member_id", memberId);
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
        MemberListParser memberListParser = new MemberListParser();
        try {
            List<Member> memberList = memberListParser.doParse(response);
            if (memberListParser.getResponseCode() == BaseParser.SUCCESS && memberList != null && memberList.size() > 0) {
                sendMessage(ON_SUCCESS, memberList);
            } else {
                sendMessage(ON_FAILURE, ctx.getString(R.string.no_result_try_again));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            sendMessage(ON_FAILURE, ctx.getString(R.string.data_error));
        }
    }
}
