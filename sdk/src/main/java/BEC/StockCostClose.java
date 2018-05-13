package BEC;

public final class StockCostClose extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDate = "";

    public float fMainCapCost = 0;

    public float fClose = 0;

    public float fRetailCost = 0;

    public String getSDate()
    {
        return sDate;
    }

    public void  setSDate(String sDate)
    {
        this.sDate = sDate;
    }

    public float getFMainCapCost()
    {
        return fMainCapCost;
    }

    public void  setFMainCapCost(float fMainCapCost)
    {
        this.fMainCapCost = fMainCapCost;
    }

    public float getFClose()
    {
        return fClose;
    }

    public void  setFClose(float fClose)
    {
        this.fClose = fClose;
    }

    public float getFRetailCost()
    {
        return fRetailCost;
    }

    public void  setFRetailCost(float fRetailCost)
    {
        this.fRetailCost = fRetailCost;
    }

    public StockCostClose()
    {
    }

    public StockCostClose(String sDate, float fMainCapCost, float fClose, float fRetailCost)
    {
        this.sDate = sDate;
        this.fMainCapCost = fMainCapCost;
        this.fClose = fClose;
        this.fRetailCost = fRetailCost;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDate) {
            ostream.writeString(0, sDate);
        }
        ostream.writeFloat(1, fMainCapCost);
        ostream.writeFloat(2, fClose);
        ostream.writeFloat(3, fRetailCost);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDate = (String)istream.readString(0, false, this.sDate);
        this.fMainCapCost = (float)istream.readFloat(1, false, this.fMainCapCost);
        this.fClose = (float)istream.readFloat(2, false, this.fClose);
        this.fRetailCost = (float)istream.readFloat(3, false, this.fRetailCost);
    }

}

