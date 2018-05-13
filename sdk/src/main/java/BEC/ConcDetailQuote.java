package BEC;

public final class ConcDetailQuote extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public float fConcUpdownsRange = 0;

    public String sUpdownsCount = "";

    public String sConcDesc = "";

    public String sConcName = "";

    public float fHot = 0;

    public java.util.ArrayList<ConcDetailRelaStock> vtConcDetailRelaStock = null;

    public float getFConcUpdownsRange()
    {
        return fConcUpdownsRange;
    }

    public void  setFConcUpdownsRange(float fConcUpdownsRange)
    {
        this.fConcUpdownsRange = fConcUpdownsRange;
    }

    public String getSUpdownsCount()
    {
        return sUpdownsCount;
    }

    public void  setSUpdownsCount(String sUpdownsCount)
    {
        this.sUpdownsCount = sUpdownsCount;
    }

    public String getSConcDesc()
    {
        return sConcDesc;
    }

    public void  setSConcDesc(String sConcDesc)
    {
        this.sConcDesc = sConcDesc;
    }

    public String getSConcName()
    {
        return sConcName;
    }

    public void  setSConcName(String sConcName)
    {
        this.sConcName = sConcName;
    }

    public float getFHot()
    {
        return fHot;
    }

    public void  setFHot(float fHot)
    {
        this.fHot = fHot;
    }

    public java.util.ArrayList<ConcDetailRelaStock> getVtConcDetailRelaStock()
    {
        return vtConcDetailRelaStock;
    }

    public void  setVtConcDetailRelaStock(java.util.ArrayList<ConcDetailRelaStock> vtConcDetailRelaStock)
    {
        this.vtConcDetailRelaStock = vtConcDetailRelaStock;
    }

    public ConcDetailQuote()
    {
    }

    public ConcDetailQuote(float fConcUpdownsRange, String sUpdownsCount, String sConcDesc, String sConcName, float fHot, java.util.ArrayList<ConcDetailRelaStock> vtConcDetailRelaStock)
    {
        this.fConcUpdownsRange = fConcUpdownsRange;
        this.sUpdownsCount = sUpdownsCount;
        this.sConcDesc = sConcDesc;
        this.sConcName = sConcName;
        this.fHot = fHot;
        this.vtConcDetailRelaStock = vtConcDetailRelaStock;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeFloat(0, fConcUpdownsRange);
        if (null != sUpdownsCount) {
            ostream.writeString(1, sUpdownsCount);
        }
        if (null != sConcDesc) {
            ostream.writeString(2, sConcDesc);
        }
        if (null != sConcName) {
            ostream.writeString(3, sConcName);
        }
        ostream.writeFloat(4, fHot);
        if (null != vtConcDetailRelaStock) {
            ostream.writeList(5, vtConcDetailRelaStock);
        }
    }

    static java.util.ArrayList<ConcDetailRelaStock> VAR_TYPE_4_VTCONCDETAILRELASTOCK = new java.util.ArrayList<ConcDetailRelaStock>();
    static {
        VAR_TYPE_4_VTCONCDETAILRELASTOCK.add(new ConcDetailRelaStock());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.fConcUpdownsRange = (float)istream.readFloat(0, false, this.fConcUpdownsRange);
        this.sUpdownsCount = (String)istream.readString(1, false, this.sUpdownsCount);
        this.sConcDesc = (String)istream.readString(2, false, this.sConcDesc);
        this.sConcName = (String)istream.readString(3, false, this.sConcName);
        this.fHot = (float)istream.readFloat(4, false, this.fHot);
        this.vtConcDetailRelaStock = (java.util.ArrayList<ConcDetailRelaStock>)istream.readList(5, false, VAR_TYPE_4_VTCONCDETAILRELASTOCK);
    }

}

