package com.huassit.imenu.android.model;

/**
 * Created by Sylar on 14-7-10.
 */
public class MemberDetail {

    public Member viewMember;
    public Order orderInfo;
    public Cart cartInfo;
    public int messageCount;
    public int score;
    /**
     * 动态数量
     */
    public int dynamicCount;
    /**
     * 1有新数据，0无
     */
    public int dynamicNew;
    /**
     * 未发布动态数量
     */
    public int dynamicUnpublishedCount;
}
