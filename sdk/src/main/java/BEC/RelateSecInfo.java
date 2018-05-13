package BEC;

public final class RelateSecInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public float fRelate = 0;

    public BEC.SecInfo stSecInfo = null;

    public float getFRelate()
    {
        return fRelate;
    }

    public void  setFRelate(float fRelate)
    {
        this.fRelate = fRelate;
    }

    public BEC.SecInfo getStSecInfo()
    {
        return stSecInfo;
    }

    public void  setStSecInfo(BEC.SecInfo stSecInfo)
    {
        this.stSecInfo = stSecInfo;
    }

    public RelateSecInfo()
    {
    }

    public RelateSecInfo(float fRelate, BEC.SecInfo stSecInfo)
    {
        this.fRelate = fRelate;
        this.stSecInfo = stSecInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeFloat(0, fRelate);
        if (null != stSecInfo) {
            ostream.writeMessage(1, stSecInfo);
        }
    }

    static BEC.SecInfo VAR_TYPE_4_STSECINFO = new BEC.SecInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.fRelate = (float)istream.readFloat(0, false, this.fRelate);
        this.stSecInfo = (BEC.SecInfo)istream.readMessage(1, false, VAR_TYPE_4_STSECINFO);
    }

}

