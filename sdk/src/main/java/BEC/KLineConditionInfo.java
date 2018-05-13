package BEC;

public final class KLineConditionInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iTradingTime = 0;

    public float fUpDownRate = 0;

    public int getITradingTime()
    {
        return iTradingTime;
    }

    public void  setITradingTime(int iTradingTime)
    {
        this.iTradingTime = iTradingTime;
    }

    public float getFUpDownRate()
    {
        return fUpDownRate;
    }

    public void  setFUpDownRate(float fUpDownRate)
    {
        this.fUpDownRate = fUpDownRate;
    }

    public KLineConditionInfo()
    {
    }

    public KLineConditionInfo(int iTradingTime, float fUpDownRate)
    {
        this.iTradingTime = iTradingTime;
        this.fUpDownRate = fUpDownRate;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iTradingTime);
        ostream.writeFloat(1, fUpDownRate);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iTradingTime = (int)istream.readInt32(0, false, this.iTradingTime);
        this.fUpDownRate = (float)istream.readFloat(1, false, this.fUpDownRate);
    }

}

