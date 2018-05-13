package BEC;

public final class ValueAddedDesc extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iType = 0;

    public String sName = "";

    public String sDesc = "";

    public String sFeeDesc = "";

    public String sIcon = "";

    public float fFee = 0;

    public int iNumber = 0;

    public int iOnlineState = 0;

    public String sBeforeSaleIcon = "";

    public String sPreSalingUrl = "";

    public int iBuyCount = 0;

    public int getIType()
    {
        return iType;
    }

    public void  setIType(int iType)
    {
        this.iType = iType;
    }

    public String getSName()
    {
        return sName;
    }

    public void  setSName(String sName)
    {
        this.sName = sName;
    }

    public String getSDesc()
    {
        return sDesc;
    }

    public void  setSDesc(String sDesc)
    {
        this.sDesc = sDesc;
    }

    public String getSFeeDesc()
    {
        return sFeeDesc;
    }

    public void  setSFeeDesc(String sFeeDesc)
    {
        this.sFeeDesc = sFeeDesc;
    }

    public String getSIcon()
    {
        return sIcon;
    }

    public void  setSIcon(String sIcon)
    {
        this.sIcon = sIcon;
    }

    public float getFFee()
    {
        return fFee;
    }

    public void  setFFee(float fFee)
    {
        this.fFee = fFee;
    }

    public int getINumber()
    {
        return iNumber;
    }

    public void  setINumber(int iNumber)
    {
        this.iNumber = iNumber;
    }

    public int getIOnlineState()
    {
        return iOnlineState;
    }

    public void  setIOnlineState(int iOnlineState)
    {
        this.iOnlineState = iOnlineState;
    }

    public String getSBeforeSaleIcon()
    {
        return sBeforeSaleIcon;
    }

    public void  setSBeforeSaleIcon(String sBeforeSaleIcon)
    {
        this.sBeforeSaleIcon = sBeforeSaleIcon;
    }

    public String getSPreSalingUrl()
    {
        return sPreSalingUrl;
    }

    public void  setSPreSalingUrl(String sPreSalingUrl)
    {
        this.sPreSalingUrl = sPreSalingUrl;
    }

    public int getIBuyCount()
    {
        return iBuyCount;
    }

    public void  setIBuyCount(int iBuyCount)
    {
        this.iBuyCount = iBuyCount;
    }

    public ValueAddedDesc()
    {
    }

    public ValueAddedDesc(int iType, String sName, String sDesc, String sFeeDesc, String sIcon, float fFee, int iNumber, int iOnlineState, String sBeforeSaleIcon, String sPreSalingUrl, int iBuyCount)
    {
        this.iType = iType;
        this.sName = sName;
        this.sDesc = sDesc;
        this.sFeeDesc = sFeeDesc;
        this.sIcon = sIcon;
        this.fFee = fFee;
        this.iNumber = iNumber;
        this.iOnlineState = iOnlineState;
        this.sBeforeSaleIcon = sBeforeSaleIcon;
        this.sPreSalingUrl = sPreSalingUrl;
        this.iBuyCount = iBuyCount;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iType);
        if (null != sName) {
            ostream.writeString(1, sName);
        }
        if (null != sDesc) {
            ostream.writeString(2, sDesc);
        }
        if (null != sFeeDesc) {
            ostream.writeString(3, sFeeDesc);
        }
        if (null != sIcon) {
            ostream.writeString(4, sIcon);
        }
        ostream.writeFloat(5, fFee);
        ostream.writeInt32(6, iNumber);
        ostream.writeInt32(7, iOnlineState);
        if (null != sBeforeSaleIcon) {
            ostream.writeString(8, sBeforeSaleIcon);
        }
        if (null != sPreSalingUrl) {
            ostream.writeString(9, sPreSalingUrl);
        }
        ostream.writeInt32(10, iBuyCount);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iType = (int)istream.readInt32(0, false, this.iType);
        this.sName = (String)istream.readString(1, false, this.sName);
        this.sDesc = (String)istream.readString(2, false, this.sDesc);
        this.sFeeDesc = (String)istream.readString(3, false, this.sFeeDesc);
        this.sIcon = (String)istream.readString(4, false, this.sIcon);
        this.fFee = (float)istream.readFloat(5, false, this.fFee);
        this.iNumber = (int)istream.readInt32(6, false, this.iNumber);
        this.iOnlineState = (int)istream.readInt32(7, false, this.iOnlineState);
        this.sBeforeSaleIcon = (String)istream.readString(8, false, this.sBeforeSaleIcon);
        this.sPreSalingUrl = (String)istream.readString(9, false, this.sPreSalingUrl);
        this.iBuyCount = (int)istream.readInt32(10, false, this.iBuyCount);
    }

}

