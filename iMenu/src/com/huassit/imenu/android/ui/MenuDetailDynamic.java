package com.huassit.imenu.android.ui;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.biz.GetMenuCommentResp;
import com.huassit.imenu.android.biz.GetMenuGoodsResp;
import com.huassit.imenu.android.model.Dynamic;
import com.huassit.imenu.android.model.Member;
import com.huassit.imenu.android.model.MemberMenu;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.ScreenUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 菜品详情页面（包括赞以及评论）
 * 
 * @author pxx
 * 
 */
public class MenuDetailDynamic extends BaseActivity {

	/** 获取评论成功 */
	private static final int GET_COMMENT_SUCCESS = 200;
	/** 获取评论失败 */
	private static final int GET_COMMENT_FAILED = 201;
	/** 获取赞成功 */
	private static final int GET_GOODS_SUCCESS = 400;
	/** 获取赞失败 */
	private static final int GET_GOODS_FAILED = 401;
	private ImageView close;
	private ImageView iv_member;
	private TextView memberName;
	private TextView time;
	private ImageView iv_menu;
	private TextView dynamic;
	private TextView likeCount;
	private TextView commentCount;
	private RelativeLayout commentAndLike;
	private ImageLoader imageLoader;
	private MemberMenu memberMenu;
	private Member viewMember;
	private String sourceActivity;
	private ImageView dynamicImg;
	private DisplayImageOptions options;
	private String memberId;
	/** 动态跳转layout */
	private LinearLayout layout_dynamic;

	@Override
	public int getContentView() {
		return R.layout.menu_detail_dynamic;
	}

	@Override
	public void initView() {
		
		memberId = PreferencesUtils.getString(context, "member_id");
		imageLoader = ImageLoader.getInstance();
		options = MyApplication.getDisplayImageOptions(context, 20);
		commentAndLike = (RelativeLayout) findViewById(R.id.comment_like);
		close = (ImageView) findViewById(R.id.close);
		iv_member = (ImageView) findViewById(R.id.iv_member);
		memberName = (TextView) findViewById(R.id.member_name);
		time = (TextView) findViewById(R.id.time);
		iv_menu = (ImageView) findViewById(R.id.iv_menu);
		dynamic = (TextView) findViewById(R.id.dynamic);
		layout_dynamic = (LinearLayout) findViewById(R.id.layout_dynamic);
		likeCount = (TextView) findViewById(R.id.like_count);
		commentCount = (TextView) findViewById(R.id.comment_count);
		dynamicImg = (ImageView) findViewById(R.id.dynamic_img);
		ScreenUtils.ScreenResolution screenResolution = ScreenUtils
				.getScreenResolution(this);
		LinearLayout.LayoutParams newLayoutParams = new LinearLayout.LayoutParams(
				screenResolution.getWidth(), screenResolution.getWidth());
		iv_menu.setLayoutParams(newLayoutParams);
	}

	@Override
	public void initData() {
		sourceActivity = getIntent().getStringExtra("sourceActivity");
		memberMenu = (MemberMenu) getIntent()
				.getSerializableExtra("memberMenu");
		if ("MenuDetail".equals(sourceActivity)) {
			Member member = memberMenu.member_info;
			imageLoader.displayImage(member.iconLocation + member.iconName,
					iv_member, options);
			memberName.setText(member.memberName);
			time.setText(memberMenu.member_menu_image_date.substring(0, 16));
			layout_dynamic.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 跳转至此动态详情
					Intent intent = new Intent(MenuDetailDynamic.this,
							DynamicDetailActivity.class);
					intent.putExtra("dynamic", pottingDynamic());
					startActivity(intent);
				}
			});
		} else if ("DynamicDetail".equals(sourceActivity)) {
			viewMember = (Member) getIntent().getSerializableExtra(
					"view_member");
			dynamic.setText(memberMenu.menu_name);
			imageLoader.displayImage(viewMember.iconLocation
					+ viewMember.iconName, iv_member, options);
			memberName.setText(viewMember.memberName);
			time.setVisibility(View.GONE);
			dynamicImg.setVisibility(View.GONE);
		}
		imageLoader.displayImage(memberMenu.image_location
				+ memberMenu.image_name, iv_menu);
		likeCount.setText(memberMenu.goods_count);
		commentCount.setText(memberMenu.comment_count);

		commentAndLike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 显示评论和赞
				Intent intent = new Intent(MenuDetailDynamic.this,
						MenuLikeAndCommentsActivity.class);
				if ("MenuDetail".equals(sourceActivity)) {
					intent.putExtra("sourceActivity", "MenuDetail");
				} else if ("DynamicDetail".equals(sourceActivity)) {
					intent.putExtra("sourceActivity", "DynamicDetail");
				}
				intent.putExtra("memberMenu", memberMenu);
				startActivity(intent);
			}
		});
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 更新评论以及赞的数目
		GetMenuGoodsResp getMenuGoodsResp = new GetMenuGoodsResp(
				MenuDetailDynamic.this, handler, memberId, memberMenu, "10",
				"", token);
		getMenuGoodsResp.asyncInvoke(GET_GOODS_SUCCESS, GET_GOODS_FAILED);

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GET_GOODS_SUCCESS:
				@SuppressWarnings("unchecked")
				Map<String, Object> goodsMap = (HashMap<String, Object>) msg.obj;
				String goodsCountStr = (String) goodsMap.get("goodsCount");
				likeCount.setText(goodsCountStr);
				GetMenuCommentResp getMenuCommentResp = new GetMenuCommentResp(
						MenuDetailDynamic.this, handler, memberMenu, "10", "");
				getMenuCommentResp.asyncInvoke(GET_COMMENT_SUCCESS,
						GET_COMMENT_FAILED);
				break;
			case GET_GOODS_FAILED:
				prompt(StringUtils.getString(MenuDetailDynamic.this,
						R.string.get_goods_false));
				break;
			case GET_COMMENT_SUCCESS:
				Map<String, Object> map = (HashMap<String, Object>) msg.obj;
				String commentCountStr = (String) map.get("comment_count");
				commentCount.setText(commentCountStr);
				break;
			case GET_COMMENT_FAILED:
				prompt(StringUtils.getString(MenuDetailDynamic.this,
						R.string.get_comment_false));
				break;
			default:
				break;
			}
		};
	};

	/** 封装跳转至动态详情的动态实体类 */
	private Dynamic pottingDynamic() {
		Dynamic mDynamic = new Dynamic();
		mDynamic.setDynamicId(memberMenu.dynamic_id);
		Member memberInfo = new Member();
		memberInfo.memberId = memberId;
		memberInfo.token = token;
		mDynamic.setMemberInfo(memberInfo);
		return mDynamic;
	}
}
