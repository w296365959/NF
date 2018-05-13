package BEC;

public final class VerifyCodeValue extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sPhoneCode = "";

    public int iPhoneCodeTime = 0;

    public String getSPhoneCode()
    {
        return sPhoneCode;
    }

    public void  setSPhoneCode(String sPhoneCode)
    {
        this.sPhoneCode = sPhoneCode;
    }

    public int getIPhoneCodeTime()
    {
        return iPhoneCodeTime;
    }

    public void  setIPhoneCodeTime(int iPhoneCodeTime)
    {
        this.iPhoneCodeTime = iPhoneCodeTime;
    }

    public VerifyCodeValue()
    {
    }

    public VerifyCodeValue(String sPhoneCode, int iPhoneCodeTime)
    {
        this.sPhoneCode = sPhoneCode;
        this.iPhoneCodeTime = iPhoneCodeTime;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sPhoneCode) {
            ostream.writeString(0, sPhoneCode);
        }
        ostream.writeInt32(1, iPhoneCodeTime);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sPhoneCode = (String)istream.readString(0, false, this.sPhoneCode);
        this.iPhoneCodeTime = (int)istream.readInt32(1, false, this.iPhoneCodeTime);
    }

}

