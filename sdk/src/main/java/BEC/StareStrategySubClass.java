package BEC;

public final class StareStrategySubClass extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sName = "";

    public java.util.ArrayList<BEC.StareStrategyDetail> vDetail = null;

    public String getSName()
    {
        return sName;
    }

    public void  setSName(String sName)
    {
        this.sName = sName;
    }

    public java.util.ArrayList<BEC.StareStrategyDetail> getVDetail()
    {
        return vDetail;
    }

    public void  setVDetail(java.util.ArrayList<BEC.StareStrategyDetail> vDetail)
    {
        this.vDetail = vDetail;
    }

    public StareStrategySubClass()
    {
    }

    public StareStrategySubClass(String sName, java.util.ArrayList<BEC.StareStrategyDetail> vDetail)
    {
        this.sName = sName;
        this.vDetail = vDetail;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sName) {
            ostream.writeString(0, sName);
        }
        if (null != vDetail) {
            ostream.writeList(1, vDetail);
        }
    }

    static java.util.ArrayList<BEC.StareStrategyDetail> VAR_TYPE_4_VDETAIL = new java.util.ArrayList<BEC.StareStrategyDetail>();
    static {
        VAR_TYPE_4_VDETAIL.add(new BEC.StareStrategyDetail());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sName = (String)istream.readString(0, false, this.sName);
        this.vDetail = (java.util.ArrayList<BEC.StareStrategyDetail>)istream.readList(1, false, VAR_TYPE_4_VDETAIL);
    }

}

