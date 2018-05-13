package BEC;

public final class FiltedDetail extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sName = "";

    public float fLiftedCount = 0;

    public String getSName()
    {
        return sName;
    }

    public void  setSName(String sName)
    {
        this.sName = sName;
    }

    public float getFLiftedCount()
    {
        return fLiftedCount;
    }

    public void  setFLiftedCount(float fLiftedCount)
    {
        this.fLiftedCount = fLiftedCount;
    }

    public FiltedDetail()
    {
    }

    public FiltedDetail(String sName, float fLiftedCount)
    {
        this.sName = sName;
        this.fLiftedCount = fLiftedCount;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sName) {
            ostream.writeString(0, sName);
        }
        ostream.writeFloat(1, fLiftedCount);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sName = (String)istream.readString(0, false, this.sName);
        this.fLiftedCount = (float)istream.readFloat(1, false, this.fLiftedCount);
    }

}

