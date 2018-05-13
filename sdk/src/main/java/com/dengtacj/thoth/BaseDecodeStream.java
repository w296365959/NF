package com.dengtacj.thoth;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.*;

public class BaseDecodeStream {
	private String _sCharset = "UTF-8";
	private ByteBuffer _baseBuffer;
	private int _iLastId = 0;

	/*********************************************************************************************
	 ** 构造函数
	 ** 
	 **/
	public BaseDecodeStream() {
		_baseBuffer = null;
	}

	public BaseDecodeStream(byte[] buffer) {
		_baseBuffer = ByteBuffer.wrap(buffer);
		_baseBuffer.position(0);
	}

	public BaseDecodeStream(byte[] buffer, int iPosition) {
		_baseBuffer = ByteBuffer.wrap(buffer);
		_baseBuffer.position(iPosition);
	}

	public BaseDecodeStream(ByteBuffer buffer) {
		_baseBuffer = buffer;
		_baseBuffer.position(0);
	}

	public BaseDecodeStream(BaseDecodeStream is) {
		_baseBuffer = is._baseBuffer;
	}

	/*********************************************************************************************
	 ** 暴露给用户的使用函数
	 ** 
	 **/
	public void setCharset(String sCharset) {
		_sCharset = sCharset;
	}

	public String getCharset() {
		return _sCharset;
	}

	public void setBuffer(byte[] buffer) {
		if (null != this._baseBuffer) {
			this._baseBuffer.clear();
		}
		this._baseBuffer = ByteBuffer.wrap(buffer);
		this._baseBuffer.position(0);
		this._iLastId = 0;
	}

	public void printHex() {
		Tools.printHex(_baseBuffer.array(), _baseBuffer.limit());
	}

	public ByteBuffer getBaseBuffer() {
		return this._baseBuffer;
	}

	/*********************************************************************************************
	 ** 暴露给外部用户的读取函数
	 ** 
	 **/
	public short   read(int iTagId, boolean required, short VAR_TYPE)   { return readInt16(iTagId, required, VAR_TYPE); }
	public int     read(int iTagId, boolean required, int VAR_TYPE)     { return readInt32(iTagId, required, VAR_TYPE); }
	public long    read(int iTagId, boolean required, long VAR_TYPE)    { return readInt64(iTagId, required, VAR_TYPE); }
	public float   read(int iTagId, boolean required, float VAR_TYPE)   { return readFloat(iTagId, required, VAR_TYPE); }
	public double  read(int iTagId, boolean required, double VAR_TYPE)  { return readDouble(iTagId, required, VAR_TYPE); }
	public String read(int iTagId, boolean required, String VAR_TYPE)  { return readString(iTagId, required, VAR_TYPE); }
	public byte[]  read(int iTagId, boolean required, byte[] VAR_TYPE)  { return readBytes(iTagId, required, VAR_TYPE); }
	public Message read(int iTagId, boolean required, Message VAR_TYPE) { return readMessage(iTagId, required, null, VAR_TYPE);  		}
	public <T> ArrayList<T> read(int iTagId, boolean required, ArrayList<T> VAR_TYPE) { return readList(iTagId, required, VAR_TYPE); 	}
	public <K, V> Map<K, V> read(int iTagId, boolean required, Map<K, V> VAR_TYPE) 	  { return readMap(iTagId, required, VAR_TYPE);     }

