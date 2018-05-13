package BEC;

public final class GetAliPaySignRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iReturnCode = 0;

    public String sSign = "";

    public int iStatus = 0;

    public int getIReturnCode()
    {
        return iReturnCode;
    }

    public void  setIReturnCode(int iReturnCode)
    {
        this.iReturnCode = iReturnCode;
    }

    public String getSSign()
    {
        return sSign;
    }

    public void  setSSign(String sSign)
    {
        this.sSign = sSign;
    }

    public int getIStatus()
    {
        return iStatus;
    }

    public void  setIStatus(int iStatus)
    {
        this.iStatus = iStatus;
    }

    public GetAliPaySignRsp()
    {
    }

    public GetAliPaySignRsp(int iReturnCode, String sSign, int iStatus)
    {
        this.iReturnCode = iReturnCode;
        this.sSign = sSign;
        this.iStatus = iStatus;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iReturnCode);
        if (null != sSign) {
            ostream.writeString(1, sSign);
        }
        ostream.writeInt32(2, iStatus);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iReturnCode = (int)istream.readInt32(0, false, this.iReturnCode);
        this.sSign = (String)istream.readString(1, false, this.sSign);
        this.iStatus = (int)istream.readInt32(2, false, this.iStatus);
    }

}

