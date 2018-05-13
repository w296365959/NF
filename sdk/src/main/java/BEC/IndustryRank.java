package BEC;

public final class IndustryRank extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iPosition = 0;

    public int iTotal = 0;

    public int getIPosition()
    {
        return iPosition;
    }

    public void  setIPosition(int iPosition)
    {
        this.iPosition = iPosition;
    }

    public int getITotal()
    {
        return iTotal;
    }

    public void  setITotal(int iTotal)
    {
        this.iTotal = iTotal;
    }

    public IndustryRank()
    {
    }

    public IndustryRank(int iPosition, int iTotal)
    {
        this.iPosition = iPosition;
        this.iTotal = iTotal;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iPosition);
        ostream.writeInt32(1, iTotal);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iPosition = (int)istream.readInt32(0, false, this.iPosition);
        this.iTotal = (int)istream.readInt32(1, false, this.iTotal);
    }

}

