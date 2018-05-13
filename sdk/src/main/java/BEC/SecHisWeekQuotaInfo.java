package BEC;

public final class SecHisWeekQuotaInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iWeek = 0;

    public float fAvgUpPct = 0;

    public int getIWeek()
    {
        return iWeek;
    }

    public void  setIWeek(int iWeek)
    {
        this.iWeek = iWeek;
    }

    public float getFAvgUpPct()
    {
        return fAvgUpPct;
    }

    public void  setFAvgUpPct(float fAvgUpPct)
    {
        this.fAvgUpPct = fAvgUpPct;
    }

    public SecHisWeekQuotaInfo()
    {
    }

    public SecHisWeekQuotaInfo(int iWeek, float fAvgUpPct)
    {
        this.iWeek = iWeek;
        this.fAvgUpPct = fAvgUpPct;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iWeek);
        ostream.writeFloat(1, fAvgUpPct);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iWeek = (int)istream.readInt32(0, false, this.iWeek);
        this.fAvgUpPct = (float)istream.readFloat(1, false, this.fAvgUpPct);
    }

}

