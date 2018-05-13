package BEC;

public final class SimilarKLineBase extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sSecName = "";

    public String sDtSecCode = "";

    public String sUpBanner = "";

    public String sUpValue = "";

    public String sLeftValue1 = "";

    public String sLeftValue2 = "";

    public String sRightBanner = "";

    public java.util.ArrayList<BEC.KLineDesc> vKLineDesc = null;

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

    public String getSUpBanner()
    {
        return sUpBanner;
    }

    public void  setSUpBanner(String sUpBanner)
    {
        this.sUpBanner = sUpBanner;
    }

    public String getSUpValue()
    {
        return sUpValue;
    }

    public void  setSUpValue(String sUpValue)
    {
        this.sUpValue = sUpValue;
    }

    public String getSLeftValue1()
    {
        return sLeftValue1;
    }

    public void  setSLeftValue1(String sLeftValue1)
    {
        this.sLeftValue1 = sLeftValue1;
    }

    public String getSLeftValue2()
    {
        return sLeftValue2;
    }

    public void  setSLeftValue2(String sLeftValue2)
    {
        this.sLeftValue2 = sLeftValue2;
    }

    public String getSRightBanner()
    {
        return sRightBanner;
    }

    public void  setSRightBanner(String sRightBanner)
    {
        this.sRightBanner = sRightBanner;
    }

    public java.util.ArrayList<BEC.KLineDesc> getVKLineDesc()
    {
        return vKLineDesc;
    }

    public void  setVKLineDesc(java.util.ArrayList<BEC.KLineDesc> vKLineDesc)
    {
        this.vKLineDesc = vKLineDesc;
    }

    public SimilarKLineBase()
    {
    }

    public SimilarKLineBase(String sSecName, String sDtSecCode, String sUpBanner, String sUpValue, String sLeftValue1, String sLeftValue2, String sRightBanner, java.util.ArrayList<BEC.KLineDesc> vKLineDesc)
    {
        this.sSecName = sSecName;
        this.sDtSecCode = sDtSecCode;
        this.sUpBanner = sUpBanner;
        this.sUpValue = sUpValue;
        this.sLeftValue1 = sLeftValue1;
        this.sLeftValue2 = sLeftValue2;
        this.sRightBanner = sRightBanner;
        this.vKLineDesc = vKLineDesc;
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
        if (null != sUpBanner) {
            ostream.writeString(2, sUpBanner);
        }
        if (null != sUpValue) {
            ostream.writeString(3, sUpValue);
        }
        if (null != sLeftValue1) {
            ostream.writeString(4, sLeftValue1);
        }
        if (null != sLeftValue2) {
            ostream.writeString(5, sLeftValue2);
        }
        if (null != sRightBanner) {
            ostream.writeString(6, sRightBanner);
        }
        if (null != vKLineDesc) {
            ostream.writeList(7, vKLineDesc);
        }
    }

    static java.util.ArrayList<BEC.KLineDesc> VAR_TYPE_4_VKLINEDESC = new java.util.ArrayList<BEC.KLineDesc>();
    static {
        VAR_TYPE_4_VKLINEDESC.add(new BEC.KLineDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sSecName = (String)istream.readString(0, false, this.sSecName);
        this.sDtSecCode = (String)istream.readString(1, false, this.sDtSecCode);
        this.sUpBanner = (String)istream.readString(2, false, this.sUpBanner);
        this.sUpValue = (String)istream.readString(3, false, this.sUpValue);
        this.sLeftValue1 = (String)istream.readString(4, false, this.sLeftValue1);
        this.sLeftValue2 = (String)istream.readString(5, false, this.sLeftValue2);
        this.sRightBanner = (String)istream.readString(6, false, this.sRightBanner);
        this.vKLineDesc = (java.util.ArrayList<BEC.KLineDesc>)istream.readList(7, false, VAR_TYPE_4_VKLINEDESC);
    }

}

