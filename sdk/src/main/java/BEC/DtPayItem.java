package BEC;

public final class DtPayItem extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtPayOrderId = "";

    public int iType = 0;

    public long lTimeStamp = 0;

    public String sTitle = "";

    public int iTotalAmount = 0;

    public int iNumber = 0;

    public String sDesc = "";

    public int iStatus = 0;

    public int iNumberUnit = E_DT_PAY_TIME_UNIT.E_DT_PAY_TIME_MONTH;

    public int iThirdPaySource = E_THIRD_PAY_SOURCE.E_THIRD_PAY_SDK;

    public int iH5OpenType = E_H5_PAY_OPEN_TYPE.E_H5_PAY_OPEN_BY_URL;

    public String getSDtPayOrderId()
    {
        return sDtPayOrderId;
    }

    public void  setSDtPayOrderId(String sDtPayOrderId)
    {
        this.sDtPayOrderId = sDtPayOrderId;
    }

    public int getIType()
    {
        return iType;
    }

    public void  setIType(int iType)
    {
        this.iType = iType;
    }

    public long getLTimeStamp()
    {
        return lTimeStamp;
    }

    public void  setLTimeStamp(long lTimeStamp)
    {
        this.lTimeStamp = lTimeStamp;
    }

    public String getSTitle()
    {
        return sTitle;
    }

    public void  setSTitle(String sTitle)
    {
        this.sTitle = sTitle;
    }

    public int getITotalAmount()
    {
        return iTotalAmount;
    }

    public void  setITotalAmount(int iTotalAmount)
    {
        this.iTotalAmount = iTotalAmount;
    }

    public int getINumber()
    {
        return iNumber;
    }

    public void  setINumber(int iNumber)
    {
        this.iNumber = iNumber;
    }

    public String getSDesc()
    {
        return sDesc;
    }

    public void  setSDesc(String sDesc)
    {
        this.sDesc = sDesc;
    }

    public int getIStatus()
    {
        return iStatus;
    }

    public void  setIStatus(int iStatus)
    {
        this.iStatus = iStatus;
    }

    public int getINumberUnit()
    {
        return iNumberUnit;
    }

    public void  setINumberUnit(int iNumberUnit)
    {
        this.iNumberUnit = iNumberUnit;
    }

    public int getIThirdPaySource()
    {
        return iThirdPaySource;
    }

    public void  setIThirdPaySource(int iThirdPaySource)
    {
        this.iThirdPaySource = iThirdPaySource;
    }

    public int getIH5OpenType()
    {
        return iH5OpenType;
    }

    public void  setIH5OpenType(int iH5OpenType)
    {
        this.iH5OpenType = iH5OpenType;
    }

    public DtPayItem()
    {
    }

    public DtPayItem(String sDtPayOrderId, int iType, long lTimeStamp, String sTitle, int iTotalAmount, int iNumber, String sDesc, int iStatus, int iNumberUnit, int iThirdPaySource, int iH5OpenType)
    {
        this.sDtPayOrderId = sDtPayOrderId;
        this.iType = iType;
        this.lTimeStamp = lTimeStamp;
        this.sTitle = sTitle;
        this.iTotalAmount = iTotalAmount;
        this.iNumber = iNumber;
        this.sDesc = sDesc;
        this.iStatus = iStatus;
        this.iNumberUnit = iNumberUnit;
        this.iThirdPaySource = iThirdPaySource;
        this.iH5OpenType = iH5OpenType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtPayOrderId) {
            ostream.writeString(0, sDtPayOrderId);
        }
        ostream.writeInt32(1, iType);
        ostream.writeInt64(2, lTimeStamp);
        if (null != sTitle) {
            ostream.writeString(3, sTitle);
        }
        ostream.writeInt32(4, iTotalAmount);
        ostream.writeInt32(5, iNumber);
        if (null != sDesc) {
            ostream.writeString(6, sDesc);
        }
        ostream.writeInt32(7, iStatus);
        ostream.writeInt32(8, iNumberUnit);
        ostream.writeInt32(9, iThirdPaySource);
        ostream.writeInt32(10, iH5OpenType);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtPayOrderId = (String)istream.readString(0, false, this.sDtPayOrderId);
        this.iType = (int)istream.readInt32(1, false, this.iType);
        this.lTimeStamp = (long)istream.readInt64(2, false, this.lTimeStamp);
        this.sTitle = (String)istream.readString(3, false, this.sTitle);
        this.iTotalAmount = (int)istream.readInt32(4, false, this.iTotalAmount);
        this.iNumber = (int)istream.readInt32(5, false, this.iNumber);
        this.sDesc = (String)istream.readString(6, false, this.sDesc);
        this.iStatus = (int)istream.readInt32(7, false, this.iStatus);
        this.iNumberUnit = (int)istream.readInt32(8, false, this.iNumberUnit);
        this.iThirdPaySource = (int)istream.readInt32(9, false, this.iThirdPaySource);
        this.iH5OpenType = (int)istream.readInt32(10, false, this.iH5OpenType);
    }

}

