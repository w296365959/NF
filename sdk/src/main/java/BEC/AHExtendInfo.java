package BEC;

public final class AHExtendInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public float fRmbHkExchangeRate = 0;

    public float fSHHKFlowInto = 0;

    public float fSHHKQuota = 0;

    public float fHKSHFlowInto = 0;

    public float fHKSHQuota = 0;

    public float getFRmbHkExchangeRate()
    {
        return fRmbHkExchangeRate;
    }

    public void  setFRmbHkExchangeRate(float fRmbHkExchangeRate)
    {
        this.fRmbHkExchangeRate = fRmbHkExchangeRate;
    }

    public float getFSHHKFlowInto()
    {
        return fSHHKFlowInto;
    }

    public void  setFSHHKFlowInto(float fSHHKFlowInto)
    {
        this.fSHHKFlowInto = fSHHKFlowInto;
    }

    public float getFSHHKQuota()
    {
        return fSHHKQuota;
    }

    public void  setFSHHKQuota(float fSHHKQuota)
    {
        this.fSHHKQuota = fSHHKQuota;
    }

    public float getFHKSHFlowInto()
    {
        return fHKSHFlowInto;
    }

    public void  setFHKSHFlowInto(float fHKSHFlowInto)
    {
        this.fHKSHFlowInto = fHKSHFlowInto;
    }

    public float getFHKSHQuota()
    {
        return fHKSHQuota;
    }

    public void  setFHKSHQuota(float fHKSHQuota)
    {
        this.fHKSHQuota = fHKSHQuota;
    }

    public AHExtendInfo()
    {
    }

    public AHExtendInfo(float fRmbHkExchangeRate, float fSHHKFlowInto, float fSHHKQuota, float fHKSHFlowInto, float fHKSHQuota)
    {
        this.fRmbHkExchangeRate = fRmbHkExchangeRate;
        this.fSHHKFlowInto = fSHHKFlowInto;
        this.fSHHKQuota = fSHHKQuota;
        this.fHKSHFlowInto = fHKSHFlowInto;
        this.fHKSHQuota = fHKSHQuota;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeFloat(0, fRmbHkExchangeRate);
        ostream.writeFloat(1, fSHHKFlowInto);
        ostream.writeFloat(2, fSHHKQuota);
        ostream.writeFloat(3, fHKSHFlowInto);
        ostream.writeFloat(4, fHKSHQuota);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.fRmbHkExchangeRate = (float)istream.readFloat(0, false, this.fRmbHkExchangeRate);
        this.fSHHKFlowInto = (float)istream.readFloat(1, false, this.fSHHKFlowInto);
        this.fSHHKQuota = (float)istream.readFloat(2, false, this.fSHHKQuota);
        this.fHKSHFlowInto = (float)istream.readFloat(3, false, this.fHKSHFlowInto);
        this.fHKSHQuota = (float)istream.readFloat(4, false, this.fHKSHQuota);
    }

}

