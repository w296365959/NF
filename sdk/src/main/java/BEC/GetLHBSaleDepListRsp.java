package BEC;

public final class GetLHBSaleDepListRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDay = "";

    public java.util.ArrayList<LHBSaleDepInfo> vSaleDepList = null;

    public java.util.ArrayList<String> vDay = null;

    public String getSDay()
    {
        return sDay;
    }

    public void  setSDay(String sDay)
    {
        this.sDay = sDay;
    }

    public java.util.ArrayList<LHBSaleDepInfo> getVSaleDepList()
    {
        return vSaleDepList;
    }

    public void  setVSaleDepList(java.util.ArrayList<LHBSaleDepInfo> vSaleDepList)
    {
        this.vSaleDepList = vSaleDepList;
    }

    public java.util.ArrayList<String> getVDay()
    {
        return vDay;
    }

    public void  setVDay(java.util.ArrayList<String> vDay)
    {
        this.vDay = vDay;
    }

    public GetLHBSaleDepListRsp()
    {
    }

    public GetLHBSaleDepListRsp(String sDay, java.util.ArrayList<LHBSaleDepInfo> vSaleDepList, java.util.ArrayList<String> vDay)
    {
        this.sDay = sDay;
        this.vSaleDepList = vSaleDepList;
        this.vDay = vDay;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDay) {
            ostream.writeString(0, sDay);
        }
        if (null != vSaleDepList) {
            ostream.writeList(1, vSaleDepList);
        }
        if (null != vDay) {
            ostream.writeList(2, vDay);
        }
    }

    static java.util.ArrayList<LHBSaleDepInfo> VAR_TYPE_4_VSALEDEPLIST = new java.util.ArrayList<LHBSaleDepInfo>();
    static {
        VAR_TYPE_4_VSALEDEPLIST.add(new LHBSaleDepInfo());
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
        this.vSaleDepList = (java.util.ArrayList<LHBSaleDepInfo>)istream.readList(1, false, VAR_TYPE_4_VSALEDEPLIST);
        this.vDay = (java.util.ArrayList<String>)istream.readList(2, false, VAR_TYPE_4_VDAY);
    }

}

