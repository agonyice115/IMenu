package com.huassit.imenu.android.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;

/**
 * 时间转换工具类
 * 
 * @author Administrator
 * 
 */
public class TimeUtil {
	/** 种类为年份 */
	public final static int YEAR = 1;
	/** 种类为月份 */
	public final static int MONTH = 2;
	/** 种类为周 */
	public final static int WEEK = 3;
	private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};
	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};

	/***
	 * 子项的时间转换为与key相同的形式
	 * 
	 * @param dynamicDate时间
	 * @param type转换类型
	 *            （100=YEAR/200=MONTH/300=WEEK）
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String itemMonthDate(String dynamicDate, int type) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = formatter.parse(dynamicDate);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		switch (type) {
		case YEAR:
			formatter = new SimpleDateFormat("yyyy");
			break;
		case MONTH:
			formatter = new SimpleDateFormat("yyyyMM");
			break;
		case WEEK:
			try {
				Calendar cal = Calendar.getInstance();
				/** 这一句必须要设置，否则美国认为第一天是周日，而我国认为是周一，对计算当期日期是第几周会有错误 */
				cal.setFirstDayOfWeek(Calendar.MONDAY); // 设置每周的第一天为星期一
				cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);// 每周从周一开始
				cal.setMinimalDaysInFirstWeek(1); // 设置每周最少为1天
				cal.setTime(formatter.parse(dynamicDate));
				int time = cal.get(Calendar.WEEK_OF_YEAR);
				if (time < 10) {
					return "0" + time;
				}
				return time + "";
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		String dateString = formatter.format(date);
		return dateString;
	}

	/** 转换时间格式为2006/07/16 */
	@SuppressLint("SimpleDateFormat")
	public static String FormatTime(String time, String pattern) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = formatter.parse(time);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		formatter = new SimpleDateFormat(pattern);
		return formatter.format(date);
	}

	/** 转换时间格式String-long */
	@SuppressLint("SimpleDateFormat")
	public static long getLongTime(String time) {
		long timeString;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = formatter.parse(time);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		timeString = date.getTime();
		return timeString;
	}

	/** 获取当前时间String：yyyy-MM-dd hh:mm */
	@SuppressLint("SimpleDateFormat")
	public static String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		java.util.Date date = new java.util.Date();
		String str = sdf.format(date);
		return str;
	}

	/**
	 * 判断给定字符串时间是否为今日
	 * 
	 * @param sdate
	 * @return boolean
	 */
	public static boolean isToday(String sdate) {
		boolean b = false;
		Date time = toDate(sdate);
		Date today = new Date();
		if (time != null) {
			String nowDate = dateFormater2.get().format(today);
			String timeDate = dateFormater2.get().format(time);
			if (nowDate.equals(timeDate)) {
				b = true;
			}
		}
		return b;
	}

	/** 判断是否为过期订单，用时间戳进行对比 */
	public static boolean isExpire(String date) {
		// 此时此刻
		long moment = System.currentTimeMillis();
		long time = getTime(date, "yyyy-MM-dd HH:mm:ss");
		if (time < moment) {
			return true;
		}
		return false;
	}

	/**
	 * 将字符串转位日期类型
	 * 
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		try {
			return dateFormater.get().parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 将字符串转为时间戳 "yyyy年MM月dd日HH时mm分ss秒"
	 * 
	 * @param user_time
	 * @param model
	 * @return
	 */
	public static long getTime(String user_time, String model) {
		// "yyyy年MM月dd日HH时mm分ss秒"
		String re_time = null;
		SimpleDateFormat sdf = new SimpleDateFormat(model);
		Date d = null;
		try {
			d = sdf.parse(user_time);
			// long l = d.getTime();
			// String str = String.valueOf(l);
			// re_time = str.substring(0, 10);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long strTime = 0;
		if (d != null) {
			strTime = d.getTime();
		}
		return strTime;
	}

	/**
	 * 将时间戳转为字符串 "yyyy年MM月dd日HH时mm分ss秒"
	 * 
	 * @param lon
	 * @param model
	 * @return
	 */
	public static String getStrTime(Long lon, String model) {
		// "yyyy年MM月dd日HH时mm分ss秒"
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat(model);
		// 例如：cc_time=1291778220
		// long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lon));
		return re_StrTime;
	}

}
