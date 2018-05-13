package com.dengtacj.thoth;

public class Tools {
    private static final char[] digits = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public static void printHex(byte[] bytes, int iLength, int iLineNum) {
        char[] buf = new char[3 * iLength];
        for (int i = 0; i < bytes.length && i < iLength; i++)
        {
            byte b = bytes[i];
            buf[3 * i + 1] = digits[b & 0xF];
            b = (byte) (b >>> 4);
            buf[3 * i + 0] = digits[b & 0xF];
            buf[3 * i + 2] = ((i+1)%iLineNum == 0)?'\n':' ';
        }

//        System.out.println(new String(buf));
	}
	
	public static void printHex(byte[] bytes, int iLength) {
		Tools.printHex(bytes, iLength, 16);
	}

	public static void printHex(byte[] bytes) {
		Tools.printHex(bytes, bytes.length, 16);
	}

	public static void printHexP(byte[] bytes, int iPosition) {
        char[] buf = new char[3 * bytes.length];
        for (int i = iPosition; i < bytes.length; i++)
        {
            byte b = bytes[i];
            buf[3 * i + 1] = digits[b & 0xF];
            b = (byte) (b >>> 4);
            buf[3 * i + 0] = digits[b & 0xF];
            buf[3 * i + 2] = ((i+1)%16 == 0)?'\n':' ';
        }

//        System.out.println(new String(buf));
	}

	private static int _readRawVarint32(byte[] buffer) {
		int result = 0;
		int shift = 0;
		int off = 0;

		if (buffer.length >= 5) {
			while (true) {
				byte b = buffer[off];
				result |= (int) (b & 0x7f) << shift;
				if ((b & 0x80) != 0x80)
					break;
				shift += 7;
				off++;
			}
		} else {
			while (true) {
				byte b = buffer[off++];
				result |= (int) (b & 0x7f) << shift;
				if ((b & 0x80) != 0x80)
					break;
				shift += 7;
			}
		}

		return result;
	}

	public static long toUInt(byte[] buffer) {
		return _readRawVarint32(buffer);
	}

	/*
	private static _readRawVarint64() {
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
	
	*/
	
	
}
