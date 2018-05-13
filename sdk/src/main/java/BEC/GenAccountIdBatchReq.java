package BEC;

public final class GenAccountIdBatchReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int eChannelType = 0;

    public java.util.ArrayList<BEC.GenAccountIdReqItem> vItem = null;

    public int getEChannelType()
    {
        return eChannelType;
    }

    public void  setEChannelType(int eChannelType)
    {
        this.eChannelType = eChannelType;
    }

    public java.util.ArrayList<BEC.GenAccountIdReqItem> getVItem()
    {
        return vItem;
    }

    public void  setVItem(java.util.ArrayList<BEC.GenAccountIdReqItem> vItem)
    {
        this.vItem = vItem;
    }

    public GenAccountIdBatchReq()
    {
    }

    public GenAccountIdBatchReq(int eChannelType, java.util.ArrayList<BEC.GenAccountIdReqItem> vItem)
    {
        this.eChannelType = eChannelType;
        this.vItem = vItem;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, eChannelType);
        if (null != vItem) {
            ostream.writeList(1, vItem);
        }
    }

    static java.util.ArrayList<BEC.GenAccountIdReqItem> VAR_TYPE_4_VITEM = new java.util.ArrayList<BEC.GenAccountIdReqItem>();
    static {
        VAR_TYPE_4_VITEM.add(new BEC.GenAccountIdReqItem());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.eChannelType = (int)istream.readInt32(0, false, this.eChannelType);
        this.vItem = (java.util.ArrayList<BEC.GenAccountIdReqItem>)istream.readList(1, false, VAR_TYPE_4_VITEM);
    }

}

