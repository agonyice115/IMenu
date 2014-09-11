package com.huassit.imenu.android.model;

import java.io.Serializable;
import org.json.JSONObject;

/**
 * 动态消息列表实体类
 * 
 * @author 上官明月
 * 
 */
@SuppressWarnings("rawtypes")
public class DynamicMessageList extends BaseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 动态消息ID */
	public String dynamic_message_to_member_id;
	/** 消息内容 */
	public String message_content;
	/** 消息类型(1-动态赞/2-动态评论/3-图片赞/4-图片评论) */
	public String message_type;
	/** 动态ID */
	public String dynamic_id;
	/** 会员上传图片ID */
	public String member_menu_image_id;
	/** 图片路径 */
	public String image_location;
	/** 图片路径名 */
	public String image_name;
	/** 1-已读/0-未读 */
	public String is_open;
	/** 消息时间 */
	public String date_added;
	/** 用户信息 */
	public Member member_info;
	/** 未读消息总数 */
	public String unread_count;
	/** 轮询结果实体类 */
	public HaveNews new_info;

	public DynamicMessageList parse(JSONObject jsonObject) {
		if (jsonObject != null) {
			dynamic_message_to_member_id = jsonObject
					.optString("dynamic_message_to_member_id");
			message_content = jsonObject.optString("message_content");
			message_type = jsonObject.optString("message_type");
			dynamic_id = jsonObject.optString("dynamic_id");
			member_menu_image_id = jsonObject.optString("member_menu_image_id");
			image_location = jsonObject.optString("image_location");
			image_name = jsonObject.optString("image_name");
			is_open = jsonObject.optString("is_open");
			date_added = jsonObject.optString("date_added");
			JSONObject obj = jsonObject.optJSONObject("member_info");
			Member member = new Member();
			member.memberId = obj.optString("member_id");
			member.memberName = obj.optString("memberName");
			member.iconName = obj.optString("iconName");
			member.iconLocation = obj.optString("iconLocation");
			member.iconDate = obj.optString("iconDate");
			member_info = member;
		}
		return this;
	}
}
