package BEC;

public final class SaveFavorIndRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int eRet = 0;

    public int getERet()
    {
        return eRet;
    }

    public void  setERet(int eRet)
    {
        this.eRet = eRet;
    }

    public SaveFavorIndRsp()
    {
    }

    public SaveFavorIndRsp(int eRet)
    {
        this.eRet = eRet;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, eRet);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.eRet = (int)istream.readInt32(0, true, this.eRet);
    }

}

