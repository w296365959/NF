package com.dengtacj.thoth;

public class FieldType {
	public static final byte FT_ZERO  			= 0x00;	//0
	public static final byte FT_FALSE 			= 0X01;	//1
	public static final byte FT_TRUE  			= 0x02; //2
	public static final byte FT_INT    		 	= 0x03; //3
	public static final byte FT_FLOAT	   		= 0x04; //4
	public static final byte FT_DOUBLE       	= 0x05; //5
	public static final byte FT_STRING			= 0x06; //6
	public static final byte FT_BYTES        	= 0x07; //7
	public static final byte FT_LIST         	= 0x08; //8
	public static final byte FT_SET          	= 0x09; //9
	public static final byte FT_MAP          	= 0x0A; //10
	public static final byte FT_MESSAGE 		= 0x0B; //11
	public static final byte FT_MESSAGE_STOP	= 0x0C; //12
	public static final byte FT_UNCHECK			= 0X1F;
};
