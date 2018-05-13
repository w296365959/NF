package BEC;

public final class ReportFinishTaskRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iGetPoints = 0;

    public int getIGetPoints()
    {
        return iGetPoints;
    }

    public void  setIGetPoints(int iGetPoints)
    {
        this.iGetPoints = iGetPoints;
    }

    public ReportFinishTaskRsp()
    {
    }

    public ReportFinishTaskRsp(int iGetPoints)
    {
        this.iGetPoints = iGetPoints;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iGetPoints);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iGetPoints = (int)istream.readInt32(0, false, this.iGetPoints);
    }

}

