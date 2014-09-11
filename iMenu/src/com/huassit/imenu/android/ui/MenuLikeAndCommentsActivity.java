package com.huassit.imenu.android.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.MyApplication;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.adapter.CommentAdapter;
import com.huassit.imenu.android.biz.EditMenuCommentResp;
import com.huassit.imenu.android.biz.EditMenuGoodsResp;
import com.huassit.imenu.android.biz.GetMenuCommentResp;
import com.huassit.imenu.android.biz.GetMenuGoodsResp;
import com.huassit.imenu.android.model.Comment;
import com.huassit.imenu.android.model.Goods;
import com.huassit.imenu.android.model.MemberGoods;
import com.huassit.imenu.android.model.MemberMenu;
import com.huassit.imenu.android.model.RMember;
import com.huassit.imenu.android.util.PreferencesUtils;
import com.huassit.imenu.android.util.ScreenUtils;
import com.huassit.imenu.android.util.StringUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MenuLikeAndCommentsActivity extends BaseActivity {

	private static final int EDIT_COMMENT_SUCCESS = 100;
	private static final int EDIT_COMMENT_FAILED = 101;
	private static final int EDIT_GOODS_SUCCESS = 300;
	private static final int EDIT_GOODS_FAILED = 301;
	private static final int GET_COMMENT_SUCCESS = 200;
	private static final int GET_COMMENT_FAILED = 201;
	private static final int GET_GOODS_SUCCESS = 400;
	private static final int GET_GOODS_FAILED = 401;
	private ImageView close;
	private TextView likeCount;
	private LinearLayout likeView;
	private ListView commentListView;
	private EditText et_comment;
	private Button send;
	private CommentAdapter commentAdapter;
	private MemberMenu memberMenu;
	private LinearLayout goodsBackground;
	private String goodsType = "0";
	private ImageLoader imageLoader;
	private String member_menu_image_goods_id;
	private Goods mygoods;
	private boolean isGood = false;
	private ArrayList<LinearLayout> llList = new ArrayList<LinearLayout>();
	private String memberId;
	private DisplayImageOptions options;
	private String sourceActivity;
	private ArrayList<Comment> commentList;
	/**全局*/
	private FrameLayout layout_menu_like_comments;
	/**背景图片*/
	private ImageView menu_img;

	@Override
	public int getContentView() {
		return R.layout.menu_like_and_comments;
	}

	@Override
	public void initView() {
		options = MyApplication.getDisplayImageOptions(context, 20);
		memberId = PreferencesUtils.getString(context, "member_id");
		imageLoader = ImageLoader.getInstance();
		goodsBackground = (LinearLayout) findViewById(R.id.goods_background);
		menu_img=(ImageView)findViewById(R.id.menu_img);
		layout_menu_like_comments=(FrameLayout)findViewById(R.id.layout_menu_like_comments);
		close = (ImageView) findViewById(R.id.close);
		likeCount = (TextView) findViewById(R.id.like_count);
		commentListView = (ListView) findViewById(R.id.comment_listview);
		et_comment = (EditText) findViewById(R.id.et_comment);
		send = (Button) findViewById(R.id.send);
		commentAdapter = new CommentAdapter(context,
				R.layout.menu_comment_list_item);
		commentListView.setAdapter(commentAdapter);
		likeView = (LinearLayout) findViewById(R.id.like_view);

		goodsBackground.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!StringUtils.isBlank(token)) {
					mygoods = new Goods();
					mygoods.member_id = PreferencesUtils.getString(context,
							"member_id");
					mygoods.icon_location = PreferencesUtils.getString(context,
							"iconLocation");
					mygoods.icon_name = PreferencesUtils.getString(context,
							"iconName");
					if (goodsType.equals("0")) {
						// 发布赞
						goodsType = "1";
						mygoods.member_menu_image_goods_id = "";
						v.setBackgroundColor(getResources().getColor(
								R.color.blue));
						EditMenuGoodsResp resp = new EditMenuGoodsResp(
								MenuLikeAndCommentsActivity.this, handler,
								memberMenu, mygoods, token, goodsType);
						resp.asyncInvoke(EDIT_GOODS_SUCCESS, EDIT_GOODS_FAILED);
					} else {
						// 删除赞
						goodsType = "0";
						mygoods.member_menu_image_goods_id = member_menu_image_goods_id;
						v.setBackgroundColor(getResources().getColor(
								R.color.black));
						EditMenuGoodsResp resp = new EditMenuGoodsResp(
								MenuLikeAndCommentsActivity.this, handler,
								memberMenu, mygoods, token, goodsType);
						resp.asyncInvoke(EDIT_GOODS_SUCCESS, EDIT_GOODS_FAILED);
					}
				} else {
					new AlertDialog.Builder(context)
							.setMessage(
									StringUtils.getString(
											MenuLikeAndCommentsActivity.this,
											R.string.login_to_continue))
							.setPositiveButton(
									StringUtils.getString(
											MenuLikeAndCommentsActivity.this,
											R.string.login_now),
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											Intent intent = new Intent(
													MenuLikeAndCommentsActivity.this,
													LoginActivity.class);
											intent.putExtra("sourceActivity",
													"MenuLikeAndComment");
											startActivity(intent);
										}
									})
							.setNegativeButton(
									StringUtils.getString(
											MenuLikeAndCommentsActivity.this,
											R.string.reg_new_member),
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											Intent intent = new Intent(
													MenuLikeAndCommentsActivity.this,
													RegisterActivity.class);
											intent.putExtra("sourceActivity",
													"MenuLikeAndComment");
											startActivity(intent);
										}
									}).create().show();
				}

			}
		});

		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String token = PreferencesUtils.getString(context, "token");
				if (!"".equals(token) && token != null) {
					Comment comment = new Comment();
					RMember r = new RMember();
					r.r_member_id = "";
					comment.r_member_info = r;
					comment.member_menu_image_comment_id = "";
					comment.member_id = PreferencesUtils.getString(context,
							"member_id");
					comment.comment_content = et_comment.getText().toString();
					EditMenuCommentResp resp = new EditMenuCommentResp(
							MenuLikeAndCommentsActivity.this, handler, comment,
							memberMenu, "1", token);
					resp.asyncInvoke(EDIT_COMMENT_SUCCESS, EDIT_COMMENT_FAILED);
				} else {
					new AlertDialog.Builder(context)
							.setMessage(
									StringUtils.getString(
											MenuLikeAndCommentsActivity.this,
											R.string.login_to_continue))
							.setPositiveButton(
									StringUtils.getString(
											MenuLikeAndCommentsActivity.this,
											R.string.login_now),
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											Intent intent = new Intent(
													MenuLikeAndCommentsActivity.this,
													LoginActivity.class);
											intent.putExtra("sourceActivity",
													"MenuLikeAndComment");
											startActivity(intent);

										}
									})
							.setNegativeButton(
									StringUtils.getString(
											MenuLikeAndCommentsActivity.this,
											R.string.reg_new_member),
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											Intent intent = new Intent(
													MenuLikeAndCommentsActivity.this,
													RegisterActivity.class);
											intent.putExtra("sourceActivity",
													"MenuLikeAndComment");
											startActivity(intent);
										}
									}).create().show();
				}
				et_comment.getText().clear();
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

			}
		});
	}

	Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case EDIT_COMMENT_FAILED:
				prompt(StringUtils.getString(MenuLikeAndCommentsActivity.this,
						R.string.conmment_false));
				break;
			case EDIT_COMMENT_SUCCESS:
				String member_menu_image_comment_id = (String) msg.obj;
				GetMenuCommentResp resp = new GetMenuCommentResp(
						MenuLikeAndCommentsActivity.this, handler, memberMenu,
						String.valueOf(Integer.MAX_VALUE), "");
				resp.asyncInvoke(GET_COMMENT_SUCCESS, GET_COMMENT_FAILED);
				break;
			case GET_COMMENT_FAILED:
				break;
			case GET_COMMENT_SUCCESS:
				final Map<String, Object> commentMap = (Map<String, Object>) msg.obj;
				commentList = (ArrayList<Comment>) commentMap
						.get("comment_list");
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						commentAdapter.getDataList().clear();
						commentAdapter.getDataList().addAll(commentList);
						commentAdapter.notifyDataSetChanged();
						commentListView.setSelection(commentListView
								.getBottom());
					}
				});
				break;
			case GET_GOODS_SUCCESS:
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (HashMap<String, Object>) msg.obj;
				String goodsCount = (String) map.get("goodsCount");
				@SuppressWarnings("unchecked")
				ArrayList<Goods> goodsList = (ArrayList<Goods>) map
						.get("goodsList");
				MemberGoods memberGoods = (MemberGoods) map.get("memberGoods");
				likeCount.setText(goodsCount);
				if (memberGoods.goods_status.equals("1")) {
					// 已赞
					member_menu_image_goods_id = memberGoods.member_menu_image_goods_id;
					goodsType = "1";
					goodsBackground.setBackgroundColor(getResources().getColor(
							R.color.blue));
				} else {
					// 未赞
					goodsType = "0";
					goodsBackground.setBackgroundColor(getResources().getColor(
							R.color.black));
				}
				likeView.removeAllViews();
				for (Goods goods : goodsList) {
					if (goods.member_menu_image_goods_id
							.equals(memberGoods.member_menu_image_goods_id)) {
						isGood = true;
					}
					likeView.addView(getImageView(goods, 0));
				}
				break;
			case GET_GOODS_FAILED:
				prompt(StringUtils.getString(MenuLikeAndCommentsActivity.this,
						R.string.goods_list_loading_false));
				break;
			case EDIT_GOODS_SUCCESS:
				member_menu_image_goods_id = (String) msg.obj;
				if (goodsType.equals("1")) {
					// 发布赞
					goodsType = "1";
					goodsBackground.setBackgroundColor(getResources().getColor(
							R.color.blue));
				} else {
					// 删除赞
					goodsType = "0";
					goodsBackground.setBackgroundColor(getResources().getColor(
							R.color.black));
				}

				GetMenuGoodsResp goodsResp = new GetMenuGoodsResp(
						MenuLikeAndCommentsActivity.this, handler, memberId,
						memberMenu, "10", "", token);
				goodsResp.asyncInvoke(GET_GOODS_SUCCESS, GET_GOODS_FAILED);
				break;
			case EDIT_GOODS_FAILED:
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void initData() {
		memberMenu = (MemberMenu) getIntent()
				.getSerializableExtra("memberMenu");
		ScreenUtils.ScreenResolution screenResolution = ScreenUtils
				.getScreenResolution(this);
		FrameLayout.LayoutParams newLayoutParams = new FrameLayout.LayoutParams(
				screenResolution.getWidth(), screenResolution.getWidth());
		newLayoutParams.setMargins(0, screenResolution.getHeight()/6, 0, 0);
		menu_img.setLayoutParams(newLayoutParams);
		imageLoader.displayImage(memberMenu.image_location+memberMenu.image_name, menu_img);
		
		GetMenuCommentResp commentResp = new GetMenuCommentResp(
				MenuLikeAndCommentsActivity.this, handler, memberMenu,
				Integer.MAX_VALUE + "", "");
		commentResp.asyncInvoke(GET_COMMENT_SUCCESS, GET_COMMENT_FAILED);
		GetMenuGoodsResp goodsResp = new GetMenuGoodsResp(
				MenuLikeAndCommentsActivity.this, handler, memberId,
				memberMenu, "10", "", token);
		goodsResp.asyncInvoke(GET_GOODS_SUCCESS, GET_GOODS_FAILED);

	}

	private View getImageView(Goods goods, int code) {

//		ImageView imageView = new ImageView(this);
//		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ScreenUtils
//				.dpToPxInt(context, 30), ScreenUtils.dpToPxInt(context, 30));
//		params.setMargins(5, 10, 5, 5);
//		imageView.setLayoutParams(params);
		
		
		LinearLayout layout = new LinearLayout(getApplicationContext());
		
		layout.setLayoutParams(new LayoutParams(70, 70));
		
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ScreenUtils
				.dpToPxInt(context, 40), ScreenUtils.dpToPxInt(context, 40));
		
		ImageView imageView = new ImageView(this);
		params.setMargins(5, 5, 5, 5);
		imageView.setLayoutParams(params);
		imageView.setBackgroundResource(R.drawable.take_photo_up);
		imageView.setPadding(2, 2, 2, 2);
//		imageView.setLayoutParams(new LayoutParams(60, 60));
//		imageView.setScaleType(ImageView.ScaleType.CENTER);
		if(StringUtils.isBlank(goods.icon_location + goods.icon_name)){
			imageView.setImageResource(R.drawable.defult_headimg_small);
		}else{
			imageLoader.displayImage(goods.icon_location + goods.icon_name,
					imageView, options);
		}
		layout.addView(imageView);
		llList.add(layout);
		if (code == 1 || isGood == true) {
			// 我点的赞
			layout.setTag("myGood");
			isGood = false;
		}

		return layout;
	}
}
