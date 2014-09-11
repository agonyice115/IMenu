package com.huassit.imenu.android.model;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Dynamic extends BaseModel implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String dynamicId;
    private String dynamicDate;
    private String title;
    private String people;
    private String distance;
    private String goodsCount;
    private String commentCount;
    private String dynamicType;
    private Member memberInfo;
    private Store storeInfo;
    private ArrayList<MemberMenu> menuList;

    public String getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(String dynamicId) {
        this.dynamicId = dynamicId;
    }

    public String getDynamicDate() {
        return dynamicDate;
    }

    public void setDynamicDate(String dynamicDate) {
        this.dynamicDate = dynamicDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(String goodsCount) {
        this.goodsCount = goodsCount;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getDynamicType() {
        return dynamicType;
    }

    public void setDynamicType(String dynamicType) {
        this.dynamicType = dynamicType;
    }

    public Member getMemberInfo() {
        return memberInfo;
    }

    public void setMemberInfo(Member memberInfo) {
        this.memberInfo = memberInfo;
    }

    public Store getStoreInfo() {
        return storeInfo;
    }

    public void setStoreInfo(Store storeInfo) {
        this.storeInfo = storeInfo;
    }

    public ArrayList<MemberMenu> getMenuList() {
        return menuList;
    }

    public void setMenuList(ArrayList<MemberMenu> menuList) {
        this.menuList = menuList;
    }

    public Dynamic parse(JSONObject jsonObject) {
        if (jsonObject != null) {
            setDynamicId(jsonObject.optString("dynamic_id"));
            setDynamicDate(jsonObject.optString("dynamic_date"));
            setTitle(jsonObject.optString("title"));
            setPeople(jsonObject.optString("people"));
            setDistance(jsonObject.optString("distance"));
            setGoodsCount(jsonObject.optString("goods_count"));
            setCommentCount(jsonObject.optString("comment_count"));
            setDynamicType(jsonObject.optString("dynamic_type"));
        }
        return this;
    }

}
