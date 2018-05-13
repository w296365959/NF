package BEC;

public final class GenAccountReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sOpenId = "";

    public String sUnionId = "";

    public int eLoginType = 0;

    public byte [] vtFaceData = null;

    public BEC.AccountInfo stAccountInfo = null;

    public String getSOpenId()
    {
        return sOpenId;
    }

    public void  setSOpenId(String sOpenId)
    {
        this.sOpenId = sOpenId;
    }

    public String getSUnionId()
    {
        return sUnionId;
    }

    public void  setSUnionId(String sUnionId)
    {
        this.sUnionId = sUnionId;
    }

    public int getELoginType()
    {
        return eLoginType;
    }

    public void  setELoginType(int eLoginType)
    {
        this.eLoginType = eLoginType;
    }

    public byte [] getVtFaceData()
    {
        return vtFaceData;
    }

    public void  setVtFaceData(byte [] vtFaceData)
    {
        this.vtFaceData = vtFaceData;
    }

    public BEC.AccountInfo getStAccountInfo()
    {
        return stAccountInfo;
    }

    public void  setStAccountInfo(BEC.AccountInfo stAccountInfo)
    {
        this.stAccountInfo = stAccountInfo;
    }

    public GenAccountReq()
    {
    }

    public GenAccountReq(String sOpenId, String sUnionId, int eLoginType, byte [] vtFaceData, BEC.AccountInfo stAccountInfo)
    {
        this.sOpenId = sOpenId;
        this.sUnionId = sUnionId;
        this.eLoginType = eLoginType;
        this.vtFaceData = vtFaceData;
        this.stAccountInfo = stAccountInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sOpenId) {
            ostream.writeString(0, sOpenId);
        }
        if (null != sUnionId) {
            ostream.writeString(1, sUnionId);
        }
        ostream.writeInt32(2, eLoginType);
        if (null != vtFaceData) {
            ostream.writeBytes(3, vtFaceData);
        }
        if (null != stAccountInfo) {
            ostream.writeMessage(4, stAccountInfo);
        }
    }

    static BEC.AccountInfo VAR_TYPE_4_STACCOUNTINFO = new BEC.AccountInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sOpenId = (String)istream.readString(0, false, this.sOpenId);
        this.sUnionId = (String)istream.readString(1, false, this.sUnionId);
        this.eLoginType = (int)istream.readInt32(2, false, this.eLoginType);
        this.vtFaceData = (byte [])istream.readBytes(3, false, this.vtFaceData);
        this.stAccountInfo = (BEC.AccountInfo)istream.readMessage(4, false, VAR_TYPE_4_STACCOUNTINFO);
    }

}

