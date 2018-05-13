package BEC;

public final class TradingDayRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iTradingDay = 0;

    public int getITradingDay()
    {
        return iTradingDay;
    }

    public void  setITradingDay(int iTradingDay)
    {
        this.iTradingDay = iTradingDay;
    }

    public TradingDayRsp()
    {
    }

    public TradingDayRsp(int iTradingDay)
    {
        this.iTradingDay = iTradingDay;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iTradingDay);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iTradingDay = (int)istream.readInt32(0, false, this.iTradingDay);
    }

}

