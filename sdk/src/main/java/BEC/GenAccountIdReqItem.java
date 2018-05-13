package BEC;

public final class GenAccountIdReqItem extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sNickName = "";

    public byte [] vtFaceData = null;

    public String sProfile = "";

    public int eUserType = BEC.E_FEED_USER_TYPE.E_FEED_USER_NORMAL;

    public String getSNickName()
    {
        return sNickName;
    }

    public void  setSNickName(String sNickName)
    {
        this.sNickName = sNickName;
    }

    public byte [] getVtFaceData()
    {
        return vtFaceData;
    }

    public void  setVtFaceData(byte [] vtFaceData)
    {
        this.vtFaceData = vtFaceData;
    }

    public String getSProfile()
    {
        return sProfile;
    }

    public void  setSProfile(String sProfile)
    {
        this.sProfile = sProfile;
    }

    public int getEUserType()
    {
        return eUserType;
    }

    public void  setEUserType(int eUserType)
    {
        this.eUserType = eUserType;
    }

    public GenAccountIdReqItem()
    {
    }

    public GenAccountIdReqItem(String sNickName, byte [] vtFaceData, String sProfile, int eUserType)
    {
        this.sNickName = sNickName;
        this.vtFaceData = vtFaceData;
        this.sProfile = sProfile;
        this.eUserType = eUserType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sNickName) {
            ostream.writeString(0, sNickName);
        }
        if (null != vtFaceData) {
            ostream.writeBytes(1, vtFaceData);
        }
        if (null != sProfile) {
            ostream.writeString(2, sProfile);
        }
        ostream.writeInt32(3, eUserType);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sNickName = (String)istream.readString(0, false, this.sNickName);
        this.vtFaceData = (byte [])istream.readBytes(1, false, this.vtFaceData);
        this.sProfile = (String)istream.readString(2, false, this.sProfile);
        this.eUserType = (int)istream.readInt32(3, false, this.eUserType);
    }

}

