package com.sscf.investment.sdk.utils;

import android.os.SystemClock;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import BEC.KLineDesc;

public final class TimeUtils {
	private static long lastOperationTime;

	public static boolean isFrequentOperation() {
		final long now = SystemClock.elapsedRealtime();
		final long gap = now - lastOperationTime;
		if (gap < 500) {
			return true;
		} else{
			lastOperationTime = now;
			return false;
		}
	}

	public static int systemCurrentTimeSeconds() {
		return (int) (System.currentTimeMillis() / 1000);
	}

	public static String getTimeString(String format, long timestamp) {
		final Date date = new Date(timestamp < 0 ? System.currentTimeMillis() : timestamp);
		format = format == null ? "yyyyMMdd_HHmmss" : format;
		return new SimpleDateFormat(format).format(date);
	}

	public static String getCurrentTimeString(String format) {
		return getTimeString(format, -1);
	}

	public static String getCurrentTimeString() {
		return getCurrentTimeString(null);
	}

	private static SimpleDateFormat mDateFormat = new SimpleDateFormat();
	private static Calendar mNowCalendar = Calendar.getInstance();

	private static void updateDate() {
		mNowCalendar.setTimeInMillis(System.currentTimeMillis());
	}

	/**
	 * 判断是否是同一天
	 * @return
	 */
	private static boolean isSameDay(final Calendar calendar1, final Calendar calendar2) {
		return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
				&& calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 判断是否是同一年
	 * @return
	 */
	private static boolean isSameYear(final Calendar calendar1, final Calendar calendar2) {
		return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR);
	}

	private static boolean isYesterday(final Calendar nowCalendar, final Calendar calendar) {
		nowCalendar.add(Calendar.DAY_OF_YEAR, -1); // 转为昨天
		final boolean b = isSameDay(nowCalendar, calendar);
		nowCalendar.add(Calendar.DAY_OF_YEAR, 1); // 转回今天
		return b;
	}

	public static String timeStamp2SimpleDate(long timestamp) {
		updateDate();
		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		if (isSameDay(mNowCalendar, calendar)) {
			return "今天";
		} else { //不是今天
			mDateFormat.applyPattern("yy-MM-dd");
			final String date = mDateFormat.format(calendar.getTime());
			final StringBuilder dateBuilder = new StringBuilder(date);
			dateBuilder.append(' ').append('周').append(getDayInChineseForCalendar(calendar.get(Calendar.DAY_OF_WEEK)));
			return dateBuilder.toString();
		}
	}

	public static String timeStamp2DateForTeacher(long timestamp) {
		updateDate();
		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		String format;
		if (isSameDay(mNowCalendar, calendar)) { // 今天
			format = "HH:mm";
		} else if (isYesterday(mNowCalendar, calendar)) { // 昨天
			format = "HH:mm";
			mDateFormat.applyPattern(format);
			return "昨天 " + mDateFormat.format(calendar.getTime());
		} else if (isSameYear(mNowCalendar, calendar)) { // 同一年
			//显示日期
			format = "MM-dd HH:mm";
		} else {// 不同年，显示年份
			format = "yyyy-MM-dd HH:mm";
		}
		mDateFormat.applyPattern(format);
		return mDateFormat.format(calendar.getTime());
	}

	public static String timeStamp2Date(long timestamp) {
		updateDate();
		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		String format;
		if (isSameDay(mNowCalendar, calendar)) { // 今天
			format = "HH:mm";
		} else if (isYesterday(mNowCalendar, calendar)) { // 昨天
			return "昨天";
		} else if (isSameYear(mNowCalendar, calendar)) { // 同一年
			//显示日期
			format = "MM-dd";
		} else {// 不同年，显示年份
			format = "yyyy-MM-dd";
		}
		mDateFormat.applyPattern(format);
		return mDateFormat.format(calendar.getTime());
	}

	public static String transForDate(String date){
		if (TextUtils.isEmpty(date)){
			return "";
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date temp=null;
			try {
				long millions = sdf.parse(date).getTime();
				return timeStamp2DateForTeacher(millions);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		return "";
	}

	public static String timeStamp2DateWithoutMinute(long timestamp){
		updateDate();
		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		String format;

		if (isSameDay(mNowCalendar, calendar)) { // 今天
			return "今天";
		} else if (isYesterday(mNowCalendar, calendar)) { // 昨天
			return "昨天";
		} else if (isSameYear(mNowCalendar, calendar)) { // 同一年
			//显示日期
			format = "MM-dd";
		} else {// 不同年，显示年份
			format = "yyyy-MM-dd";
		}
		mDateFormat.applyPattern(format);
		return mDateFormat.format(calendar.getTime());
	}

	public static char getDayInChineseForCalendar(final int day) {
		switch (day) {
			case 1:
				return '日';
			case 2:
				return '一';
			case 3:
				return '二';
			case 4:
				return '三';
			case 5:
				return '四';
			case 6:
				return '五';
			case 7:
				return '六';
			default:
				return ' ';
		}
	}

	/**
	 * * Returns the day of the week represented by this date. The
	 * returned value (<tt>0</tt> = Sunday, <tt>1</tt> = Monday,
	 * <tt>2</tt> = Tuesday, <tt>3</tt> = Wednesday, <tt>4</tt> =
	 * Thursday, <tt>5</tt> = Friday, <tt>6</tt> = Saturday)
	 * represents the day of the week that contains or begins with
	 * the instant in time represented by this <tt>Date</tt> object,
	 * as interpreted in the local time zone.
	 *
	 * */
	public static char getDayInChinese(final int day) {
		switch (day) {
			case 0:
				return '日';
			case 1:
				return '一';
			case 2:
				return '二';
			case 3:
				return '三';
			case 4:
				return '四';
			case 5:
				return '五';
			case 6:
				return '六';
			default:
				return ' ';
		}
	}

	/**
	 * 比较k线时间大小用的
	 */
	public static long getCompareTime(final KLineDesc data) {
		return data.lYmd * 10000L + data.lMinute;
	}

	public static int convertTimeStamp2Ymd(long timeStamp) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeStamp);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		return year * 10000 + month * 100 + day;
	}

	public static int convertYmd2TimeStamp(int ymd) {
		int year = ymd / 10000;
		int month = (ymd - year * 10000) / 100;
		int day = ymd - year * 10000 - month * 100;
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, day, 0, 0, 0);
		return (int) (calendar.getTimeInMillis() / 1000);
	}
}
