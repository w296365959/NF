package BEC;

public final class CapitalDDZReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iStartxh = 0;

    public int iNum = 0;

    public String sDtSecCode = "";

    public byte [] vGuid = null;

    public int eCapitalDDZType = BEC.E_CAPITAL_DDZ_TYPE.E_CDT_MIN;

    public int getIStartxh()
    {
        return iStartxh;
    }

    public void  setIStartxh(int iStartxh)
    {
        this.iStartxh = iStartxh;
    }

    public int getINum()
    {
        return iNum;
    }

    public void  setINum(int iNum)
    {
        this.iNum = iNum;
    }

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public byte [] getVGuid()
    {
        return vGuid;
    }

    public void  setVGuid(byte [] vGuid)
    {
        this.vGuid = vGuid;
    }

    public int getECapitalDDZType()
    {
        return eCapitalDDZType;
    }

    public void  setECapitalDDZType(int eCapitalDDZType)
    {
        this.eCapitalDDZType = eCapitalDDZType;
    }

    public CapitalDDZReq()
    {
    }

    public CapitalDDZReq(int iStartxh, int iNum, String sDtSecCode, byte [] vGuid, int eCapitalDDZType)
    {
        this.iStartxh = iStartxh;
        this.iNum = iNum;
        this.sDtSecCode = sDtSecCode;
        this.vGuid = vGuid;
        this.eCapitalDDZType = eCapitalDDZType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(1, iStartxh);
        ostream.writeInt32(2, iNum);
        if (null != sDtSecCode) {
            ostream.writeString(3, sDtSecCode);
        }
        if (null != vGuid) {
            ostream.writeBytes(4, vGuid);
        }
        ostream.writeInt32(5, eCapitalDDZType);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iStartxh = (int)istream.readInt32(1, false, this.iStartxh);
        this.iNum = (int)istream.readInt32(2, false, this.iNum);
        this.sDtSecCode = (String)istream.readString(3, false, this.sDtSecCode);
        this.vGuid = (byte [])istream.readBytes(4, false, this.vGuid);
        this.eCapitalDDZType = (int)istream.readInt32(5, false, this.eCapitalDDZType);
    }

}

