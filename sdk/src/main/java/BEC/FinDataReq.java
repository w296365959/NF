package BEC;

public final class FinDataReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.FinDataInfo> vtFinDateInfo = null;

    public byte [] vGuid = null;

    public java.util.ArrayList<BEC.FinDataInfo> getVtFinDateInfo()
    {
        return vtFinDateInfo;
    }

    public void  setVtFinDateInfo(java.util.ArrayList<BEC.FinDataInfo> vtFinDateInfo)
    {
        this.vtFinDateInfo = vtFinDateInfo;
    }

    public byte [] getVGuid()
    {
        return vGuid;
    }

    public void  setVGuid(byte [] vGuid)
    {
        this.vGuid = vGuid;
    }

    public FinDataReq()
    {
    }

    public FinDataReq(java.util.ArrayList<BEC.FinDataInfo> vtFinDateInfo, byte [] vGuid)
    {
        this.vtFinDateInfo = vtFinDateInfo;
        this.vGuid = vGuid;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtFinDateInfo) {
            ostream.writeList(0, vtFinDateInfo);
        }
        if (null != vGuid) {
            ostream.writeBytes(1, vGuid);
        }
    }

    static java.util.ArrayList<BEC.FinDataInfo> VAR_TYPE_4_VTFINDATEINFO = new java.util.ArrayList<BEC.FinDataInfo>();
    static {
        VAR_TYPE_4_VTFINDATEINFO.add(new BEC.FinDataInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtFinDateInfo = (java.util.ArrayList<BEC.FinDataInfo>)istream.readList(0, false, VAR_TYPE_4_VTFINDATEINFO);
        this.vGuid = (byte [])istream.readBytes(1, false, this.vGuid);
    }

}

