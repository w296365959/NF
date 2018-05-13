package BEC;

public final class GetPrivTopListRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<PrivInfo> vPrivInfo = null;

    public java.util.ArrayList<PrivInfo> getVPrivInfo()
    {
        return vPrivInfo;
    }

    public void  setVPrivInfo(java.util.ArrayList<PrivInfo> vPrivInfo)
    {
        this.vPrivInfo = vPrivInfo;
    }

    public GetPrivTopListRsp()
    {
    }

    public GetPrivTopListRsp(java.util.ArrayList<PrivInfo> vPrivInfo)
    {
        this.vPrivInfo = vPrivInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vPrivInfo) {
            ostream.writeList(0, vPrivInfo);
        }
    }

    static java.util.ArrayList<PrivInfo> VAR_TYPE_4_VPRIVINFO = new java.util.ArrayList<PrivInfo>();
    static {
        VAR_TYPE_4_VPRIVINFO.add(new PrivInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vPrivInfo = (java.util.ArrayList<PrivInfo>)istream.readList(0, false, VAR_TYPE_4_VPRIVINFO);
    }

}

