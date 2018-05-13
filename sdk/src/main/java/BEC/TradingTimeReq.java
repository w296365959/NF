package BEC;

public final class TradingTimeReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iLocalTime = 0;

    public byte [] vGuid = null;

    public int getILocalTime()
    {
        return iLocalTime;
    }

    public void  setILocalTime(int iLocalTime)
    {
        this.iLocalTime = iLocalTime;
    }

    public byte [] getVGuid()
    {
        return vGuid;
    }

    public void  setVGuid(byte [] vGuid)
    {
        this.vGuid = vGuid;
    }

    public TradingTimeReq()
    {
    }

    public TradingTimeReq(int iLocalTime, byte [] vGuid)
    {
        this.iLocalTime = iLocalTime;
        this.vGuid = vGuid;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iLocalTime);
        if (null != vGuid) {
            ostream.writeBytes(1, vGuid);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iLocalTime = (int)istream.readInt32(0, false, this.iLocalTime);
        this.vGuid = (byte [])istream.readBytes(1, false, this.vGuid);
    }

}