	public Object read(int iTagId, boolean required, Object VAR_TYPE) { //MapField需要用到
		if (VAR_TYPE instanceof Boolean) 			{
			return readBoolean(iTagId, required, (Boolean)VAR_TYPE);
		} else if (VAR_TYPE instanceof Byte) 		{
			return readInt8(iTagId, required, (Byte) VAR_TYPE);
		} else if (VAR_TYPE instanceof Short)		{
			return readInt16(iTagId, required, (Short) VAR_TYPE);
		} else if (VAR_TYPE instanceof Integer) 	{
			return readInt32(iTagId, required, (Integer) VAR_TYPE);
		} else if (VAR_TYPE instanceof Long)		{
			return readInt64(iTagId, required, (Long) VAR_TYPE);
		} else if (VAR_TYPE instanceof Float)		{
			return readFloat(iTagId, required, (Float) VAR_TYPE);
		} else if (VAR_TYPE instanceof Double)		{
			return readDouble(iTagId, required, (Double) VAR_TYPE);
		} else if (VAR_TYPE instanceof String)		{
			return readString(iTagId, required, (String) VAR_TYPE);
		} else if (VAR_TYPE instanceof byte[])		{
			return readBytes(iTagId, required, (byte[]) VAR_TYPE);
		} else if (VAR_TYPE instanceof Message) 	{
			return readMessage(iTagId, required, (Message) VAR_TYPE);
		} else if (VAR_TYPE instanceof Collection) 	{
			return readList(iTagId, required, (ArrayList<?>) VAR_TYPE);
		} else if (VAR_TYPE instanceof Map)			{
			return readMap(iTagId, required, (Map<?,?>) VAR_TYPE);
		} else {
			throw new ThothException("BaseDecodeStream::read:unknown var type");
		}
	}

	// 读取Boolean类型的数据
	public boolean readBoolean(int iTagId) { return readBoolean(iTagId, false, false); }
	public boolean readBoolean(int iTagId, boolean required) { return readBoolean(iTagId, required, false); }
	public boolean readBoolean(int iTagId, boolean required, boolean def) {
		byte realType = _findFiledByTagId(iTagId, FieldType.FT_TRUE, FieldType.FT_FALSE, required);
		if (realType == FieldType.FT_UNCHECK) {
			return def;
		}
		return realType == FieldType.FT_TRUE ? true : false;
	}

	// 读取Int8/Byte类型的数据
	public byte readInt8(int iTagId) { return readInt8(iTagId, false, (byte) 0); }
	public byte readInt8(int iTagId, boolean required) { return readInt8(iTagId, false, (byte) 0); }
	public byte readInt8(int iTagId, boolean required, byte def) { return (byte) readInt32(iTagId, required, (int) def); }

	// 读取Int16/short类型的数据
	public short readUInt8(int iTagId) { return (short) readUInt16(iTagId, false, 0);}
	public short readUInt8(int iTagId, boolean required) { return (short) readUInt16(iTagId, required, 0); }
	public short readUInt8(int iTagId, boolean required, short def) { return (short) readInt32(iTagId, required, (int) def); }

	// 读取Int16类型的数据
	public short readInt16(int iTagId) { return readInt16(iTagId, false, (short) 0); }
	public short readInt16(int iTagId, boolean required) { return readInt16(iTagId, false, (short) 0); }
	public short readInt16(int iTagId, boolean required, short def) { return (short) readInt32(iTagId, required, (int) def); }

	// 读取UInt16类型的数据
	public int readUInt16(int iTagId) {	return readUInt16(iTagId, false, (int) 0); }
	public int readUInt16(int iTagId, boolean required) { return readUInt16(iTagId, false, (int) 0); }
	public int readUInt16(int iTagId, boolean required, int def) { return (int) readInt32(iTagId, required, (int) def);	}

	// 读取Int32类型的数据
	public int readInt32(int iTagId) { return readInt32(iTagId, false, 0); }
	public int readInt32(int iTagId, boolean required) { return readInt32(iTagId, required, 0); }
	public int readInt32(int iTagId, boolean required, int def) {
		byte realType = _findFiledByTagId(iTagId, FieldType.FT_INT,	FieldType.FT_ZERO, required);
		if (realType == FieldType.FT_UNCHECK) {
			return def;
		}

		return realType == FieldType.FT_ZERO ? 0 : decodeZigZag32(_readRawVarint32());
	}

	// 读取UInt32类型的数据
	public long readUInt32(int iTagId) { return readUInt32(iTagId, false, 0); }
	public long readUInt32(int iTagId, boolean required) { return readUInt32(iTagId, required, 0); }
	public long readUInt32(int iTagId, boolean required, long def) { return (long) readInt64(iTagId, required, def); }

	// 读取Int64类型的数据
	public long readInt64(int iTagId) {	return readInt64(iTagId, false, (long) 0); }
	public long readInt64(int iTagId, boolean required) { return readInt64(iTagId, required, (long) 0); }
	public long readInt64(int iTagId, boolean required, long def) {
		byte realType = _findFiledByTagId(iTagId, FieldType.FT_INT,	FieldType.FT_ZERO, required);
		if (realType == FieldType.FT_UNCHECK) {
			return def;
		}

		return realType == FieldType.FT_ZERO ? 0 : decodeZigZag64(_readRawVarint64());
	}

