package com.dengtacj.thoth;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class BaseEncodeStream 
{
	/*********************************************************************************************
	 * 定义一个ByteBuffer的读写类
	 * 当需要写入到数据流时，BaseOutputStream不再关心内存分配问题，全部交给该类来处理
	 */
	private class BaseBuffer {
		private ByteBuffer _buffer;

		public BaseBuffer(int iLength) {
			_buffer = ByteBuffer.allocate(iLength);
		}

		private void _check(int iNeedLength) {
			if (_buffer.remaining() < iNeedLength) {
				int n = (_buffer.capacity() + iNeedLength) * 2;
				ByteBuffer bs2 = ByteBuffer.allocate(n);
				bs2.put(_buffer.array(), 0, _buffer.position());
				_buffer = bs2;
				bs2 = null;
			}
		}

		public void append(byte   value) { _check(1); _buffer.put(value);	 	}
		public void append(short  value) { _check(2); _buffer.putShort(value);	}
		public void append(float  value) { _check(4); _buffer.putFloat(value);	}
		public void append(double value) { _check(8); _buffer.putDouble(value);	}

		public void append(byte[] bytes, int iOffset, int iLength) {
			_check(iLength);
			_buffer.put(bytes, iOffset, iLength);
		}

		public byte[] data()  { return _buffer.array(); 	}
		public int position() { return _buffer.position();	}
	}; //endof BaseBuffer

	private String _strCharset = "UTF-8";
	private BaseBuffer _baseBuffer;
	private int _iLastId;

	/*********************************************************************************************
	 ** 定义构造函数
	 **
	 **/
	public BaseEncodeStream() { // 默认使用256个字节长度来初始化内存
		this(256);
	}

	public BaseEncodeStream(int iLength)  { // 使用iLength指定的长度来初始化内存
		_iLastId    = 0;
		_baseBuffer = new BaseBuffer(iLength);
	}

	public BaseEncodeStream(BaseEncodeStream ostream) { // 使用BaseOutputStream进行初始化
		_iLastId    = 0;
		_baseBuffer = ostream._baseBuffer;
	}

	/*********************************************************************************************
	 ** 暴露给用户的使用函数
	 **
	 **/
	// 设置String的字符集，设置后编码字符串时全部使用该字符集
    public void setCharset(String sCharset) {
    	_strCharset = sCharset;
    }

    public String getCharset() {
    	return _strCharset;
    }

	// 获取底层的ByteBuffer
	public ByteBuffer getBaseBuffer() {
		return _baseBuffer._buffer;
	}

	// 序列化到ByteBuffer
	public ByteBuffer SerializeToByteBuffer(){
		ByteBuffer buffer = ByteBuffer.allocate(_baseBuffer.position());
		buffer.put(_baseBuffer.data(), 0, _baseBuffer.position());

		return buffer;
	}

	// 序列化到byte[]
	public byte[] getBytes() {
		byte[] bytes = new byte[this._baseBuffer.position()];
		System.arraycopy(this._baseBuffer.data(), 0, bytes, 0, bytes.length);

		return bytes;
	}

	public int getBytesLength() {
		return this._baseBuffer.position();
	}
	
	// 以16进制的方式打印当前缓存的内容，用于调试
	public void printHex() {
		Tools.printHex(_baseBuffer.data(), _baseBuffer.position());
	}

	/*********************************************************************************************
	 ** 暴露给外部用户的接口函数
	 **/
	public void write(int iTagId, boolean value) 	{ writeBoolean(iTagId, value);	}
	public void write(int iTagId, byte  value) 		{ writeInt8(iTagId, value);  	}
	public void write(int iTagId, short value) 		{ writeInt16(iTagId, value);	}
	public void write(int iTagId, int   value) 		{ writeInt32(iTagId, value);	}
	public void write(int iTagId, long  value) 		{ writeInt64(iTagId, value);	}
	public void write(int iTagId, float value) 		{ writeFloat(iTagId, value);	}
	public void write(int iTagId, double value) 	{ writeDouble(iTagId, value);   }
	public void write(int iTagId, String value) 	{ writeString(iTagId, value);   }
	public void write(int iTagId, Message value) 	{ writeMessage(iTagId, value);	}
	public void write(int iTagId, byte[] binBuf) 	{ writeBytes(iTagId, binBuf, 0, binBuf.length); }
	public <T> void write(int iTagId, Collection<T> value) { writeList(iTagId, value); }
	public <K, V> void write(int iTagId, Map<K, V> value)  { writeMap(iTagId, value);  }

    public void write(int iTagId, Object value) { //value是一个实例，当value的类型未知时，调用该函数
    	if (value instanceof Boolean) 			{
    		writeBoolean(iTagId, (Boolean)value);
    	} else if (value instanceof Byte) 		{
    		writeInt8(iTagId, (Byte)value);
    	} else if (value instanceof Short) 		{
            writeInt16(iTagId, (Short)value);
        } else if (value instanceof Integer) 	{
        	writeInt32(iTagId, (Integer)value);
        } else if (value instanceof Long) 		{
        	writeInt64(iTagId, (Long)value);
        } else if (value instanceof Float) 		{
			writeFloat(iTagId, (Float)value);
		} else if (value instanceof Double) 	{
			writeDouble(iTagId, (Double)value);
        } else if (value instanceof String) 	{
			writeString(iTagId, (String)value, _strCharset);
		} else if (value instanceof Message) 	{
			writeMessage(iTagId, (Message)value);
		} else if (value instanceof Collection) {
			writeList(iTagId, (Collection<?>)value);
		} else if (value instanceof Map) 		{
			writeMap(iTagId, (Map<?, ?>)value);
		} else if (value instanceof byte[]) 	{
			writeBytes(iTagId, (byte[])value);
		} else 									{
			throw new ThothException(ThothException.E_UNKNOWNTYPE, "BaseOutputStream::write: Unknown Value Type, " + value.getClass());
		}
    }

	public void writeBoolean(int iTagId, boolean value) { //boolean
		_writeField(iTagId, value? FieldType.FT_TRUE: FieldType.FT_FALSE);
	}

	public void writeInt8(int iTagId, byte value)   { //char, int8_t
		_writeField(iTagId, FieldType.FT_INT, encodeZigZag32(value));
	}

	public void writeUInt8(int iTagId, short value) {
		_writeField(iTagId, FieldType.FT_INT, encodeZigZag32(value));
	}

	public void writeInt16(int iTagId, short value) { //int16_t, uint8_t
		_writeField(iTagId, FieldType.FT_INT, encodeZigZag32(value));
	}

	public void writeUInt16(int iTagId, int value)  { //uint16
		_writeField(iTagId, FieldType.FT_INT, encodeZigZag32(value));
	}

	public void writeInt32(int iTagId, int   value) { //int32_t, uint16_t
		_writeField(iTagId, FieldType.FT_INT, encodeZigZag32(value));
	}

	public void writeUInt32(int iTagId, long value) { //uint32
		_writeField(iTagId, FieldType.FT_INT, encodeZigZag64(value));
	}

	public void writeInt64(int iTagId, long  value) { //int64_t, uint32_t
		_writeField(iTagId, FieldType.FT_INT, encodeZigZag64(value));
	}

	public void writeFloat(int iTagId, float value) { //float(4byte)
		_writeField(iTagId, FieldType.FT_FLOAT);
		_baseBuffer.append(value);
	}

    public void writeDouble(int iTagId, double value) { //double(8byte)
		_writeField(iTagId, FieldType.FT_DOUBLE);
		_baseBuffer.append(value);
    }

	public void writeString(int iTagId, String value) { //String
		_writeField(iTagId, FieldType.FT_STRING);
		_writeString(value, _strCharset);
	}

	public void writeString(int iTagId, String value, String sEncoding) { //String
		_writeField(iTagId, FieldType.FT_STRING);
		_writeString(value, sEncoding);
	}

	public void writeMessage(int iTagId, Message value) { //Message
		_writeField(iTagId, FieldType.FT_MESSAGE);
		_writeMessage(value);
	}

	public void writeBytes(int iTagId, ByteBuffer value, int iOffset, int iLength) { //Bytes
		_writeField(iTagId, FieldType.FT_BYTES);
		_writeBytes(value.array(), iOffset, iLength);
	}

	public void writeBytes(int iTagId, byte[] binBuf) { //Bytes
		writeBytes(iTagId, binBuf, 0, binBuf.length);
	}

	public void writeBytes(int iTagId, byte[] binBuf, int iOffset, int iLength) { //Bytes
		_writeField(iTagId, FieldType.FT_BYTES);
		_writeBytes(binBuf, iOffset, iLength);
	}

	public <T> void writeList(int iTagId, Collection<T> value) { //List(ArrayList)
		_writeField(iTagId, FieldType.FT_LIST);
		_write(value);
	}

	public <K, V> void writeMap(int iTagId, Map<K, V> value) { //Map
		_writeField(iTagId, FieldType.FT_MAP);
		_writeMap(value);
	}

	/*********************************************************************************************
	 ** 内部真实编解码
	 ** 字段的真实写入函数
	 **/
	private static int  encodeZigZag32(final int n)  { return (n << 1) ^ (n >> 31); }
	private static long encodeZigZag64(final long n) { return (n << 1) ^ (n >> 63); }

	byte[] i32buf = new byte[5];
	private void _writeVarint32(int n) {
		int idx = 0;
		while (true) {
			if ((n & ~0x7F) == 0) {
				i32buf[idx++] = (byte)n;
				break;
			} else {
				i32buf[idx++] = (byte)((n & 0x7F) | 0x80);
				n >>>= 7;
			}
		}

		_baseBuffer.append(i32buf, 0, idx);
	}

	byte[] varint64out = new byte[10];
	private void _writeVarint64(long n) {
		int idx = 0;
		while (true) {
			if ((n & ~0x7FL) == 0) {
				varint64out[idx++] = (byte)n;
				break;
			} else {
				varint64out[idx++] = ((byte)((n & 0x7F) | 0x80));
				n >>>= 7;
			}
		}

		_baseBuffer.append(varint64out, 0, idx);
	}

	private void _writeFloat(float value) {
		_baseBuffer.append(value);
	}

	private void _writeDouble(double value) {
		_baseBuffer.append(value);
	}

	private void _writeBytes(byte[] value, int iOffset, int iLength) { //Bytes (vector<char>)
		_writeVarint32(iLength);
		_baseBuffer.append(value, iOffset, iLength);
	}

	private void _writeMessage(Message value) { //结构体
		value.write(this);
		_writeField(FieldType.FT_MESSAGE_STOP);
	}

	private void _writeString(String value, String sCharset) { //String
		try	{
			byte[] bytes = value.getBytes(sCharset);
			_writeVarint32(bytes.length);
			_baseBuffer.append(bytes, 0, bytes.length);
		}
		catch (UnsupportedEncodingException e)
		{
			throw new ThothException("BaseOutputStream::writeString: convert to (" + sCharset + ") fault");
		}
	}

	// TAG以及类型的处理函数
	private void _writeField(byte enumField) {
		_baseBuffer.append(enumField);
	}

	private void _writeField(int iTagId, byte enumField) {
		if (iTagId - _iLastId >= 0x0F) {
			_baseBuffer.append((byte)(0xF0 | enumField));
			_baseBuffer.append((short)iTagId);
		} else {
			_baseBuffer.append((byte)((iTagId - _iLastId) << 4 | enumField));
		}
		_iLastId = iTagId;
	}

	private void _writeField(int iTagId, byte enumField, int value) {
		if (value == 0) {
			_writeField(iTagId, FieldType.FT_ZERO);
		} else {
			_writeField(iTagId, enumField);
			_writeVarint32(value);
		}
	}

	private void _writeField(int iTagId, byte enumField, long value) {
		if (value == 0) {
			_writeField(iTagId, FieldType.FT_ZERO);
		} else {
			_writeField(iTagId, enumField);
			_writeVarint64(value);
		}
	}

	private byte I_TYPE(Object value) 			{
		if (value instanceof Boolean) 			{
			return FieldType.FT_TRUE;
		} else if (value instanceof Byte) 		{
			return FieldType.FT_INT;
		} else if (value instanceof Short) 		{
			return FieldType.FT_INT;
		} else if (value instanceof Integer) 	{
			return FieldType.FT_INT;
		} else if (value instanceof Long) 	    {
			return FieldType.FT_INT;
		} else if (value instanceof String)  	{
			return FieldType.FT_STRING;
		} else if (value instanceof Float)   	{
			return FieldType.FT_FLOAT;
		} else if (value instanceof Double)  	{
			return FieldType.FT_DOUBLE;
		} else if (value instanceof Map)     	{
			return FieldType.FT_MAP;
		} else if (value instanceof Collection) {
			return FieldType.FT_LIST;
		} else if (value instanceof Message) 	{
			return FieldType.FT_MESSAGE;
		} else if (value instanceof byte[])     {
			return FieldType.FT_BYTES;
		} else {
			throw new ThothException("unknown type");
		}
	}

	private void _write(Object value) {
		if (value instanceof Byte) 							{
			_writeVarint32(encodeZigZag32(((Byte)value).byteValue()));
		} else if (value instanceof Short) 						{
            _writeVarint32(encodeZigZag32(((Short)value).shortValue()));
        } else if (value instanceof Integer) 				{
			_writeVarint32(encodeZigZag32(((Integer)value).intValue()));
        } else if (value instanceof Long) 					{
        	_writeVarint64(encodeZigZag64(((Long)value).longValue()));
        } else if (value instanceof String) 				{
			_writeString((String) value, _strCharset);
		} else if (value instanceof Float) 					{
			_writeFloat((Float) value);
		} else if (value instanceof Double) 				{
			_writeDouble((Double) value);
		} else if (value instanceof Message) 				{
			_writeMessage((Message) value);
		} else if (value instanceof Collection) 			{
			Collection<?> val = (Collection<?>)value;
			_writeVarint32(val.size());
			if (val.size() == 0) {
				return ;
			}
			_writeField((byte) (I_TYPE(val.iterator().next())));

			for (Iterator<?> it = val.iterator(); it.hasNext(); ) {
				_write(it.next());
			}
		} else if (value instanceof Map) 					{
			_writeMap((Map<?,?>)value);
		} else if (value instanceof byte[] )				{
			_writeBytes((byte[])value, 0, ((byte[])value).length);
		} else 												{
			throw new ThothException("write type mismatch");
		}
	}

	public <K, V> void _writeMap(Map<K, V> value) {
		_writeVarint32(value == null?0:value.size());
		if (value == null || value.size() == 0) {
			return ;
		}

		Map.Entry<K, V> first = value.entrySet().iterator().next();
		_writeField((byte) (I_TYPE(first.getKey()) << 4 | I_TYPE(first.getValue())));

		for (Map.Entry<K, V> item : value.entrySet()) {
			_write(item.getKey());
			_write(item.getValue());
		}
	}
};
