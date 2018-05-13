package BEC;

public final class TrendRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.TrendDesc> vTrendDesc = null;

    public boolean bSupport = true;

    public int iStartTime = 0;

    public int iEndTime = 0;

    public java.util.ArrayList<BEC.TrendDesc> getVTrendDesc()
    {
        return vTrendDesc;
    }

    public void  setVTrendDesc(java.util.ArrayList<BEC.TrendDesc> vTrendDesc)
    {
        this.vTrendDesc = vTrendDesc;
    }

    public boolean getBSupport()
    {
        return bSupport;
    }

    public void  setBSupport(boolean bSupport)
    {
        this.bSupport = bSupport;
    }

    public int getIStartTime()
    {
        return iStartTime;
    }

    public void  setIStartTime(int iStartTime)
    {
        this.iStartTime = iStartTime;
    }

    public int getIEndTime()
    {
        return iEndTime;
    }

    public void  setIEndTime(int iEndTime)
    {
        this.iEndTime = iEndTime;
    }

    public TrendRsp()
    {
    }

    public TrendRsp(java.util.ArrayList<BEC.TrendDesc> vTrendDesc, boolean bSupport, int iStartTime, int iEndTime)
    {
        this.vTrendDesc = vTrendDesc;
        this.bSupport = bSupport;
        this.iStartTime = iStartTime;
        this.iEndTime = iEndTime;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vTrendDesc) {
            ostream.writeList(0, vTrendDesc);
        }
        ostream.writeBoolean(1, bSupport);
        ostream.writeInt32(2, iStartTime);
        ostream.writeInt32(3, iEndTime);
    }

    static java.util.ArrayList<BEC.TrendDesc> VAR_TYPE_4_VTRENDDESC = new java.util.ArrayList<BEC.TrendDesc>();
    static {
        VAR_TYPE_4_VTRENDDESC.add(new BEC.TrendDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vTrendDesc = (java.util.ArrayList<BEC.TrendDesc>)istream.readList(0, false, VAR_TYPE_4_VTRENDDESC);
        this.bSupport = (boolean)istream.readBoolean(1, false, this.bSupport);
        this.iStartTime = (int)istream.readInt32(2, false, this.iStartTime);
        this.iEndTime = (int)istream.readInt32(3, false, this.iEndTime);
    }

}

