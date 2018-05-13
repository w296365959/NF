package BEC;

public final class GetUserPayListRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<DtPayItem> vItem = null;

    public int iTotalNum = 0;

    public int iStatus = 0;

    public java.util.ArrayList<DtPayItem> getVItem()
    {
        return vItem;
    }

    public void  setVItem(java.util.ArrayList<DtPayItem> vItem)
    {
        this.vItem = vItem;
    }

    public int getITotalNum()
    {
        return iTotalNum;
    }

    public void  setITotalNum(int iTotalNum)
    {
        this.iTotalNum = iTotalNum;
    }

    public int getIStatus()
    {
        return iStatus;
    }

    public void  setIStatus(int iStatus)
    {
        this.iStatus = iStatus;
    }

    public GetUserPayListRsp()
    {
    }

    public GetUserPayListRsp(java.util.ArrayList<DtPayItem> vItem, int iTotalNum, int iStatus)
    {
        this.vItem = vItem;
        this.iTotalNum = iTotalNum;
        this.iStatus = iStatus;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vItem) {
            ostream.writeList(0, vItem);
        }
        ostream.writeInt32(1, iTotalNum);
        ostream.writeInt32(2, iStatus);
    }

    static java.util.ArrayList<DtPayItem> VAR_TYPE_4_VITEM = new java.util.ArrayList<DtPayItem>();
    static {
        VAR_TYPE_4_VITEM.add(new DtPayItem());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vItem = (java.util.ArrayList<DtPayItem>)istream.readList(0, false, VAR_TYPE_4_VITEM);
        this.iTotalNum = (int)istream.readInt32(1, false, this.iTotalNum);
        this.iStatus = (int)istream.readInt32(2, false, this.iStatus);
    }

}

