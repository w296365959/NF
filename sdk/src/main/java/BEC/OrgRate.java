package BEC;

public final class OrgRate extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sOrgName = "";

    public int iRate = 0;

    public String sChange = "";

    public String sTargetPrice = "";

    public int iDate = 0;

    public String getSOrgName()
    {
        return sOrgName;
    }

    public void  setSOrgName(String sOrgName)
    {
        this.sOrgName = sOrgName;
    }

    public int getIRate()
    {
        return iRate;
    }

    public void  setIRate(int iRate)
    {
        this.iRate = iRate;
    }

    public String getSChange()
    {
        return sChange;
    }

    public void  setSChange(String sChange)
    {
        this.sChange = sChange;
    }

    public String getSTargetPrice()
    {
        return sTargetPrice;
    }

    public void  setSTargetPrice(String sTargetPrice)
    {
        this.sTargetPrice = sTargetPrice;
    }

    public int getIDate()
    {
        return iDate;
    }

    public void  setIDate(int iDate)
    {
        this.iDate = iDate;
    }

    public OrgRate()
    {
    }

    public OrgRate(String sOrgName, int iRate, String sChange, String sTargetPrice, int iDate)
    {
        this.sOrgName = sOrgName;
        this.iRate = iRate;
        this.sChange = sChange;
        this.sTargetPrice = sTargetPrice;
        this.iDate = iDate;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sOrgName) {
            ostream.writeString(0, sOrgName);
        }
        ostream.writeInt32(1, iRate);
        if (null != sChange) {
            ostream.writeString(2, sChange);
        }
        if (null != sTargetPrice) {
            ostream.writeString(3, sTargetPrice);
        }
        ostream.writeInt32(4, iDate);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sOrgName = (String)istream.readString(0, false, this.sOrgName);
        this.iRate = (int)istream.readInt32(1, false, this.iRate);
        this.sChange = (String)istream.readString(2, false, this.sChange);
        this.sTargetPrice = (String)istream.readString(3, false, this.sTargetPrice);
        this.iDate = (int)istream.readInt32(4, false, this.iDate);
    }

}

