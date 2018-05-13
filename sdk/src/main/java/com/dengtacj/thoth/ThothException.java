package com.dengtacj.thoth;

@SuppressWarnings("serial")
public class ThothException extends RuntimeException {
	public static int E_TYPEMISMATCH 	 = -1;
	public static int E_UNKNOWNTYPE  	 = -2;
	public static int E_FIELD_NOTFOUND 	 = -3;
	public static int E_SYSTEM_EXCEPTION = -99;

	public ThothException(String sErrorMsg) {
		super(sErrorMsg);
	}

	public ThothException(int iErrorCode, String sErrorMsg) {
		super(sErrorMsg);
		_iErrorCode = iErrorCode;
	}

	public int getErrorCode() { return _iErrorCode; }

	private int _iErrorCode = E_SYSTEM_EXCEPTION;
}
