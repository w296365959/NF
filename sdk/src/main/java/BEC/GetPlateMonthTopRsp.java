package BEC;

public final class GetPlateMonthTopRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sPlateCode = "";

    public String sPlateName = "";

    public java.util.ArrayList<BEC.HisTopSecItem> vItem = null;

    public int iYear = 0;

    public int iMonth = 0;

    public String getSPlateCode()
    {
        return sPlateCode;
    }

    public void  setSPlateCode(String sPlateCode)
    {
        this.sPlateCode = sPlateCode;
    }

    public String getSPlateName()
    {
        return sPlateName;
    }

    public void  setSPlateName(String sPlateName)
    {
        this.sPlateName = sPlateName;
    }

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

    public GetPlateMonthTopRsp()
    {
    }

    public GetPlateMonthTopRsp(String sPlateCode, String sPlateName, java.util.ArrayList<BEC.HisTopSecItem> vItem, int iYear, int iMonth)
    {
        this.sPlateCode = sPlateCode;
        this.sPlateName = sPlateName;
        this.vItem = vItem;
        this.iYear = iYear;
        this.iMonth = iMonth;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sPlateCode) {
            ostream.writeString(0, sPlateCode);
        }
        if (null != sPlateName) {
            ostream.writeString(1, sPlateName);
        }
        if (null != vItem) {
            ostream.writeList(2, vItem);
        }
        ostream.writeInt32(3, iYear);
        ostream.writeInt32(4, iMonth);
    }

    static java.util.ArrayList<BEC.HisTopSecItem> VAR_TYPE_4_VITEM = new java.util.ArrayList<BEC.HisTopSecItem>();
    static {
        VAR_TYPE_4_VITEM.add(new BEC.HisTopSecItem());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sPlateCode = (String)istream.readString(0, false, this.sPlateCode);
        this.sPlateName = (String)istream.readString(1, false, this.sPlateName);
        this.vItem = (java.util.ArrayList<BEC.HisTopSecItem>)istream.readList(2, false, VAR_TYPE_4_VITEM);
        this.iYear = (int)istream.readInt32(3, false, this.iYear);
        this.iMonth = (int)istream.readInt32(4, false, this.iMonth);
    }

}

