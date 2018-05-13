package BEC;

public final class AlertReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public long iAccountId = 0;

    public java.util.ArrayList<ProSecInfo> vProSecInfo = null;

    public long getIAccountId()
    {
        return iAccountId;
    }

    public void  setIAccountId(long iAccountId)
    {
        this.iAccountId = iAccountId;
    }

    public java.util.ArrayList<ProSecInfo> getVProSecInfo()
    {
        return vProSecInfo;
    }

    public void  setVProSecInfo(java.util.ArrayList<ProSecInfo> vProSecInfo)
    {
        this.vProSecInfo = vProSecInfo;
    }

    public AlertReq()
    {
    }

    public AlertReq(long iAccountId, java.util.ArrayList<ProSecInfo> vProSecInfo)
    {
        this.iAccountId = iAccountId;
        this.vProSecInfo = vProSecInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeUInt32(0, iAccountId);
        if (null != vProSecInfo) {
            ostream.writeList(1, vProSecInfo);
        }
    }

    static java.util.ArrayList<ProSecInfo> VAR_TYPE_4_VPROSECINFO = new java.util.ArrayList<ProSecInfo>();
    static {
        VAR_TYPE_4_VPROSECINFO.add(new ProSecInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iAccountId = (long)istream.readUInt32(0, false, this.iAccountId);
        this.vProSecInfo = (java.util.ArrayList<ProSecInfo>)istream.readList(1, false, VAR_TYPE_4_VPROSECINFO);
    }

}

