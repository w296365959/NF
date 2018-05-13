package BEC;

public final class LHBSecInfo extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public String sSecName = "";

    public int eActType = 0;

    public float fActMoney = 0;

    public float fClosePrice = 0;

    public float fChangePct = 0;

    public String sFundType = "";

    public java.util.ArrayList<LHBReason> vReason = null;

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public String getSSecName()
    {
        return sSecName;
    }

    public void  setSSecName(String sSecName)
    {
        this.sSecName = sSecName;
    }

    public int getEActType()
    {
        return eActType;
    }

    public void  setEActType(int eActType)
    {
        this.eActType = eActType;
    }

    public float getFActMoney()
    {
        return fActMoney;
    }

    public void  setFActMoney(float fActMoney)
    {
        this.fActMoney = fActMoney;
    }

    public float getFClosePrice()
    {
        return fClosePrice;
    }

    public void  setFClosePrice(float fClosePrice)
    {
        this.fClosePrice = fClosePrice;
    }

    public float getFChangePct()
    {
        return fChangePct;
    }

    public void  setFChangePct(float fChangePct)
    {
        this.fChangePct = fChangePct;
    }

    public String getSFundType()
    {
        return sFundType;
    }

    public void  setSFundType(String sFundType)
    {
        this.sFundType = sFundType;
    }

    public java.util.ArrayList<LHBReason> getVReason()
    {
        return vReason;
    }

    public void  setVReason(java.util.ArrayList<LHBReason> vReason)
    {
        this.vReason = vReason;
    }

    public LHBSecInfo()
    {
    }

    public LHBSecInfo(String sDtSecCode, String sSecName, int eActType, float fActMoney, float fClosePrice, float fChangePct, String sFundType, java.util.ArrayList<LHBReason> vReason)
    {
        this.sDtSecCode = sDtSecCode;
        this.sSecName = sSecName;
        this.eActType = eActType;
        this.fActMoney = fActMoney;
        this.fClosePrice = fClosePrice;
        this.fChangePct = fChangePct;
        this.sFundType = sFundType;
        this.vReason = vReason;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        if (null != sSecName) {
            ostream.writeString(1, sSecName);
        }
        ostream.writeInt32(2, eActType);
        ostream.writeFloat(3, fActMoney);
        ostream.writeFloat(4, fClosePrice);
        ostream.writeFloat(5, fChangePct);
        if (null != sFundType) {
            ostream.writeString(6, sFundType);
        }
        if (null != vReason) {
            ostream.writeList(7, vReason);
        }
    }

    static java.util.ArrayList<LHBReason> VAR_TYPE_4_VREASON = new java.util.ArrayList<LHBReason>();
    static {
        VAR_TYPE_4_VREASON.add(new LHBReason());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.sSecName = (String)istream.readString(1, false, this.sSecName);
        this.eActType = (int)istream.readInt32(2, false, this.eActType);
        this.fActMoney = (float)istream.readFloat(3, false, this.fActMoney);
        this.fClosePrice = (float)istream.readFloat(4, false, this.fClosePrice);
        this.fChangePct = (float)istream.readFloat(5, false, this.fChangePct);
        this.sFundType = (String)istream.readString(6, false, this.sFundType);
        this.vReason = (java.util.ArrayList<LHBReason>)istream.readList(7, false, VAR_TYPE_4_VREASON);
    }

}

