package BEC;

public final class CalendarEventDetail extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iId = 0;

    public String sDescription = "";

    public String sTag = "";

    public String sDate = "";

    public String sUrl = "";

    public java.util.ArrayList<SecInfo> vtRelaStock = null;

    public java.util.ArrayList<SecInfo> vtRelaPlate = null;

    public int getIId()
    {
        return iId;
    }

    public void  setIId(int iId)
    {
        this.iId = iId;
    }

    public String getSDescription()
    {
        return sDescription;
    }

    public void  setSDescription(String sDescription)
    {
        this.sDescription = sDescription;
    }

    public String getSTag()
    {
        return sTag;
    }

    public void  setSTag(String sTag)
    {
        this.sTag = sTag;
    }

    public String getSDate()
    {
        return sDate;
    }

    public void  setSDate(String sDate)
    {
        this.sDate = sDate;
    }

    public String getSUrl()
    {
        return sUrl;
    }

    public void  setSUrl(String sUrl)
    {
        this.sUrl = sUrl;
    }

    public java.util.ArrayList<SecInfo> getVtRelaStock()
    {
        return vtRelaStock;
    }

    public void  setVtRelaStock(java.util.ArrayList<SecInfo> vtRelaStock)
    {
        this.vtRelaStock = vtRelaStock;
    }

    public java.util.ArrayList<SecInfo> getVtRelaPlate()
    {
        return vtRelaPlate;
    }

    public void  setVtRelaPlate(java.util.ArrayList<SecInfo> vtRelaPlate)
    {
        this.vtRelaPlate = vtRelaPlate;
    }

    public CalendarEventDetail()
    {
    }

    public CalendarEventDetail(int iId, String sDescription, String sTag, String sDate, String sUrl, java.util.ArrayList<SecInfo> vtRelaStock, java.util.ArrayList<SecInfo> vtRelaPlate)
    {
        this.iId = iId;
        this.sDescription = sDescription;
        this.sTag = sTag;
        this.sDate = sDate;
        this.sUrl = sUrl;
        this.vtRelaStock = vtRelaStock;
        this.vtRelaPlate = vtRelaPlate;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iId);
        if (null != sDescription) {
            ostream.writeString(1, sDescription);
        }
        if (null != sTag) {
            ostream.writeString(2, sTag);
        }
        if (null != sDate) {
            ostream.writeString(3, sDate);
        }
        if (null != sUrl) {
            ostream.writeString(4, sUrl);
        }
        if (null != vtRelaStock) {
            ostream.writeList(5, vtRelaStock);
        }
        if (null != vtRelaPlate) {
            ostream.writeList(6, vtRelaPlate);
        }
    }

    static java.util.ArrayList<SecInfo> VAR_TYPE_4_VTRELASTOCK = new java.util.ArrayList<SecInfo>();
    static {
        VAR_TYPE_4_VTRELASTOCK.add(new SecInfo());
    }

    static java.util.ArrayList<SecInfo> VAR_TYPE_4_VTRELAPLATE = new java.util.ArrayList<SecInfo>();
    static {
        VAR_TYPE_4_VTRELAPLATE.add(new SecInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iId = (int)istream.readInt32(0, false, this.iId);
        this.sDescription = (String)istream.readString(1, false, this.sDescription);
        this.sTag = (String)istream.readString(2, false, this.sTag);
        this.sDate = (String)istream.readString(3, false, this.sDate);
        this.sUrl = (String)istream.readString(4, false, this.sUrl);
        this.vtRelaStock = (java.util.ArrayList<SecInfo>)istream.readList(5, false, VAR_TYPE_4_VTRELASTOCK);
        this.vtRelaPlate = (java.util.ArrayList<SecInfo>)istream.readList(6, false, VAR_TYPE_4_VTRELAPLATE);
    }

}

