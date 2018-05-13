package com.dengtacj.thoth;

import java.util.HashMap;

public class MapFiled {
	protected HashMap<String, byte[]> _data = new HashMap<String, byte[]>();
	protected String _strCharset = "UTF-8";

	public String getCharset() {
		return _strCharset;
	}

	public void setCharset(String strCharset) {
		this._strCharset = strCharset;
	}

	public <T> void write(String varName, T value) {
		BaseEncodeStream ostream = new BaseEncodeStream();
		ostream.setCharset(_strCharset);
		ostream.write(0, value);

		_data.put(varName, ostream.getBytes());
	}

	@SuppressWarnings("unchecked")
	public <T> T read(String varName, T varType, T def) {
		if (!_data.containsKey(varName)) {
			return def;
		}

		try {
			BaseDecodeStream istream = new BaseDecodeStream(_data.get(varName));
			istream.setCharset(_strCharset);

			Object value = istream.read(0, true, varType);
			return (T) value;
		} catch (Exception ex) {
			throw new ThothException("MapField::" + ex.getMessage());
		}
	}

	public <T> T read(String varName, T varType) {
		return read(varName, varType, null);
	}

	// 已知类型的读取
	public int readInt32(String varName) { return read(varName, (int) 0, 0); }
	public int readInt32(String varName, int def) {	return read(varName, (int) 0, def);	}

	public String readString(String varName) { return read(varName, "", ""); }
	public String readString(String varName, String def) { return read(varName, "", def); }

	public Message readMessage(String varName, Message VAR_TYPE) { return read(varName, VAR_TYPE); }
	public Message readMessage(String varName, Message def, Message VAR_TYPE) { return read(varName, VAR_TYPE, def); }
}
