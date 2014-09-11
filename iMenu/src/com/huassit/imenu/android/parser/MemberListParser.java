package com.huassit.imenu.android.parser;

import com.huassit.imenu.android.model.Member;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sylar on 14-7-10.
 */
public class MemberListParser extends BaseParser<List<Member>> {

    @Override
    public List<Member> parseData(JSONObject jsonObject) throws JSONException {
        List<Member> menuObjectList = new ArrayList<Member>();
        JSONObject data = jsonObject.optJSONObject("data");
        if (data != null) {
            JSONArray memberList = data.optJSONArray("member_list");
            for (int i = 0; i < memberList.length(); i++) {
                JSONObject memberItem = memberList.optJSONObject(i);
                Member member = new Member().parse(memberItem);
                menuObjectList.add(member);
            }
        }
        return menuObjectList;
    }
}
