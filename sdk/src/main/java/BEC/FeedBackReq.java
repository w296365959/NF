package BEC;

public final class FeedBackReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iAccountId = 0;

    public String sGuid = "";

    public String sUA = "";

    public String sMsg = "";

    public String sContact = "";

    public int getIAccountId()
    {
        return iAccountId;
    }

    public void  setIAccountId(int iAccountId)
    {
        this.iAccountId = iAccountId;
    }

    public String getSGuid()
    {
        return sGuid;
    }

    public void  setSGuid(String sGuid)
    {
        this.sGuid = sGuid;
    }

    public String getSUA()
    {
        return sUA;
    }

    public void  setSUA(String sUA)
    {
        this.sUA = sUA;
    }

    public String getSMsg()
    {
        return sMsg;
    }

    public void  setSMsg(String sMsg)
    {
        this.sMsg = sMsg;
    }

    public String getSContact()
    {
        return sContact;
    }

    public void  setSContact(String sContact)
    {
        this.sContact = sContact;
    }

    public FeedBackReq()
    {
    }

    public FeedBackReq(int iAccountId, String sGuid, String sUA, String sMsg, String sContact)
    {
        this.iAccountId = iAccountId;
        this.sGuid = sGuid;
        this.sUA = sUA;
        this.sMsg = sMsg;
        this.sContact = sContact;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iAccountId);
        if (null != sGuid) {
            ostream.writeString(1, sGuid);
        }
        if (null != sUA) {
            ostream.writeString(2, sUA);
        }
        if (null != sMsg) {
            ostream.writeString(3, sMsg);
        }
        if (null != sContact) {
            ostream.writeString(4, sContact);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iAccountId = (int)istream.readInt32(0, false, this.iAccountId);
        this.sGuid = (String)istream.readString(1, false, this.sGuid);
        this.sUA = (String)istream.readString(2, false, this.sUA);
        this.sMsg = (String)istream.readString(3, false, this.sMsg);
        this.sContact = (String)istream.readString(4, false, this.sContact);
    }

}

