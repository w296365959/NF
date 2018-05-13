package BEC;

public final class GenAccountIdBatchRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.Map<String, BEC.GenAccountIdRspItem> mItem = null;

    public java.util.Map<String, BEC.GenAccountIdRspItem> getMItem()
    {
        return mItem;
    }

    public void  setMItem(java.util.Map<String, BEC.GenAccountIdRspItem> mItem)
    {
        this.mItem = mItem;
    }

    public GenAccountIdBatchRsp()
    {
    }

    public GenAccountIdBatchRsp(java.util.Map<String, BEC.GenAccountIdRspItem> mItem)
    {
        this.mItem = mItem;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != mItem) {
            ostream.writeMap(0, mItem);
        }
    }

    static java.util.Map<String, BEC.GenAccountIdRspItem> VAR_TYPE_4_MITEM = new java.util.HashMap<String, BEC.GenAccountIdRspItem>();
    static {
        VAR_TYPE_4_MITEM.put("", new BEC.GenAccountIdRspItem());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.mItem = (java.util.Map<String, BEC.GenAccountIdRspItem>)istream.readMap(0, false, VAR_TYPE_4_MITEM);
    }

}

