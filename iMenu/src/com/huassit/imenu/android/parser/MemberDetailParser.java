package com.huassit.imenu.android.parser;

import com.huassit.imenu.android.model.Cart;
import com.huassit.imenu.android.model.Member;
import com.huassit.imenu.android.model.MemberDetail;
import com.huassit.imenu.android.model.Order;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sylar on 14-7-10.
 */
public class MemberDetailParser extends BaseParser<MemberDetail> {

    @Override
    public MemberDetail parseData(JSONObject jsonObject) throws JSONException {
        JSONObject data = jsonObject.optJSONObject("data");
        MemberDetail memberDetail = null;
        if (data != null) {
            memberDetail = new MemberDetail();
            memberDetail.viewMember = new Member().parse(data.optJSONObject("view_member"));
            if(data.optJSONObject("order_info") != null){
            	memberDetail.orderInfo = new Order().parserOrder(data.optJSONObject("order_info"));
            }
            if(data.optJSONObject("cart_info") != null){
            	memberDetail.cartInfo = new Cart().parserCart(data.optJSONObject("cart_info"));
            }
            JSONObject dynamicInfo = data.optJSONObject("dynamic_info");
            if (dynamicInfo != null) {
                memberDetail.dynamicCount = dynamicInfo.optInt("dynamic_count");
                memberDetail.dynamicNew = dynamicInfo.optInt("dynamic_new");
                memberDetail.dynamicUnpublishedCount = dynamicInfo.optInt("dynamic_unpublished_count");
            }
            JSONObject message = data.optJSONObject("message_info");
            if (message != null) {
                memberDetail.messageCount = message.optInt("message_count");
            }
            JSONObject scoreInfo = data.optJSONObject("score_info");
            if (scoreInfo != null) {
                memberDetail.score = scoreInfo.optInt("score");
            }
        }
        return memberDetail;
    }
}
