package com.huassit.imenu.android.parser;

import com.huassit.imenu.android.model.Comment;
import com.huassit.imenu.android.model.RMember;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DynamicCommentParser extends BaseParser<ArrayList<Comment>> {
    @Override
    public ArrayList<Comment> parseData(JSONObject jsonObject) throws JSONException {
        ArrayList<Comment> commentList = null;
        if (!jsonObject.isNull("data")) {
            commentList = new ArrayList<Comment>();
            JSONArray jsonData = jsonObject.optJSONArray("data");
            for (int i = 0; i < jsonData.length(); i++) {
                JSONObject json_Object = jsonData.optJSONObject(i);
                JSONArray json_comment_list = json_Object.optJSONArray("comment_list");
                for (int j = 0; j < json_comment_list.length(); j++) {
                    JSONObject json_comment = json_comment_list.optJSONObject(j);
                    Comment comment = new Comment();
                    comment.dynamic_comment_id = json_comment.optString("dynamic_comment_id");
                    comment.member_id = json_comment.optString("member_id");
                    comment.member_name = json_comment.optString("member_name");
                    comment.vip_level = json_comment.optString("vip_level");
                    comment.icon_name = json_comment.optString("iconName");
                    comment.icon_location = json_comment.optString("iconLocation");
                    comment.icon_date = json_comment.optString("iconDate");
                    comment.comment_content = json_comment.optString("comment_content");
                    comment.comment_date = json_comment.optString("comment_date");
                    if (json_comment.optJSONObject("r_member_info") != null) {
                        JSONObject json_r_member = json_comment.optJSONObject("r_member_info");
                        RMember rMember = new RMember();
                        rMember.r_member_id = json_r_member.optString("r_member_id");
                        rMember.r_member_name = json_r_member.optString("r_member_name");
                    }
                    commentList.add(comment);
                }
            }
        }
        return commentList;
    }

}
