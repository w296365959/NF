package BEC;

public final class SecBaseInfoRsp4Json extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<SecInfo4Json> vSecInfo = null;

    public java.util.ArrayList<SecInfo4Json> getVSecInfo()
    {
        return vSecInfo;
    }

    public void  setVSecInfo(java.util.ArrayList<SecInfo4Json> vSecInfo)
    {
        this.vSecInfo = vSecInfo;
    }

    public SecBaseInfoRsp4Json()
    {
    }

    public SecBaseInfoRsp4Json(java.util.ArrayList<SecInfo4Json> vSecInfo)
    {
        this.vSecInfo = vSecInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vSecInfo) {
            ostream.writeList(0, vSecInfo);
        }
    }

    static java.util.ArrayList<SecInfo4Json> VAR_TYPE_4_VSECINFO = new java.util.ArrayList<SecInfo4Json>();
    static {
        VAR_TYPE_4_VSECINFO.add(new SecInfo4Json());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vSecInfo = (java.util.ArrayList<SecInfo4Json>)istream.readList(0, false, VAR_TYPE_4_VSECINFO);
    }

}

