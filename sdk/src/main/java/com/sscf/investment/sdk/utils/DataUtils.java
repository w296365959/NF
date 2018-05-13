package com.sscf.investment.sdk.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class DataUtils {
	private static int Const_Count = 0;


	/**
	* Convert byte[] to hex string.
	 * 这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
	* @param src byte[] data
	* @return  hex string
	*/
	public static String bytesToHexString(byte[] src){
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * Convert hex string to byte[]
	 * @param hexString the hex string
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * Convert char to byte
	 * @param c char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * 计算涨跌幅
	 * @param now
	 * @param close
	 * @return
	 */
	public static String calculateUpDownString(float now, float close) {
		if (close <= 0 || now <= 0) {
			return "--";
		}

		final float updown = (now / close - 1) * 100;
		String updownString = DataUtils.rahToStr((now / close - 1) * 100) + '%';
		if (updown > 0) {
			updownString = '+' + updownString;
		}
		return updownString;
	}

	/**
	 * 保留digit位小数
	 * 
	 * @param val
	 * @return
	 */
	public static String rahToStr(float val, int digit) {
		if (!Float.isNaN(val) && val != Float.NEGATIVE_INFINITY && val != Float.POSITIVE_INFINITY) {
			BigDecimal bd = new BigDecimal(val);
			val = bd.setScale(digit, BigDecimal.ROUND_HALF_UP).floatValue();
			if (digit == 2) {
				DecimalFormat decimalFormat = new DecimalFormat("0.00");
				return decimalFormat.format(val);
			} else if (digit == 0) {
				return String.valueOf((int) val);
			} else {
				StringBuffer sb = new StringBuffer("0.");
				for (int i = 0; i < digit; i++) {
					sb.append("0");
				}
				DecimalFormat decimalFormat = new DecimalFormat(sb.toString());
				return decimalFormat.format(val);
			}
		}
		return "";
	}
	
	public static String rahToStr2(double val, int digit) {
		if (!Double.isNaN(val) && val != Double.NEGATIVE_INFINITY && val != Double.POSITIVE_INFINITY) {
			BigDecimal bd = new BigDecimal(val);
			val = bd.setScale(digit, BigDecimal.ROUND_HALF_UP).doubleValue();
			if (digit == 2) {
				DecimalFormat decimalFormat = new DecimalFormat("0.00");
				return decimalFormat.format(val);
			} else if (digit == 0) {
				return String.valueOf((int) val);
			} else {
				StringBuffer sb = new StringBuffer("0.");
				for (int i = 0; i < digit; i++) {
					sb.append("0");
				}
				DecimalFormat decimalFormat = new DecimalFormat(sb.toString());
				return decimalFormat.format(val);
			}
		}
		return "";
	}

	/**
	 * ����2λС��,�����ȡ֮����.00/.0/��β����ȡ��
	 * 
	 * @param val
	 * @return
	 */
	public static String rahToStr(float val) {
		if (!Float.isNaN(val) && val != Float.NEGATIVE_INFINITY && val != Float.POSITIVE_INFINITY) {
			BigDecimal bd = new BigDecimal(val);
			val = bd.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
			DecimalFormat decimalFormat = new DecimalFormat("0.00");
			String value = decimalFormat.format(val);
			if (value.endsWith(".00")) {
				value = value.substring(0, value.indexOf(".00"));
			}
			return value;
		}
		return "";
	}
	/**
	 * ����2λС��,�����ȡ֮����.00/.0/��β����ȡ��
	 * 
	 * @param val
	 * @return
	 */
	public static String rahToStr(double val) {
		if (!Double.isNaN(val) && val != Double.NEGATIVE_INFINITY && val != Double.POSITIVE_INFINITY) {
			BigDecimal bd = new BigDecimal(val);
			val = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			DecimalFormat decimalFormat = new DecimalFormat("0.00");
			String value = decimalFormat.format(val);
			if (value.endsWith(".00")) {
				value = value.substring(0, value.indexOf(".00"));
			}
			return value;
		}
		return "";
	}

	/**
	 * ��������
	 * 
	 * @param val
	 * @return
	 */
	public static double roundAndHalf(double val, int digit) {
		if (val != 0) {
			BigDecimal bd = new BigDecimal(val);
			val = bd.setScale(digit, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		return val;
	}

	/**
	 * ��������
	 * 
	 * @param val
	 * @return
	 */
	public static String roundAndHalfToStr(double val) {
		java.text.NumberFormat format = java.text.NumberFormat.getInstance();
		format.setGroupingUsed(false);
		String valStr = format.format(val);
		return valStr;
	}

	/**
	 * 
	 * @param val
	 * @return
	 */
	public static float doubleTofloat(double val, int digit) {
		BigDecimal bd = new BigDecimal(val);
		float result = bd.setScale(digit, BigDecimal.ROUND_HALF_UP).floatValue();
		return result;
	}

	public static synchronized int getConstCount() {
		if (Const_Count >= 10000)
			Const_Count = 0;
		return Const_Count++;
	}

	/**
	 * ��ȡʱ��������з���
	 * 
	 * @param timearray
	 * @return
	 */
	public static int getTimeNum(String[][] timearray) {
		int timenum = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		for (int i = 0; i < timearray.length; i++) {
			String[] subarray = timearray[i];
			String startstr = subarray[0];
			String endstr = subarray[1];
			try {
				Date start = sdf.parse(startstr);
				Date end = sdf.parse(endstr);
				int sec = (int) ((end.getTime() - start.getTime()) / 60000);
				timenum += sec;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return timenum;
	}

	/**
	 * 
	 * @param timearray
	 * @return
	 */
	public static int getArrayTimeNum(String[] timearray) {
		int timenum = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String startstr = timearray[0];
		String endstr = timearray[1];
		try {
			Date start = sdf.parse(startstr);
			Date end = sdf.parse(endstr);
			int sec = (int) ((end.getTime() - start.getTime()) / 60000);
			timenum += sec;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timenum;
	}

	/**
	 * K��ͼ��ʽ���ɽ���
	 * 
	 * @param vol
	 * @return
	 */
	public static String formatVolStr(int vol, int digit) {
		StringBuffer buffer = new StringBuffer();
		String unit = "";
		double dvol = vol / 100;
		if (dvol >= 100000000) {
			dvol = dvol / 100000000.0;
			unit = "��";
		} else if (dvol >= 10000) {
			dvol = dvol / 10000.0;
			unit = "��";
		}
		dvol = roundAndHalf(dvol, digit);
		if (dvol % 1.0 == 0) {
			buffer.append((int) dvol);
		} else {
			buffer.append(dvol);
		}
		buffer.append(unit);
		return buffer.toString();
	}

	public static String formatVolStr2(int vol, int digit) {
		StringBuffer buffer = new StringBuffer();
		String unit = "";
		double dvol = vol;
		if (dvol >= 100000000) {
			dvol = dvol / 100000000.0;
			unit = "��";
		} else if (dvol >= 10000) {
			dvol = dvol / 10000.0;
			unit = "��";
		}
		dvol = roundAndHalf(dvol, digit);
		if (dvol % 1.0 == 0) {
			buffer.append((int) dvol);
		} else {
			buffer.append(dvol);
		}
		buffer.append(unit);
		return buffer.toString();
	}

	/**
	 * 
	 * @param amount
	 * @param digit
	 * @return
	 */
	public static String amount2Str(float amount, int digit) {
		String unit = "";
		boolean negative = false;
		if (amount < 0) {
			negative = true;
			amount = -amount;
		}

		double damount = amount;
		if (amount >= 100000000) {
			damount = amount / 100000000.0;
			unit = "亿";
		} else if (amount >= 10000) {
			damount = amount / 10000.0;
			unit = "万";
		}
		damount = roundAndHalf(damount, digit);
		StringBuffer buffer = new StringBuffer();
		if (negative) {
			buffer.append('-');
		} else {
			buffer.append('+');
		}
		if (damount % 1.0 == 0) {
			buffer.append((int) damount);
		} else {
			buffer.append(damount);
		}
		buffer.append(unit);
		return buffer.toString();
	}

	/**
	 * �ɽ����ʽ��
	 * 
	 * @param amount
	 * @param digit
	 * @return
	 */
	public static String amount2Str(double amount, int digit) {
		StringBuffer buffer = new StringBuffer();
		String unit = "";
		String val = "";
		if (amount < 0) {
			val = "-";
			amount = Math.abs(amount);
		}
		if (amount >= 100000000) {
			amount = amount / 100000000.0;
			unit = "��";
		} else if (amount >= 10000) {
			amount = amount / 10000.0;
			unit = "��";
		}
		amount = roundAndHalf(amount, digit);
		buffer.append(val);
		buffer.append(amount);
		buffer.append(unit);
		return buffer.toString();
	}
	
	/**
	 * �ɽ����ʽ�������治��digitλ����0
	 * @param amount
	 * @param digit
	 * @return
	 */
	public static String amount2Str2(double amount, int digit) {
		StringBuffer buffer = new StringBuffer();
		String unit = "";
		String val = "";
		if (amount < 0) {
			val = "-";
			amount = Math.abs(amount);
		}
		if (amount >= 100000000) {
			amount = amount / 100000000.0;
			unit = "��";
		} else if (amount >= 10000) {
			amount = amount / 10000.0;
			unit = "��";
		}
		String amountstr=rahToStr2(amount, digit);
		buffer.append(val);
		buffer.append(amountstr);
		buffer.append(unit);
		return buffer.toString();
	}
	
	/**
	 * ���ݽ��ĳ�������������ʾ
	 * @param amount
	 * @param digit
	 * @param length
	 * @return
	 */
	public static String amount2Str3(double amount, int digit,int length) {
		StringBuffer buffer = new StringBuffer();
		String unitStr = "";
		String val = "";
		if (amount < 0) {
			val = "-";
			amount = Math.abs(amount);
		}
		double limit = 10000;
		double unit = 10000.0;
		if(length==9){
			limit = 1000000000;
		}else if(length==8){
			limit = 100000000;
		}else if(length==7){
			limit = 10000000;
		}else if(length==6){
			limit = 1000000;
		}
		if(unit>0){
			if (amount >= limit) {
				amount = amount / unit;
				unitStr = "��";
			}
		}
		String amountstr=rahToStr2(amount, digit);
		buffer.append(val);
		buffer.append(amountstr);
		buffer.append(unitStr);
		return buffer.toString();
	}

	/**
	 * @function ������ת��ʱ��571->9:31
	 * @param minute
	 * @return
	 */
	public static String minuteToTime(int minute) {
		StringBuffer result = new StringBuffer();
		int m = minute - 1;
		int h = (m) / 60;
		int n = (m) % 60;
		if (h < 10)
			result.append("0");
		result.append(h);
		result.append(":");
		if (n < 10) {
			result.append("0");
		}
		result.append(n);
		return result.toString();
	}

	/**
	 * @function ��ȡ��ǰ������
	 * @return
	 */
	public static int getNowTimetoMin() {
		Date now = new Date();
		int result = now.getHours() * 60 + now.getMinutes();
		return result;
	}

	/**
	 * @function ������ת��ʱ��571->9:31
	 * @param minute
	 * @return
	 */
	public static String mToTime(short m) {
		StringBuffer result = new StringBuffer();
		int h = (m) / 60;
		int n = (m) % 60;
		if (h < 10)
			result.append("0");
		result.append(h);
		result.append(":");
		if (n < 10) {
			result.append("0");
		}
		result.append(n);
		return result.toString();
	}

	/**
	 * @function k��ͼ����ʱ��ת��
	 * @param ktype
	 * @param date
	 * @param minute
	 * @return
	 */
	public static String formatDate(int ktype, String date, int minute) {
		StringBuffer result = new StringBuffer();
		try {
			String min = minuteToTime(minute + 1);// ������
			if (ktype < 4 || ktype == 7) {
				if (date.length() > 4)
					date = date.substring(date.length() - 4, date.length());
				if (date.length() == 4) {
					result.append(date.substring(0, 2));
					result.append("/");
					result.append(date.substring(2, date.length()));
					result.append(" ");
				} else if (date.length() == 3) {
					result.append("0");
					result.append(date.substring(0, 1));
					result.append("/");
					result.append(date.substring(1, date.length()));
					result.append(" ");
				}
				result.append(min);
			} else {
				String year = date.substring(0, 4);// ��
				String month = date.substring(4, 6);// ��
				String day = date.substring(6, date.length());// ��
				result.append(year);
				result.append("/");
				result.append(month);
				result.append("/");
				result.append(day);
			}
		} catch (Exception e) {
			DtLog.d("DataUtil", "ʱ�������������...");
		}
		return result.toString();
	}

	/**
	 * ��ȡ��ǰ���������ڼ�<br>
	 * 
	 * @param dt
	 * @return ��ǰ���������ڼ�
	 */
	public static String getWeekOfDateStr(Date dt) {
		String[] weekDays = { "������", "����һ", "���ڶ�", "������", "������", "������", "������" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	/**
	 * ��ȡ��ǰ���������ڼ�<br>
	 * 
	 * @param dt
	 * @return ��ǰ���������ڼ�
	 */
	public static int getWeekOfDate(Date dt) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return w;
	}

	/**
	 * 
	 * @return
	 */
	public static String getMDstr() {
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_MONTH);
		StringBuffer buffer = new StringBuffer();
		buffer.append((cal.get(Calendar.MONTH) + 1));
		if (String.valueOf(day).length() < 2)
			buffer.append("0");
		buffer.append(day);
		return buffer.toString();
	}

	public static void writeFile(String jsonstr) {
		try {
			File directory = Environment.getExternalStorageDirectory();
			String path = directory.getPath();
			File f = new File(path + "/test.txt");
			if (!f.exists()) {
				f.createNewFile();
			}
			FileOutputStream fout = new FileOutputStream(path + "/test.txt");
			byte[] bytes = jsonstr.getBytes();
			fout.write(bytes);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * ��ip��������ʽת����ip��ʽ
	 * 
	 * @param ipInt
	 * @return
	 */
	public static String int2ip(int ipInt) {
		StringBuilder sb = new StringBuilder();
		sb.append(ipInt & 0xFF).append(".");
		sb.append((ipInt >> 8) & 0xFF).append(".");
		sb.append((ipInt >> 16) & 0xFF).append(".");
		sb.append((ipInt >> 24) & 0xFF);
		return sb.toString();
	}

	/**
	 * ��ȡ��ǰip��ַ
	 * 
	 * @param context
	 * @return
	 */
	public static String getLocalIpAddress(Context context) {
		try {
			WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int i = wifiInfo.getIpAddress();
			return int2ip(i);
		} catch (Exception ex) {
			return " ��ȡIP����!!!!�뱣֤��WIFI,���������´�����!\n" + ex.getMessage();
		}
	}

	/**
	 * @function ��ȡ����mac
	 * @param context
	 * @return
	 */
	public static String getLocalMacAddr(Context context) {
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}
	
	/**
	 * ��ȡ����
	 * 
	 * @return
	 */
	public static String getDevicesInfo() {
		return android.os.Build.MODEL;
	}

	/**
	 * ��ȡ��Կ
	 * 
	 * @return
	 */
	public static String readKeyFile(Context context, String filename) {
		String result = "";
		try {
			FileInputStream fin = context.openFileInput(filename);
			int i;
			while ((i = fin.read()) != -1) {
				result = result + Character.toString((char) i);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * ����ַ���
	 * 
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length) { // length��ʾ�����ַ����ĳ���
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public static void writeFileTest(String bytes) {
		try {
			File directory = Environment.getExternalStorageDirectory();
			String path = directory.getPath();
			File f = new File(path + "/1.txt");
			if (!f.exists()) {
				f.createNewFile();
			}
			FileOutputStream fout = new FileOutputStream(path + "/1.txt");
			fout.write(bytes.getBytes());
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * ���ָ�����ڵĺ�һ��
	 * 
	 * @param specifiedDay
	 * @return
	 */
	public static String getSpecifiedDayAfter(String specifiedDay) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
		} catch (Exception e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + 1);

		String dayAfter = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayAfter;
	}

	/**
	 * ���ָ�����ڵ�ǰһ��
	 * 
	 * @param specifiedDay
	 * @return
	 * @throws Exception
	 */
	public static String getSpecifiedDayBefore(String specifiedDay) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
		} catch (Exception e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - 1);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayBefore;
	}
	
	/**
	 * ת���ַ����͵�char
	 * @param s
	 * @return
	 */
	public static int charToInt(String s) {
		int returnVal = 0;
		try {
			returnVal = Integer.valueOf(s);
		} catch (Exception e) {
		    try {
				char chs[]=s.toCharArray();
				returnVal = (int)chs[0];
				if(returnVal>55){
					returnVal = returnVal - 55;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	    
	    return returnVal;
	}
	
	/**
	 * ת��int���͵�char
	 * @param s
	 * @return
	 */
	public static String intToChar(int s){
		String returnVal = s+"";
		try {
			if(s<10){
				returnVal = String.valueOf(s); 
			}else{
				int c = 65 + s - 10;
				char ch = (char)c;
				returnVal = String.valueOf(ch); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnVal;
	}
	
}
