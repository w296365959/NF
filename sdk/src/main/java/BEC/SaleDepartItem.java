package BEC;

public final class SaleDepartItem extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sName = "";

    public SecCoordsInfo stCoords = null;

    public int iStockNum = 0;

    public java.util.ArrayList<SaleDepartSecItem> vStock = null;

    public String sDay = "";

    public String getSName()
    {
        return sName;
    }

    public void  setSName(String sName)
    {
        this.sName = sName;
    }

    public SecCoordsInfo getStCoords()
    {
        return stCoords;
    }

    public void  setStCoords(SecCoordsInfo stCoords)
    {
        this.stCoords = stCoords;
    }

    public int getIStockNum()
    {
        return iStockNum;
    }

    public void  setIStockNum(int iStockNum)
    {
        this.iStockNum = iStockNum;
    }

    public java.util.ArrayList<SaleDepartSecItem> getVStock()
    {
        return vStock;
    }

    public void  setVStock(java.util.ArrayList<SaleDepartSecItem> vStock)
    {
        this.vStock = vStock;
    }

    public String getSDay()
    {
        return sDay;
    }

    public void  setSDay(String sDay)
    {
        this.sDay = sDay;
    }

    public SaleDepartItem()
    {
    }

    public SaleDepartItem(String sName, SecCoordsInfo stCoords, int iStockNum, java.util.ArrayList<SaleDepartSecItem> vStock, String sDay)
    {
        this.sName = sName;
        this.stCoords = stCoords;
        this.iStockNum = iStockNum;
        this.vStock = vStock;
        this.sDay = sDay;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sName) {
            ostream.writeString(0, sName);
        }
        if (null != stCoords) {
            ostream.writeMessage(1, stCoords);
        }
        ostream.writeInt32(2, iStockNum);
        if (null != vStock) {
            ostream.writeList(3, vStock);
        }
        if (null != sDay) {
            ostream.writeString(4, sDay);
        }
    }

    static SecCoordsInfo VAR_TYPE_4_STCOORDS = new SecCoordsInfo();

    static java.util.ArrayList<SaleDepartSecItem> VAR_TYPE_4_VSTOCK = new java.util.ArrayList<SaleDepartSecItem>();
    static {
        VAR_TYPE_4_VSTOCK.add(new SaleDepartSecItem());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sName = (String)istream.readString(0, false, this.sName);
        this.stCoords = (SecCoordsInfo)istream.readMessage(1, false, VAR_TYPE_4_STCOORDS);
        this.iStockNum = (int)istream.readInt32(2, false, this.iStockNum);
        this.vStock = (java.util.ArrayList<SaleDepartSecItem>)istream.readList(3, false, VAR_TYPE_4_VSTOCK);
        this.sDay = (String)istream.readString(4, false, this.sDay);
    }

}

