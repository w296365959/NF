package BEC;

public final class GetOrderPayResultRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iReturnCode = 0;

    public int iPayStatus = 0;

    public int getIReturnCode()
    {
        return iReturnCode;
    }

    public void  setIReturnCode(int iReturnCode)
    {
        this.iReturnCode = iReturnCode;
    }

    public int getIPayStatus()
    {
        return iPayStatus;
    }

    public void  setIPayStatus(int iPayStatus)
    {
        this.iPayStatus = iPayStatus;
    }

    public GetOrderPayResultRsp()
    {
    }

    public GetOrderPayResultRsp(int iReturnCode, int iPayStatus)
    {
        this.iReturnCode = iReturnCode;
        this.iPayStatus = iPayStatus;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iReturnCode);
        ostream.writeInt32(1, iPayStatus);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iReturnCode = (int)istream.readInt32(0, false, this.iReturnCode);
        this.iPayStatus = (int)istream.readInt32(1, false, this.iPayStatus);
    }

}

