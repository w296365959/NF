package BEC;

public final class FundInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public String sCHNShortName = "";

    public String sCHNFullName = "";

    public String sFundDesc = "";

    public String sFundType = "";

    public String sFundOrgName = "";

    public String sInvestType = "";

    public String sInvestStyle = "";

    public String sInvestTarget = "";

    public String sInvestOrient = "";

    public String sPerformanceBench = "";

    public String sRiskEarn = "";

    public int iIsStructFund = 0;

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public String getSCHNShortName()
    {
        return sCHNShortName;
    }

    public void  setSCHNShortName(String sCHNShortName)
    {
        this.sCHNShortName = sCHNShortName;
    }

    public String getSCHNFullName()
    {
        return sCHNFullName;
    }

    public void  setSCHNFullName(String sCHNFullName)
    {
        this.sCHNFullName = sCHNFullName;
    }

    public String getSFundDesc()
    {
        return sFundDesc;
    }

    public void  setSFundDesc(String sFundDesc)
    {
        this.sFundDesc = sFundDesc;
    }

    public String getSFundType()
    {
        return sFundType;
    }

    public void  setSFundType(String sFundType)
    {
        this.sFundType = sFundType;
    }

    public String getSFundOrgName()
    {
        return sFundOrgName;
    }

    public void  setSFundOrgName(String sFundOrgName)
    {
        this.sFundOrgName = sFundOrgName;
    }

    public String getSInvestType()
    {
        return sInvestType;
    }

    public void  setSInvestType(String sInvestType)
    {
        this.sInvestType = sInvestType;
    }

    public String getSInvestStyle()
    {
        return sInvestStyle;
    }

    public void  setSInvestStyle(String sInvestStyle)
    {
        this.sInvestStyle = sInvestStyle;
    }

    public String getSInvestTarget()
    {
        return sInvestTarget;
    }

    public void  setSInvestTarget(String sInvestTarget)
    {
        this.sInvestTarget = sInvestTarget;
    }

    public String getSInvestOrient()
    {
        return sInvestOrient;
    }

    public void  setSInvestOrient(String sInvestOrient)
    {
        this.sInvestOrient = sInvestOrient;
    }

    public String getSPerformanceBench()
    {
        return sPerformanceBench;
    }

    public void  setSPerformanceBench(String sPerformanceBench)
    {
        this.sPerformanceBench = sPerformanceBench;
    }

    public String getSRiskEarn()
    {
        return sRiskEarn;
    }

    public void  setSRiskEarn(String sRiskEarn)
    {
        this.sRiskEarn = sRiskEarn;
    }

    public int getIIsStructFund()
    {
        return iIsStructFund;
    }

    public void  setIIsStructFund(int iIsStructFund)
    {
        this.iIsStructFund = iIsStructFund;
    }

    public FundInfo()
    {
    }

    public FundInfo(String sDtSecCode, String sCHNShortName, String sCHNFullName, String sFundDesc, String sFundType, String sFundOrgName, String sInvestType, String sInvestStyle, String sInvestTarget, String sInvestOrient, String sPerformanceBench, String sRiskEarn, int iIsStructFund)
    {
        this.sDtSecCode = sDtSecCode;
        this.sCHNShortName = sCHNShortName;
        this.sCHNFullName = sCHNFullName;
        this.sFundDesc = sFundDesc;
        this.sFundType = sFundType;
        this.sFundOrgName = sFundOrgName;
        this.sInvestType = sInvestType;
        this.sInvestStyle = sInvestStyle;
        this.sInvestTarget = sInvestTarget;
        this.sInvestOrient = sInvestOrient;
        this.sPerformanceBench = sPerformanceBench;
        this.sRiskEarn = sRiskEarn;
        this.iIsStructFund = iIsStructFund;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        if (null != sCHNShortName) {
            ostream.writeString(1, sCHNShortName);
        }
        if (null != sCHNFullName) {
            ostream.writeString(2, sCHNFullName);
        }
        if (null != sFundDesc) {
            ostream.writeString(3, sFundDesc);
        }
        if (null != sFundType) {
            ostream.writeString(4, sFundType);
        }
        if (null != sFundOrgName) {
            ostream.writeString(5, sFundOrgName);
        }
        if (null != sInvestType) {
            ostream.writeString(6, sInvestType);
        }
        if (null != sInvestStyle) {
            ostream.writeString(7, sInvestStyle);
        }
        if (null != sInvestTarget) {
            ostream.writeString(8, sInvestTarget);
        }
        if (null != sInvestOrient) {
            ostream.writeString(9, sInvestOrient);
        }
        if (null != sPerformanceBench) {
            ostream.writeString(10, sPerformanceBench);
        }
        if (null != sRiskEarn) {
            ostream.writeString(11, sRiskEarn);
        }
        ostream.writeInt32(12, iIsStructFund);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.sCHNShortName = (String)istream.readString(1, false, this.sCHNShortName);
        this.sCHNFullName = (String)istream.readString(2, false, this.sCHNFullName);
        this.sFundDesc = (String)istream.readString(3, false, this.sFundDesc);
        this.sFundType = (String)istream.readString(4, false, this.sFundType);
        this.sFundOrgName = (String)istream.readString(5, false, this.sFundOrgName);
        this.sInvestType = (String)istream.readString(6, false, this.sInvestType);
        this.sInvestStyle = (String)istream.readString(7, false, this.sInvestStyle);
        this.sInvestTarget = (String)istream.readString(8, false, this.sInvestTarget);
        this.sInvestOrient = (String)istream.readString(9, false, this.sInvestOrient);
        this.sPerformanceBench = (String)istream.readString(10, false, this.sPerformanceBench);
        this.sRiskEarn = (String)istream.readString(11, false, this.sRiskEarn);
        this.iIsStructFund = (int)istream.readInt32(12, false, this.iIsStructFund);
    }

}

