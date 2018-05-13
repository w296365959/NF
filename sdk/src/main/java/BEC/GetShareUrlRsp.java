package BEC;

public final class GetShareUrlRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sUrl = "";

    public String sPass = "";

    public int iFullUnlockNeedInvite = 0;

    public int iPartUnlockNeedInvite = 0;

    public int iInviteAlready = 0;

    public String getSUrl()
    {
        return sUrl;
    }

    public void  setSUrl(String sUrl)
    {
        this.sUrl = sUrl;
    }

    public String getSPass()
    {
        return sPass;
    }

    public void  setSPass(String sPass)
    {
        this.sPass = sPass;
    }

    public int getIFullUnlockNeedInvite()
    {
        return iFullUnlockNeedInvite;
    }

    public void  setIFullUnlockNeedInvite(int iFullUnlockNeedInvite)
    {
        this.iFullUnlockNeedInvite = iFullUnlockNeedInvite;
    }

    public int getIPartUnlockNeedInvite()
    {
        return iPartUnlockNeedInvite;
    }

    public void  setIPartUnlockNeedInvite(int iPartUnlockNeedInvite)
    {
        this.iPartUnlockNeedInvite = iPartUnlockNeedInvite;
    }

    public int getIInviteAlready()
    {
        return iInviteAlready;
    }

    public void  setIInviteAlready(int iInviteAlready)
    {
        this.iInviteAlready = iInviteAlready;
    }

    public GetShareUrlRsp()
    {
    }

    public GetShareUrlRsp(String sUrl, String sPass, int iFullUnlockNeedInvite, int iPartUnlockNeedInvite, int iInviteAlready)
    {
        this.sUrl = sUrl;
        this.sPass = sPass;
        this.iFullUnlockNeedInvite = iFullUnlockNeedInvite;
        this.iPartUnlockNeedInvite = iPartUnlockNeedInvite;
        this.iInviteAlready = iInviteAlready;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sUrl) {
            ostream.writeString(0, sUrl);
        }
        if (null != sPass) {
            ostream.writeString(1, sPass);
        }
        ostream.writeInt32(2, iFullUnlockNeedInvite);
        ostream.writeInt32(3, iPartUnlockNeedInvite);
        ostream.writeInt32(4, iInviteAlready);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sUrl = (String)istream.readString(0, false, this.sUrl);
        this.sPass = (String)istream.readString(1, false, this.sPass);
        this.iFullUnlockNeedInvite = (int)istream.readInt32(2, false, this.iFullUnlockNeedInvite);
        this.iPartUnlockNeedInvite = (int)istream.readInt32(3, false, this.iPartUnlockNeedInvite);
        this.iInviteAlready = (int)istream.readInt32(4, false, this.iInviteAlready);
    }

}

