package BEC;

public final class UserInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public byte [] vGUID = null;

    public String sDUA = "";

    public String sIMEI = "";

    public String sDeviceTokens = "";

    public String sPackageName = "";

    public long iAccountId = 0;

    public java.util.Map<Integer, String> mpDeviceTokens = null;

    public String sTag = "";

    public int iMember = 0;

    public boolean bHWTokenStatus = true;

    public byte [] getVGUID()
    {
        return vGUID;
    }

    public void  setVGUID(byte [] vGUID)
    {
        this.vGUID = vGUID;
    }

    public String getSDUA()
    {
        return sDUA;
    }

    public void  setSDUA(String sDUA)
    {
        this.sDUA = sDUA;
    }

    public String getSIMEI()
    {
        return sIMEI;
    }

    public void  setSIMEI(String sIMEI)
    {
        this.sIMEI = sIMEI;
    }

    public String getSDeviceTokens()
    {
        return sDeviceTokens;
    }

    public void  setSDeviceTokens(String sDeviceTokens)
    {
        this.sDeviceTokens = sDeviceTokens;
    }

    public String getSPackageName()
    {
        return sPackageName;
    }

    public void  setSPackageName(String sPackageName)
    {
        this.sPackageName = sPackageName;
    }

    public long getIAccountId()
    {
        return iAccountId;
    }

    public void  setIAccountId(long iAccountId)
    {
        this.iAccountId = iAccountId;
    }

    public java.util.Map<Integer, String> getMpDeviceTokens()
    {
        return mpDeviceTokens;
    }

    public void  setMpDeviceTokens(java.util.Map<Integer, String> mpDeviceTokens)
    {
        this.mpDeviceTokens = mpDeviceTokens;
    }

    public String getSTag()
    {
        return sTag;
    }

    public void  setSTag(String sTag)
    {
        this.sTag = sTag;
    }

    public int getIMember()
    {
        return iMember;
    }

    public void  setIMember(int iMember)
    {
        this.iMember = iMember;
    }

    public boolean getBHWTokenStatus()
    {
        return bHWTokenStatus;
    }

    public void  setBHWTokenStatus(boolean bHWTokenStatus)
    {
        this.bHWTokenStatus = bHWTokenStatus;
    }

    public UserInfo()
    {
    }

    public UserInfo(byte [] vGUID, String sDUA, String sIMEI, String sDeviceTokens, String sPackageName, long iAccountId, java.util.Map<Integer, String> mpDeviceTokens, String sTag, int iMember, boolean bHWTokenStatus)
    {
        this.vGUID = vGUID;
        this.sDUA = sDUA;
        this.sIMEI = sIMEI;
        this.sDeviceTokens = sDeviceTokens;
        this.sPackageName = sPackageName;
        this.iAccountId = iAccountId;
        this.mpDeviceTokens = mpDeviceTokens;
        this.sTag = sTag;
        this.iMember = iMember;
        this.bHWTokenStatus = bHWTokenStatus;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeBytes(0, vGUID);
        ostream.writeString(1, sDUA);
        if (null != sIMEI) {
            ostream.writeString(2, sIMEI);
        }
        if (null != sDeviceTokens) {
            ostream.writeString(3, sDeviceTokens);
        }
        if (null != sPackageName) {
            ostream.writeString(4, sPackageName);
        }
        ostream.writeUInt32(5, iAccountId);
        if (null != mpDeviceTokens) {
            ostream.writeMap(6, mpDeviceTokens);
        }
        if (null != sTag) {
            ostream.writeString(7, sTag);
        }
        ostream.writeInt32(8, iMember);
        ostream.writeBoolean(9, bHWTokenStatus);
    }

    static java.util.Map<Integer, String> VAR_TYPE_4_MPDEVICETOKENS = new java.util.HashMap<Integer, String>();
    static {
        VAR_TYPE_4_MPDEVICETOKENS.put(0, "");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vGUID = (byte [])istream.readBytes(0, true, this.vGUID);
        this.sDUA = (String)istream.readString(1, true, this.sDUA);
        this.sIMEI = (String)istream.readString(2, false, this.sIMEI);
        this.sDeviceTokens = (String)istream.readString(3, false, this.sDeviceTokens);
        this.sPackageName = (String)istream.readString(4, false, this.sPackageName);
        this.iAccountId = (long)istream.readUInt32(5, false, this.iAccountId);
        this.mpDeviceTokens = (java.util.Map<Integer, String>)istream.readMap(6, false, VAR_TYPE_4_MPDEVICETOKENS);
        this.sTag = (String)istream.readString(7, false, this.sTag);
        this.iMember = (int)istream.readInt32(8, false, this.iMember);
        this.bHWTokenStatus = (boolean)istream.readBoolean(9, false, this.bHWTokenStatus);
    }

}

