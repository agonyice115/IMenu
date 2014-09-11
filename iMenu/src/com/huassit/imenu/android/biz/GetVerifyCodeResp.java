package com.huassit.imenu.android.biz;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.os.Handler;
import com.huassit.imenu.android.MD5;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.http.BaseInvoker;
import com.huassit.imenu.android.model.API;
import com.huassit.imenu.android.model.AreaCode;
import com.huassit.imenu.android.model.Member;
import com.huassit.imenu.android.model.Verify;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.VerifyParser;


public class GetVerifyCodeResp extends BaseInvoker {

    private Member user;
    private AreaCode areaCode;
    private VerifyParser parser;
    private String validate_mobile;
    
    public GetVerifyCodeResp(Activity ctx, Handler handler, Member user, AreaCode areaCode,String validate_mobile) {
        super(ctx, handler);
        this.user = user;
        this.areaCode = areaCode;
        this.validate_mobile=validate_mobile;
        parser =new VerifyParser();
    }

    @Override
    protected boolean isServerHasSession() {
        return true;
    }

    @Override
    public List<NameValuePair> getParameters() {
        List<NameValuePair> nvp = new ArrayList<NameValuePair>();
        nvp.add(new BasicNameValuePair("route", API.GET_VERIFYCODE_API));
        nvp.add(new BasicNameValuePair("token", ""));
        nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
        return nvp;
    }

    private String makeJsonText() {
        JSONObject member1 = new JSONObject();
        try {
            member1.put("areaCode", areaCode.area_code_value);
            member1.put("mobile", user.getMobile());
            member1.put("code", MD5.MD5Encode(user.getMobile() + "siyo_imenu"));
            member1.put("validate_mobile", validate_mobile);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return member1.toString();
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
            Verify verify =parser.doParse(response);
            if (parser.getResponseCode() == BaseParser.SUCCESS) {
                if (verify != null) {
                    sendMessage(ON_SUCCESS, verify);
                } else {
                    sendMessage(ON_FAILURE, ctx.getString(R.string.data_error));
                }
            } else {
                sendMessage(ON_FAILURE, parser.getResponseMessage());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
