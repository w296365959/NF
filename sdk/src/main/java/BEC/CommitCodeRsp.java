package BEC;

public final class CommitCodeRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iCodeType = 0;

    public int iGetPoints = 0;

    public int getICodeType()
    {
        return iCodeType;
    }

    public void  setICodeType(int iCodeType)
    {
        this.iCodeType = iCodeType;
    }

    public int getIGetPoints()
    {
        return iGetPoints;
    }

    public void  setIGetPoints(int iGetPoints)
    {
        this.iGetPoints = iGetPoints;
    }

    public CommitCodeRsp()
    {
    }

    public CommitCodeRsp(int iCodeType, int iGetPoints)
    {
        this.iCodeType = iCodeType;
        this.iGetPoints = iGetPoints;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iCodeType);
        ostream.writeInt32(1, iGetPoints);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iCodeType = (int)istream.readInt32(0, false, this.iCodeType);
        this.iGetPoints = (int)istream.readInt32(1, false, this.iGetPoints);
    }

}