	// 读取float类型的数据
	public float readFloat(int iTagId) { return readFloat(iTagId, false, (float) 0); }
	public float readFloat(int iTagId, boolean required) { return readFloat(iTagId, required, (float) 0); }
	public float readFloat(int iTagId, boolean required, float def) {
		byte realType = _findFiledByTagId(iTagId, FieldType.FT_FLOAT, FieldType.FT_ZERO, required);
		if (realType == FieldType.FT_UNCHECK) {
			return def;
		}

		return _baseBuffer.getFloat();
	}

	// 读取Double类型的数据
	public double readDouble(int iTagId) { return readDouble(iTagId, false, (double) 0); }
	public double readDouble(int iTagId, boolean required) { return readDouble(iTagId, required, (double) 0); }
	public double readDouble(int iTagId, boolean required, double def) {
		byte realType = _findFiledByTagId(iTagId, FieldType.FT_DOUBLE, FieldType.FT_ZERO, required);
		if (realType == FieldType.FT_UNCHECK) {
			return def;
		}
		return _baseBuffer.getDouble();
	}

	// 读取String类型的数据
	public String readString(int iTagId) { return readString(iTagId, false, ""); }
	public String readString(int iTagId, boolean required) { return readString(iTagId, required, ""); }
	public String readString(int iTagId, boolean required, String def) {
		byte realType = _findFiledByTagId(iTagId, FieldType.FT_STRING, FieldType.FT_UNCHECK, required);
		if (realType == FieldType.FT_UNCHECK) {
			return def;
		}

		return _readString();
	}

	// 读取二进制Buffer类型的数据
	public byte[] readBytes(int iTagId) { return readBytes(iTagId, false, new byte[0]); }
	public byte[] readBytes(int iTagId, boolean required) {	return readBytes(iTagId, required, new byte[0]); }
	public byte[] readBytes(int iTagId, boolean required, byte[] def) {
		byte realType = _findFiledByTagId(iTagId, FieldType.FT_BYTES, FieldType.FT_UNCHECK, required);
		if (realType == FieldType.FT_UNCHECK) {
			return def;
		}

		return _readBytes();
	}

	// 读取Message类型的数据
	public Message readMessage(int iTagId, Message VAR_TYPE) { return readMessage(iTagId, false, null, VAR_TYPE); }
	public Message readMessage(int iTagId, boolean required, Message VAR_TYPE) { return readMessage(iTagId, required, null, VAR_TYPE); }
	public Message readMessage(int iTagId, boolean required, Message def, Message VAR_TYPE) {
		byte realType = _findFiledByTagId(iTagId, FieldType.FT_MESSAGE, FieldType.FT_UNCHECK, required);
		if (realType == FieldType.FT_UNCHECK) {
			return def;
		}

		return _readMessage(VAR_TYPE);
	}

	// 读取List类型的数据
	public <T> ArrayList<T> readList(int iTagId, ArrayList<T> VAR_TYPE) { return readList(iTagId, false, new ArrayList<T>(), VAR_TYPE); }
	public <T> ArrayList<T> readList(int iTagId, boolean required, ArrayList<T> VAR_TYPE) { return readList(iTagId, required, new ArrayList<T>(), VAR_TYPE); }
	public <T> ArrayList<T> readList(int iTagId, boolean required, ArrayList<T> def, ArrayList<T> VAR_TYPE) {
		byte realType = _findFiledByTagId(iTagId, FieldType.FT_LIST, FieldType.FT_UNCHECK, required);
		if (realType == FieldType.FT_UNCHECK) {
			return def;
		}

		return _readList(def, VAR_TYPE);
	}

