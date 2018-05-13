package BEC;

public final class GetUserPointDetailRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<UserPointDetailItem> vTaskItem = null;

    public int iTotalNum = 0;

    public String sNextStartId = "";

    public java.util.ArrayList<UserPointDetailItem> getVTaskItem()
    {
        return vTaskItem;
    }

    public void  setVTaskItem(java.util.ArrayList<UserPointDetailItem> vTaskItem)
    {
        this.vTaskItem = vTaskItem;
    }

    public int getITotalNum()
    {
        return iTotalNum;
    }

    public void  setITotalNum(int iTotalNum)
    {
        this.iTotalNum = iTotalNum;
    }

    public String getSNextStartId()
    {
        return sNextStartId;
    }

    public void  setSNextStartId(String sNextStartId)
    {
        this.sNextStartId = sNextStartId;
    }

    public GetUserPointDetailRsp()
    {
    }

    public GetUserPointDetailRsp(java.util.ArrayList<UserPointDetailItem> vTaskItem, int iTotalNum, String sNextStartId)
    {
        this.vTaskItem = vTaskItem;
        this.iTotalNum = iTotalNum;
        this.sNextStartId = sNextStartId;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vTaskItem) {
            ostream.writeList(0, vTaskItem);
        }
        ostream.writeInt32(1, iTotalNum);
        if (null != sNextStartId) {
            ostream.writeString(2, sNextStartId);
        }
    }

    static java.util.ArrayList<UserPointDetailItem> VAR_TYPE_4_VTASKITEM = new java.util.ArrayList<UserPointDetailItem>();
    static {
        VAR_TYPE_4_VTASKITEM.add(new UserPointDetailItem());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vTaskItem = (java.util.ArrayList<UserPointDetailItem>)istream.readList(0, false, VAR_TYPE_4_VTASKITEM);
        this.iTotalNum = (int)istream.readInt32(1, false, this.iTotalNum);
        this.sNextStartId = (String)istream.readString(2, false, this.sNextStartId);
    }

}

