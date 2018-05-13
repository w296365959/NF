package BEC;

public final class MarketStatDesc extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iTime = 0;

    public int iChange10 = 0;

    public int iChange5 = 0;

    public int iChangeN5 = 0;

    public int iChangeN10 = 0;

    public int iChangeMax = 0;

    public int iChangeMin = 0;

    public int getITime()
    {
        return iTime;
    }

    public void  setITime(int iTime)
    {
        this.iTime = iTime;
    }

    public int getIChange10()
    {
        return iChange10;
    }

    public void  setIChange10(int iChange10)
    {
        this.iChange10 = iChange10;
    }

    public int getIChange5()
    {
        return iChange5;
    }

    public void  setIChange5(int iChange5)
    {
        this.iChange5 = iChange5;
    }

    public int getIChangeN5()
    {
        return iChangeN5;
    }

    public void  setIChangeN5(int iChangeN5)
    {
        this.iChangeN5 = iChangeN5;
    }

    public int getIChangeN10()
    {
        return iChangeN10;
    }

    public void  setIChangeN10(int iChangeN10)
    {
        this.iChangeN10 = iChangeN10;
    }

    public int getIChangeMax()
    {
        return iChangeMax;
    }

    public void  setIChangeMax(int iChangeMax)
    {
        this.iChangeMax = iChangeMax;
    }

    public int getIChangeMin()
    {
        return iChangeMin;
    }

    public void  setIChangeMin(int iChangeMin)
    {
        this.iChangeMin = iChangeMin;
    }

    public MarketStatDesc()
    {
    }

    public MarketStatDesc(int iTime, int iChange10, int iChange5, int iChangeN5, int iChangeN10, int iChangeMax, int iChangeMin)
    {
        this.iTime = iTime;
        this.iChange10 = iChange10;
        this.iChange5 = iChange5;
        this.iChangeN5 = iChangeN5;
        this.iChangeN10 = iChangeN10;
        this.iChangeMax = iChangeMax;
        this.iChangeMin = iChangeMin;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iTime);
        ostream.writeInt32(1, iChange10);
        ostream.writeInt32(2, iChange5);
        ostream.writeInt32(3, iChangeN5);
        ostream.writeInt32(4, iChangeN10);
        ostream.writeInt32(5, iChangeMax);
        ostream.writeInt32(6, iChangeMin);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iTime = (int)istream.readInt32(0, false, this.iTime);
        this.iChange10 = (int)istream.readInt32(1, false, this.iChange10);
        this.iChange5 = (int)istream.readInt32(2, false, this.iChange5);
        this.iChangeN5 = (int)istream.readInt32(3, false, this.iChangeN5);
        this.iChangeN10 = (int)istream.readInt32(4, false, this.iChangeN10);
        this.iChangeMax = (int)istream.readInt32(5, false, this.iChangeMax);
        this.iChangeMin = (int)istream.readInt32(6, false, this.iChangeMin);
    }

}

