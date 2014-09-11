package com.huassit.imenu.android.model;

public class API {
	/** 服务器路径 */
	public static String server = "http://218.244.158.175/imenu_test/app_interface/index.php";
	/** 连接超时 */
	public static final String TIMEOUT = "Connection to " + server
			+ " timed out";
	/** 连接失败 */
	public static final String REFUSED = "Connection to " + server + " refused";
	/** 判断动态有新消息是，红点是否显示的标石 */
	public static boolean isHave = false;
	/** 存储系统消息数量 */
	public static int systemMessageCount = 0;
	/** 检查客户端 */
	public static final String CHECK_CLIENT_CONFIG = "setting/client_config/checkClientConfig";
	/** 获取验证码 */
	public static final String GET_VERIFYCODE_API = "member/member/getVerifyCode";
	/** 注册用户 */
	public static final String REGIST_MEMBER_API = "member/member/registMember";
	/** 登录 */
	public static final String LOGIN_API = "member/member/login";
	/** 编辑个人信息 */
	public static final String EDIT_MEMBER_INFO = "member/member/editMemberInfo";
	/** 编辑用户头像 */
	public static final String EDIT_MEMBER_ICON = "member/member/editMemberIcon";
	/** 编辑用户动态图片 */
	public static final String EDIT_MEMBER_DYNAMICIMAGE = "member/member/editMemberDynamicImage";
	/** 忘记密码 */
	public static final String FORGET_PASSWORD = "member/member/forgetPassword";
	/** 重置密码 */
	public static final String RESET_PASSWORD = "member/member/resetPassword";
	/**
	 * 获取粉丝列表
	 */
	public static final String GET_FOLLOWED_LIST = "member/member/getFollowedList";
	/**
	 * 快速登陆注册
	 */
	public static final String QUICK_LOGIN = "member/member/quickLogin";
	/**
	 * 获取关注列表
	 */
	public static final String GET_FOLLOWING_LIST = "member/member/getFollowingList";
	/**
	 * 编辑关注状态更改
	 */
	public static final String EDIT_FOLLOWING_STATUS = "member/member/editFollowingStatus";
	/** 提交反馈信息接口 */
	public static final String SUBMIT_FEEDBACK = "base/feedback/submitFeedback";
	/** 获取商家列表及菜单 */
	public static final String GET_STORE_LIST_AND_MENUS = "store/store/getStoreListAndMenus";
	/** 获取菜单及分类 */
	public static final String GET_MENULIST_AND_CATEGORYLIST = "store/menu/getMenuListAndCategoryList";
	/** 获取菜单详情 */
	public static final String GET_MENU_DETAIL = "store/menu/getMenuDetail";
	/** 获取个人信息 */
	public static final String GET_MENU_INFO_LIST = "store/menu/getMenuInfoList";
	/** 获取菜单评论 */
	public static final String GET_MENU_COMMENT = "dynamic/dynamic/getMenuComment";
	/** 编辑菜品评论 */
	public static final String EDIT_MENU_COMMENT = "dynamic/dynamic/editMenuComment";
	/** 获取菜品赞 */
	public static final String GET_MENU_GOODS = "dynamic/dynamic/getMenuGoods";
	/** 编辑菜品赞 */
	public static final String EDIT_MENU_GOODS = "dynamic/dynamic/editMenuGoods";
	/** 获取动态列表 */
	public static final String GET_DYNAMIC_LIST = "dynamic/dynamic/getDynamicList";
	/** 获取动态详情 */
	public static final String GET_DYNAMIC_DETAIL = "dynamic/dynamic/getDynamicDetail";
	/** 获取动态赞 */
	public static final String GET_DYNAMIC_GOODS = "dynamic/dynamic/getDynamicGoods";
	/** 获取动态评论 */
	public static final String GET_DYNAMIC_COMMENT = "dynamic/dynamic/getDynamicComment";
	/** 编辑及动态评论 */
	public static final String EDIT_DYNAMCI_COMMENT = "dynamic/dynamic/editDynamicComment";
	/** 编辑动态赞 */
	public static final String EDIT_DYNAMIC_GOODS = "dynamic/dynamic/editDynamicGoods";
	/** 获取推荐动态列表 */
	public static final String GET_RECOMMAND_DYNAMICLIST = "dynamic/dynamic/getRecommendDynamicList";
	/** 获取商家动态列表 */
	public static final String GET_STORE_DYNAMICLIST = "dynamic/dynamic/getStoreDynamicList";
	/** 动态-编辑菜单图片 */
	public static final String EDIT_MENU_IMAGE = "dynamic/dynamic/editMenuImage";
	/** 动态-编辑动态内容(标题/权限) */
	public static final String EDIT_DYNAMIC = "dynamic/dynamic/editDynamic";
	/** 搜索菜单 */
	public static final String SEARCH_MENU = "search/search/searchMenu";
	/** 搜索商家 */
	public static final String SEARCH_STORE = "search/search/searchStore";
	/** 模糊搜索 */
	public static final String SEARCH_VAGUE = "search/search/searchVague";
	/** 搜索动态 */
	public static final String SEARCH_ACTIVITIES = "search/search/searchDynamic";
	/** 搜索用户 */
	public static final String SEARCH_MEMBER = "search/search/searchMember";
	/** 获取商家详情 */
	public static final String GET_STORE_DETAILS = "store/store/getStoreDetails";
	/**
	 * 获取启动封面图片
	 */
	public static final String GET_FRONT_PAGE = "dynamic/dynamic/getFrontPage";
	/** 获取用户详情 */
	public static final String MEMBER_DETAIL = "member/member/getMemberDetail";
	/**
	 * 获取积分列表
	 */
	public static final String GET_SCORE_LIST = "score/score/getScoreList";
	/**
	 * 轮询获取是否有新消息接口
	 */
	public static final String HAVE_NEW_SYSTEM_MESSAGE = "message_center/message_center/haveNewSystemMessage";
	/**
	 * 获取系统信息列表接口
	 */
	public static final String GET_SYSTEM_MESSAGELIST = "message_center/message_center/getSystemMessageList";
	/**
	 * 获取系统信息详情接口
	 */
	public static final String GET_SYSTEM_MESSAGE_DETAIL = "message_center/message_center/getSystemMessageDetail";
	/** 轮询是否有心动态提示，头部红点显示接口 */
	public static final String HAVE_NEW_DYNAMIC = "message_center/message_center/haveNewDynamic";
	/** 轮询是否有心动态消息提示，动态列表layout显示接口 */
	public static final String HAVE_NEW_DYNAMIC_MESSAGE = "message_center/message_center/haveNewDynamicMessage";
	/** 轮询是否有心动态消息接口 */
	public static final String GET_DYNAMIC_MESSAGE_LIST = "message_center/message_center/getDynamicMessageList";
	/** 登出 */
	public static final String LOGOUT = "member/member/logout";
	/** 获取购物车数据 */
	public static final String GET_CART = "order/order/getCart";
	/** 编辑购物车数据 */
	public static final String EDIT_CART = "order/order/editCart";
	/** 生成订单接口 */
	public static final String ADD_ORDER = "order/order/addOrder";
	/** 获取订单列表接口 */
	public static final String GET_ORDER_LIST = "order/order/getOrderList";
	/** 获取订单详情接口 */
	public static final String GET_ORDER_DETAIL = "order/order/getOrderDetail";
	/** 提交退款申请接口 */
	public static final String SUBMIT_RETURN = "order/return/submitReturn";
	/** 获取退款详情接口 */
	public static final String GET_RETURN_DETAIL = "order/return/getReturnDetail";
	/** 生成支付数据接口 */
	public static final String GENERATE_PAY_DATA = "order/order/generatePayData";
	/** 提交支付订单接口 */
	public static final String Submit_PayOrder = "order/order/submitPayOrder";
	/** 获取店长推荐列表 */
	public static final String GET_RECOMMEND_MENULIST = "store/store/getRecommendMenuList";
}
