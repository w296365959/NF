package BEC;

public final class LargeUnitDesc extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sSecName = "";

    public String sDtSecCode = "";

    public int iTime = 0;

    public float fNow = 0;

    public long lLargeBuy = 0;

    public long lLargeSell = 0;

    public long lSuperBuy = 0;

    public long lSuperSell = 0;

    public long lSuperLargeBuy = 0;

    public long lSuperLargeSell = 0;

    public long lCancelBuy = 0;

    public long lCancelSell = 0;

    public long lPostLargeBuy = 0;

    public long lPostLargeSell = 0;

    public float fClose = 0;

    public int ePanKouType = 0;

    public long lFenDan = 0;

    public String getSSecName()
    {
        return sSecName;
    }

    public void  setSSecName(String sSecName)
    {
        this.sSecName = sSecName;
    }

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public int getITime()
    {
        return iTime;
    }

    public void  setITime(int iTime)
    {
        this.iTime = iTime;
    }

    public float getFNow()
    {
        return fNow;
    }

    public void  setFNow(float fNow)
    {
        this.fNow = fNow;
    }

    public long getLLargeBuy()
    {
        return lLargeBuy;
    }

    public void  setLLargeBuy(long lLargeBuy)
    {
        this.lLargeBuy = lLargeBuy;
    }

    public long getLLargeSell()
    {
        return lLargeSell;
    }

    public void  setLLargeSell(long lLargeSell)
    {
        this.lLargeSell = lLargeSell;
    }

    public long getLSuperBuy()
    {
        return lSuperBuy;
    }

    public void  setLSuperBuy(long lSuperBuy)
    {
        this.lSuperBuy = lSuperBuy;
    }

    public long getLSuperSell()
    {
        return lSuperSell;
    }

    public void  setLSuperSell(long lSuperSell)
    {
        this.lSuperSell = lSuperSell;
    }

    public long getLSuperLargeBuy()
    {
        return lSuperLargeBuy;
    }

    public void  setLSuperLargeBuy(long lSuperLargeBuy)
    {
        this.lSuperLargeBuy = lSuperLargeBuy;
    }

    public long getLSuperLargeSell()
    {
        return lSuperLargeSell;
    }

    public void  setLSuperLargeSell(long lSuperLargeSell)
    {
        this.lSuperLargeSell = lSuperLargeSell;
    }

    public long getLCancelBuy()
    {
        return lCancelBuy;
    }

    public void  setLCancelBuy(long lCancelBuy)
    {
        this.lCancelBuy = lCancelBuy;
    }

    public long getLCancelSell()
    {
        return lCancelSell;
    }

    public void  setLCancelSell(long lCancelSell)
    {
        this.lCancelSell = lCancelSell;
    }

    public long getLPostLargeBuy()
    {
        return lPostLargeBuy;
    }

    public void  setLPostLargeBuy(long lPostLargeBuy)
    {
        this.lPostLargeBuy = lPostLargeBuy;
    }

    public long getLPostLargeSell()
    {
        return lPostLargeSell;
    }

    public void  setLPostLargeSell(long lPostLargeSell)
    {
        this.lPostLargeSell = lPostLargeSell;
    }

    public float getFClose()
    {
        return fClose;
    }

    public void  setFClose(float fClose)
    {
        this.fClose = fClose;
    }

    public int getEPanKouType()
    {
        return ePanKouType;
    }

    public void  setEPanKouType(int ePanKouType)
    {
        this.ePanKouType = ePanKouType;
    }

    public long getLFenDan()
    {
        return lFenDan;
    }

    public void  setLFenDan(long lFenDan)
    {
        this.lFenDan = lFenDan;
    }

    public LargeUnitDesc()
    {
    }

    public LargeUnitDesc(String sSecName, String sDtSecCode, int iTime, float fNow, long lLargeBuy, long lLargeSell, long lSuperBuy, long lSuperSell, long lSuperLargeBuy, long lSuperLargeSell, long lCancelBuy, long lCancelSell, long lPostLargeBuy, long lPostLargeSell, float fClose, int ePanKouType, long lFenDan)
    {
        this.sSecName = sSecName;
        this.sDtSecCode = sDtSecCode;
        this.iTime = iTime;
        this.fNow = fNow;
        this.lLargeBuy = lLargeBuy;
        this.lLargeSell = lLargeSell;
        this.lSuperBuy = lSuperBuy;
        this.lSuperSell = lSuperSell;
        this.lSuperLargeBuy = lSuperLargeBuy;
        this.lSuperLargeSell = lSuperLargeSell;
        this.lCancelBuy = lCancelBuy;
        this.lCancelSell = lCancelSell;
        this.lPostLargeBuy = lPostLargeBuy;
        this.lPostLargeSell = lPostLargeSell;
        this.fClose = fClose;
        this.ePanKouType = ePanKouType;
        this.lFenDan = lFenDan;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sSecName) {
            ostream.writeString(0, sSecName);
        }
        if (null != sDtSecCode) {
            ostream.writeString(1, sDtSecCode);
        }
        ostream.writeInt32(2, iTime);
        ostream.writeFloat(3, fNow);
        ostream.writeInt64(4, lLargeBuy);
        ostream.writeInt64(5, lLargeSell);
        ostream.writeInt64(6, lSuperBuy);
        ostream.writeInt64(7, lSuperSell);
        ostream.writeInt64(8, lSuperLargeBuy);
        ostream.writeInt64(9, lSuperLargeSell);
        ostream.writeInt64(10, lCancelBuy);
        ostream.writeInt64(11, lCancelSell);
        ostream.writeInt64(12, lPostLargeBuy);
        ostream.writeInt64(13, lPostLargeSell);
        ostream.writeFloat(14, fClose);
        ostream.writeInt32(15, ePanKouType);
        ostream.writeInt64(16, lFenDan);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sSecName = (String)istream.readString(0, false, this.sSecName);
        this.sDtSecCode = (String)istream.readString(1, false, this.sDtSecCode);
        this.iTime = (int)istream.readInt32(2, false, this.iTime);
        this.fNow = (float)istream.readFloat(3, false, this.fNow);
        this.lLargeBuy = (long)istream.readInt64(4, false, this.lLargeBuy);
        this.lLargeSell = (long)istream.readInt64(5, false, this.lLargeSell);
        this.lSuperBuy = (long)istream.readInt64(6, false, this.lSuperBuy);
        this.lSuperSell = (long)istream.readInt64(7, false, this.lSuperSell);
        this.lSuperLargeBuy = (long)istream.readInt64(8, false, this.lSuperLargeBuy);
        this.lSuperLargeSell = (long)istream.readInt64(9, false, this.lSuperLargeSell);
        this.lCancelBuy = (long)istream.readInt64(10, false, this.lCancelBuy);
        this.lCancelSell = (long)istream.readInt64(11, false, this.lCancelSell);
        this.lPostLargeBuy = (long)istream.readInt64(12, false, this.lPostLargeBuy);
        this.lPostLargeSell = (long)istream.readInt64(13, false, this.lPostLargeSell);
        this.fClose = (float)istream.readFloat(14, false, this.fClose);
        this.ePanKouType = (int)istream.readInt32(15, false, this.ePanKouType);
        this.lFenDan = (long)istream.readInt64(16, false, this.lFenDan);
    }

}

