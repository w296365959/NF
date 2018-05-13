package BEC;

public final class SetUserRelationRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iRetCode = 0;

    public int getIRetCode()
    {
        return iRetCode;
    }

    public void  setIRetCode(int iRetCode)
    {
        this.iRetCode = iRetCode;
    }

    public SetUserRelationRsp()
    {
    }

    public SetUserRelationRsp(int iRetCode)
    {
        this.iRetCode = iRetCode;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iRetCode);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iRetCode = (int)istream.readInt32(0, false, this.iRetCode);
    }

}

