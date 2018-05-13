package BEC;

public final class ConcBaseInfoRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.ConcInfo> vConcInfo = null;

    public java.util.ArrayList<BEC.ConcInfo> getVConcInfo()
    {
        return vConcInfo;
    }

    public void  setVConcInfo(java.util.ArrayList<BEC.ConcInfo> vConcInfo)
    {
        this.vConcInfo = vConcInfo;
    }

    public ConcBaseInfoRsp()
    {
    }

    public ConcBaseInfoRsp(java.util.ArrayList<BEC.ConcInfo> vConcInfo)
    {
        this.vConcInfo = vConcInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vConcInfo) {
            ostream.writeList(0, vConcInfo);
        }
    }

    static java.util.ArrayList<BEC.ConcInfo> VAR_TYPE_4_VCONCINFO = new java.util.ArrayList<BEC.ConcInfo>();
    static {
        VAR_TYPE_4_VCONCINFO.add(new BEC.ConcInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vConcInfo = (java.util.ArrayList<BEC.ConcInfo>)istream.readList(0, false, VAR_TYPE_4_VCONCINFO);
    }

}

