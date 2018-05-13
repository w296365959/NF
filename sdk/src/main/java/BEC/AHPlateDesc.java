package BEC;

public final class AHPlateDesc extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public float fPremiumrate = 0;

    public String sADtSecCode = "";

    public String sHKDtSecCode = "";

    public String sASecName = "";

    public String sHKSecName = "";

    public float fANow = 0;

    public float fHKNow = 0;

    public float fAIncrease = 0;

    public float fHKIncrease = 0;

    public long lATotalhand = 0;

    public long lHKTotalhand = 0;

    public float fAFhsl = 0;

    public float fHKFhsl = 0;

    public float getFPremiumrate()
    {
        return fPremiumrate;
    }

    public void  setFPremiumrate(float fPremiumrate)
    {
        this.fPremiumrate = fPremiumrate;
    }

    public String getSADtSecCode()
    {
        return sADtSecCode;
    }

    public void  setSADtSecCode(String sADtSecCode)
    {
        this.sADtSecCode = sADtSecCode;
    }

    public String getSHKDtSecCode()
    {
        return sHKDtSecCode;
    }

    public void  setSHKDtSecCode(String sHKDtSecCode)
    {
        this.sHKDtSecCode = sHKDtSecCode;
    }

    public String getSASecName()
    {
        return sASecName;
    }

    public void  setSASecName(String sASecName)
    {
        this.sASecName = sASecName;
    }

    public String getSHKSecName()
    {
        return sHKSecName;
    }

    public void  setSHKSecName(String sHKSecName)
    {
        this.sHKSecName = sHKSecName;
    }

    public float getFANow()
    {
        return fANow;
    }

    public void  setFANow(float fANow)
    {
        this.fANow = fANow;
    }

    public float getFHKNow()
    {
        return fHKNow;
    }

    public void  setFHKNow(float fHKNow)
    {
        this.fHKNow = fHKNow;
    }

    public float getFAIncrease()
    {
        return fAIncrease;
    }

    public void  setFAIncrease(float fAIncrease)
    {
        this.fAIncrease = fAIncrease;
    }

    public float getFHKIncrease()
    {
        return fHKIncrease;
    }

    public void  setFHKIncrease(float fHKIncrease)
    {
        this.fHKIncrease = fHKIncrease;
    }

    public long getLATotalhand()
    {
        return lATotalhand;
    }

    public void  setLATotalhand(long lATotalhand)
    {
        this.lATotalhand = lATotalhand;
    }

    public long getLHKTotalhand()
    {
        return lHKTotalhand;
    }

    public void  setLHKTotalhand(long lHKTotalhand)
    {
        this.lHKTotalhand = lHKTotalhand;
    }

    public float getFAFhsl()
    {
        return fAFhsl;
    }

    public void  setFAFhsl(float fAFhsl)
    {
        this.fAFhsl = fAFhsl;
    }

    public float getFHKFhsl()
    {
        return fHKFhsl;
    }

    public void  setFHKFhsl(float fHKFhsl)
    {
        this.fHKFhsl = fHKFhsl;
    }

    public AHPlateDesc()
    {
    }

    public AHPlateDesc(float fPremiumrate, String sADtSecCode, String sHKDtSecCode, String sASecName, String sHKSecName, float fANow, float fHKNow, float fAIncrease, float fHKIncrease, long lATotalhand, long lHKTotalhand, float fAFhsl, float fHKFhsl)
    {
        this.fPremiumrate = fPremiumrate;
        this.sADtSecCode = sADtSecCode;
        this.sHKDtSecCode = sHKDtSecCode;
        this.sASecName = sASecName;
        this.sHKSecName = sHKSecName;
        this.fANow = fANow;
        this.fHKNow = fHKNow;
        this.fAIncrease = fAIncrease;
        this.fHKIncrease = fHKIncrease;
        this.lATotalhand = lATotalhand;
        this.lHKTotalhand = lHKTotalhand;
        this.fAFhsl = fAFhsl;
        this.fHKFhsl = fHKFhsl;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeFloat(0, fPremiumrate);
        if (null != sADtSecCode) {
            ostream.writeString(1, sADtSecCode);
        }
        if (null != sHKDtSecCode) {
            ostream.writeString(2, sHKDtSecCode);
        }
        if (null != sASecName) {
            ostream.writeString(3, sASecName);
        }
        if (null != sHKSecName) {
            ostream.writeString(4, sHKSecName);
        }
        ostream.writeFloat(5, fANow);
        ostream.writeFloat(6, fHKNow);
        ostream.writeFloat(7, fAIncrease);
        ostream.writeFloat(8, fHKIncrease);
        ostream.writeInt64(9, lATotalhand);
        ostream.writeInt64(10, lHKTotalhand);
        ostream.writeFloat(11, fAFhsl);
        ostream.writeFloat(12, fHKFhsl);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.fPremiumrate = (float)istream.readFloat(0, false, this.fPremiumrate);
        this.sADtSecCode = (String)istream.readString(1, false, this.sADtSecCode);
        this.sHKDtSecCode = (String)istream.readString(2, false, this.sHKDtSecCode);
        this.sASecName = (String)istream.readString(3, false, this.sASecName);
        this.sHKSecName = (String)istream.readString(4, false, this.sHKSecName);
        this.fANow = (float)istream.readFloat(5, false, this.fANow);
        this.fHKNow = (float)istream.readFloat(6, false, this.fHKNow);
        this.fAIncrease = (float)istream.readFloat(7, false, this.fAIncrease);
        this.fHKIncrease = (float)istream.readFloat(8, false, this.fHKIncrease);
        this.lATotalhand = (long)istream.readInt64(9, false, this.lATotalhand);
        this.lHKTotalhand = (long)istream.readInt64(10, false, this.lHKTotalhand);
        this.fAFhsl = (float)istream.readFloat(11, false, this.fAFhsl);
        this.fHKFhsl = (float)istream.readFloat(12, false, this.fHKFhsl);
    }

}

