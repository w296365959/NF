package BEC;

public final class IntelliSec extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sChnName = "";

    public String sDtCode = "";

    public float fFirstIncrease = 0;

    public float fToNowIncrease = 0;

    public float fDayAvgIncrease = 0;

    public float fSelectedPrice = 0;

    public float fMaxIncrease = 0;

    public String getSChnName()
    {
        return sChnName;
    }

    public void  setSChnName(String sChnName)
    {
        this.sChnName = sChnName;
    }

    public String getSDtCode()
    {
        return sDtCode;
    }

    public void  setSDtCode(String sDtCode)
    {
        this.sDtCode = sDtCode;
    }

    public float getFFirstIncrease()
    {
        return fFirstIncrease;
    }

    public void  setFFirstIncrease(float fFirstIncrease)
    {
        this.fFirstIncrease = fFirstIncrease;
    }

    public float getFToNowIncrease()
    {
        return fToNowIncrease;
    }

    public void  setFToNowIncrease(float fToNowIncrease)
    {
        this.fToNowIncrease = fToNowIncrease;
    }

    public float getFDayAvgIncrease()
    {
        return fDayAvgIncrease;
    }

    public void  setFDayAvgIncrease(float fDayAvgIncrease)
    {
        this.fDayAvgIncrease = fDayAvgIncrease;
    }

    public float getFSelectedPrice()
    {
        return fSelectedPrice;
    }

    public void  setFSelectedPrice(float fSelectedPrice)
    {
        this.fSelectedPrice = fSelectedPrice;
    }

    public float getFMaxIncrease()
    {
        return fMaxIncrease;
    }

    public void  setFMaxIncrease(float fMaxIncrease)
    {
        this.fMaxIncrease = fMaxIncrease;
    }

    public IntelliSec()
    {
    }

    public IntelliSec(String sChnName, String sDtCode, float fFirstIncrease, float fToNowIncrease, float fDayAvgIncrease, float fSelectedPrice, float fMaxIncrease)
    {
        this.sChnName = sChnName;
        this.sDtCode = sDtCode;
        this.fFirstIncrease = fFirstIncrease;
        this.fToNowIncrease = fToNowIncrease;
        this.fDayAvgIncrease = fDayAvgIncrease;
        this.fSelectedPrice = fSelectedPrice;
        this.fMaxIncrease = fMaxIncrease;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sChnName) {
            ostream.writeString(0, sChnName);
        }
        if (null != sDtCode) {
            ostream.writeString(1, sDtCode);
        }
        ostream.writeFloat(2, fFirstIncrease);
        ostream.writeFloat(3, fToNowIncrease);
        ostream.writeFloat(4, fDayAvgIncrease);
        ostream.writeFloat(5, fSelectedPrice);
        ostream.writeFloat(6, fMaxIncrease);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sChnName = (String)istream.readString(0, false, this.sChnName);
        this.sDtCode = (String)istream.readString(1, false, this.sDtCode);
        this.fFirstIncrease = (float)istream.readFloat(2, false, this.fFirstIncrease);
        this.fToNowIncrease = (float)istream.readFloat(3, false, this.fToNowIncrease);
        this.fDayAvgIncrease = (float)istream.readFloat(4, false, this.fDayAvgIncrease);
        this.fSelectedPrice = (float)istream.readFloat(5, false, this.fSelectedPrice);
        this.fMaxIncrease = (float)istream.readFloat(6, false, this.fMaxIncrease);
    }

}