	// 读取Map类型的数据
	public <K, V> Map<K, V> readMap(int iTagId, Map<K, V> VAR_TYPE) { return readMap(iTagId, false, new HashMap<K, V>(), VAR_TYPE); }
	public <K, V> Map<K, V> readMap(int iTagId, boolean required, Map<K, V> VAR_TYPE) {	return readMap(iTagId, required, new HashMap<K, V>(), VAR_TYPE); }
	public <K, V> Map<K, V> readMap(int iTagId, boolean required, Map<K, V> def, Map<K, V> VAR_TYPE) {
		byte realType = _findFiledByTagId(iTagId, FieldType.FT_MAP,	FieldType.FT_UNCHECK, required);
		if (realType == FieldType.FT_UNCHECK) {
			return def;
		}

		return _readMap(def, VAR_TYPE);
	}

	/*********************************************************************************************
	 ** 内部使用函数
	 **
	 **/
	private class pair {
		public int tag = 0;
		public byte type = 0;
	};

	private int _readFiled(pair current) {
		ByteBuffer temp = _baseBuffer.duplicate();
		byte item       = temp.get();
		current.tag     = ((item & (15 << 4)) >> 4);
		current.type    = (byte) (item & 0x0F);

		if (current.tag == 0x0F) {
			current.tag = temp.getShort();
			return 3;
		} else {
			current.tag = _iLastId + current.tag;
			return 1;
		}
	}

	private void _skipFiledByType(byte iType) {
		switch (iType) {
			case FieldType.FT_ZERO  :
			case FieldType.FT_TRUE  :
			case FieldType.FT_FALSE : break;
			case FieldType.FT_INT   : _readRawVarint32(); break;
			case FieldType.FT_FLOAT : _baseBuffer.position(_baseBuffer.position() + 4); break;
			case FieldType.FT_DOUBLE: _baseBuffer.position(_baseBuffer.position() + 8); break;
			case FieldType.FT_STRING:
			case FieldType.FT_BYTES : 	{
				int size = _readRawVarint32();
				_baseBuffer.position(_baseBuffer.position() + size);
				break;
			}
			case FieldType.FT_MESSAGE: 	{
				_findMessageStop();
				break;
			}
			case FieldType.FT_LIST: 	{
				int size = _readRawVarint32();
				if (size == 0) {
					break;
				}
				byte type = _baseBuffer.get();
				for (int i = 0; i < size; i++) {
					_skipFiledByType(type);
				}
				break;
			}
			case FieldType.FT_MAP: 		{
				int size = _readRawVarint32();
				if (size == 0) {
					break;
				}

				byte item = _baseBuffer.get();
				byte k = (byte) ((item & (15 << 4)) >> 4);
				byte v = (byte) (item & 0x0F);

				for (int i = 0; i < size; i++) {
					_skipFiledByType(k);
					_skipFiledByType(v);
				}
				break;
			}
			default: throw new ThothException(ThothException.E_UNKNOWNTYPE,	"BaseInputStream::_skipFiledByType: unknown type, " + iType);
		}
	}

	private byte _findFiledByTagId(int iTagId, byte aMay, byte bMay, boolean require) {
		pair current = new pair();

		while (_baseBuffer.remaining() > 0) {
			int size = _readFiled(current);
			if (current.type == FieldType.FT_MESSAGE_STOP || iTagId < current.tag) {
				break;
			}
			this._iLastId = current.tag;
			this._baseBuffer.position(this._baseBuffer.position() + size);

			if (current.tag == iTagId) {
				if (current.type == aMay || current.type == bMay) {
					return current.type;
				} else {
					throw new ThothException(ThothException.E_FIELD_NOTFOUND, "BaseInputStream field type mismatch:" + iTagId + "," + aMay + "," + bMay + "," + current.type);
				}
			}

			_skipFiledByType(current.type);
		}

		if (require) {
			throw new ThothException(ThothException.E_FIELD_NOTFOUND, "BaseInputStream Field Not Found:" + iTagId + "," + aMay + "," + bMay);
		}

		return FieldType.FT_UNCHECK;
	}

	private void _findMessageStop() {
		pair current = new pair();
		while (_baseBuffer.remaining() > 0) {
			int size = _readFiled(current);
			this._baseBuffer.position(this._baseBuffer.position() + size);

			if (current.type == FieldType.FT_MESSAGE_STOP) {
				return ;
			}
			this._skipFiledByType(current.type);
		}

		throw new ThothException("BaseInputStream Find Filed (FT_MESSAGE_STOP) Fault");
	}

