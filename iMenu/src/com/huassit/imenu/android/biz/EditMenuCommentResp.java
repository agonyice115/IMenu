package com.huassit.imenu.android.biz;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Handler;
import android.widget.Toast;

import com.huassit.imenu.android.R;
import com.huassit.imenu.android.http.BaseInvoker;
import com.huassit.imenu.android.model.API;
import com.huassit.imenu.android.model.Comment;
import com.huassit.imenu.android.model.MemberMenu;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.DefaultParser;

public class EditMenuCommentResp extends BaseInvoker {

	private Comment comment;
	private MemberMenu memberMenu;
	private String commentType;
	private String token;

	public EditMenuCommentResp(Activity ctx, Handler handler, Comment comment,
			MemberMenu memberMenu, String commentType,String token) {
		super(ctx, handler);
		this.comment = comment;
		this.memberMenu = memberMenu;
		this.commentType = commentType;
		this.token =token;
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.EDIT_MENU_COMMENT));
		nvp.add(new BasicNameValuePair("token", token));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}

	private String makeJsonText() {
		JSONObject json = new JSONObject();
		try {
			json.put("member_id", comment.member_id);
			json.put("comment_content", comment.comment_content);
			json.put("member_menu_image_comment_id",comment.member_menu_image_comment_id);
			json.put("r_member_id", comment.r_member_info.r_member_id);
			json.put("member_menu_image_id", memberMenu.member_menu_image_id);
			json.put("comment_type", commentType);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	@Override
	public String getRequestUrl() {
		return API.server;
	}

	@Override
	public HTTP_METHOD getRequestMethod() {
		return HTTP_METHOD.METHOD_POST;
	}

	@Override
	protected void handleResponse(String response) {
		
		DefaultParser parser =new DefaultParser();
		try {
			parser.setKey("member_menu_image_comment_id");
			String member_menu_image_comment_id =(String) parser.doParse(response);
			 if (parser.getResponseCode() == BaseParser.SUCCESS) {
	                if (member_menu_image_comment_id != null) {
	                    sendMessage(ON_SUCCESS, member_menu_image_comment_id);
	                } else {
	                    sendMessage(ON_FAILURE, ctx.getString(R.string.data_error));
	                }
	            } else {
	                sendMessage(ON_FAILURE, parser.getResponseMessage());
	            }
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
