package BEC;

public final class ConcQuoteDesc extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public String sConcName = "";

    public float fClose = 0;

    public float fOpen = 0;

    public float fMax = 0;

    public float fMin = 0;

    public float fNow = 0;

    public long lNowVolume = 0;

    public float fAmout = 0;

    public float fHot = 0;

    public String sHeadName = "";

    public float fHeadNow = 0;

    public float fHeadClose = 0;

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public String getSConcName()
    {
        return sConcName;
    }

    public void  setSConcName(String sConcName)
    {
        this.sConcName = sConcName;
    }

    public float getFClose()
    {
        return fClose;
    }

    public void  setFClose(float fClose)
    {
        this.fClose = fClose;
    }

    public float getFOpen()
    {
        return fOpen;
    }

    public void  setFOpen(float fOpen)
    {
        this.fOpen = fOpen;
    }

    public float getFMax()
    {
        return fMax;
    }

    public void  setFMax(float fMax)
    {
        this.fMax = fMax;
    }

    public float getFMin()
    {
        return fMin;
    }

    public void  setFMin(float fMin)
    {
        this.fMin = fMin;
    }

    public float getFNow()
    {
        return fNow;
    }

    public void  setFNow(float fNow)
    {
        this.fNow = fNow;
    }

    public long getLNowVolume()
    {
        return lNowVolume;
    }

    public void  setLNowVolume(long lNowVolume)
    {
        this.lNowVolume = lNowVolume;
    }

    public float getFAmout()
    {
        return fAmout;
    }

    public void  setFAmout(float fAmout)
    {
        this.fAmout = fAmout;
    }

    public float getFHot()
    {
        return fHot;
    }

    public void  setFHot(float fHot)
    {
        this.fHot = fHot;
    }

    public String getSHeadName()
    {
        return sHeadName;
    }

    public void  setSHeadName(String sHeadName)
    {
        this.sHeadName = sHeadName;
    }

    public float getFHeadNow()
    {
        return fHeadNow;
    }

    public void  setFHeadNow(float fHeadNow)
    {
        this.fHeadNow = fHeadNow;
    }

    public float getFHeadClose()
    {
        return fHeadClose;
    }

    public void  setFHeadClose(float fHeadClose)
    {
        this.fHeadClose = fHeadClose;
    }

    public ConcQuoteDesc()
    {
    }

    public ConcQuoteDesc(String sDtSecCode, String sConcName, float fClose, float fOpen, float fMax, float fMin, float fNow, long lNowVolume, float fAmout, float fHot, String sHeadName, float fHeadNow, float fHeadClose)
    {
        this.sDtSecCode = sDtSecCode;
        this.sConcName = sConcName;
        this.fClose = fClose;
        this.fOpen = fOpen;
        this.fMax = fMax;
        this.fMin = fMin;
        this.fNow = fNow;
        this.lNowVolume = lNowVolume;
        this.fAmout = fAmout;
        this.fHot = fHot;
        this.sHeadName = sHeadName;
        this.fHeadNow = fHeadNow;
        this.fHeadClose = fHeadClose;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        if (null != sConcName) {
            ostream.writeString(1, sConcName);
        }
        ostream.writeFloat(2, fClose);
        ostream.writeFloat(3, fOpen);
        ostream.writeFloat(4, fMax);
        ostream.writeFloat(5, fMin);
        ostream.writeFloat(6, fNow);
        ostream.writeInt64(7, lNowVolume);
        ostream.writeFloat(8, fAmout);
        ostream.writeFloat(9, fHot);
        if (null != sHeadName) {
            ostream.writeString(10, sHeadName);
        }
        ostream.writeFloat(11, fHeadNow);
        ostream.writeFloat(12, fHeadClose);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.sConcName = (String)istream.readString(1, false, this.sConcName);
        this.fClose = (float)istream.readFloat(2, false, this.fClose);
        this.fOpen = (float)istream.readFloat(3, false, this.fOpen);
        this.fMax = (float)istream.readFloat(4, false, this.fMax);
        this.fMin = (float)istream.readFloat(5, false, this.fMin);
        this.fNow = (float)istream.readFloat(6, false, this.fNow);
        this.lNowVolume = (long)istream.readInt64(7, false, this.lNowVolume);
        this.fAmout = (float)istream.readFloat(8, false, this.fAmout);
        this.fHot = (float)istream.readFloat(9, false, this.fHot);
        this.sHeadName = (String)istream.readString(10, false, this.sHeadName);
        this.fHeadNow = (float)istream.readFloat(11, false, this.fHeadNow);
        this.fHeadClose = (float)istream.readFloat(12, false, this.fHeadClose);
    }

}

