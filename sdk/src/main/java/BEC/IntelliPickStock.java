package BEC;

public final class IntelliPickStock extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sId = "";

    public String sTitle = "";

    public int eBackgroundType = 0;

    public java.util.ArrayList<TagInfo> vtTagInfo = null;

    public java.util.ArrayList<IntelliStock> vtIntelliStock = null;

    public String sUrl = "";

    public String sDate = "";

    public String sDescription = "";

    public String getSId()
    {
        return sId;
    }

    public void  setSId(String sId)
    {
        this.sId = sId;
    }

    public String getSTitle()
    {
        return sTitle;
    }

    public void  setSTitle(String sTitle)
    {
        this.sTitle = sTitle;
    }

    public int getEBackgroundType()
    {
        return eBackgroundType;
    }

    public void  setEBackgroundType(int eBackgroundType)
    {
        this.eBackgroundType = eBackgroundType;
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

    public String getSUrl()
    {
        return sUrl;
    }

    public void  setSUrl(String sUrl)
    {
        this.sUrl = sUrl;
    }

    public String getSDate()
    {
        return sDate;
    }

    public void  setSDate(String sDate)
    {
        this.sDate = sDate;
    }

    public String getSDescription()
    {
        return sDescription;
    }

    public void  setSDescription(String sDescription)
    {
        this.sDescription = sDescription;
    }

    public IntelliPickStock()
    {
    }

    public IntelliPickStock(String sId, String sTitle, int eBackgroundType, java.util.ArrayList<TagInfo> vtTagInfo, java.util.ArrayList<IntelliStock> vtIntelliStock, String sUrl, String sDate, String sDescription)
    {
        this.sId = sId;
        this.sTitle = sTitle;
        this.eBackgroundType = eBackgroundType;
        this.vtTagInfo = vtTagInfo;
        this.vtIntelliStock = vtIntelliStock;
        this.sUrl = sUrl;
        this.sDate = sDate;
        this.sDescription = sDescription;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sId) {
            ostream.writeString(0, sId);
        }
        if (null != sTitle) {
            ostream.writeString(1, sTitle);
        }
        ostream.writeInt32(2, eBackgroundType);
        if (null != vtTagInfo) {
            ostream.writeList(3, vtTagInfo);
        }
        if (null != vtIntelliStock) {
            ostream.writeList(4, vtIntelliStock);
        }
        if (null != sUrl) {
            ostream.writeString(5, sUrl);
        }
        if (null != sDate) {
            ostream.writeString(6, sDate);
        }
        if (null != sDescription) {
            ostream.writeString(7, sDescription);
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

        this.sId = (String)istream.readString(0, false, this.sId);
        this.sTitle = (String)istream.readString(1, false, this.sTitle);
        this.eBackgroundType = (int)istream.readInt32(2, false, this.eBackgroundType);
        this.vtTagInfo = (java.util.ArrayList<TagInfo>)istream.readList(3, false, VAR_TYPE_4_VTTAGINFO);
        this.vtIntelliStock = (java.util.ArrayList<IntelliStock>)istream.readList(4, false, VAR_TYPE_4_VTINTELLISTOCK);
        this.sUrl = (String)istream.readString(5, false, this.sUrl);
        this.sDate = (String)istream.readString(6, false, this.sDate);
        this.sDescription = (String)istream.readString(7, false, this.sDescription);
    }

}

