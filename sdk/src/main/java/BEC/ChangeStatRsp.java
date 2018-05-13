package BEC;

public final class ChangeStatRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.ChangeStatDesc> vChangeStatDesc = null;

    public java.util.ArrayList<BEC.ChangeStatDesc> vStChangeStatDesc = null;

    public long lTimeStamp = 0;

    public java.util.ArrayList<BEC.ChangeStatDesc> getVChangeStatDesc()
    {
        return vChangeStatDesc;
    }

    public void  setVChangeStatDesc(java.util.ArrayList<BEC.ChangeStatDesc> vChangeStatDesc)
    {
        this.vChangeStatDesc = vChangeStatDesc;
    }

    public java.util.ArrayList<BEC.ChangeStatDesc> getVStChangeStatDesc()
    {
        return vStChangeStatDesc;
    }

    public void  setVStChangeStatDesc(java.util.ArrayList<BEC.ChangeStatDesc> vStChangeStatDesc)
    {
        this.vStChangeStatDesc = vStChangeStatDesc;
    }

    public long getLTimeStamp()
    {
        return lTimeStamp;
    }

    public void  setLTimeStamp(long lTimeStamp)
    {
        this.lTimeStamp = lTimeStamp;
    }

    public ChangeStatRsp()
    {
    }

    public ChangeStatRsp(java.util.ArrayList<BEC.ChangeStatDesc> vChangeStatDesc, java.util.ArrayList<BEC.ChangeStatDesc> vStChangeStatDesc, long lTimeStamp)
    {
        this.vChangeStatDesc = vChangeStatDesc;
        this.vStChangeStatDesc = vStChangeStatDesc;
        this.lTimeStamp = lTimeStamp;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vChangeStatDesc) {
            ostream.writeList(0, vChangeStatDesc);
        }
        if (null != vStChangeStatDesc) {
            ostream.writeList(1, vStChangeStatDesc);
        }
        ostream.writeInt64(2, lTimeStamp);
    }

    static java.util.ArrayList<BEC.ChangeStatDesc> VAR_TYPE_4_VCHANGESTATDESC = new java.util.ArrayList<BEC.ChangeStatDesc>();
    static {
        VAR_TYPE_4_VCHANGESTATDESC.add(new BEC.ChangeStatDesc());
    }

    static java.util.ArrayList<BEC.ChangeStatDesc> VAR_TYPE_4_VSTCHANGESTATDESC = new java.util.ArrayList<BEC.ChangeStatDesc>();
    static {
        VAR_TYPE_4_VSTCHANGESTATDESC.add(new BEC.ChangeStatDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vChangeStatDesc = (java.util.ArrayList<BEC.ChangeStatDesc>)istream.readList(0, false, VAR_TYPE_4_VCHANGESTATDESC);
        this.vStChangeStatDesc = (java.util.ArrayList<BEC.ChangeStatDesc>)istream.readList(1, false, VAR_TYPE_4_VSTCHANGESTATDESC);
        this.lTimeStamp = (long)istream.readInt64(2, false, this.lTimeStamp);
    }

}

