package BEC;

public final class ProSecInfoList extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<ProSecInfo> vProSecInfo = null;

    public java.util.ArrayList<SubjectInfo> vSubjectInfo = null;

    public int iVersion = 0;

    public java.util.ArrayList<GroupInfo> vGroupInfo = null;

    public java.util.ArrayList<ProSecInfo> getVProSecInfo()
    {
        return vProSecInfo;
    }

    public void  setVProSecInfo(java.util.ArrayList<ProSecInfo> vProSecInfo)
    {
        this.vProSecInfo = vProSecInfo;
    }

    public java.util.ArrayList<SubjectInfo> getVSubjectInfo()
    {
        return vSubjectInfo;
    }

    public void  setVSubjectInfo(java.util.ArrayList<SubjectInfo> vSubjectInfo)
    {
        this.vSubjectInfo = vSubjectInfo;
    }

    public int getIVersion()
    {
        return iVersion;
    }

    public void  setIVersion(int iVersion)
    {
        this.iVersion = iVersion;
    }

    public java.util.ArrayList<GroupInfo> getVGroupInfo()
    {
        return vGroupInfo;
    }

    public void  setVGroupInfo(java.util.ArrayList<GroupInfo> vGroupInfo)
    {
        this.vGroupInfo = vGroupInfo;
    }

    public ProSecInfoList()
    {
    }

    public ProSecInfoList(java.util.ArrayList<ProSecInfo> vProSecInfo, java.util.ArrayList<SubjectInfo> vSubjectInfo, int iVersion, java.util.ArrayList<GroupInfo> vGroupInfo)
    {
        this.vProSecInfo = vProSecInfo;
        this.vSubjectInfo = vSubjectInfo;
        this.iVersion = iVersion;
        this.vGroupInfo = vGroupInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vProSecInfo) {
            ostream.writeList(0, vProSecInfo);
        }
        if (null != vSubjectInfo) {
            ostream.writeList(1, vSubjectInfo);
        }
        ostream.writeInt32(2, iVersion);
        if (null != vGroupInfo) {
            ostream.writeList(3, vGroupInfo);
        }
    }

    static java.util.ArrayList<ProSecInfo> VAR_TYPE_4_VPROSECINFO = new java.util.ArrayList<ProSecInfo>();
    static {
        VAR_TYPE_4_VPROSECINFO.add(new ProSecInfo());
    }

    static java.util.ArrayList<SubjectInfo> VAR_TYPE_4_VSUBJECTINFO = new java.util.ArrayList<SubjectInfo>();
    static {
        VAR_TYPE_4_VSUBJECTINFO.add(new SubjectInfo());
    }

    static java.util.ArrayList<GroupInfo> VAR_TYPE_4_VGROUPINFO = new java.util.ArrayList<GroupInfo>();
    static {
        VAR_TYPE_4_VGROUPINFO.add(new GroupInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vProSecInfo = (java.util.ArrayList<ProSecInfo>)istream.readList(0, false, VAR_TYPE_4_VPROSECINFO);
        this.vSubjectInfo = (java.util.ArrayList<SubjectInfo>)istream.readList(1, false, VAR_TYPE_4_VSUBJECTINFO);
        this.iVersion = (int)istream.readInt32(2, false, this.iVersion);
        this.vGroupInfo = (java.util.ArrayList<GroupInfo>)istream.readList(3, false, VAR_TYPE_4_VGROUPINFO);
    }

}

