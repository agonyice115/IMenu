package com.huassit.imenu.android.db;

/**
 * Created by sylar on 14-3-12.
 */
public class DbConstants {

	public static final String DB_NAME = "imenu.db";

	public static final int DB_VERSION = 1;

	public static class TABLE {
		public static final String TABLE_SEARCH_HISTORY = "search_history";
		public static final String TABLE_REGION = "region";
		public static final String TABLE_CATEGORY = "category";
		public static final String TABLE_SERVICE = "service";
		public static final String TABLE_FEEDBACK = "feedback";
		public static final String TABLE_ENVIRONMENT = "environment";
		public static final String TABLE_MENU_UNIT = "menu_unit";
		public static final String TABLE_MENU_TASTE = "menu_taste";
		public static final String TABLE_AREA_CODE = "area_code";
		public static final String TABLE_SHARE_MENU = "share_menu";
		public static final String TABLE_SHARE_STORE = "share_store";
		public static final String TABLE_SHARE_MEMBER = "share_member";
		public static final String TABLE_SHARE_DYNAMIC = "share_dynamic";
		public static final String TABLE_CLIENT_SKIN = "client_skin";
		public static final String TABLE_ABOUT_INFO = "about_info";
		public static final String TABLE_MEMBER = "member";
		public static final String TABLE_RETURN_REASON = "return_reason";
	}

	/**
	 * 搜索历史表字段
	 */
	public final static class COLUMN_HISTORY {
		public static final String KEYWORD = "keyword";
		public static final String LAST_UPDATED = "last_updated";
	}

	/**
	 * 服务表字段
	 */
	public final static class COLUMN_SERVICE {
		public static final String SERVICE_ID = "service_id";
		public static final String SERVICE_NAME = "service_name";
		public static final String SERVICE_IMAGE = "service_image";
		public static final String THUMB_IMAGE = "thumb_image";
		public static final String SORT_ORDER = "sort_order";
	}

	/**
	 * 区域表字段
	 */
	public final static class COLUMN_REGION {
		public static final String REGION_ID = "region_id";
		public static final String REGION_IMAGE = "region_image";
		public static final String PARENT_ID = "parent_id";
		public static final String SORT_ORDER = "sort_order";
		public static final String REGION_NAME = "region_name";
		public static final String ALIASES_NAME = "aliases_name";
		public static final String LEVEL = "level";
		public static final String IS_OPEN = "is_open";
	}

	/** 退款原因字段 */
	public final static class COLUMN_RETURN_REASON {
		public static final String RETURN_REASON_ID = "return_reason_id";
		public static final String RETURN_REASON_NAME = "return_reason_name";
	}

	/**
	 * 分类表字段
	 */
	public static final class COLUMN_CATEGORY {
		public static final String CATEGORY_ID = "category_id";
		public static final String CATEGORY_IMAGE = "category_image";
		public static final String PARENT_ID = "parent_id";
		public static final String SORT_ORDER = "sort_order";
		public static final String CATEGORY_NAME = "category_name";
		public static final String ALIASES_NAME = "aliases_name";
		public static final String LEVEL = "level";
	}

	/**
	 * 反馈表字段
	 */
	public static final class COLUMN_FEEDBACK {
		public static final String FEEDBACK_ID = "feedback_id";
		public static final String FEEDBACK_NAME = "feedback_name";
		public static final String PARENT_ID = "parent_id";
		public static final String SORT_ORDER = "sort_order";
		public static final String FEEDBACK_TYPE = "feedback_type";
		public static final String LEVEL = "level";
	}

	/**
	 * 环境表字段
	 */
	public static final class COLUMN_ENVIRONMENT {
		public static final String ENVIRONMENT_ID = "environment_id";
		public static final String ENVIRONMENT_IMAGE = "environment_image";
		public static final String THUMB_IMAGE = "thumb_image";
		public static final String SORT_ORDER = "sort_order";
		public static final String ENVIRONMENT_NAME = "environment_name";
	}

	/**
	 * 口味表字段
	 */
	public static final class COLUMN_MENU_TASTE {
		public static final String MENU_TASTE_ID = "menu_taste_id";
		public static final String MENU_TASTE_IMAGE = "menu_taste_image";
		public static final String SORT_ORDER = "sort_order";
		public static final String MENU_TASTE_NAME = "menu_taste_name";
	}

