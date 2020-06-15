package cn.soilove.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * TimeUtils
 * 
 */
public class TimeUtils {
	/** 1分钟的毫秒数 */
	public final static long MILLISECOND_MINUTE = 60 * 1000;
	/** 1小时的毫秒数 */
	public final static long MILLISECOND_HOUR = 60 * MILLISECOND_MINUTE;
	/** 1天的毫秒数 */
	public final static long MILLISECOND_DAY = 24 * MILLISECOND_HOUR;
	/** 1月的毫秒数（31天） */
	public final static long MILLISECOND_MONTH = 31 * MILLISECOND_DAY;
	/** 1年的毫秒数 */
	public final static long MILLISECOND_YEAR = 12 * MILLISECOND_MONTH;

	public final static String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
	public final static String FORMAT_YYYY_MM = "yyyy-MM";
	public final static String FORMAT_YYYYMMDD = "yyyyMMDD";
	public final static String FORMAT_YYYY_MM_DD_HH_SMM_SS = "yyyy-MM-dd HH:mm:ss";
	public final static String FORMAT_YYYY_MM_DD_HH_SMM = "yyyy-MM-dd HH:mm";
	public final static String FORMAT_YYYY_CN_MM_CN_DD_CN = "yyyy年MM月dd日";
	public final static String FORMAT_CN_MM_CN_DD_CN = "MM月dd日";
	public final static String FORMAT_HH_SMM = "HH:mm";
	public final static String FORMAT_DAY_START = "yyyy-MM-dd 00:00:00";
	public final static String FORMAT_DAY_END = "yyyy-MM-dd 23:59:59";

	private TimeUtils() {
		throw new AssertionError();
	}

	/**
	 * 获取Calendar对象
	 * @param date
	 * @return
	 */
	public static Calendar getCalendar(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("GMT+8")); // 时区设置
		cal.setTime(date);
		return cal;
	}

	/**
	 * 获取指定格式的时间字符
	 * 
	 * @param timeInMillis
	 * @param format
	 * @return
	 */
	public static String getTime(long timeInMillis, String format) {
		return new SimpleDateFormat(format).format(new Date(timeInMillis));
	}

	/**
	 * 获取指定格式的时间字符
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getTime(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}

	/**
	 * 获取指定格式的时间Date
	 * @param date
	 * @param format
	 * @return
	 */
	public static Date getTime(String date, String format) {
		try {
			return new SimpleDateFormat(format).parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
	}

	/**
	 * 增加年
	 * @param date
	 * @param year
	 * @return
	 */
	public static Date addYear(Date date, int year) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("GMT+8")); // 时区设置
		cal.setTime(date);
		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + year);
		return cal.getTime();
	}

	/**
	 * 增加月
	 * @param date
	 * @param month
	 * @return
	 */
	public static Date addMonth(Date date, int month) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("GMT+8")); // 时区设置
		cal.setTime(date);
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + month);
		return cal.getTime();
	}

	/**
	 * 增加天
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date addDay(Date date, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("GMT+8")); // 时区设置
		cal.setTime(date);
		cal.set(Calendar.DATE, cal.get(Calendar.DATE) + day);
		return cal.getTime();
	}

	/**
	 * 增加小时
	 * @param date
	 * @param hours
	 * @return
	 */
	public static Date addHour(Date date, int hours) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("GMT+8")); // 时区设置
		cal.setTime(date);
		cal.set(Calendar.HOUR, cal.get(Calendar.HOUR) + hours);
		return cal.getTime();
	}

	/**
	 * 增加分钟
	 * @param date
	 * @param minutes
	 * @return
	 */
	public static Date addMinute(Date date, int minutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("GMT+8")); // 时区设置
		cal.setTime(date);
		cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + minutes);
		return cal.getTime();
	}

	/**
	 * 增加秒
	 * @param date
	 * @param seconds
	 * @return
	 */
	public static Date addSecond(Date date, int seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("GMT+8")); // 时区设置
		cal.setTime(date);
		cal.set(Calendar.SECOND, cal.get(Calendar.SECOND) + seconds);
		return cal.getTime();
	}

	/**
	 * 获取月份第一天
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getMonthFirstDay(Date date,String format){
		Calendar cale = Calendar.getInstance();
		cale.setTime(date);
		cale.add(Calendar.MONTH, 0);
		cale.set(Calendar.DAY_OF_MONTH, 1);
		return new SimpleDateFormat(format).format(cale.getTime());
	}

	/**
	 * 获取月份最后一天
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getMonthLastDay(Date date,String format){
		Calendar cale = Calendar.getInstance();
		cale.setTime(date);
		cale.add(Calendar.MONTH, 1);
		cale.set(Calendar.DAY_OF_MONTH, 0);
		return new SimpleDateFormat(format).format(cale.getTime());
	}


	/**
	 * 获取最近时间格式显示
	 * <pre>
	 * 几年前，几个月前，几天前...
	 * </pre>
	 * @param date
	 * @return
	 */
	public static String getLatelyTxtFormat(Date date) {
		if (date == null) {
			return null;
		}
		long diff = System.currentTimeMillis() - date.getTime();
		long r = 0;
		if (diff > MILLISECOND_YEAR) {
			r = (diff / MILLISECOND_YEAR);
			return r + "年前";
		}
		if (diff > MILLISECOND_MONTH) {
			r = (diff / MILLISECOND_MONTH);
			return r + "个月前";
		}
		if (diff > MILLISECOND_DAY) {
			r = (diff / MILLISECOND_DAY);
			return r + "天前";
		}
		if (diff > MILLISECOND_HOUR) {
			r = (diff / MILLISECOND_HOUR);
			return r + "小时前";
		}
		if (diff > MILLISECOND_MINUTE) {
			r = (diff / MILLISECOND_MINUTE);
			return r + "分钟前";
		}
		return "刚刚";
	}

	/**
	 * 获取当前日期是星期几<br>
	 *
	 * @param date
	 * @return 当前日期是星期几
	 */
	public static String getWeekFormat(Date date) {
		String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("GMT+8")); // 时区设置
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0){
			w = 0;
		}
		return weekDays[w];
	}



	/**
	 * 返回距今已有
	 * @param date 今天之前的时间
	 * @return x年x月x日
	 */
	public static String getBetweenFormat(Date date){
		Period between = Period.between(
				LocalDate.parse(getTime(date,FORMAT_YYYY_MM_DD)),
				LocalDate.now()
		);
		StringBuffer sb = new StringBuffer();
		if(between.getYears() > 0){
			sb.append(between.getYears()).append("年");
		}
		if(between.getMonths() > 0){
			sb.append(between.getMonths()).append("月");
		}
		if(between.getDays() > 0){
			sb.append(between.getDays()).append("日");
		}
		return sb.toString();
	}



}
