package com.huassit.imenu.android.http;

import java.io.IOException;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.service.MessageService;
import com.huassit.imenu.android.ui.LoginActivity;
import com.huassit.imenu.android.ui.MainActivity;
import com.huassit.imenu.android.util.ActivityStackManager;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.StringUtils;

public abstract class BaseInvoker extends Thread {

    /**
     * 默认请求成功后的标识 *
     */
    public static final int ON_REQUEST_SUCCESS = 1000;

    /**
     * 默认请求失败后的标识 *
     */
    public static final int ON_REQUEST_FAILURE = 1001;

    /**
     * http 請求的方法枚舉 *
     */
    public static enum HTTP_METHOD {
        METHOD_POST, METHOD_GET, METHOD_UPLOAD_IMAGE
    }

    /**
     * 上下文 *
     */
    protected Context ctx;

    /**
     * 接受异步请求结果的handler *
     */
    private Handler handler;

    /**
     * 成功标识 *
     */
    protected int ON_SUCCESS = ON_REQUEST_SUCCESS;
    /**
     * 失败标识 *
     */
    protected int ON_FAILURE = ON_REQUEST_FAILURE;

    /**
     * 请求时的参数列表 *
     */
    public abstract List<NameValuePair> getParameters();

    /**
     * 請求的API地址 *
     */
    public abstract String getRequestUrl();

    /**
     * 獲取請求的方式 *
     */
    public abstract HTTP_METHOD getRequestMethod();

    /**
     * 获取上传的文件*
     */
    public NameValuePair getUploadFiles() {
        return null;
    }

    protected boolean isServerHasSession() {
        return false;
    }

    private HttpInvoker httpInvoker;

    public BaseInvoker(Context ctx, Handler handler) {
        this.ctx = ctx;
        this.handler = handler;
        httpInvoker = new HttpInvoker();
    }

    public void cancel() {
        handler = null;
        httpInvoker.close();
    }

    public void sendMessage(int what, Object obj) {
        if (handler != null)
            handler.sendMessage(handler.obtainMessage(what, obj));
    }

    @Override
    public void run() {
        execute();
    }

    private void execute() {
        try {
            String result = "";
            if (getRequestMethod() == HTTP_METHOD.METHOD_GET) {
                result = httpInvoker.parseResponse(httpInvoker.doHttpGet(getRequestUrl()));
            } else if (getRequestMethod() == HTTP_METHOD.METHOD_POST) {
                Log.e("BaseInvoker", getParameters().toString());
                if (isServerHasSession()) {
                    if (MyApplication.httpClient != null) {
                        httpInvoker.setHttpClient(MyApplication.httpClient);
                    }
                    httpInvoker.setHttpPost(MyApplication.httpPost);
                }
                result = httpInvoker.parseResponse(httpInvoker.doHttpPost(getRequestUrl(), getParameters()));
                if (isServerHasSession()) {
                    MyApplication.httpClient = httpInvoker.getHttpClient();
                    MyApplication.httpPost = httpInvoker.getHttpPost();
                } else {
                    MyApplication.httpClient = null;
                    MyApplication.httpPost = null;
                }
                Log.e("BaseInvoker", result);
            } else if (getRequestMethod() == HTTP_METHOD.METHOD_UPLOAD_IMAGE) {
                result = httpInvoker.parseResponse(httpInvoker.doImageUpload(getRequestUrl(), getParameters(), getUploadFiles()));
            }

            if (result.equals("")) {
                if (handler != null)
                    handler.sendMessage(handler.obtainMessage(ON_FAILURE, "response null"));
            } else {
            	try {
					JSONObject jsonObject = new JSONObject(result);
					if (!StringUtils.isBlank(jsonObject.optString("error"))){
						if (jsonObject.optString("error").equals("12|错误：非法TOKEN！")) {
							PreferencesUtils.putString(ctx, "token", null);
							ctx.stopService(new Intent(ctx, MessageService.class));
							if(ctx instanceof Activity){
								((Activity)ctx).runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										((BaseActivity)ctx).showTokenDialog((Activity)ctx, "登录已过期，请重新登录", new DialogInterface.OnClickListener() {
											
											@Override
											public void onClick(DialogInterface dialog, int which) {
												//确定
												Intent intent = new Intent(ctx, LoginActivity.class);
												ctx.startActivity(intent);
												
											}
										});
										
									}
								});
							}
					}else{
						handleResponse(result);
					}
				}else{
					handleResponse(result);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
          }
        } catch (IOException e) {
            e.printStackTrace();
            if (handler != null)
                handler.sendMessage(handler.obtainMessage(ON_FAILURE,
                        e.getMessage()));
        }
    }

    protected abstract void handleResponse(String response);

    /**
     * @param successCode 请求成功后的标识
     * @param failCode    失败后的标识
     * @Desc 异步调用, 请求时输入参数在handler的what中用来区分请求结果
     * *
     */
    synchronized public void asyncInvoke(int successCode, int failCode) {
        ON_SUCCESS = successCode;
        ON_FAILURE = failCode;
        super.start();
    }

    synchronized public void asyncInvoke() {
        super.start();
    }

    synchronized public void syncInvoke() {
        execute();
    }
}
