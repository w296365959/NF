package BEC;

public final class Fundsholder extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sName = "";

    public String sRatio = "";

    public int eShareholderChange = 0;

    public String sChangeDetail = "";

    public String sUniCode = "";

    public String getSName()
    {
        return sName;
    }

    public void  setSName(String sName)
    {
        this.sName = sName;
    }

    public String getSRatio()
    {
        return sRatio;
    }

    public void  setSRatio(String sRatio)
    {
        this.sRatio = sRatio;
    }

    public int getEShareholderChange()
    {
        return eShareholderChange;
    }

    public void  setEShareholderChange(int eShareholderChange)
    {
        this.eShareholderChange = eShareholderChange;
    }

    public String getSChangeDetail()
    {
        return sChangeDetail;
    }

    public void  setSChangeDetail(String sChangeDetail)
    {
        this.sChangeDetail = sChangeDetail;
    }

    public String getSUniCode()
    {
        return sUniCode;
    }

    public void  setSUniCode(String sUniCode)
    {
        this.sUniCode = sUniCode;
    }

    public Fundsholder()
    {
    }

    public Fundsholder(String sName, String sRatio, int eShareholderChange, String sChangeDetail, String sUniCode)
    {
        this.sName = sName;
        this.sRatio = sRatio;
        this.eShareholderChange = eShareholderChange;
        this.sChangeDetail = sChangeDetail;
        this.sUniCode = sUniCode;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sName) {
            ostream.writeString(0, sName);
        }
        if (null != sRatio) {
            ostream.writeString(1, sRatio);
        }
        ostream.writeInt32(2, eShareholderChange);
        if (null != sChangeDetail) {
            ostream.writeString(3, sChangeDetail);
        }
        if (null != sUniCode) {
            ostream.writeString(4, sUniCode);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sName = (String)istream.readString(0, false, this.sName);
        this.sRatio = (String)istream.readString(1, false, this.sRatio);
        this.eShareholderChange = (int)istream.readInt32(2, false, this.eShareholderChange);
        this.sChangeDetail = (String)istream.readString(3, false, this.sChangeDetail);
        this.sUniCode = (String)istream.readString(4, false, this.sUniCode);
    }

}

