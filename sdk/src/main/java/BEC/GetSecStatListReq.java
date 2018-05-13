package BEC;

public final class GetSecStatListReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iStart = 0;

    public int iWantNum = 0;

    public int getIStart()
    {
        return iStart;
    }

    public void  setIStart(int iStart)
    {
        this.iStart = iStart;
    }

    public int getIWantNum()
    {
        return iWantNum;
    }

    public void  setIWantNum(int iWantNum)
    {
        this.iWantNum = iWantNum;
    }

    public GetSecStatListReq()
    {
    }

    public GetSecStatListReq(int iStart, int iWantNum)
    {
        this.iStart = iStart;
        this.iWantNum = iWantNum;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iStart);
        ostream.writeInt32(1, iWantNum);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iStart = (int)istream.readInt32(0, false, this.iStart);
        this.iWantNum = (int)istream.readInt32(1, false, this.iWantNum);
    }

}

