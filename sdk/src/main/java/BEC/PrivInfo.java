package BEC;

public final class PrivInfo extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtCode = "";

    public String sSecCode = "";

    public String sSecName = "";

    public String sReleaseTime = "";

    public float fRedeemPrice = 0;

    public float fArbitrageSpace = 0;

    public int ePaceCode = 0;

    public String sPace = "";

    public String sNegotiatedTime = "";

    public float fNegotiatedPrice = 0;

    public String sDelistingTime = "";

    public String sOfferor = "";

    public String sDescription = "";

    public String sWhereAbouts = "";

    public String sWBDtCode = "";

    public String sWBSecName = "";

    public String sWBDescription = "";

    public String getSDtCode()
    {
        return sDtCode;
    }

    public void  setSDtCode(String sDtCode)
    {
        this.sDtCode = sDtCode;
    }

    public String getSSecCode()
    {
        return sSecCode;
    }

    public void  setSSecCode(String sSecCode)
    {
        this.sSecCode = sSecCode;
    }

    public String getSSecName()
    {
        return sSecName;
    }

    public void  setSSecName(String sSecName)
    {
        this.sSecName = sSecName;
    }

    public String getSReleaseTime()
    {
        return sReleaseTime;
    }

    public void  setSReleaseTime(String sReleaseTime)
    {
        this.sReleaseTime = sReleaseTime;
    }

    public float getFRedeemPrice()
    {
        return fRedeemPrice;
    }

    public void  setFRedeemPrice(float fRedeemPrice)
    {
        this.fRedeemPrice = fRedeemPrice;
    }

    public float getFArbitrageSpace()
    {
        return fArbitrageSpace;
    }

    public void  setFArbitrageSpace(float fArbitrageSpace)
    {
        this.fArbitrageSpace = fArbitrageSpace;
    }

    public int getEPaceCode()
    {
        return ePaceCode;
    }

    public void  setEPaceCode(int ePaceCode)
    {
        this.ePaceCode = ePaceCode;
    }

    public String getSPace()
    {
        return sPace;
    }

    public void  setSPace(String sPace)
    {
        this.sPace = sPace;
    }

    public String getSNegotiatedTime()
    {
        return sNegotiatedTime;
    }

    public void  setSNegotiatedTime(String sNegotiatedTime)
    {
        this.sNegotiatedTime = sNegotiatedTime;
    }

    public float getFNegotiatedPrice()
    {
        return fNegotiatedPrice;
    }

    public void  setFNegotiatedPrice(float fNegotiatedPrice)
    {
        this.fNegotiatedPrice = fNegotiatedPrice;
    }

    public String getSDelistingTime()
    {
        return sDelistingTime;
    }

    public void  setSDelistingTime(String sDelistingTime)
    {
        this.sDelistingTime = sDelistingTime;
    }

    public String getSOfferor()
    {
        return sOfferor;
    }

    public void  setSOfferor(String sOfferor)
    {
        this.sOfferor = sOfferor;
    }

    public String getSDescription()
    {
        return sDescription;
    }

    public void  setSDescription(String sDescription)
    {
        this.sDescription = sDescription;
    }

    public String getSWhereAbouts()
    {
        return sWhereAbouts;
    }

    public void  setSWhereAbouts(String sWhereAbouts)
    {
        this.sWhereAbouts = sWhereAbouts;
    }

    public String getSWBDtCode()
    {
        return sWBDtCode;
    }

    public void  setSWBDtCode(String sWBDtCode)
    {
        this.sWBDtCode = sWBDtCode;
    }

    public String getSWBSecName()
    {
        return sWBSecName;
    }

    public void  setSWBSecName(String sWBSecName)
    {
        this.sWBSecName = sWBSecName;
    }

    public String getSWBDescription()
    {
        return sWBDescription;
    }

    public void  setSWBDescription(String sWBDescription)
    {
        this.sWBDescription = sWBDescription;
    }

    public PrivInfo()
    {
    }

    public PrivInfo(String sDtCode, String sSecCode, String sSecName, String sReleaseTime, float fRedeemPrice, float fArbitrageSpace, int ePaceCode, String sPace, String sNegotiatedTime, float fNegotiatedPrice, String sDelistingTime, String sOfferor, String sDescription, String sWhereAbouts, String sWBDtCode, String sWBSecName, String sWBDescription)
    {
        this.sDtCode = sDtCode;
        this.sSecCode = sSecCode;
        this.sSecName = sSecName;
        this.sReleaseTime = sReleaseTime;
        this.fRedeemPrice = fRedeemPrice;
        this.fArbitrageSpace = fArbitrageSpace;
        this.ePaceCode = ePaceCode;
        this.sPace = sPace;
        this.sNegotiatedTime = sNegotiatedTime;
        this.fNegotiatedPrice = fNegotiatedPrice;
        this.sDelistingTime = sDelistingTime;
        this.sOfferor = sOfferor;
        this.sDescription = sDescription;
        this.sWhereAbouts = sWhereAbouts;
        this.sWBDtCode = sWBDtCode;
        this.sWBSecName = sWBSecName;
        this.sWBDescription = sWBDescription;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtCode) {
            ostream.writeString(0, sDtCode);
        }
        if (null != sSecCode) {
            ostream.writeString(1, sSecCode);
        }
        if (null != sSecName) {
            ostream.writeString(2, sSecName);
        }
        if (null != sReleaseTime) {
            ostream.writeString(3, sReleaseTime);
        }
        ostream.writeFloat(4, fRedeemPrice);
        ostream.writeFloat(5, fArbitrageSpace);
        ostream.writeInt32(6, ePaceCode);
        if (null != sPace) {
            ostream.writeString(7, sPace);
        }
        if (null != sNegotiatedTime) {
            ostream.writeString(8, sNegotiatedTime);
        }
        ostream.writeFloat(9, fNegotiatedPrice);
        if (null != sDelistingTime) {
            ostream.writeString(10, sDelistingTime);
        }
        if (null != sOfferor) {
            ostream.writeString(11, sOfferor);
        }
        if (null != sDescription) {
            ostream.writeString(12, sDescription);
        }
        if (null != sWhereAbouts) {
            ostream.writeString(13, sWhereAbouts);
        }
        if (null != sWBDtCode) {
            ostream.writeString(14, sWBDtCode);
        }
        if (null != sWBSecName) {
            ostream.writeString(15, sWBSecName);
        }
        if (null != sWBDescription) {
            ostream.writeString(16, sWBDescription);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtCode = (String)istream.readString(0, false, this.sDtCode);
        this.sSecCode = (String)istream.readString(1, false, this.sSecCode);
        this.sSecName = (String)istream.readString(2, false, this.sSecName);
        this.sReleaseTime = (String)istream.readString(3, false, this.sReleaseTime);
        this.fRedeemPrice = (float)istream.readFloat(4, false, this.fRedeemPrice);
        this.fArbitrageSpace = (float)istream.readFloat(5, false, this.fArbitrageSpace);
        this.ePaceCode = (int)istream.readInt32(6, false, this.ePaceCode);
        this.sPace = (String)istream.readString(7, false, this.sPace);
        this.sNegotiatedTime = (String)istream.readString(8, false, this.sNegotiatedTime);
        this.fNegotiatedPrice = (float)istream.readFloat(9, false, this.fNegotiatedPrice);
        this.sDelistingTime = (String)istream.readString(10, false, this.sDelistingTime);
        this.sOfferor = (String)istream.readString(11, false, this.sOfferor);
        this.sDescription = (String)istream.readString(12, false, this.sDescription);
        this.sWhereAbouts = (String)istream.readString(13, false, this.sWhereAbouts);
        this.sWBDtCode = (String)istream.readString(14, false, this.sWBDtCode);
        this.sWBSecName = (String)istream.readString(15, false, this.sWBSecName);
        this.sWBDescription = (String)istream.readString(16, false, this.sWBDescription);
    }

}

