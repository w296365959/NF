package BEC;

public final class GetH5PayUrlRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iReturnCode = 0;

    public String sPayUrl = "";

    public int getIReturnCode()
    {
        return iReturnCode;
    }

    public void  setIReturnCode(int iReturnCode)
    {
        this.iReturnCode = iReturnCode;
    }

    public String getSPayUrl()
    {
        return sPayUrl;
    }

    public void  setSPayUrl(String sPayUrl)
    {
        this.sPayUrl = sPayUrl;
    }

    public GetH5PayUrlRsp()
    {
    }

    public GetH5PayUrlRsp(int iReturnCode, String sPayUrl)
    {
        this.iReturnCode = iReturnCode;
        this.sPayUrl = sPayUrl;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iReturnCode);
        if (null != sPayUrl) {
            ostream.writeString(1, sPayUrl);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iReturnCode = (int)istream.readInt32(0, false, this.iReturnCode);
        this.sPayUrl = (String)istream.readString(1, false, this.sPayUrl);
    }

}

