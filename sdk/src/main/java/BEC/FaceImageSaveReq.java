package BEC;

public final class FaceImageSaveReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUserInfo = null;

    public String sFileName = "";

    public long iAccountId = 0;

    public byte [] vtData = null;

    public UserInfo getStUserInfo()
    {
        return stUserInfo;
    }

    public void  setStUserInfo(UserInfo stUserInfo)
    {
        this.stUserInfo = stUserInfo;
    }

    public String getSFileName()
    {
        return sFileName;
    }

    public void  setSFileName(String sFileName)
    {
        this.sFileName = sFileName;
    }

    public long getIAccountId()
    {
        return iAccountId;
    }

    public void  setIAccountId(long iAccountId)
    {
        this.iAccountId = iAccountId;
    }

    public byte [] getVtData()
    {
        return vtData;
    }

    public void  setVtData(byte [] vtData)
    {
        this.vtData = vtData;
    }

    public FaceImageSaveReq()
    {
    }

    public FaceImageSaveReq(UserInfo stUserInfo, String sFileName, long iAccountId, byte [] vtData)
    {
        this.stUserInfo = stUserInfo;
        this.sFileName = sFileName;
        this.iAccountId = iAccountId;
        this.vtData = vtData;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUserInfo) {
            ostream.writeMessage(0, stUserInfo);
        }
        if (null != sFileName) {
            ostream.writeString(1, sFileName);
        }
        ostream.writeUInt32(2, iAccountId);
        if (null != vtData) {
            ostream.writeBytes(3, vtData);
        }
    }

    static UserInfo VAR_TYPE_4_STUSERINFO = new UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUserInfo = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSERINFO);
        this.sFileName = (String)istream.readString(1, false, this.sFileName);
        this.iAccountId = (long)istream.readUInt32(2, false, this.iAccountId);
        this.vtData = (byte [])istream.readBytes(3, false, this.vtData);
    }

}