	private int  decodeZigZag32(final int n)  { return (n >>> 1) ^ -(n & 1); }
	private long decodeZigZag64(final long n) {	return (n >>> 1) ^ -(n & 1); }

	private byte I_TYPE(Object value) 			{
		if (value instanceof Boolean) 			{
			return FieldType.FT_TRUE;
		} else if (value instanceof Byte) 		{
			return FieldType.FT_INT;
		} else if (value instanceof Short) 		{
			return FieldType.FT_INT;
		} else if (value instanceof Integer) 	{
			return FieldType.FT_INT;
		} else if (value instanceof Long) 		{
			return FieldType.FT_INT;
		} else if (value instanceof String) 	{
			return FieldType.FT_STRING;
		} else if (value instanceof Float) 		{
			return FieldType.FT_FLOAT;
		} else if (value instanceof Double) 	{
			return FieldType.FT_DOUBLE;
		} else if (value instanceof Map) 		{
			return FieldType.FT_MAP;
		} else if (value instanceof Collection) {
			return FieldType.FT_LIST;
		} else if (value instanceof Message) 	{
			return FieldType.FT_MESSAGE;
		} else if (value instanceof byte[]) 	{
			return FieldType.FT_BYTES;
		}

		return FieldType.FT_UNCHECK;
	}

	private Object _read(Object value) 			{
		if (value instanceof Byte) 				{
			return decodeZigZag32(_readRawVarint32());
		} else if (value instanceof Short) 		{
			return decodeZigZag32(_readRawVarint32());
		} else if (value instanceof Integer) 	{
			return decodeZigZag32(_readRawVarint32());
		} else if (value instanceof Long) 		{
			return decodeZigZag64(_readRawVarint64());
		} else if (value instanceof Float) 		{
			return _readFloat();
		} else if (value instanceof Double) 	{
			return _readDouble();
		} else if (value instanceof String) 	{
			return _readString();
		} else if (value instanceof Map) 		{
			return _readMap(null, (Map<?, ?>) value);
		} else if (value instanceof Message) 	{
			return _readMessage((Message) value);
		} else if (value instanceof byte[]) 	{
			return _readBytes();
		} else if (value instanceof ArrayList) 	{
			return _readList(null, (ArrayList<?>) value);
		} else {
			throw new ThothException("BaseDecodeStream::_read type mismatch");
		}
	}

	private int _readRawVarint32() {
		int result = 0;
		int shift = 0;

		if (_baseBuffer.remaining() >= 5) {
			byte[] buf = _baseBuffer.array();
			int pos = _baseBuffer.position();
			int off = 0;

			while (true) {
				byte b = buf[pos + off];
				result |= (int) (b & 0x7f) << shift;
				if ((b & 0x80) != 0x80)
					break;
				shift += 7;
				off++;
			}
			_baseBuffer.position(_baseBuffer.position() + off + 1);
		} else {
			while (true) {
				byte b = _baseBuffer.get();
				result |= (int) (b & 0x7f) << shift;
				if ((b & 0x80) != 0x80)
					break;
				shift += 7;
			}
		}

		return result;
	}

