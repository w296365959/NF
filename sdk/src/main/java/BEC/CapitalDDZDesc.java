package BEC;

public final class CapitalDDZDesc extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public long lTime = 0;

    public float fSuperInAmt = 0;

    public float fSuperOutAmt = 0;

    public float fBigInAmt = 0;

    public float fBigOutAmt = 0;

    public float fMidInAmt = 0;

    public float fMidOutAmt = 0;

    public float fSmallInAmt = 0;

    public float fSmallOutAmt = 0;

    public long getLTime()
    {
        return lTime;
    }

    public void  setLTime(long lTime)
    {
        this.lTime = lTime;
    }

    public float getFSuperInAmt()
    {
        return fSuperInAmt;
    }

    public void  setFSuperInAmt(float fSuperInAmt)
    {
        this.fSuperInAmt = fSuperInAmt;
    }

    public float getFSuperOutAmt()
    {
        return fSuperOutAmt;
    }

    public void  setFSuperOutAmt(float fSuperOutAmt)
    {
        this.fSuperOutAmt = fSuperOutAmt;
    }

    public float getFBigInAmt()
    {
        return fBigInAmt;
    }

    public void  setFBigInAmt(float fBigInAmt)
    {
        this.fBigInAmt = fBigInAmt;
    }

    public float getFBigOutAmt()
    {
        return fBigOutAmt;
    }

    public void  setFBigOutAmt(float fBigOutAmt)
    {
        this.fBigOutAmt = fBigOutAmt;
    }

    public float getFMidInAmt()
    {
        return fMidInAmt;
    }

    public void  setFMidInAmt(float fMidInAmt)
    {
        this.fMidInAmt = fMidInAmt;
    }

    public float getFMidOutAmt()
    {
        return fMidOutAmt;
    }

    public void  setFMidOutAmt(float fMidOutAmt)
    {
        this.fMidOutAmt = fMidOutAmt;
    }

    public float getFSmallInAmt()
    {
        return fSmallInAmt;
    }

    public void  setFSmallInAmt(float fSmallInAmt)
    {
        this.fSmallInAmt = fSmallInAmt;
    }

    public float getFSmallOutAmt()
    {
        return fSmallOutAmt;
    }

    public void  setFSmallOutAmt(float fSmallOutAmt)
    {
        this.fSmallOutAmt = fSmallOutAmt;
    }

    public CapitalDDZDesc()
    {
    }

    public CapitalDDZDesc(long lTime, float fSuperInAmt, float fSuperOutAmt, float fBigInAmt, float fBigOutAmt, float fMidInAmt, float fMidOutAmt, float fSmallInAmt, float fSmallOutAmt)
    {
        this.lTime = lTime;
        this.fSuperInAmt = fSuperInAmt;
        this.fSuperOutAmt = fSuperOutAmt;
        this.fBigInAmt = fBigInAmt;
        this.fBigOutAmt = fBigOutAmt;
        this.fMidInAmt = fMidInAmt;
        this.fMidOutAmt = fMidOutAmt;
        this.fSmallInAmt = fSmallInAmt;
        this.fSmallOutAmt = fSmallOutAmt;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt64(2, lTime);
        ostream.writeFloat(3, fSuperInAmt);
        ostream.writeFloat(4, fSuperOutAmt);
        ostream.writeFloat(5, fBigInAmt);
        ostream.writeFloat(6, fBigOutAmt);
        ostream.writeFloat(7, fMidInAmt);
        ostream.writeFloat(8, fMidOutAmt);
        ostream.writeFloat(9, fSmallInAmt);
        ostream.writeFloat(10, fSmallOutAmt);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.lTime = (long)istream.readInt64(2, false, this.lTime);
        this.fSuperInAmt = (float)istream.readFloat(3, false, this.fSuperInAmt);
        this.fSuperOutAmt = (float)istream.readFloat(4, false, this.fSuperOutAmt);
        this.fBigInAmt = (float)istream.readFloat(5, false, this.fBigInAmt);
        this.fBigOutAmt = (float)istream.readFloat(6, false, this.fBigOutAmt);
        this.fMidInAmt = (float)istream.readFloat(7, false, this.fMidInAmt);
        this.fMidOutAmt = (float)istream.readFloat(8, false, this.fMidOutAmt);
        this.fSmallInAmt = (float)istream.readFloat(9, false, this.fSmallInAmt);
        this.fSmallOutAmt = (float)istream.readFloat(10, false, this.fSmallOutAmt);
    }

}

