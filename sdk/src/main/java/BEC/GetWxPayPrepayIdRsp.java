package BEC;

public final class GetWxPayPrepayIdRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iReturnCode = 0;

    public String sResultDesc = "";

    public WxPayPrepayInfo stPrepay = null;

    public int getIReturnCode()
    {
        return iReturnCode;
    }

    public void  setIReturnCode(int iReturnCode)
    {
        this.iReturnCode = iReturnCode;
    }

    public String getSResultDesc()
    {
        return sResultDesc;
    }

    public void  setSResultDesc(String sResultDesc)
    {
        this.sResultDesc = sResultDesc;
    }

    public WxPayPrepayInfo getStPrepay()
    {
        return stPrepay;
    }

    public void  setStPrepay(WxPayPrepayInfo stPrepay)
    {
        this.stPrepay = stPrepay;
    }

    public GetWxPayPrepayIdRsp()
    {
    }

    public GetWxPayPrepayIdRsp(int iReturnCode, String sResultDesc, WxPayPrepayInfo stPrepay)
    {
        this.iReturnCode = iReturnCode;
        this.sResultDesc = sResultDesc;
        this.stPrepay = stPrepay;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iReturnCode);
        if (null != sResultDesc) {
            ostream.writeString(1, sResultDesc);
        }
        if (null != stPrepay) {
            ostream.writeMessage(2, stPrepay);
        }
    }

    static WxPayPrepayInfo VAR_TYPE_4_STPREPAY = new WxPayPrepayInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iReturnCode = (int)istream.readInt32(0, false, this.iReturnCode);
        this.sResultDesc = (String)istream.readString(1, false, this.sResultDesc);
        this.stPrepay = (WxPayPrepayInfo)istream.readMessage(2, false, VAR_TYPE_4_STPREPAY);
    }

}

