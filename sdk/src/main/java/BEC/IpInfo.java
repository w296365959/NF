package BEC;

public final class IpInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int eIPType = 0;

    public java.util.ArrayList<String> vtIPList = null;

    public int eApnType = 0;

    public int getEIPType()
    {
        return eIPType;
    }

    public void  setEIPType(int eIPType)
    {
        this.eIPType = eIPType;
    }

    public java.util.ArrayList<String> getVtIPList()
    {
        return vtIPList;
    }

    public void  setVtIPList(java.util.ArrayList<String> vtIPList)
    {
        this.vtIPList = vtIPList;
    }

    public int getEApnType()
    {
        return eApnType;
    }

    public void  setEApnType(int eApnType)
    {
        this.eApnType = eApnType;
    }

    public IpInfo()
    {
    }

    public IpInfo(int eIPType, java.util.ArrayList<String> vtIPList, int eApnType)
    {
        this.eIPType = eIPType;
        this.vtIPList = vtIPList;
        this.eApnType = eApnType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, eIPType);
        if (null != vtIPList) {
            ostream.writeList(1, vtIPList);
        }
        ostream.writeInt32(2, eApnType);
    }

    static java.util.ArrayList<String> VAR_TYPE_4_VTIPLIST = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VTIPLIST.add("");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.eIPType = (int)istream.readInt32(0, false, this.eIPType);
        this.vtIPList = (java.util.ArrayList<String>)istream.readList(1, false, VAR_TYPE_4_VTIPLIST);
        this.eApnType = (int)istream.readInt32(2, false, this.eApnType);
    }

}

