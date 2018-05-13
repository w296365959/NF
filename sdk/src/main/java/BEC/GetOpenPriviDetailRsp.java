package BEC;

public final class GetOpenPriviDetailRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<OpenPriviDetailItem> vPriviItem = null;

    public int iTotalNum = 0;

    public int iStatus = 0;

    public java.util.ArrayList<OpenPriviDetailItem> getVPriviItem()
    {
        return vPriviItem;
    }

    public void  setVPriviItem(java.util.ArrayList<OpenPriviDetailItem> vPriviItem)
    {
        this.vPriviItem = vPriviItem;
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

    public GetOpenPriviDetailRsp()
    {
    }

    public GetOpenPriviDetailRsp(java.util.ArrayList<OpenPriviDetailItem> vPriviItem, int iTotalNum, int iStatus)
    {
        this.vPriviItem = vPriviItem;
        this.iTotalNum = iTotalNum;
        this.iStatus = iStatus;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vPriviItem) {
            ostream.writeList(0, vPriviItem);
        }
        ostream.writeInt32(1, iTotalNum);
        ostream.writeInt32(2, iStatus);
    }

    static java.util.ArrayList<OpenPriviDetailItem> VAR_TYPE_4_VPRIVIITEM = new java.util.ArrayList<OpenPriviDetailItem>();
    static {
        VAR_TYPE_4_VPRIVIITEM.add(new OpenPriviDetailItem());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vPriviItem = (java.util.ArrayList<OpenPriviDetailItem>)istream.readList(0, false, VAR_TYPE_4_VPRIVIITEM);
        this.iTotalNum = (int)istream.readInt32(1, false, this.iTotalNum);
        this.iStatus = (int)istream.readInt32(2, false, this.iStatus);
    }

}

