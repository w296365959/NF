package BEC;

public final class DefaultDailyInfo extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<DefaultDailyItem> vItem = null;

    public String sTradingDay = "";

    public java.util.ArrayList<DefaultDailyItem> getVItem()
    {
        return vItem;
    }

    public void  setVItem(java.util.ArrayList<DefaultDailyItem> vItem)
    {
        this.vItem = vItem;
    }

    public String getSTradingDay()
    {
        return sTradingDay;
    }

    public void  setSTradingDay(String sTradingDay)
    {
        this.sTradingDay = sTradingDay;
    }

    public DefaultDailyInfo()
    {
    }

    public DefaultDailyInfo(java.util.ArrayList<DefaultDailyItem> vItem, String sTradingDay)
    {
        this.vItem = vItem;
        this.sTradingDay = sTradingDay;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vItem) {
            ostream.writeList(0, vItem);
        }
        if (null != sTradingDay) {
            ostream.writeString(1, sTradingDay);
        }
    }

    static java.util.ArrayList<DefaultDailyItem> VAR_TYPE_4_VITEM = new java.util.ArrayList<DefaultDailyItem>();
    static {
        VAR_TYPE_4_VITEM.add(new DefaultDailyItem());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vItem = (java.util.ArrayList<DefaultDailyItem>)istream.readList(0, false, VAR_TYPE_4_VITEM);
        this.sTradingDay = (String)istream.readString(1, false, this.sTradingDay);
    }

}

