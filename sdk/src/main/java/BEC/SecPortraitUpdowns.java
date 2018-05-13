package BEC;

public final class SecPortraitUpdowns extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iTradeDate = 0;

    public String sUpDowns = "";

    public int getITradeDate()
    {
        return iTradeDate;
    }

    public void  setITradeDate(int iTradeDate)
    {
        this.iTradeDate = iTradeDate;
    }

    public String getSUpDowns()
    {
        return sUpDowns;
    }

    public void  setSUpDowns(String sUpDowns)
    {
        this.sUpDowns = sUpDowns;
    }

    public SecPortraitUpdowns()
    {
    }

    public SecPortraitUpdowns(int iTradeDate, String sUpDowns)
    {
        this.iTradeDate = iTradeDate;
        this.sUpDowns = sUpDowns;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iTradeDate);
        if (null != sUpDowns) {
            ostream.writeString(1, sUpDowns);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iTradeDate = (int)istream.readInt32(0, false, this.iTradeDate);
        this.sUpDowns = (String)istream.readString(1, false, this.sUpDowns);
    }

}

