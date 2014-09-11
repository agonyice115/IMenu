package com.huassit.imenu.android.model;

import org.json.JSONObject;

public class Share extends BaseModel<Share> {

    public String title;
    /**
     * 分享的图片路径
     */
    public String description;
    public String content;
    public String link;
    /**
     * 分享图片本地地址
     */
    public String imagePath;
    /**
     * 图片链接
     */
    public String imageUrl;

    @Override
    public Share parse(JSONObject jsonObject) {
        if (jsonObject != null) {
            title = jsonObject.optString("title");
            description = jsonObject.optString("description");
            content = jsonObject.optString("content");
            link = jsonObject.optString("link");
        }
        return this;
    }
}
