package BEC;

public final class StareStrategyDetail extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iId = 0;

    public String sName = "";

    public int getIId()
    {
        return iId;
    }

    public void  setIId(int iId)
    {
        this.iId = iId;
    }

    public String getSName()
    {
        return sName;
    }

    public void  setSName(String sName)
    {
        this.sName = sName;
    }

    public StareStrategyDetail()
    {
    }

    public StareStrategyDetail(int iId, String sName)
    {
        this.iId = iId;
        this.sName = sName;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iId);
        if (null != sName) {
            ostream.writeString(1, sName);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iId = (int)istream.readInt32(0, false, this.iId);
        this.sName = (String)istream.readString(1, false, this.sName);
    }

}

