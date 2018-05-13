package BEC;

public final class IntelliPickStockDetail extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sTitle = "";

    public String sFrom = "";

    public String sUpdateTime = "";

    public String sStrategy = "";

    public java.util.ArrayList<TagInfo> vtTagInfo = null;

    public java.util.ArrayList<IntelliStock> vtIntelliStock = null;

    public String getSTitle()
    {
        return sTitle;
    }

    public void  setSTitle(String sTitle)
    {
        this.sTitle = sTitle;
    }

    public String getSFrom()
    {
        return sFrom;
    }

    public void  setSFrom(String sFrom)
    {
        this.sFrom = sFrom;
    }

    public String getSUpdateTime()
    {
        return sUpdateTime;
    }

    public void  setSUpdateTime(String sUpdateTime)
    {
        this.sUpdateTime = sUpdateTime;
    }

    public String getSStrategy()
    {
        return sStrategy;
    }

    public void  setSStrategy(String sStrategy)
    {
        this.sStrategy = sStrategy;
    }

    public java.util.ArrayList<TagInfo> getVtTagInfo()
    {
        return vtTagInfo;
    }

    public void  setVtTagInfo(java.util.ArrayList<TagInfo> vtTagInfo)
    {
        this.vtTagInfo = vtTagInfo;
    }

    public java.util.ArrayList<IntelliStock> getVtIntelliStock()
    {
        return vtIntelliStock;
    }

    public void  setVtIntelliStock(java.util.ArrayList<IntelliStock> vtIntelliStock)
    {
        this.vtIntelliStock = vtIntelliStock;
    }

    public IntelliPickStockDetail()
    {
    }

    public IntelliPickStockDetail(String sTitle, String sFrom, String sUpdateTime, String sStrategy, java.util.ArrayList<TagInfo> vtTagInfo, java.util.ArrayList<IntelliStock> vtIntelliStock)
    {
        this.sTitle = sTitle;
        this.sFrom = sFrom;
        this.sUpdateTime = sUpdateTime;
        this.sStrategy = sStrategy;
        this.vtTagInfo = vtTagInfo;
        this.vtIntelliStock = vtIntelliStock;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sTitle) {
            ostream.writeString(0, sTitle);
        }
        if (null != sFrom) {
            ostream.writeString(1, sFrom);
        }
        if (null != sUpdateTime) {
            ostream.writeString(2, sUpdateTime);
        }
        if (null != sStrategy) {
            ostream.writeString(3, sStrategy);
        }
        if (null != vtTagInfo) {
            ostream.writeList(4, vtTagInfo);
        }
        if (null != vtIntelliStock) {
            ostream.writeList(5, vtIntelliStock);
        }
    }

    static java.util.ArrayList<TagInfo> VAR_TYPE_4_VTTAGINFO = new java.util.ArrayList<TagInfo>();
    static {
        VAR_TYPE_4_VTTAGINFO.add(new TagInfo());
    }

    static java.util.ArrayList<IntelliStock> VAR_TYPE_4_VTINTELLISTOCK = new java.util.ArrayList<IntelliStock>();
    static {
        VAR_TYPE_4_VTINTELLISTOCK.add(new IntelliStock());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sTitle = (String)istream.readString(0, false, this.sTitle);
        this.sFrom = (String)istream.readString(1, false, this.sFrom);
        this.sUpdateTime = (String)istream.readString(2, false, this.sUpdateTime);
        this.sStrategy = (String)istream.readString(3, false, this.sStrategy);
        this.vtTagInfo = (java.util.ArrayList<TagInfo>)istream.readList(4, false, VAR_TYPE_4_VTTAGINFO);
        this.vtIntelliStock = (java.util.ArrayList<IntelliStock>)istream.readList(5, false, VAR_TYPE_4_VTINTELLISTOCK);
    }

}

