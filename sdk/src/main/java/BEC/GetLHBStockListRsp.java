package BEC;

public final class GetLHBStockListRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDay = "";

    public int iSHSecNum = 0;

    public int iSZSecNum = 0;

    public java.util.ArrayList<LHBSecInfo> vSecList = null;

    public java.util.ArrayList<String> vDay = null;

    public String getSDay()
    {
        return sDay;
    }

    public void  setSDay(String sDay)
    {
        this.sDay = sDay;
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

    public java.util.ArrayList<String> getVDay()
    {
        return vDay;
    }

    public void  setVDay(java.util.ArrayList<String> vDay)
    {
        this.vDay = vDay;
    }

    public GetLHBStockListRsp()
    {
    }

    public GetLHBStockListRsp(String sDay, int iSHSecNum, int iSZSecNum, java.util.ArrayList<LHBSecInfo> vSecList, java.util.ArrayList<String> vDay)
    {
        this.sDay = sDay;
        this.iSHSecNum = iSHSecNum;
        this.iSZSecNum = iSZSecNum;
        this.vSecList = vSecList;
        this.vDay = vDay;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDay) {
            ostream.writeString(0, sDay);
        }
        ostream.writeInt32(1, iSHSecNum);
        ostream.writeInt32(2, iSZSecNum);
        if (null != vSecList) {
            ostream.writeList(3, vSecList);
        }
        if (null != vDay) {
            ostream.writeList(4, vDay);
        }
    }

    static java.util.ArrayList<LHBSecInfo> VAR_TYPE_4_VSECLIST = new java.util.ArrayList<LHBSecInfo>();
    static {
        VAR_TYPE_4_VSECLIST.add(new LHBSecInfo());
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
        this.iSHSecNum = (int)istream.readInt32(1, false, this.iSHSecNum);
        this.iSZSecNum = (int)istream.readInt32(2, false, this.iSZSecNum);
        this.vSecList = (java.util.ArrayList<LHBSecInfo>)istream.readList(3, false, VAR_TYPE_4_VSECLIST);
        this.vDay = (java.util.ArrayList<String>)istream.readList(4, false, VAR_TYPE_4_VDAY);
    }

}

