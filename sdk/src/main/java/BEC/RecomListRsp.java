package BEC;

public final class RecomListRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.RecomItem> vRecomItem = null;

    public java.util.ArrayList<BEC.RecomItem> getVRecomItem()
    {
        return vRecomItem;
    }

    public void  setVRecomItem(java.util.ArrayList<BEC.RecomItem> vRecomItem)
    {
        this.vRecomItem = vRecomItem;
    }

    public RecomListRsp()
    {
    }

    public RecomListRsp(java.util.ArrayList<BEC.RecomItem> vRecomItem)
    {
        this.vRecomItem = vRecomItem;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vRecomItem) {
            ostream.writeList(0, vRecomItem);
        }
    }

    static java.util.ArrayList<BEC.RecomItem> VAR_TYPE_4_VRECOMITEM = new java.util.ArrayList<BEC.RecomItem>();
    static {
        VAR_TYPE_4_VRECOMITEM.add(new BEC.RecomItem());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vRecomItem = (java.util.ArrayList<BEC.RecomItem>)istream.readList(0, false, VAR_TYPE_4_VRECOMITEM);
    }

}

