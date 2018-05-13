package BEC;

public final class SingalModels extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iID = 0;

    public String sName = "";

    public String sDesc = "";

    public String sType = "";

    public float fRank = 0;

    public int iTotal = 0;

    public int getIID()
    {
        return iID;
    }

    public void  setIID(int iID)
    {
        this.iID = iID;
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

    public String getSType()
    {
        return sType;
    }

    public void  setSType(String sType)
    {
        this.sType = sType;
    }

    public float getFRank()
    {
        return fRank;
    }

    public void  setFRank(float fRank)
    {
        this.fRank = fRank;
    }

    public int getITotal()
    {
        return iTotal;
    }

    public void  setITotal(int iTotal)
    {
        this.iTotal = iTotal;
    }

    public SingalModels()
    {
    }

    public SingalModels(int iID, String sName, String sDesc, String sType, float fRank, int iTotal)
    {
        this.iID = iID;
        this.sName = sName;
        this.sDesc = sDesc;
        this.sType = sType;
        this.fRank = fRank;
        this.iTotal = iTotal;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iID);
        if (null != sName) {
            ostream.writeString(1, sName);
        }
        if (null != sDesc) {
            ostream.writeString(2, sDesc);
        }
        if (null != sType) {
            ostream.writeString(3, sType);
        }
        ostream.writeFloat(4, fRank);
        ostream.writeInt32(5, iTotal);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iID = (int)istream.readInt32(0, false, this.iID);
        this.sName = (String)istream.readString(1, false, this.sName);
        this.sDesc = (String)istream.readString(2, false, this.sDesc);
        this.sType = (String)istream.readString(3, false, this.sType);
        this.fRank = (float)istream.readFloat(4, false, this.fRank);
        this.iTotal = (int)istream.readInt32(5, false, this.iTotal);
    }

}

