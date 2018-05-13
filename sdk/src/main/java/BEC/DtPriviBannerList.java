package BEC;

public final class DtPriviBannerList extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.Map<Integer, java.util.ArrayList<BEC.DtPriviBannerItem>> mBannerItem = null;

    public java.util.Map<Integer, java.util.ArrayList<BEC.DtPriviBannerItem>> getMBannerItem()
    {
        return mBannerItem;
    }

    public void  setMBannerItem(java.util.Map<Integer, java.util.ArrayList<BEC.DtPriviBannerItem>> mBannerItem)
    {
        this.mBannerItem = mBannerItem;
    }

    public DtPriviBannerList()
    {
    }

    public DtPriviBannerList(java.util.Map<Integer, java.util.ArrayList<BEC.DtPriviBannerItem>> mBannerItem)
    {
        this.mBannerItem = mBannerItem;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != mBannerItem) {
            ostream.writeMap(0, mBannerItem);
        }
    }

    static java.util.Map<Integer, java.util.ArrayList<BEC.DtPriviBannerItem>> VAR_TYPE_4_MBANNERITEM = new java.util.HashMap<Integer, java.util.ArrayList<BEC.DtPriviBannerItem>>();
    static {
        java.util.ArrayList<BEC.DtPriviBannerItem> VAR_TYPE_4_MBANNERITEM_V_C = new java.util.ArrayList<BEC.DtPriviBannerItem>();
        VAR_TYPE_4_MBANNERITEM_V_C.add(new BEC.DtPriviBannerItem());
        VAR_TYPE_4_MBANNERITEM.put(0, VAR_TYPE_4_MBANNERITEM_V_C);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.mBannerItem = (java.util.Map<Integer, java.util.ArrayList<BEC.DtPriviBannerItem>>)istream.readMap(0, false, VAR_TYPE_4_MBANNERITEM);
    }

}

