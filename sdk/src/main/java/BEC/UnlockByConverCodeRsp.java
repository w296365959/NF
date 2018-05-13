package BEC;

public final class UnlockByConverCodeRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iResult = 0;

    public int getIResult()
    {
        return iResult;
    }

    public void  setIResult(int iResult)
    {
        this.iResult = iResult;
    }

    public UnlockByConverCodeRsp()
    {
    }

    public UnlockByConverCodeRsp(int iResult)
    {
        this.iResult = iResult;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iResult);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iResult = (int)istream.readInt32(0, false, this.iResult);
    }

}

