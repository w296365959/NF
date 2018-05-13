package com.sscf.investment.sdk.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

/**
 * 通信格式转换
 * 
 * Java 和一些windows编程语言如c、c++、delphi所写的网络程序进行通讯时， 需要进行相应的转换 高、低字节之间的转换
 * 
 * windows的字节序为低字节开头 linux,unix的字节序为高字节开头 java则无论平台变化，都是高字节开头
 * 
 */

public class FormatTransfer {
	/**
	 * 将 int转为低字节在前，高字节在后的byte数组
	 * 
	 * @param n
	 *            int
	 * @return byte[]
	 */
	public static byte[] toLH(int n) {
		byte[] b = new byte[4];
		b[0] = (byte) (n & 0xff);
		b[1] = (byte) (n >> 8 & 0xff);
		b[2] = (byte) (n >> 16 & 0xff);
		b[3] = (byte) (n >> 24 & 0xff);
		return b;
	}

	/**
	 * 将short类型转为低字节在前，高字节在后的short类型
	 * 
	 * @param in
	 *            short
	 * @return short
	 */
	public static short toLH_Short(short in) {
		short out = 0;
		out |= (in & 0xff) << 8;
		out |= (in & 0xff00) >> 8;
		return out;
	}

	/**
	 * 将 int转为高字节在前，低字节在后的byte数组
	 * 
	 * @param n
	 *            int
	 * @return byte[]
	 */
	public static byte[] toHH(int n) {
		byte[] b = new byte[4];
		b[3] = (byte) (n & 0xff);
		b[2] = (byte) (n >> 8 & 0xff);
		b[1] = (byte) (n >> 16 & 0xff);
		b[0] = (byte) (n >> 24 & 0xff);
		return b;
	}

	/**
	 * 将 short转为低字节在前，高字节在后的byte数组
	 * 
	 * @param n
	 *            short
	 * @return byte[]
	 */
	public static byte[] toLH(short n) {
		byte[] b = new byte[2];
		b[0] = (byte) (n & 0xff);
		b[1] = (byte) (n >> 8 & 0xff);
		return b;
	}

	/**
	 * 将 long 转为低字节在前，高字节在后的byte数组
	 * 
	 * @param n
	 *            long
	 * @return byte[]
	 */
	public static byte[] toLH(long n) {
		byte[] b = new byte[8];
		b[0] = (byte) (n & 0xff);
		b[1] = (byte) (n >> 8 & 0xff);
		b[2] = (byte) (n >> 16 & 0xff);
		b[3] = (byte) (n >> 24 & 0xff);
		b[4] = (byte) (n >> 32 & 0xff);
		b[5] = (byte) (n >> 40 & 0xff);
		b[6] = (byte) (n >> 48 & 0xff);
		b[7] = (byte) (n >> 56 & 0xff);

		return b;
	}

	/**
	 * 将 short转为高字节在前，低字节在后的byte数组
	 * 
	 * @param n
	 *            short
	 * @return byte[]
	 */
	public static byte[] toHH(short n) {
		byte[] b = new byte[2];
		b[1] = (byte) (n & 0xff);
		b[0] = (byte) (n >> 8 & 0xff);
		return b;
	}

	/**
	 * -----------------------------------------------
	 * 
	 * 将将int转为高字节在前，低字节在后的byte数组
	 * -------------------------------------------------
	 * 
	 * public static byte[] toHH(int number) { int temp = number; byte[] b = new
	 * byte[4]; for (int i = b.length - 1; i > -1; i--) { b = new Integer(temp &
	 * 0xff).byteValue(); temp = temp >> 8; } return b; }
	 * 
	 * public static byte[] IntToByteArray(int i) { byte[] abyte0 = new byte[4];
	 * abyte0[3] = (byte) (0xff & i); abyte0[2] = (byte) ((0xff00 & i) >> 8);
	 * abyte0[1] = (byte) ((0xff0000 & i) >> 16); abyte0[0] = (byte)
	 * ((0xff000000 & i) >> 24); return abyte0; }
	 * 
	 * --------------------------------------------------
	 */

	/**
	 * 将 float转为低字节在前，高字节在后的byte数组
	 * 
	 * @param f
	 *            float
	 * @return byte[]
	 */
	public static byte[] toLH(float f) {
		return toLH(Float.floatToRawIntBits(f));
	}

	/**
	 * 将 float转为高字节在前，低字节在后的byte数组
	 * 
	 * @param f
	 *            float
	 * @return byte[]
	 */
	public static byte[] toHH(float f) {
		return toHH(Float.floatToRawIntBits(f));
	}

	/**
	 * 将 String转为byte数组
	 */
	public static byte[] stringToBytes(String s, int length) {
		while (s.getBytes().length < length) {
			s += " ";
		}
		return s.getBytes();
	}

