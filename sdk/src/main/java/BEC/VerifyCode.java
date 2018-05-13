package BEC;

public final class VerifyCode extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public short iType = 0;

    public String sPhoneVerifyCode = "";

    public String sMachineVerifyCode = "";

    public String sMachineCodeUrl = "";

    public short getIType()
    {
        return iType;
    }

    public void  setIType(short iType)
    {
        this.iType = iType;
    }

    public String getSPhoneVerifyCode()
    {
        return sPhoneVerifyCode;
    }

    public void  setSPhoneVerifyCode(String sPhoneVerifyCode)
    {
        this.sPhoneVerifyCode = sPhoneVerifyCode;
    }

    public String getSMachineVerifyCode()
    {
        return sMachineVerifyCode;
    }

    public void  setSMachineVerifyCode(String sMachineVerifyCode)
    {
        this.sMachineVerifyCode = sMachineVerifyCode;
    }

    public String getSMachineCodeUrl()
    {
        return sMachineCodeUrl;
    }

    public void  setSMachineCodeUrl(String sMachineCodeUrl)
    {
        this.sMachineCodeUrl = sMachineCodeUrl;
    }

    public VerifyCode()
    {
    }

    public VerifyCode(short iType, String sPhoneVerifyCode, String sMachineVerifyCode, String sMachineCodeUrl)
    {
        this.iType = iType;
        this.sPhoneVerifyCode = sPhoneVerifyCode;
        this.sMachineVerifyCode = sMachineVerifyCode;
        this.sMachineCodeUrl = sMachineCodeUrl;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt16(0, iType);
        if (null != sPhoneVerifyCode) {
            ostream.writeString(1, sPhoneVerifyCode);
        }
        if (null != sMachineVerifyCode) {
            ostream.writeString(2, sMachineVerifyCode);
        }
        if (null != sMachineCodeUrl) {
            ostream.writeString(3, sMachineCodeUrl);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iType = (short)istream.readInt16(0, true, this.iType);
        this.sPhoneVerifyCode = (String)istream.readString(1, false, this.sPhoneVerifyCode);
        this.sMachineVerifyCode = (String)istream.readString(2, false, this.sMachineVerifyCode);
        this.sMachineCodeUrl = (String)istream.readString(3, false, this.sMachineCodeUrl);
    }

}

