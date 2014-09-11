package com.huassit.imenu.android.model;

import org.json.JSONObject;

import java.io.Serializable;

public class Member extends BaseModel implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public String memberId;
    public String memberName;
    public String email;
    public String areaCode;
    public String mobile;
    public String iconName;
    public String iconLocation;
    public String iconDate;
    public String dynamicName;
    public String dynamicLocation;
    public String dynamicDate;
    public String regionId;
    public String status;
    public String token;
    public String realname;
    public String birthday;
    public String signature;
    public String passWord;
    public String sex;
    public String vipLevel;
    public String followingCount;
    public String followedCount;
    public String followStatus;
    public int score;


    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public String getIconLocation() {
        return iconLocation;
    }

    public void setIconLocation(String iconLocation) {
        this.iconLocation = iconLocation;
    }

    public String getIconDate() {
        return iconDate;
    }

    public void setIconDate(String iconDate) {
        this.iconDate = iconDate;
    }

    public String getDynamicName() {
        return dynamicName;
    }

    public void setDynamicName(String dynamicName) {
        this.dynamicName = dynamicName;
    }

    public String getDynamicLocation() {
        return dynamicLocation;
    }

    public void setDynamicLocation(String dynamicLocation) {
        this.dynamicLocation = dynamicLocation;
    }

    public String getDynamicDate() {
        return dynamicDate;
    }

    public void setDynamicDate(String dynamicDate) {
        this.dynamicDate = dynamicDate;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(String vipLevel) {
        this.vipLevel = vipLevel;
    }

    public String getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(String followingCount) {
        this.followingCount = followingCount;
    }

    public String getFollowedCount() {
        return followedCount;
    }

    public void setFollowedCount(String followedCount) {
        this.followedCount = followedCount;
    }

    public String getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(String followStatus) {
        this.followStatus = followStatus;
    }

    public Member() {
    }

    public Member(String mobile) {
        this.mobile = mobile;
    }

    public Member(String mobile, String password, String memberName, String sex) {
        this.memberName = memberName;
        this.mobile = mobile;
        this.passWord = password;
        this.sex = sex;
    }

    @Override
    public Member parse(JSONObject jsonObject) {
        if (jsonObject != null) {
            memberId = jsonObject.optString("member_id");
            memberName = jsonObject.optString("member_name");
            if (jsonObject.has("icon_name")) {
                iconName = jsonObject.optString("icon_name");
            } else {
                iconName = jsonObject.optString("iconName");
            }

            if (jsonObject.has("icon_date")) {
                iconDate = jsonObject.optString("icon_date");
            } else {
                iconDate = jsonObject.optString("iconDate");
            }

            if (jsonObject.has("icon_location")) {
                iconLocation = jsonObject.optString("icon_location");
            } else {
                iconLocation = jsonObject.optString("iconLocation");
            }

            vipLevel = jsonObject.optString("vip_level");
            sex = jsonObject.optString("sex");
            followingCount = jsonObject.optString("following_count");
            followedCount = jsonObject.optString("followed_count");
            followStatus = jsonObject.optString("follow_status");
            signature = jsonObject.optString("signature");
            score = jsonObject.optInt("score");
            dynamicDate = jsonObject.optString("dynamic_date");
            dynamicName = jsonObject.optString("dynamic_name");
            dynamicLocation = jsonObject.optString("dynamic_location");
            email = jsonObject.optString("email");
            areaCode = jsonObject.optString("areaCode");
            mobile = jsonObject.optString("mobile");
            regionId = jsonObject.optString("regionId");
            status = jsonObject.optString("status");
            token = jsonObject.optString("token");
            realname = jsonObject.optString("realname");
            birthday = jsonObject.optString("birthday");
            passWord = jsonObject.optString("password");
        }
        return this;
    }
}
