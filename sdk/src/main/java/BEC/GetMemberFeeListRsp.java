package BEC;

public final class GetMemberFeeListRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<DtMemberFeeItem> vItem = null;

    public java.util.ArrayList<DtMemberFeeItem> getVItem()
    {
        return vItem;
    }

    public void  setVItem(java.util.ArrayList<DtMemberFeeItem> vItem)
    {
        this.vItem = vItem;
    }

    public GetMemberFeeListRsp()
    {
    }

    public GetMemberFeeListRsp(java.util.ArrayList<DtMemberFeeItem> vItem)
    {
        this.vItem = vItem;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vItem) {
            ostream.writeList(0, vItem);
        }
    }

    static java.util.ArrayList<DtMemberFeeItem> VAR_TYPE_4_VITEM = new java.util.ArrayList<DtMemberFeeItem>();
    static {
        VAR_TYPE_4_VITEM.add(new DtMemberFeeItem());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vItem = (java.util.ArrayList<DtMemberFeeItem>)istream.readList(0, false, VAR_TYPE_4_VITEM);
    }

}

