package BEC;

public final class CapitalFlowReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public int iPeriod = 0;

    public int iStartxh = 0;

    public int iNum = 0;

    public byte [] vGuid = null;

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public int getIPeriod()
    {
        return iPeriod;
    }

    public void  setIPeriod(int iPeriod)
    {
        this.iPeriod = iPeriod;
    }

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

    public byte [] getVGuid()
    {
        return vGuid;
    }

    public void  setVGuid(byte [] vGuid)
    {
        this.vGuid = vGuid;
    }

    public CapitalFlowReq()
    {
    }

    public CapitalFlowReq(String sDtSecCode, int iPeriod, int iStartxh, int iNum, byte [] vGuid)
    {
        this.sDtSecCode = sDtSecCode;
        this.iPeriod = iPeriod;
        this.iStartxh = iStartxh;
        this.iNum = iNum;
        this.vGuid = vGuid;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        ostream.writeInt32(1, iPeriod);
        ostream.writeInt32(2, iStartxh);
        ostream.writeInt32(3, iNum);
        if (null != vGuid) {
            ostream.writeBytes(4, vGuid);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.iPeriod = (int)istream.readInt32(1, false, this.iPeriod);
        this.iStartxh = (int)istream.readInt32(2, false, this.iStartxh);
        this.iNum = (int)istream.readInt32(3, false, this.iNum);
        this.vGuid = (byte [])istream.readBytes(4, false, this.vGuid);
    }

}

