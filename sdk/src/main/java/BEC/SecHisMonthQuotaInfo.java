package BEC;

public final class SecHisMonthQuotaInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iYear = 0;

    public float fRisePct = 0;

    public int getIYear()
    {
        return iYear;
    }

    public void  setIYear(int iYear)
    {
        this.iYear = iYear;
    }

    public float getFRisePct()
    {
        return fRisePct;
    }

    public void  setFRisePct(float fRisePct)
    {
        this.fRisePct = fRisePct;
    }

    public SecHisMonthQuotaInfo()
    {
    }

    public SecHisMonthQuotaInfo(int iYear, float fRisePct)
    {
        this.iYear = iYear;
        this.fRisePct = fRisePct;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iYear);
        ostream.writeFloat(1, fRisePct);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iYear = (int)istream.readInt32(0, false, this.iYear);
        this.fRisePct = (float)istream.readFloat(1, false, this.fRisePct);
    }

}