	/**
	 * 将字节数组转换为String
	 * 
	 * @param b
	 *            byte[]
	 * @return String
	 */
	public static String bytesToString(byte[] b) {
		if(null==b)
			return "";
		String s = "";
		try {
			s = new String(b, "GB2312");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return s;
	}

	public static String bytesToString(byte[] b, int offset, int length) {
		String s = "";
		try {
			s = new String(b, offset, length, "GB2312");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	public static char[] bytesToChars(byte[] bytes) {
		char[] chars = new char[bytes.length / 2];
		int b1, b2;
		for (int b = 0, c = 0; b < bytes.length; b += 2, c++) {
			b1 = (int) bytes[b] & 0xFF;
			b2 = (int) bytes[b + 1] & 0xFF;
			chars[c] = (char) ((b2 << 8) + b1);
		}
		return chars;
	}

	/**
	 * 将字符串转换为byte数组
	 * 
	 * @param s
	 *            String
	 * @return byte[]
	 */
	public static byte[] stringToBytes(String s) {
		return s.getBytes();
	}

	/**
	 * 将高字节数组转换为int
	 * 
	 * @param b
	 *            byte[]
	 * @return int
	 */
	public static int hBytesToInt(byte[] b) {
		int s = 0;
		for (int i = 0; i < 3; i++) {
			if (b[i] >= 0) {
				s = s + b[i];
			} else {
				s = s + 256 + b[i];
			}
			s = s * 256;
		}
		if (b[3] >= 0) {
			s = s + b[3];
		} else {
			s = s + 256 + b[3];
		}
		return s;
	}

	/**
	 * 将低字节数组转换为int
	 * 
	 * @param b
	 *            byte[]
	 * @return int
	 */
	public static int lBytesToInt(byte[] b) {
		int s = 0;
		for (int i = 0; i < 3; i++) {
			if (b[3 - i] >= 0) {
				s = s + b[3 - i];
			} else {
				s = s + 256 + b[3 - i];
			}
			s = s * 256;
		}
		if (b[0] >= 0) {
			s = s + b[0];
		} else {
			s = s + 256 + b[0];
		}
		return s;
	}

	/**
	 * 高字节数组到short的转换
	 * 
	 * @param b
	 *            byte[]
	 * @return short
	 */
	public static short hBytesToShort(byte[] b) {
		int s = 0;
		if (b[0] >= 0) {
			s = s + b[0];
		} else {
			s = s + 256 + b[0];
		}
		s = s * 256;
		if (b[1] >= 0) {
			s = s + b[1];
		} else {
			s = s + 256 + b[1];
		}
		short result = (short) s;
		return result;
	}

	/**
	 * 低字节数组到short的转换
	 * 
	 * @param b
	 *            byte[]
	 * @return short
	 */
	public static short lBytesToShort(byte[] b) {
		int s = 0;
		if (b[1] >= 0) {
			s = s + b[1];
		} else {
			s = s + 256 + b[1];
		}
		s = s * 256;
		if (b[0] >= 0) {
			s = s + b[0];
		} else {
			s = s + 256 + b[0];
		}
		short result = (short) s;
		return result;
	}

	/**
	 * 高字节数组转换为float
	 * 
	 * @param b
	 *            byte[]
	 * @return float
	 */
	public static float hBytesToFloat(byte[] b) {
		int i = 0;
		// Float F = new Float(0.0);
		i = ((((b[0] & 0xff) << 8 | (b[1] & 0xff)) << 8) | (b[2] & 0xff)) << 8 | (b[3] & 0xff);
		return Float.intBitsToFloat(i);
	}

	/**
	 * 低字节数组转换为float
	 * 
	 * @param b
	 *            byte[]
	 * @return float
	 */
	public static float lBytesToFloat(byte[] b) {
		int i = 0;
		// Float F = new Float(0.0);
		i = ((((b[3] & 0xff) << 8 | (b[2] & 0xff)) << 8) | (b[1] & 0xff)) << 8 | (b[0] & 0xff);
		return Float.intBitsToFloat(i);
	}

	/**
	 * 将 byte数组中的元素倒序排列
	 */
	public static byte[] bytesReverseOrder(byte[] b) {
		int length = b.length;
		byte[] result = new byte[length];
		for (int i = 0; i < length; i++) {
			result[length - i - 1] = b[i];
		}
		return result;
	}

	/**
	 * 打印byte数组
	 */
	public static void printBytes(byte[] bb) {
		int length = bb.length;
		for (int i = 0; i < length; i++) {
			System.out.print(bb + " ");
		}
	}

	public static void logBytes(byte[] bb) {
		int length = bb.length;
		String out = "";
		for (int i = 0; i < length; i++) {
			out = out + bb + " ";
		}

	}

	/**
	 * 将 int类型的值转换为字节序颠倒过来对应的int值
	 * 
	 * @param i
	 *            int
	 * @return int
	 */
	public static int reverseInt(int i) {
		int result = FormatTransfer.hBytesToInt(FormatTransfer.toLH(i));
		return result;
	}

	/**
	 * 将 short类型的值转换为字节序颠倒过来对应的short值
	 * 
	 * @param s
	 *            short
	 * @return short
	 */
	public static short reverseShort(short s) {
		short result = FormatTransfer.hBytesToShort(FormatTransfer.toLH(s));
		return result;
	}

	/**
	 * 将 float类型的值转换为字节序颠倒过来对应的float值
	 * 
	 * @param f
	 *            float
	 * @return float
	 */
	public static float reverseFloat(float f) {
		float result = FormatTransfer.hBytesToFloat(FormatTransfer.toLH(f));
		return result;
	}

	/**
	 * @功能: 将一个长度为2 byte数组转为short
	 * 
	 * @param: byte[] buffer 要转的字节数组
	 * @param: int offset 字节数组的起始位置
	 * @param: int length 字节数组的长度
	 * 
	 * @return: short value 转后的short值
	 */
	public static final short bytesToShort(byte buffer[], int offset, int length) {
		short value = 0;
		value = (short) bytesToInt(buffer, offset, length);
		return value;
	}

	/**
	 * @功能: 将一个长度为4 byte数组转为int
	 * 
	 * @param: byte[] buffer 要转的字节数组
	 * @param: int offset 字节数组的起始位置
	 * @param: int length 字节数组的长度
	 * 
	 * @return: int IByte 转后的int值
	 */
	public static final int bytesToInt(byte buffer[], int offset, int length) {
		int IByte = 0;
		try {
			for (int i = length - 1; i >= 0; i--) {
				IByte = (IByte << 8) + (int) (buffer[offset + i] < 0 ? buffer[offset + i] + 256 : buffer[offset + i]);
			}
		} catch (Exception ex) {
		}
		return IByte;
	}

	/**
	 * @功能:将字节数组转换为对应的long值
	 * @参数: b byte[]
	 * @返回值: long
	 */
	public static long bytesToLong(byte[] b, int offset, int length) {
		long d = 0;
		for (int i = length - 1; i >= 0; i--) {
			d = (d << 8) | (b[offset + i] & 0x00000000000000ff);
		}

		return d;
	}

	/**
	 * @功能: 将一个长度为4 byte数组转为float
	 * 
	 * @param: byte[] buffer 要转的字节数组
	 * @param: int offset 字节数组的起始位置
	 * @param: int length 字节数组的长度
	 * 
	 * @return: float value 转后的float值
	 */
	public static final float bytesToFloat(byte buffer[], int offset, int length) {
		float value = 0;
		int bits = 0;
		for (int i = 3; i >= 0; i--) {
			bits = (bits << 8) + (int) (buffer[offset + i] < 0 ? buffer[offset + i] + 256 : buffer[offset + i]);
		}

		value = Float.intBitsToFloat(bits);

		return value;
	}

	/**
	 * 取小数点后两位，格式化数据
	 */
	public static final String DataFormat(float data, int accuracy) {
		String str = String.valueOf(data);

		// 四舍五入，保留两位小数
		BigDecimal mData = new BigDecimal(str).setScale(accuracy, BigDecimal.ROUND_HALF_UP);
		return mData.toString();
	}

	/**
	 * 格式化成交量，金额数据，保留小数点后一位
	 */
	public static final String AmountFormat(float data) {
		String amount = "";
		if (data >= 100000000) {
			data = (float) (data / 100000000.0);
			amount = "亿";
		} else if (data >= 10000) {
			data = (float) (data / 10000.0);
			amount = "万";
		}
		String str = String.valueOf(data);

		// 四舍五入，保留一位小数
		BigDecimal mData = new BigDecimal(str).setScale(1, BigDecimal.ROUND_HALF_UP);
		return mData.toString() + amount;
	}

	public static byte[] shortToByteArray(short s) {
		byte[] shortBuf = new byte[2];
		for (int i = 0; i < 2; i++) {
			int offset = (shortBuf.length - 1 - i) * 8;
			shortBuf[i] = (byte) ((s >>> offset) & 0xff);
		}
		return shortBuf;
	}

	public static byte[] intToBytes(int value) {
		byte[] src = new byte[4];
		src[3] = (byte) ((value >> 24) & 0xFF);
		src[2] = (byte) ((value >> 16) & 0xFF);
		src[1] = (byte) ((value >> 8) & 0xFF);
		src[0] = (byte) (value & 0xFF);
		return src;
	}
}