package com.huassit.imenu.android.http;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;


public class HttpInvoker {

    //链接超时15秒
    private static final int CONNECTION_TIMEOUT = 15 * 1000;

    //链接超时15秒
    private static final int SO_TIMEOUT = 15 * 1000;

    private DefaultHttpClient httpClient = null;

    private HttpPost httpPost;

    public HttpInvoker() {
        httpClient = createHttpClient();
    }


    public void close() {
        if (httpClient != null) {
            httpClient.getConnectionManager().closeExpiredConnections();
            httpClient.getConnectionManager().shutdown();
        }
    }


    public HttpResponse doHttpPost(String url, List<NameValuePair> params) throws IOException {
        HttpPost httpPost = createHttpPost(url);
        if (params != null) {
            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        }
        return executeHttpRequest(httpPost);
    }

    public HttpResponse doImageUpload(String url, List<NameValuePair> params, NameValuePair fileParams) throws IOException {
        HttpPost httpPost = createHttpPost(url);
        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        if (params != null) {
            for (NameValuePair p : params) {
                entity.addPart(p.getName(), new StringBody(p.getValue(), Charset.forName(HTTP.UTF_8)));
            }
        }

        if (fileParams != null) {
            File file = new File(fileParams.getValue());
            if (file.exists()) {
                entity.addPart(fileParams.getName(), new InputStreamBody(new FileInputStream(fileParams.getValue()), "image/jpeg", file.getName()));
            }
        }
        httpPost.setEntity(entity);
        return executeHttpRequest(httpPost);
    }


    public HttpResponse doHttpGet(String url) throws IOException {
        return executeHttpRequest(createHttpGet(url));
    }

    public String parseResponse(HttpResponse resp) throws ParseException, IOException {
        if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            return EntityUtils.toString(resp.getEntity(), HTTP.UTF_8);
        } else {
            resp.getEntity().consumeContent();
            return "";
        }
    }

    public HttpResponse executeHttpRequest(HttpRequestBase httpRequest) throws IOException {
        HttpResponse hr = null;
        if (httpClient != null) {
            httpClient.getConnectionManager().closeExpiredConnections();
            hr = httpClient.execute(httpRequest);
        }
        return hr;
    }

    private HttpGet createHttpGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        return httpGet;
    }


    private HttpPost createHttpPost(String url) {
        if (httpPost == null) {
            httpPost = new HttpPost();
        }
        try {
            httpPost.setURI(new URI(url));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return httpPost;
    }

    public void setHttpPost(HttpPost post) {
        this.httpPost = post;
    }

    public HttpPost getHttpPost() {
        return httpPost;
    }

    public DefaultHttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(DefaultHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    private static DefaultHttpClient createHttpClient() {
        final SchemeRegistry supportedScheme = new SchemeRegistry();
        final PlainSocketFactory sf = PlainSocketFactory.getSocketFactory();
        supportedScheme.register(new Scheme("http", sf, 80));
        supportedScheme.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        final HttpParams httpParams = createHttpParams();
        HttpClientParams.setRedirecting(httpParams, false);
        final ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParams, supportedScheme);
        return new DefaultHttpClient(ccm, httpParams);
    }

    private static HttpParams createHttpParams() {
        final HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setStaleCheckingEnabled(params, false);
        HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, SO_TIMEOUT);
        HttpConnectionParams.setSocketBufferSize(params, 2048);
        return params;
    }
}
