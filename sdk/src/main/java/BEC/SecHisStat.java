package BEC;

public final class SecHisStat extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iHisYears = 0;

    public int iWeekDay = 0;

    public float fUpRatio = 0;

    public float fPeriodUpRatio = 0;

    public int getIHisYears()
    {
        return iHisYears;
    }

    public void  setIHisYears(int iHisYears)
    {
        this.iHisYears = iHisYears;
    }

    public int getIWeekDay()
    {
        return iWeekDay;
    }

    public void  setIWeekDay(int iWeekDay)
    {
        this.iWeekDay = iWeekDay;
    }

    public float getFUpRatio()
    {
        return fUpRatio;
    }

    public void  setFUpRatio(float fUpRatio)
    {
        this.fUpRatio = fUpRatio;
    }

    public float getFPeriodUpRatio()
    {
        return fPeriodUpRatio;
    }

    public void  setFPeriodUpRatio(float fPeriodUpRatio)
    {
        this.fPeriodUpRatio = fPeriodUpRatio;
    }

    public SecHisStat()
    {
    }

    public SecHisStat(int iHisYears, int iWeekDay, float fUpRatio, float fPeriodUpRatio)
    {
        this.iHisYears = iHisYears;
        this.iWeekDay = iWeekDay;
        this.fUpRatio = fUpRatio;
        this.fPeriodUpRatio = fPeriodUpRatio;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iHisYears);
        ostream.writeInt32(1, iWeekDay);
        ostream.writeFloat(2, fUpRatio);
        ostream.writeFloat(3, fPeriodUpRatio);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iHisYears = (int)istream.readInt32(0, false, this.iHisYears);
        this.iWeekDay = (int)istream.readInt32(1, false, this.iWeekDay);
        this.fUpRatio = (float)istream.readFloat(2, false, this.fUpRatio);
        this.fPeriodUpRatio = (float)istream.readFloat(3, false, this.fPeriodUpRatio);
    }

}

