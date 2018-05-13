package BEC;

public final class GetLHBListRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDay = "";

    public String sWeekDay = "";

    public int iSHSecNum = 0;

    public int iSZSecNum = 0;

    public java.util.ArrayList<LHBSecInfo> vSecList = null;

    public java.util.ArrayList<LHBSecInfo> vJgqcList = null;

    public java.util.ArrayList<LHBSecInfo> vYzbyList = null;

    public java.util.ArrayList<LHBSaleDepInfo> vSaleDepList = null;

    public java.util.ArrayList<String> vDay = null;

    public String getSDay()
    {
        return sDay;
    }

    public void  setSDay(String sDay)
    {
        this.sDay = sDay;
    }

    public String getSWeekDay()
    {
        return sWeekDay;
    }

    public void  setSWeekDay(String sWeekDay)
    {
        this.sWeekDay = sWeekDay;
    }

    public int getISHSecNum()
    {
        return iSHSecNum;
    }

    public void  setISHSecNum(int iSHSecNum)
    {
        this.iSHSecNum = iSHSecNum;
    }

    public int getISZSecNum()
    {
        return iSZSecNum;
    }

    public void  setISZSecNum(int iSZSecNum)
    {
        this.iSZSecNum = iSZSecNum;
    }

    public java.util.ArrayList<LHBSecInfo> getVSecList()
    {
        return vSecList;
    }

    public void  setVSecList(java.util.ArrayList<LHBSecInfo> vSecList)
    {
        this.vSecList = vSecList;
    }

    public java.util.ArrayList<LHBSecInfo> getVJgqcList()
    {
        return vJgqcList;
    }

    public void  setVJgqcList(java.util.ArrayList<LHBSecInfo> vJgqcList)
    {
        this.vJgqcList = vJgqcList;
    }

    public java.util.ArrayList<LHBSecInfo> getVYzbyList()
    {
        return vYzbyList;
    }

    public void  setVYzbyList(java.util.ArrayList<LHBSecInfo> vYzbyList)
    {
        this.vYzbyList = vYzbyList;
    }

    public java.util.ArrayList<LHBSaleDepInfo> getVSaleDepList()
    {
        return vSaleDepList;
    }

    public void  setVSaleDepList(java.util.ArrayList<LHBSaleDepInfo> vSaleDepList)
    {
        this.vSaleDepList = vSaleDepList;
    }

    public java.util.ArrayList<String> getVDay()
    {
        return vDay;
    }

    public void  setVDay(java.util.ArrayList<String> vDay)
    {
        this.vDay = vDay;
    }

    public GetLHBListRsp()
    {
    }

    public GetLHBListRsp(String sDay, String sWeekDay, int iSHSecNum, int iSZSecNum, java.util.ArrayList<LHBSecInfo> vSecList, java.util.ArrayList<LHBSecInfo> vJgqcList, java.util.ArrayList<LHBSecInfo> vYzbyList, java.util.ArrayList<LHBSaleDepInfo> vSaleDepList, java.util.ArrayList<String> vDay)
    {
        this.sDay = sDay;
        this.sWeekDay = sWeekDay;
        this.iSHSecNum = iSHSecNum;
        this.iSZSecNum = iSZSecNum;
        this.vSecList = vSecList;
        this.vJgqcList = vJgqcList;
        this.vYzbyList = vYzbyList;
        this.vSaleDepList = vSaleDepList;
        this.vDay = vDay;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDay) {
            ostream.writeString(0, sDay);
        }
        if (null != sWeekDay) {
            ostream.writeString(1, sWeekDay);
        }
        ostream.writeInt32(2, iSHSecNum);
        ostream.writeInt32(3, iSZSecNum);
        if (null != vSecList) {
            ostream.writeList(4, vSecList);
        }
        if (null != vJgqcList) {
            ostream.writeList(5, vJgqcList);
        }
        if (null != vYzbyList) {
            ostream.writeList(6, vYzbyList);
        }
        if (null != vSaleDepList) {
            ostream.writeList(7, vSaleDepList);
        }
        if (null != vDay) {
            ostream.writeList(8, vDay);
        }
    }

    static java.util.ArrayList<LHBSecInfo> VAR_TYPE_4_VSECLIST = new java.util.ArrayList<LHBSecInfo>();
    static {
        VAR_TYPE_4_VSECLIST.add(new LHBSecInfo());
    }

    static java.util.ArrayList<LHBSecInfo> VAR_TYPE_4_VJGQCLIST = new java.util.ArrayList<LHBSecInfo>();
    static {
        VAR_TYPE_4_VJGQCLIST.add(new LHBSecInfo());
    }

    static java.util.ArrayList<LHBSecInfo> VAR_TYPE_4_VYZBYLIST = new java.util.ArrayList<LHBSecInfo>();
    static {
        VAR_TYPE_4_VYZBYLIST.add(new LHBSecInfo());
    }

    static java.util.ArrayList<LHBSaleDepInfo> VAR_TYPE_4_VSALEDEPLIST = new java.util.ArrayList<LHBSaleDepInfo>();
    static {
        VAR_TYPE_4_VSALEDEPLIST.add(new LHBSaleDepInfo());
    }

    static java.util.ArrayList<String> VAR_TYPE_4_VDAY = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VDAY.add("");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDay = (String)istream.readString(0, false, this.sDay);
        this.sWeekDay = (String)istream.readString(1, false, this.sWeekDay);
        this.iSHSecNum = (int)istream.readInt32(2, false, this.iSHSecNum);
        this.iSZSecNum = (int)istream.readInt32(3, false, this.iSZSecNum);
        this.vSecList = (java.util.ArrayList<LHBSecInfo>)istream.readList(4, false, VAR_TYPE_4_VSECLIST);
        this.vJgqcList = (java.util.ArrayList<LHBSecInfo>)istream.readList(5, false, VAR_TYPE_4_VJGQCLIST);
        this.vYzbyList = (java.util.ArrayList<LHBSecInfo>)istream.readList(6, false, VAR_TYPE_4_VYZBYLIST);
        this.vSaleDepList = (java.util.ArrayList<LHBSaleDepInfo>)istream.readList(7, false, VAR_TYPE_4_VSALEDEPLIST);
        this.vDay = (java.util.ArrayList<String>)istream.readList(8, false, VAR_TYPE_4_VDAY);
    }

}