	/**
	 * 菜单表字段
	 */
	public static final class COLUMN_MENU_UNIT {
		public static final String MENU_UNIT_ID = "menu_unit_id";
		public static final String MENU_UNIT_TITLE = "menu_unit_title";
	}

	/**
	 * 国家表字段
	 */
	public static final class COLUMN_AREA_CODE {
		public static final String AREA_CODE_ID = "area_code_id";
		public static final String AREA_CODE_NAME = "area_code_name";
		public static final String AREA_CODE_VALUE = "area_code_value";
		public static final String SORT_ORDER = "sort_order";
		public static final String DEFAULT_VALUE = "default_value";
	}

	/**
	 * 分享相关表字段
	 */
	public static final class COLUMN_SHARE {
		public static final String TITLE = "title";
		public static final String DESCRIPTION = "description";
		public static final String CONTENT = "content";
		public static final String LINK = "link";
	}

	/**
	 * 皮肤表字段
	 */
	public static final class COLUMN_CLIENT_SKIN {
		public static final String CLIENT_SKIN_ID = "client_skin_id";
		public static final String CLIENT_SKIN_NAME = "client_skin_name";
		public static final String CLIENT_SKIN_VALUE = "client_skin_value";
		public static final String SORT_ORDER = "sort_order";
		public static final String SEX = "sex";
		public static final String DEFAULT_VALUE = "default_value";
	}

	/**
	 * 关于表字段
	 */
	public static final class COLUMN_ABOUT_INFO {
		public static final String KEY = "key";
		public static final String VALUE = "value";
	}

	// /**
	// * 用户表字段
	// */
	// public static final class COLUMN_MEMBER {
	// public static final String
	// }
	/**
	 * 创建数据库表SQL
	 */
	public static StringBuffer CREATE_HISTORY_TABLE_SQL = new StringBuffer();
	public static StringBuffer CREATE_REGION_TABLE_SQL = new StringBuffer();
	public static StringBuffer CREATE_CATEGORY_TABLE_SQL = new StringBuffer();
	public static StringBuffer CREATE_SERVICE_TABLE_SQL = new StringBuffer();
	public static StringBuffer CREATE_FEEDBACK_TABLE_SQL = new StringBuffer();
	public static StringBuffer CREATE_ENVIRONMENT_TABLE_SQL = new StringBuffer();
	public static StringBuffer CREATE_MENU_UNIT_TABLE_SQL = new StringBuffer();
	public static StringBuffer CREATE_MENU_TASTE_TABLE_SQL = new StringBuffer();
	public static StringBuffer CREATE_AREA_CODE_TABLE_SQL = new StringBuffer();
	public static StringBuffer CREATE_SHARE_MENU_TABLE_SQL = new StringBuffer();
	public static StringBuffer CREATE_SHARE_STORE_TABLE_SQL = new StringBuffer();
	public static StringBuffer CREATE_SHARE_MEMBER_TABLE_SQL = new StringBuffer();
	public static StringBuffer CREATE_SHARE_DYNAMIC_TABLE_SQL = new StringBuffer();
	public static StringBuffer CREATE_CLIENT_SKIN_TABLE_SQL = new StringBuffer();
	public static StringBuffer CREATE_ABOUT_INFO_TABLE_SQL = new StringBuffer();
	public static StringBuffer CREATE_RETURN_REASON_TABLE_SQL = new StringBuffer();

