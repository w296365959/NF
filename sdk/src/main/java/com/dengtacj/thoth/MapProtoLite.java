package com.dengtacj.thoth;

import com.sscf.investment.sdk.utils.ZipUtils;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class MapProtoLite extends MapFiled {
	protected RequestPacket _package = new RequestPacket();
	protected static HashMap<String, byte[]> newCache__tempdata = null; // 用作读取类型

	public MapProtoLite() {
		_package.iVersion = 3;
	}

	public void 	setHandleName(String sHandleName) { _package.sServantName = sHandleName; }
	public String getHandleName() { return _package.sServantName; }

	public void 	setFuncName(String sFuncName) { _package.sFuncName = sFuncName; }
	public String getFuncName() 	{ return _package.sFuncName; }

	public void		setRequestId(int iRequestId) { _package.iRequestId = iRequestId; }
	public int		getRequestId() 	{ return _package.iRequestId; }

	public void setGUID(final String guid) {
		Map<String, String> context = _package.context;
		if (context == null) {
			context = new HashMap<String, String>(1);
			_package.context = context;
		}
		context.put("GUID", guid);
	}

	public int		getErrorCode() 	{
		if (!_package.status.containsKey("STATUS_RESULT_CODE")) return 0;

		return Integer.parseInt(_package.status.get("STATUS_RESULT_CODE"));
	}

	public String getErrorMsg() {
		if (!_package.status.containsKey("STATUS_RESULT_CODE")) return "";

		return _package.status.get("STATUS_RESULT_DESC");
	}

	private void setCompress(byte compress) {
		_package.bCompress = compress;
	}

	public byte getCompress() {
		return _package.bCompress;
	}

	public byte[] encode() {
		//先把Map编码
		BaseEncodeStream ostream = new BaseEncodeStream();
		ostream.setCharset(_strCharset);
		ostream.write(0, _data);

		// GZIP压缩
		byte[] valueData = ostream.getBytes();
		// 如果没有压缩就直接传原始数据
		setCompress((byte) 0);
		_package.sBuffer = valueData;

		//再整体编码
		ostream = new BaseEncodeStream(0);
		ostream.setCharset(_strCharset);
		_package.write(ostream);
		byte[] bodys = ostream.SerializeToByteBuffer().array();

		int size = bodys.length;
		ByteBuffer buf = ByteBuffer.allocate(size + 4);
		buf.putInt(size + 4).put(bodys).flip();
		return buf.array();
	}

	// 对返回的数据进行解码
	static HashMap<String, byte[]> VAR_TYPE_4_DATA = new HashMap<String, byte[]>();
	static {
		VAR_TYPE_4_DATA.put("", new byte[0]);
	}

	public void decode(byte[] buffer) { this.decode(buffer, 0); }

	public void decode(byte[] buffer, int iPosition) {
		try {
			BaseDecodeStream istream = new BaseDecodeStream(buffer, iPosition);
			istream.setCharset(_strCharset);

			_package.read(istream);

			// GZIP解压
			byte[] valueData = _package.sBuffer;
			istream = new BaseDecodeStream(valueData);
			istream.setCharset(_strCharset);

			_data = (HashMap<String, byte[]>) istream.readMap(0, false, VAR_TYPE_4_DATA);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
