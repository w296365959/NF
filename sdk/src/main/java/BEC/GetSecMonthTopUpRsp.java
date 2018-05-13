package BEC;

public final class GetSecMonthTopUpRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.HisTopSecItem> vItem = null;

    public int iYear = 0;

    public int iMonth = 0;

    public java.util.ArrayList<BEC.HisTopSecItem> getVItem()
    {
        return vItem;
    }

    public void  setVItem(java.util.ArrayList<BEC.HisTopSecItem> vItem)
    {
        this.vItem = vItem;
    }

    public int getIYear()
    {
        return iYear;
    }

    public void  setIYear(int iYear)
    {
        this.iYear = iYear;
    }

    public int getIMonth()
    {
        return iMonth;
    }

    public void  setIMonth(int iMonth)
    {
        this.iMonth = iMonth;
    }

    public GetSecMonthTopUpRsp()
    {
    }

    public GetSecMonthTopUpRsp(java.util.ArrayList<BEC.HisTopSecItem> vItem, int iYear, int iMonth)
    {
        this.vItem = vItem;
        this.iYear = iYear;
        this.iMonth = iMonth;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vItem) {
            ostream.writeList(0, vItem);
        }
        ostream.writeInt32(1, iYear);
        ostream.writeInt32(2, iMonth);
    }

    static java.util.ArrayList<BEC.HisTopSecItem> VAR_TYPE_4_VITEM = new java.util.ArrayList<BEC.HisTopSecItem>();
    static {
        VAR_TYPE_4_VITEM.add(new BEC.HisTopSecItem());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vItem = (java.util.ArrayList<BEC.HisTopSecItem>)istream.readList(0, false, VAR_TYPE_4_VITEM);
        this.iYear = (int)istream.readInt32(1, false, this.iYear);
        this.iMonth = (int)istream.readInt32(2, false, this.iMonth);
    }

}

