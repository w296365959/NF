package BEC;

public final class CapitalFlow extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public long lTime = 0;

    public float fSuperin = 0;

    public float fSuperout = 0;

    public float fBigin = 0;

    public float fBigout = 0;

    public float fMidin = 0;

    public float fMidout = 0;

    public float fSmallin = 0;

    public float fSmallout = 0;

    public long getLTime()
    {
        return lTime;
    }

    public void  setLTime(long lTime)
    {
        this.lTime = lTime;
    }

    public float getFSuperin()
    {
        return fSuperin;
    }

    public void  setFSuperin(float fSuperin)
    {
        this.fSuperin = fSuperin;
    }

    public float getFSuperout()
    {
        return fSuperout;
    }

    public void  setFSuperout(float fSuperout)
    {
        this.fSuperout = fSuperout;
    }

    public float getFBigin()
    {
        return fBigin;
    }

    public void  setFBigin(float fBigin)
    {
        this.fBigin = fBigin;
    }

    public float getFBigout()
    {
        return fBigout;
    }

    public void  setFBigout(float fBigout)
    {
        this.fBigout = fBigout;
    }

    public float getFMidin()
    {
        return fMidin;
    }

    public void  setFMidin(float fMidin)
    {
        this.fMidin = fMidin;
    }

    public float getFMidout()
    {
        return fMidout;
    }

    public void  setFMidout(float fMidout)
    {
        this.fMidout = fMidout;
    }

    public float getFSmallin()
    {
        return fSmallin;
    }

    public void  setFSmallin(float fSmallin)
    {
        this.fSmallin = fSmallin;
    }

    public float getFSmallout()
    {
        return fSmallout;
    }

    public void  setFSmallout(float fSmallout)
    {
        this.fSmallout = fSmallout;
    }

    public CapitalFlow()
    {
    }

    public CapitalFlow(long lTime, float fSuperin, float fSuperout, float fBigin, float fBigout, float fMidin, float fMidout, float fSmallin, float fSmallout)
    {
        this.lTime = lTime;
        this.fSuperin = fSuperin;
        this.fSuperout = fSuperout;
        this.fBigin = fBigin;
        this.fBigout = fBigout;
        this.fMidin = fMidin;
        this.fMidout = fMidout;
        this.fSmallin = fSmallin;
        this.fSmallout = fSmallout;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt64(0, lTime);
        ostream.writeFloat(1, fSuperin);
        ostream.writeFloat(2, fSuperout);
        ostream.writeFloat(3, fBigin);
        ostream.writeFloat(4, fBigout);
        ostream.writeFloat(5, fMidin);
        ostream.writeFloat(6, fMidout);
        ostream.writeFloat(7, fSmallin);
        ostream.writeFloat(8, fSmallout);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.lTime = (long)istream.readInt64(0, false, this.lTime);
        this.fSuperin = (float)istream.readFloat(1, false, this.fSuperin);
        this.fSuperout = (float)istream.readFloat(2, false, this.fSuperout);
        this.fBigin = (float)istream.readFloat(3, false, this.fBigin);
        this.fBigout = (float)istream.readFloat(4, false, this.fBigout);
        this.fMidin = (float)istream.readFloat(5, false, this.fMidin);
        this.fMidout = (float)istream.readFloat(6, false, this.fMidout);
        this.fSmallin = (float)istream.readFloat(7, false, this.fSmallin);
        this.fSmallout = (float)istream.readFloat(8, false, this.fSmallout);
    }

}

