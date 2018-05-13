package BEC;

public final class ExpertInfoListRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<ExpertInfo> vtExpertInfo = null;

    public int iTotalCount = 0;

    public java.util.ArrayList<ExpertInfo> getVtExpertInfo()
    {
        return vtExpertInfo;
    }

    public void  setVtExpertInfo(java.util.ArrayList<ExpertInfo> vtExpertInfo)
    {
        this.vtExpertInfo = vtExpertInfo;
    }

    public int getITotalCount()
    {
        return iTotalCount;
    }

    public void  setITotalCount(int iTotalCount)
    {
        this.iTotalCount = iTotalCount;
    }

    public ExpertInfoListRsp()
    {
    }

    public ExpertInfoListRsp(java.util.ArrayList<ExpertInfo> vtExpertInfo, int iTotalCount)
    {
        this.vtExpertInfo = vtExpertInfo;
        this.iTotalCount = iTotalCount;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtExpertInfo) {
            ostream.writeList(0, vtExpertInfo);
        }
        ostream.writeInt32(1, iTotalCount);
    }

    static java.util.ArrayList<ExpertInfo> VAR_TYPE_4_VTEXPERTINFO = new java.util.ArrayList<ExpertInfo>();
    static {
        VAR_TYPE_4_VTEXPERTINFO.add(new ExpertInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtExpertInfo = (java.util.ArrayList<ExpertInfo>)istream.readList(0, false, VAR_TYPE_4_VTEXPERTINFO);
        this.iTotalCount = (int)istream.readInt32(1, false, this.iTotalCount);
    }

}

