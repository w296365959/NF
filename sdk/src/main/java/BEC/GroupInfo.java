package BEC;

public final class GroupInfo extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sGroupName = "";

    public boolean isDel = false;

    public int iCreateTime = -1;

    public int iUpdateTime = -1;

    public java.util.ArrayList<BEC.GroupSecInfo> vGroupSecInfo = null;

    public String getSGroupName()
    {
        return sGroupName;
    }

    public void  setSGroupName(String sGroupName)
    {
        this.sGroupName = sGroupName;
    }

    public boolean getIsDel()
    {
        return isDel;
    }

    public void  setIsDel(boolean isDel)
    {
        this.isDel = isDel;
    }

    public int getICreateTime()
    {
        return iCreateTime;
    }

    public void  setICreateTime(int iCreateTime)
    {
        this.iCreateTime = iCreateTime;
    }

    public int getIUpdateTime()
    {
        return iUpdateTime;
    }

    public void  setIUpdateTime(int iUpdateTime)
    {
        this.iUpdateTime = iUpdateTime;
    }

    public java.util.ArrayList<BEC.GroupSecInfo> getVGroupSecInfo()
    {
        return vGroupSecInfo;
    }

    public void  setVGroupSecInfo(java.util.ArrayList<BEC.GroupSecInfo> vGroupSecInfo)
    {
        this.vGroupSecInfo = vGroupSecInfo;
    }

    public GroupInfo()
    {
    }

    public GroupInfo(String sGroupName, boolean isDel, int iCreateTime, int iUpdateTime, java.util.ArrayList<BEC.GroupSecInfo> vGroupSecInfo)
    {
        this.sGroupName = sGroupName;
        this.isDel = isDel;
        this.iCreateTime = iCreateTime;
        this.iUpdateTime = iUpdateTime;
        this.vGroupSecInfo = vGroupSecInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sGroupName) {
            ostream.writeString(0, sGroupName);
        }
        ostream.writeBoolean(1, isDel);
        ostream.writeInt32(2, iCreateTime);
        ostream.writeInt32(3, iUpdateTime);
        if (null != vGroupSecInfo) {
            ostream.writeList(4, vGroupSecInfo);
        }
    }

    static java.util.ArrayList<BEC.GroupSecInfo> VAR_TYPE_4_VGROUPSECINFO = new java.util.ArrayList<BEC.GroupSecInfo>();
    static {
        VAR_TYPE_4_VGROUPSECINFO.add(new BEC.GroupSecInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sGroupName = (String)istream.readString(0, false, this.sGroupName);
        this.isDel = (boolean)istream.readBoolean(1, false, this.isDel);
        this.iCreateTime = (int)istream.readInt32(2, false, this.iCreateTime);
        this.iUpdateTime = (int)istream.readInt32(3, false, this.iUpdateTime);
        this.vGroupSecInfo = (java.util.ArrayList<BEC.GroupSecInfo>)istream.readList(4, false, VAR_TYPE_4_VGROUPSECINFO);
    }

}

