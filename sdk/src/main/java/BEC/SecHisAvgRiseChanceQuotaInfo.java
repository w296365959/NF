package BEC;

public final class SecHisAvgRiseChanceQuotaInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iMonth = 0;

    public float fRiseChance = 0;

    public int getIMonth()
    {
        return iMonth;
    }

    public void  setIMonth(int iMonth)
    {
        this.iMonth = iMonth;
    }

    public float getFRiseChance()
    {
        return fRiseChance;
    }

    public void  setFRiseChance(float fRiseChance)
    {
        this.fRiseChance = fRiseChance;
    }

    public SecHisAvgRiseChanceQuotaInfo()
    {
    }

    public SecHisAvgRiseChanceQuotaInfo(int iMonth, float fRiseChance)
    {
        this.iMonth = iMonth;
        this.fRiseChance = fRiseChance;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iMonth);
        ostream.writeFloat(1, fRiseChance);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iMonth = (int)istream.readInt32(0, false, this.iMonth);
        this.fRiseChance = (float)istream.readFloat(1, false, this.fRiseChance);
    }

}