	static {
		CREATE_REGION_TABLE_SQL.append("create table ")
				.append(TABLE.TABLE_REGION).append(" ( ");
		CREATE_REGION_TABLE_SQL.append(COLUMN_REGION.REGION_ID).append(
				" text primary key, ");
		CREATE_REGION_TABLE_SQL.append(COLUMN_REGION.REGION_NAME).append(
				" text , ");
		CREATE_REGION_TABLE_SQL.append(COLUMN_REGION.REGION_IMAGE).append(
				" text , ");
		CREATE_REGION_TABLE_SQL.append(COLUMN_REGION.PARENT_ID).append(
				" text , ");
		CREATE_REGION_TABLE_SQL.append(COLUMN_REGION.ALIASES_NAME).append(
				" text , ");
		CREATE_REGION_TABLE_SQL.append(COLUMN_REGION.LEVEL).append(
				" INTEGER , ");
		CREATE_REGION_TABLE_SQL.append(COLUMN_REGION.IS_OPEN).append(
				" INTEGER , ");
		CREATE_REGION_TABLE_SQL.append(COLUMN_REGION.SORT_ORDER).append(
				" INTEGER");
		CREATE_REGION_TABLE_SQL.append(" );");

		CREATE_HISTORY_TABLE_SQL.append("create table ")
				.append(TABLE.TABLE_SEARCH_HISTORY).append(" ( ");
		CREATE_HISTORY_TABLE_SQL.append(COLUMN_HISTORY.KEYWORD).append(
				" text primary key, ");
		CREATE_HISTORY_TABLE_SQL.append(COLUMN_HISTORY.LAST_UPDATED).append(
				" text");
		CREATE_HISTORY_TABLE_SQL.append(" );");

		CREATE_CATEGORY_TABLE_SQL.append("create table ")
				.append(TABLE.TABLE_CATEGORY).append(" ( ");
		CREATE_CATEGORY_TABLE_SQL.append(COLUMN_CATEGORY.CATEGORY_ID).append(
				" text primary key, ");
		CREATE_CATEGORY_TABLE_SQL.append(COLUMN_CATEGORY.CATEGORY_NAME).append(
				" text , ");
		CREATE_CATEGORY_TABLE_SQL.append(COLUMN_CATEGORY.CATEGORY_IMAGE)
				.append(" text , ");
		CREATE_CATEGORY_TABLE_SQL.append(COLUMN_CATEGORY.PARENT_ID).append(
				" text , ");
		CREATE_CATEGORY_TABLE_SQL.append(COLUMN_CATEGORY.ALIASES_NAME).append(
				" text , ");
		CREATE_CATEGORY_TABLE_SQL.append(COLUMN_CATEGORY.LEVEL).append(
				" INTEGER , ");
		CREATE_CATEGORY_TABLE_SQL.append(COLUMN_CATEGORY.SORT_ORDER).append(
				" INTEGER");
		CREATE_CATEGORY_TABLE_SQL.append(" );");

		CREATE_SERVICE_TABLE_SQL.append("create table ")
				.append(TABLE.TABLE_SERVICE).append(" ( ");
		CREATE_SERVICE_TABLE_SQL.append(COLUMN_SERVICE.SERVICE_ID).append(
				" text primary key, ");
		CREATE_SERVICE_TABLE_SQL.append(COLUMN_SERVICE.SERVICE_NAME).append(
				" text , ");
		CREATE_SERVICE_TABLE_SQL.append(COLUMN_SERVICE.SERVICE_IMAGE).append(
				" text , ");
		CREATE_SERVICE_TABLE_SQL.append(COLUMN_SERVICE.THUMB_IMAGE).append(
				" text , ");
		CREATE_SERVICE_TABLE_SQL.append(COLUMN_SERVICE.SORT_ORDER).append(
				" INTEGER");
		CREATE_SERVICE_TABLE_SQL.append(" );");

		CREATE_RETURN_REASON_TABLE_SQL.append("create table ").append(TABLE.TABLE_RETURN_REASON).append(" ( ");
		CREATE_RETURN_REASON_TABLE_SQL.append(COLUMN_RETURN_REASON.RETURN_REASON_ID).append(" text primary key, ");
		CREATE_RETURN_REASON_TABLE_SQL.append(COLUMN_RETURN_REASON.RETURN_REASON_NAME).append(" text ");
		CREATE_RETURN_REASON_TABLE_SQL.append(" );");

		CREATE_FEEDBACK_TABLE_SQL.append("create table ").append(TABLE.TABLE_FEEDBACK).append(" ( ");
		CREATE_FEEDBACK_TABLE_SQL.append(COLUMN_FEEDBACK.FEEDBACK_ID).append(" text primary key, ");
		CREATE_FEEDBACK_TABLE_SQL.append(COLUMN_FEEDBACK.FEEDBACK_NAME).append(" text , ");
		CREATE_FEEDBACK_TABLE_SQL.append(COLUMN_FEEDBACK.FEEDBACK_TYPE).append(" text , ");
		CREATE_FEEDBACK_TABLE_SQL.append(COLUMN_FEEDBACK.PARENT_ID).append(" INTEGER , ");
		CREATE_FEEDBACK_TABLE_SQL.append(COLUMN_FEEDBACK.LEVEL).append(" INTEGER , ");
		CREATE_FEEDBACK_TABLE_SQL.append(COLUMN_FEEDBACK.SORT_ORDER).append(" INTEGER");
		CREATE_FEEDBACK_TABLE_SQL.append(" );");

		CREATE_ENVIRONMENT_TABLE_SQL.append("create table ")
				.append(TABLE.TABLE_ENVIRONMENT).append(" ( ");
		CREATE_ENVIRONMENT_TABLE_SQL.append(COLUMN_ENVIRONMENT.ENVIRONMENT_ID)
				.append(" text primary key, ");
		CREATE_ENVIRONMENT_TABLE_SQL
				.append(COLUMN_ENVIRONMENT.ENVIRONMENT_NAME).append(" text , ");
		CREATE_ENVIRONMENT_TABLE_SQL.append(
				COLUMN_ENVIRONMENT.ENVIRONMENT_IMAGE).append(" text , ");
		CREATE_ENVIRONMENT_TABLE_SQL.append(COLUMN_ENVIRONMENT.THUMB_IMAGE)
				.append(" text , ");
		CREATE_ENVIRONMENT_TABLE_SQL.append(COLUMN_ENVIRONMENT.SORT_ORDER)
				.append(" INTEGER");
		CREATE_ENVIRONMENT_TABLE_SQL.append(" );");

		CREATE_MENU_TASTE_TABLE_SQL.append("create table ")
				.append(TABLE.TABLE_MENU_TASTE).append(" ( ");
		CREATE_MENU_TASTE_TABLE_SQL.append(COLUMN_MENU_TASTE.MENU_TASTE_ID)
				.append(" text primary key, ");
		CREATE_MENU_TASTE_TABLE_SQL.append(COLUMN_MENU_TASTE.MENU_TASTE_NAME)
				.append(" text , ");
		CREATE_MENU_TASTE_TABLE_SQL.append(COLUMN_MENU_TASTE.MENU_TASTE_IMAGE)
				.append(" text , ");
		CREATE_MENU_TASTE_TABLE_SQL.append(COLUMN_MENU_TASTE.SORT_ORDER)
				.append(" INTEGER");
		CREATE_MENU_TASTE_TABLE_SQL.append(" );");

		CREATE_MENU_UNIT_TABLE_SQL.append("create table ")
				.append(TABLE.TABLE_MENU_UNIT).append(" ( ");
		CREATE_MENU_UNIT_TABLE_SQL.append(COLUMN_MENU_UNIT.MENU_UNIT_ID)
				.append(" text primary key, ");
		CREATE_MENU_UNIT_TABLE_SQL.append(COLUMN_MENU_UNIT.MENU_UNIT_TITLE)
				.append(" text ");
		CREATE_MENU_UNIT_TABLE_SQL.append(" );");

		CREATE_AREA_CODE_TABLE_SQL.append("create table ")
				.append(TABLE.TABLE_AREA_CODE).append(" ( ");
		CREATE_AREA_CODE_TABLE_SQL.append(COLUMN_AREA_CODE.AREA_CODE_ID)
				.append(" text primary key, ");
		CREATE_AREA_CODE_TABLE_SQL.append(COLUMN_AREA_CODE.AREA_CODE_NAME)
				.append(" text , ");
		CREATE_AREA_CODE_TABLE_SQL.append(COLUMN_AREA_CODE.AREA_CODE_VALUE)
				.append(" text , ");
		CREATE_AREA_CODE_TABLE_SQL.append(COLUMN_AREA_CODE.DEFAULT_VALUE)
				.append(" text , ");
		CREATE_AREA_CODE_TABLE_SQL.append(COLUMN_AREA_CODE.SORT_ORDER).append(
				" INTEGER");
		CREATE_AREA_CODE_TABLE_SQL.append(" );");

		CREATE_SHARE_STORE_TABLE_SQL.append("create table ")
				.append(TABLE.TABLE_SHARE_STORE).append(" ( ");
		CREATE_SHARE_STORE_TABLE_SQL.append(COLUMN_SHARE.TITLE).append(
				" text primary key, ");
		CREATE_SHARE_STORE_TABLE_SQL.append(COLUMN_SHARE.DESCRIPTION).append(
				" text , ");
		CREATE_SHARE_STORE_TABLE_SQL.append(COLUMN_SHARE.CONTENT).append(
				" text , ");
		CREATE_SHARE_STORE_TABLE_SQL.append(COLUMN_SHARE.LINK).append(" text ");
		CREATE_SHARE_STORE_TABLE_SQL.append(" );");

		CREATE_SHARE_MEMBER_TABLE_SQL.append("create table ")
				.append(TABLE.TABLE_SHARE_MEMBER).append(" ( ");
		CREATE_SHARE_MEMBER_TABLE_SQL.append(COLUMN_SHARE.TITLE).append(
				" text primary key, ");
		CREATE_SHARE_MEMBER_TABLE_SQL.append(COLUMN_SHARE.DESCRIPTION).append(
				" text , ");
		CREATE_SHARE_MEMBER_TABLE_SQL.append(COLUMN_SHARE.CONTENT).append(
				" text , ");
		CREATE_SHARE_MEMBER_TABLE_SQL.append(COLUMN_SHARE.LINK)
				.append(" text ");
		CREATE_SHARE_MEMBER_TABLE_SQL.append(" );");

		CREATE_SHARE_MENU_TABLE_SQL.append("create table ")
				.append(TABLE.TABLE_SHARE_MENU).append(" ( ");
		CREATE_SHARE_MENU_TABLE_SQL.append(COLUMN_SHARE.TITLE).append(
				" text primary key, ");
		CREATE_SHARE_MENU_TABLE_SQL.append(COLUMN_SHARE.DESCRIPTION).append(
				" text , ");
		CREATE_SHARE_MENU_TABLE_SQL.append(COLUMN_SHARE.CONTENT).append(
				" text , ");
		CREATE_SHARE_MENU_TABLE_SQL.append(COLUMN_SHARE.LINK).append(" text ");
		CREATE_SHARE_MENU_TABLE_SQL.append(" );");

		CREATE_SHARE_DYNAMIC_TABLE_SQL.append("create table ")
				.append(TABLE.TABLE_SHARE_DYNAMIC).append(" ( ");
		CREATE_SHARE_DYNAMIC_TABLE_SQL.append(COLUMN_SHARE.TITLE).append(
				" text primary key, ");
		CREATE_SHARE_DYNAMIC_TABLE_SQL.append(COLUMN_SHARE.DESCRIPTION).append(
				" text , ");
		CREATE_SHARE_DYNAMIC_TABLE_SQL.append(COLUMN_SHARE.CONTENT).append(
				" text , ");
		CREATE_SHARE_DYNAMIC_TABLE_SQL.append(COLUMN_SHARE.LINK).append(
				" text ");
		CREATE_SHARE_DYNAMIC_TABLE_SQL.append(" );");

		CREATE_CLIENT_SKIN_TABLE_SQL.append("create table ")
				.append(TABLE.TABLE_CLIENT_SKIN).append(" ( ");
		CREATE_CLIENT_SKIN_TABLE_SQL.append(COLUMN_CLIENT_SKIN.CLIENT_SKIN_ID)
				.append(" text primary key, ");
		CREATE_CLIENT_SKIN_TABLE_SQL
				.append(COLUMN_CLIENT_SKIN.CLIENT_SKIN_NAME).append(" text , ");
		CREATE_CLIENT_SKIN_TABLE_SQL.append(
				COLUMN_CLIENT_SKIN.CLIENT_SKIN_VALUE).append(" text , ");
		CREATE_CLIENT_SKIN_TABLE_SQL.append(COLUMN_CLIENT_SKIN.SEX).append(
				" text , ");
		CREATE_CLIENT_SKIN_TABLE_SQL.append(COLUMN_CLIENT_SKIN.DEFAULT_VALUE)
				.append(" text , ");
		CREATE_CLIENT_SKIN_TABLE_SQL.append(COLUMN_CLIENT_SKIN.SORT_ORDER)
				.append(" INTEGER");
		CREATE_CLIENT_SKIN_TABLE_SQL.append(" );");

		CREATE_ABOUT_INFO_TABLE_SQL.append("create table ")
				.append(TABLE.TABLE_ABOUT_INFO).append(" ( ");
		CREATE_ABOUT_INFO_TABLE_SQL.append(COLUMN_ABOUT_INFO.KEY).append(
				" text primary key, ");
		CREATE_ABOUT_INFO_TABLE_SQL.append(COLUMN_ABOUT_INFO.VALUE).append(
				" text ");
		CREATE_ABOUT_INFO_TABLE_SQL.append(" );");
	}

}