	private long _readRawVarint64() {
		fastpath: {
			int pos = this._baseBuffer.position();
			if (this._baseBuffer.limit() == pos) {
				break fastpath;
			}

			final byte[] buffer = _baseBuffer.array();
			long x;
			int y;
			if ((y = buffer[pos++]) >= 0) {
				this._baseBuffer.position(pos);
				return y;
			} else if (this._baseBuffer.limit() - pos < 9) {
				break fastpath;
			} else if ((y ^= (buffer[pos++] << 7)) < 0) {
				x = y ^ (~0 << 7);
			} else if ((y ^= (buffer[pos++] << 14)) >= 0) {
				x = y ^ ((~0 << 7) ^ (~0 << 14));
			} else if ((y ^= (buffer[pos++] << 21)) < 0) {
				x = y ^ ((~0 << 7) ^ (~0 << 14) ^ (~0 << 21));
			} else if ((x = ((long) y) ^ ((long) buffer[pos++] << 28)) >= 0L) {
				x ^= (~0L << 7) ^ (~0L << 14) ^ (~0L << 21) ^ (~0L << 28);
			} else if ((x ^= ((long) buffer[pos++] << 35)) < 0L) {
				x ^= (~0L << 7) ^ (~0L << 14) ^ (~0L << 21) ^ (~0L << 28)
						^ (~0L << 35);
			} else if ((x ^= ((long) buffer[pos++] << 42)) >= 0L) {
				x ^= (~0L << 7) ^ (~0L << 14) ^ (~0L << 21) ^ (~0L << 28)
						^ (~0L << 35) ^ (~0L << 42);
			} else if ((x ^= ((long) buffer[pos++] << 49)) < 0L) {
				x ^= (~0L << 7) ^ (~0L << 14) ^ (~0L << 21) ^ (~0L << 28) ^ (~0L << 35) ^ (~0L << 42) ^ (~0L << 49);
			} else {
				x ^= ((long) buffer[pos++] << 56);
				x ^= (~0L << 7) ^ (~0L << 14) ^ (~0L << 21) ^ (~0L << 28) ^ (~0L << 35) ^ (~0L << 42) ^ (~0L << 49) ^ (~0L << 56);
				if (x < 0L) {
					if (buffer[pos++] < 0L) {
						break fastpath; // Will throw malformedVarint()
					}
				}
			}
			this._baseBuffer.position(pos);
			return x;
		}

		long result = 0;
		for (int shift = 0; shift < 64; shift += 7) {
			final byte b = this._baseBuffer.get();
			result |= (long) (b & 0x7F) << shift;
			if ((b & 0x80) == 0) {
				return result;
			}
		}

		throw new ThothException("read varint fault");
	}

	private float _readFloat() {
		return _baseBuffer.getFloat();
	}

	private double _readDouble() {
		return _baseBuffer.getDouble();
	}

	private String _readString() {
		String value;
		int size = _readRawVarint32();

		if (size == 0) {
			return "";
		}

		try {
			value = new String(_baseBuffer.array(), _baseBuffer.position(),	size, _sCharset);
		} catch (UnsupportedEncodingException e) {
			throw new ThothException(ThothException.E_SYSTEM_EXCEPTION,	e.getMessage());
		}

		_baseBuffer.position(_baseBuffer.position() + size);
		return value;
	}

	private byte[] _readBytes() {
		int size = _readRawVarint32();
		byte[] value = new byte[size];

		_baseBuffer.get(value);
		return value;
	}

	private Message _readMessage(Message def) {
		try {
			Message value = def.getClass().newInstance(); // 新创建一个结构体
			value.read(this); // 调用结构体的读取函数

			this._findMessageStop();
			return value;
		} catch (Exception e) {
			throw new ThothException(ThothException.E_SYSTEM_EXCEPTION, e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	private <T> ArrayList<T> _readList(ArrayList<T> def, ArrayList<T> VAR_TYPE) {
		int size = _readRawVarint32();
		if (size == 0) {
			return def;
		}

		T VART  = VAR_TYPE.get(0);
		byte kt = this._baseBuffer.get();
		byte rt = I_TYPE(VART); 
		if (rt != kt) {
			throw new ThothException("BaseInputStream::read list:type mismactch:" + rt + "," + kt);
		}

		ArrayList<T> res = new ArrayList<T>();
		for (int i = 0; i < size; i++) {
			T item = (T) _read(VART);
			res.add(item);
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	private <K, V> Map<K, V> _readMap(Map<K, V> def, Map<K, V> VAR_TYPE) {
		int size = _readRawVarint32();
		if (size == 0) {
			return def;
		}

		Iterator<Map.Entry<K, V>> it = VAR_TYPE.entrySet().iterator();
		Map.Entry<K, V> en = it.next();
		K mk = en.getKey();
		V mv = en.getValue();

		byte item = _baseBuffer.get();
		byte kt = (byte) ((item & (15 << 4)) >> 4);
		byte vt = (byte) (item & 0x0F);

		if (I_TYPE(mk) != kt || I_TYPE(mv) != vt) {
			throw new ThothException("BaseInputStream::read map:type mismactch");
		}

		try {
			Map<K, V> result = (Map<K, V>) VAR_TYPE.getClass().newInstance();
			for (int i = 0; i < size; i++) {
				K key = (K) _read(mk);
				V val = (V) _read(mv);
				result.put(key, val);
			}
			return result;
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}

		return VAR_TYPE;
	}
};
