package BEC;

public final class AccountValue extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public long iAccountId = 0;

    public String sUserName = "";

    public String sPasswordHash = "";

    public String sPasswordSalt = "";

    public String sPhone = "";

    public String sWxOpenId = "";

    public String sQqOpenId = "";

    public String sWbOpenId = "";

    public String sFaceUrl = "";

    public int iUpdateTime = 0;

    public long getIAccountId()
    {
        return iAccountId;
    }

    public void  setIAccountId(long iAccountId)
    {
        this.iAccountId = iAccountId;
    }

    public String getSUserName()
    {
        return sUserName;
    }

    public void  setSUserName(String sUserName)
    {
        this.sUserName = sUserName;
    }

    public String getSPasswordHash()
    {
        return sPasswordHash;
    }

    public void  setSPasswordHash(String sPasswordHash)
    {
        this.sPasswordHash = sPasswordHash;
    }

    public String getSPasswordSalt()
    {
        return sPasswordSalt;
    }

    public void  setSPasswordSalt(String sPasswordSalt)
    {
        this.sPasswordSalt = sPasswordSalt;
    }

    public String getSPhone()
    {
        return sPhone;
    }

    public void  setSPhone(String sPhone)
    {
        this.sPhone = sPhone;
    }

    public String getSWxOpenId()
    {
        return sWxOpenId;
    }

    public void  setSWxOpenId(String sWxOpenId)
    {
        this.sWxOpenId = sWxOpenId;
    }

    public String getSQqOpenId()
    {
        return sQqOpenId;
    }

    public void  setSQqOpenId(String sQqOpenId)
    {
        this.sQqOpenId = sQqOpenId;
    }

    public String getSWbOpenId()
    {
        return sWbOpenId;
    }

    public void  setSWbOpenId(String sWbOpenId)
    {
        this.sWbOpenId = sWbOpenId;
    }

    public String getSFaceUrl()
    {
        return sFaceUrl;
    }

    public void  setSFaceUrl(String sFaceUrl)
    {
        this.sFaceUrl = sFaceUrl;
    }

    public int getIUpdateTime()
    {
        return iUpdateTime;
    }

    public void  setIUpdateTime(int iUpdateTime)
    {
        this.iUpdateTime = iUpdateTime;
    }

    public AccountValue()
    {
    }

    public AccountValue(long iAccountId, String sUserName, String sPasswordHash, String sPasswordSalt, String sPhone, String sWxOpenId, String sQqOpenId, String sWbOpenId, String sFaceUrl, int iUpdateTime)
    {
        this.iAccountId = iAccountId;
        this.sUserName = sUserName;
        this.sPasswordHash = sPasswordHash;
        this.sPasswordSalt = sPasswordSalt;
        this.sPhone = sPhone;
        this.sWxOpenId = sWxOpenId;
        this.sQqOpenId = sQqOpenId;
        this.sWbOpenId = sWbOpenId;
        this.sFaceUrl = sFaceUrl;
        this.iUpdateTime = iUpdateTime;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeUInt32(0, iAccountId);
        if (null != sUserName) {
            ostream.writeString(1, sUserName);
        }
        if (null != sPasswordHash) {
            ostream.writeString(2, sPasswordHash);
        }
        if (null != sPasswordSalt) {
            ostream.writeString(3, sPasswordSalt);
        }
        if (null != sPhone) {
            ostream.writeString(4, sPhone);
        }
        if (null != sWxOpenId) {
            ostream.writeString(5, sWxOpenId);
        }
        if (null != sQqOpenId) {
            ostream.writeString(6, sQqOpenId);
        }
        if (null != sWbOpenId) {
            ostream.writeString(7, sWbOpenId);
        }
        if (null != sFaceUrl) {
            ostream.writeString(8, sFaceUrl);
        }
        ostream.writeInt32(9, iUpdateTime);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iAccountId = (long)istream.readUInt32(0, false, this.iAccountId);
        this.sUserName = (String)istream.readString(1, false, this.sUserName);
        this.sPasswordHash = (String)istream.readString(2, false, this.sPasswordHash);
        this.sPasswordSalt = (String)istream.readString(3, false, this.sPasswordSalt);
        this.sPhone = (String)istream.readString(4, false, this.sPhone);
        this.sWxOpenId = (String)istream.readString(5, false, this.sWxOpenId);
        this.sQqOpenId = (String)istream.readString(6, false, this.sQqOpenId);
        this.sWbOpenId = (String)istream.readString(7, false, this.sWbOpenId);
        this.sFaceUrl = (String)istream.readString(8, false, this.sFaceUrl);
        this.iUpdateTime = (int)istream.readInt32(9, false, this.iUpdateTime);
    }

}

