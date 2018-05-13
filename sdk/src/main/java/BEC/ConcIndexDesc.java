package BEC;

public final class ConcIndexDesc extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public long lYmd = 0;

    public float fHotIndex = 0;

    public long getLYmd()
    {
        return lYmd;
    }

    public void  setLYmd(long lYmd)
    {
        this.lYmd = lYmd;
    }

    public float getFHotIndex()
    {
        return fHotIndex;
    }

    public void  setFHotIndex(float fHotIndex)
    {
        this.fHotIndex = fHotIndex;
    }

    public ConcIndexDesc()
    {
    }

    public ConcIndexDesc(long lYmd, float fHotIndex)
    {
        this.lYmd = lYmd;
        this.fHotIndex = fHotIndex;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt64(0, lYmd);
        ostream.writeFloat(1, fHotIndex);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.lYmd = (long)istream.readInt64(0, false, this.lYmd);
        this.fHotIndex = (float)istream.readFloat(1, false, this.fHotIndex);
    }

}

