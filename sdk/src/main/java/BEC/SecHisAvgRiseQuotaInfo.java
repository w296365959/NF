package BEC;

public final class SecHisAvgRiseQuotaInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iMonth = 0;

    public float fRisePct = 0;

    public int getIMonth()
    {
        return iMonth;
    }

    public void  setIMonth(int iMonth)
    {
        this.iMonth = iMonth;
    }

    public float getFRisePct()
    {
        return fRisePct;
    }

    public void  setFRisePct(float fRisePct)
    {
        this.fRisePct = fRisePct;
    }

    public SecHisAvgRiseQuotaInfo()
    {
    }

    public SecHisAvgRiseQuotaInfo(int iMonth, float fRisePct)
    {
        this.iMonth = iMonth;
        this.fRisePct = fRisePct;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iMonth);
        ostream.writeFloat(1, fRisePct);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iMonth = (int)istream.readInt32(0, false, this.iMonth);
        this.fRisePct = (float)istream.readFloat(1, false, this.fRisePct);
    }

}

