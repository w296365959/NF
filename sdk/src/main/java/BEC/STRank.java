package BEC;

public final class STRank extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iRank = 0;

    public int iDtNum = 0;

    public int getIRank()
    {
        return iRank;
    }

    public void  setIRank(int iRank)
    {
        this.iRank = iRank;
    }

    public int getIDtNum()
    {
        return iDtNum;
    }

    public void  setIDtNum(int iDtNum)
    {
        this.iDtNum = iDtNum;
    }

    public STRank()
    {
    }

    public STRank(int iRank, int iDtNum)
    {
        this.iRank = iRank;
        this.iDtNum = iDtNum;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iRank);
        ostream.writeInt32(1, iDtNum);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iRank = (int)istream.readInt32(0, false, this.iRank);
        this.iDtNum = (int)istream.readInt32(1, false, this.iDtNum);
    }

}

