package BEC;

public final class VerifyAccountInfoRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.VerifyCode stVerifyCode = null;

    public BEC.VerifyCode getStVerifyCode()
    {
        return stVerifyCode;
    }

    public void  setStVerifyCode(BEC.VerifyCode stVerifyCode)
    {
        this.stVerifyCode = stVerifyCode;
    }

    public VerifyAccountInfoRsp()
    {
    }

    public VerifyAccountInfoRsp(BEC.VerifyCode stVerifyCode)
    {
        this.stVerifyCode = stVerifyCode;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stVerifyCode) {
            ostream.writeMessage(0, stVerifyCode);
        }
    }

    static BEC.VerifyCode VAR_TYPE_4_STVERIFYCODE = new BEC.VerifyCode();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stVerifyCode = (BEC.VerifyCode)istream.readMessage(0, false, VAR_TYPE_4_STVERIFYCODE);
    }

}

