package BEC;

public final class IAConsultStock extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public SecInfo stSecInfo = null;

    public float fNowPrice = 0;

    public float fLastPrice = 0;

    public float fPressPrice = 0;

    public float fSupportPrice = 0;

    public float fAvgCost = 0;

    public String sOverallRate = "";

    public String sShortSuggest = "";

    public String sMidLongSuggest = "";

    public String sUpdateTime = "";

    public String sConsultDetailUrl = "";

    public SecInfo getStSecInfo()
    {
        return stSecInfo;
    }

    public void  setStSecInfo(SecInfo stSecInfo)
    {
        this.stSecInfo = stSecInfo;
    }

    public float getFNowPrice()
    {
        return fNowPrice;
    }

    public void  setFNowPrice(float fNowPrice)
    {
        this.fNowPrice = fNowPrice;
    }

    public float getFLastPrice()
    {
        return fLastPrice;
    }

    public void  setFLastPrice(float fLastPrice)
    {
        this.fLastPrice = fLastPrice;
    }

    public float getFPressPrice()
    {
        return fPressPrice;
    }

    public void  setFPressPrice(float fPressPrice)
    {
        this.fPressPrice = fPressPrice;
    }

    public float getFSupportPrice()
    {
        return fSupportPrice;
    }

    public void  setFSupportPrice(float fSupportPrice)
    {
        this.fSupportPrice = fSupportPrice;
    }

    public float getFAvgCost()
    {
        return fAvgCost;
    }

    public void  setFAvgCost(float fAvgCost)
    {
        this.fAvgCost = fAvgCost;
    }

    public String getSOverallRate()
    {
        return sOverallRate;
    }

    public void  setSOverallRate(String sOverallRate)
    {
        this.sOverallRate = sOverallRate;
    }

    public String getSShortSuggest()
    {
        return sShortSuggest;
    }

    public void  setSShortSuggest(String sShortSuggest)
    {
        this.sShortSuggest = sShortSuggest;
    }

    public String getSMidLongSuggest()
    {
        return sMidLongSuggest;
    }

    public void  setSMidLongSuggest(String sMidLongSuggest)
    {
        this.sMidLongSuggest = sMidLongSuggest;
    }

    public String getSUpdateTime()
    {
        return sUpdateTime;
    }

    public void  setSUpdateTime(String sUpdateTime)
    {
        this.sUpdateTime = sUpdateTime;
    }

    public String getSConsultDetailUrl()
    {
        return sConsultDetailUrl;
    }

    public void  setSConsultDetailUrl(String sConsultDetailUrl)
    {
        this.sConsultDetailUrl = sConsultDetailUrl;
    }

    public IAConsultStock()
    {
    }

    public IAConsultStock(SecInfo stSecInfo, float fNowPrice, float fLastPrice, float fPressPrice, float fSupportPrice, float fAvgCost, String sOverallRate, String sShortSuggest, String sMidLongSuggest, String sUpdateTime, String sConsultDetailUrl)
    {
        this.stSecInfo = stSecInfo;
        this.fNowPrice = fNowPrice;
        this.fLastPrice = fLastPrice;
        this.fPressPrice = fPressPrice;
        this.fSupportPrice = fSupportPrice;
        this.fAvgCost = fAvgCost;
        this.sOverallRate = sOverallRate;
        this.sShortSuggest = sShortSuggest;
        this.sMidLongSuggest = sMidLongSuggest;
        this.sUpdateTime = sUpdateTime;
        this.sConsultDetailUrl = sConsultDetailUrl;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stSecInfo) {
            ostream.writeMessage(0, stSecInfo);
        }
        ostream.writeFloat(1, fNowPrice);
        ostream.writeFloat(2, fLastPrice);
        ostream.writeFloat(3, fPressPrice);
        ostream.writeFloat(4, fSupportPrice);
        ostream.writeFloat(5, fAvgCost);
        if (null != sOverallRate) {
            ostream.writeString(6, sOverallRate);
        }
        if (null != sShortSuggest) {
            ostream.writeString(7, sShortSuggest);
        }
        if (null != sMidLongSuggest) {
            ostream.writeString(8, sMidLongSuggest);
        }
        if (null != sUpdateTime) {
            ostream.writeString(9, sUpdateTime);
        }
        if (null != sConsultDetailUrl) {
            ostream.writeString(10, sConsultDetailUrl);
        }
    }

    static SecInfo VAR_TYPE_4_STSECINFO = new SecInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stSecInfo = (SecInfo)istream.readMessage(0, false, VAR_TYPE_4_STSECINFO);
        this.fNowPrice = (float)istream.readFloat(1, false, this.fNowPrice);
        this.fLastPrice = (float)istream.readFloat(2, false, this.fLastPrice);
        this.fPressPrice = (float)istream.readFloat(3, false, this.fPressPrice);
        this.fSupportPrice = (float)istream.readFloat(4, false, this.fSupportPrice);
        this.fAvgCost = (float)istream.readFloat(5, false, this.fAvgCost);
        this.sOverallRate = (String)istream.readString(6, false, this.sOverallRate);
        this.sShortSuggest = (String)istream.readString(7, false, this.sShortSuggest);
        this.sMidLongSuggest = (String)istream.readString(8, false, this.sMidLongSuggest);
        this.sUpdateTime = (String)istream.readString(9, false, this.sUpdateTime);
        this.sConsultDetailUrl = (String)istream.readString(10, false, this.sConsultDetailUrl);
    }

}

