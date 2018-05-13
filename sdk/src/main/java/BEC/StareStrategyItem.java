package BEC;

public final class StareStrategyItem extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sClassName = "";

    public java.util.ArrayList<BEC.StareStrategyDetail> vDetail = null;

    public String getSClassName()
    {
        return sClassName;
    }

    public void  setSClassName(String sClassName)
    {
        this.sClassName = sClassName;
    }

    public java.util.ArrayList<BEC.StareStrategyDetail> getVDetail()
    {
        return vDetail;
    }

    public void  setVDetail(java.util.ArrayList<BEC.StareStrategyDetail> vDetail)
    {
        this.vDetail = vDetail;
    }

    public StareStrategyItem()
    {
    }

    public StareStrategyItem(String sClassName, java.util.ArrayList<BEC.StareStrategyDetail> vDetail)
    {
        this.sClassName = sClassName;
        this.vDetail = vDetail;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sClassName) {
            ostream.writeString(0, sClassName);
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

        this.sClassName = (String)istream.readString(0, false, this.sClassName);
        this.vDetail = (java.util.ArrayList<BEC.StareStrategyDetail>)istream.readList(1, false, VAR_TYPE_4_VDETAIL);
    }

}

