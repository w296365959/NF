package BEC;

public final class KLineReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public int eKLineType = 0;

    public int iMulnum = 0;

    public int iStartxh = 0;

    public int iWantnum = 0;

    public boolean bTg = true;

    public byte [] vGuid = null;

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public int getEKLineType()
    {
        return eKLineType;
    }

    public void  setEKLineType(int eKLineType)
    {
        this.eKLineType = eKLineType;
    }

    public int getIMulnum()
    {
        return iMulnum;
    }

    public void  setIMulnum(int iMulnum)
    {
        this.iMulnum = iMulnum;
    }

    public int getIStartxh()
    {
        return iStartxh;
    }

    public void  setIStartxh(int iStartxh)
    {
        this.iStartxh = iStartxh;
    }

    public int getIWantnum()
    {
        return iWantnum;
    }

    public void  setIWantnum(int iWantnum)
    {
        this.iWantnum = iWantnum;
    }

    public boolean getBTg()
    {
        return bTg;
    }

    public void  setBTg(boolean bTg)
    {
        this.bTg = bTg;
    }

    public byte [] getVGuid()
    {
        return vGuid;
    }

    public void  setVGuid(byte [] vGuid)
    {
        this.vGuid = vGuid;
    }

    public KLineReq()
    {
    }

    public KLineReq(String sDtSecCode, int eKLineType, int iMulnum, int iStartxh, int iWantnum, boolean bTg, byte [] vGuid)
    {
        this.sDtSecCode = sDtSecCode;
        this.eKLineType = eKLineType;
        this.iMulnum = iMulnum;
        this.iStartxh = iStartxh;
        this.iWantnum = iWantnum;
        this.bTg = bTg;
        this.vGuid = vGuid;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        ostream.writeInt32(1, eKLineType);
        ostream.writeInt32(2, iMulnum);
        ostream.writeInt32(3, iStartxh);
        ostream.writeInt32(4, iWantnum);
        ostream.writeBoolean(5, bTg);
        if (null != vGuid) {
            ostream.writeBytes(6, vGuid);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.eKLineType = (int)istream.readInt32(1, false, this.eKLineType);
        this.iMulnum = (int)istream.readInt32(2, false, this.iMulnum);
        this.iStartxh = (int)istream.readInt32(3, false, this.iStartxh);
        this.iWantnum = (int)istream.readInt32(4, false, this.iWantnum);
        this.bTg = (boolean)istream.readBoolean(5, false, this.bTg);
        this.vGuid = (byte [])istream.readBytes(6, false, this.vGuid);
    }

}

