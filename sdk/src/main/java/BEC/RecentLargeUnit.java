package BEC;

public final class RecentLargeUnit extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.LargeUnitDesc stDesc = null;

    public int iTotalNum = 0;

    public BEC.LargeUnitDesc getStDesc()
    {
        return stDesc;
    }

    public void  setStDesc(BEC.LargeUnitDesc stDesc)
    {
        this.stDesc = stDesc;
    }

    public int getITotalNum()
    {
        return iTotalNum;
    }

    public void  setITotalNum(int iTotalNum)
    {
        this.iTotalNum = iTotalNum;
    }

    public RecentLargeUnit()
    {
    }

    public RecentLargeUnit(BEC.LargeUnitDesc stDesc, int iTotalNum)
    {
        this.stDesc = stDesc;
        this.iTotalNum = iTotalNum;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stDesc) {
            ostream.writeMessage(0, stDesc);
        }
        ostream.writeInt32(1, iTotalNum);
    }

    static BEC.LargeUnitDesc VAR_TYPE_4_STDESC = new BEC.LargeUnitDesc();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stDesc = (BEC.LargeUnitDesc)istream.readMessage(0, false, VAR_TYPE_4_STDESC);
        this.iTotalNum = (int)istream.readInt32(1, false, this.iTotalNum);
    }

}

