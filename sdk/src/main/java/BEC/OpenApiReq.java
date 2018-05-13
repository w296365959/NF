package BEC;

public final class OpenApiReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iAppID = 0;

    public String sPackageName = "";

    public String sSign = "";

    public String sGuid = "";

    public String sDua = "";

    public byte [] vBuffer = null;

    public int getIAppID()
    {
        return iAppID;
    }

    public void  setIAppID(int iAppID)
    {
        this.iAppID = iAppID;
    }

    public String getSPackageName()
    {
        return sPackageName;
    }

    public void  setSPackageName(String sPackageName)
    {
        this.sPackageName = sPackageName;
    }

    public String getSSign()
    {
        return sSign;
    }

    public void  setSSign(String sSign)
    {
        this.sSign = sSign;
    }

    public String getSGuid()
    {
        return sGuid;
    }

    public void  setSGuid(String sGuid)
    {
        this.sGuid = sGuid;
    }

    public String getSDua()
    {
        return sDua;
    }

    public void  setSDua(String sDua)
    {
        this.sDua = sDua;
    }

    public byte [] getVBuffer()
    {
        return vBuffer;
    }

    public void  setVBuffer(byte [] vBuffer)
    {
        this.vBuffer = vBuffer;
    }

    public OpenApiReq()
    {
    }

    public OpenApiReq(int iAppID, String sPackageName, String sSign, String sGuid, String sDua, byte [] vBuffer)
    {
        this.iAppID = iAppID;
        this.sPackageName = sPackageName;
        this.sSign = sSign;
        this.sGuid = sGuid;
        this.sDua = sDua;
        this.vBuffer = vBuffer;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iAppID);
        if (null != sPackageName) {
            ostream.writeString(1, sPackageName);
        }
        if (null != sSign) {
            ostream.writeString(2, sSign);
        }
        if (null != sGuid) {
            ostream.writeString(3, sGuid);
        }
        if (null != sDua) {
            ostream.writeString(4, sDua);
        }
        if (null != vBuffer) {
            ostream.writeBytes(5, vBuffer);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iAppID = (int)istream.readInt32(0, false, this.iAppID);
        this.sPackageName = (String)istream.readString(1, false, this.sPackageName);
        this.sSign = (String)istream.readString(2, false, this.sSign);
        this.sGuid = (String)istream.readString(3, false, this.sGuid);
        this.sDua = (String)istream.readString(4, false, this.sDua);
        this.vBuffer = (byte [])istream.readBytes(5, false, this.vBuffer);
    }

}

