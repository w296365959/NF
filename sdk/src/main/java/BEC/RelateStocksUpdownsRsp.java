package BEC;

public final class RelateStocksUpdownsRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.SecUpdowns> vtSecUpdowns = null;

    public int iUpdateTime = 0;

    public java.util.ArrayList<BEC.SecUpdowns> getVtSecUpdowns()
    {
        return vtSecUpdowns;
    }

    public void  setVtSecUpdowns(java.util.ArrayList<BEC.SecUpdowns> vtSecUpdowns)
    {
        this.vtSecUpdowns = vtSecUpdowns;
    }

    public int getIUpdateTime()
    {
        return iUpdateTime;
    }

    public void  setIUpdateTime(int iUpdateTime)
    {
        this.iUpdateTime = iUpdateTime;
    }

    public RelateStocksUpdownsRsp()
    {
    }

    public RelateStocksUpdownsRsp(java.util.ArrayList<BEC.SecUpdowns> vtSecUpdowns, int iUpdateTime)
    {
        this.vtSecUpdowns = vtSecUpdowns;
        this.iUpdateTime = iUpdateTime;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtSecUpdowns) {
            ostream.writeList(0, vtSecUpdowns);
        }
        ostream.writeInt32(1, iUpdateTime);
    }

    static java.util.ArrayList<BEC.SecUpdowns> VAR_TYPE_4_VTSECUPDOWNS = new java.util.ArrayList<BEC.SecUpdowns>();
    static {
        VAR_TYPE_4_VTSECUPDOWNS.add(new BEC.SecUpdowns());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtSecUpdowns = (java.util.ArrayList<BEC.SecUpdowns>)istream.readList(0, false, VAR_TYPE_4_VTSECUPDOWNS);
        this.iUpdateTime = (int)istream.readInt32(1, false, this.iUpdateTime);
    }

}

