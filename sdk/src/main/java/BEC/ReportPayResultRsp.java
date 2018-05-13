package BEC;

public final class ReportPayResultRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iPayStatus = 0;

    public int getIPayStatus()
    {
        return iPayStatus;
    }

    public void  setIPayStatus(int iPayStatus)
    {
        this.iPayStatus = iPayStatus;
    }

    public ReportPayResultRsp()
    {
    }

    public ReportPayResultRsp(int iPayStatus)
    {
        this.iPayStatus = iPayStatus;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iPayStatus);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iPayStatus = (int)istream.readInt32(0, false, this.iPayStatus);
    }

}

