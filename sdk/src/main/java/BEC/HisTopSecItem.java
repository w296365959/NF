package BEC;

public final class HisTopSecItem extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sSecName = "";

    public String sDtSecCode = "";

    public int iTotalYear = 0;

    public int iUpYear = 0;

    public float fRisePct = 0;

    public float fRiseChance = 0;

    public String getSSecName()
    {
        return sSecName;
    }

    public void  setSSecName(String sSecName)
    {
        this.sSecName = sSecName;
    }

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public int getITotalYear()
    {
        return iTotalYear;
    }

    public void  setITotalYear(int iTotalYear)
    {
        this.iTotalYear = iTotalYear;
    }

    public int getIUpYear()
    {
        return iUpYear;
    }

    public void  setIUpYear(int iUpYear)
    {
        this.iUpYear = iUpYear;
    }

    public float getFRisePct()
    {
        return fRisePct;
    }

    public void  setFRisePct(float fRisePct)
    {
        this.fRisePct = fRisePct;
    }

    public float getFRiseChance()
    {
        return fRiseChance;
    }

    public void  setFRiseChance(float fRiseChance)
    {
        this.fRiseChance = fRiseChance;
    }

    public HisTopSecItem()
    {
    }

    public HisTopSecItem(String sSecName, String sDtSecCode, int iTotalYear, int iUpYear, float fRisePct, float fRiseChance)
    {
        this.sSecName = sSecName;
        this.sDtSecCode = sDtSecCode;
        this.iTotalYear = iTotalYear;
        this.iUpYear = iUpYear;
        this.fRisePct = fRisePct;
        this.fRiseChance = fRiseChance;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sSecName) {
            ostream.writeString(0, sSecName);
        }
        if (null != sDtSecCode) {
            ostream.writeString(1, sDtSecCode);
        }
        ostream.writeInt32(2, iTotalYear);
        ostream.writeInt32(3, iUpYear);
        ostream.writeFloat(4, fRisePct);
        ostream.writeFloat(5, fRiseChance);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sSecName = (String)istream.readString(0, false, this.sSecName);
        this.sDtSecCode = (String)istream.readString(1, false, this.sDtSecCode);
        this.iTotalYear = (int)istream.readInt32(2, false, this.iTotalYear);
        this.iUpYear = (int)istream.readInt32(3, false, this.iUpYear);
        this.fRisePct = (float)istream.readFloat(4, false, this.fRisePct);
        this.fRiseChance = (float)istream.readFloat(5, false, this.fRiseChance);
    }

}

