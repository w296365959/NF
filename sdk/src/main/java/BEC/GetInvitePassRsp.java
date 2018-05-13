package BEC;

public final class GetInvitePassRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sInvitePass = "";

    public boolean bIsDtInvitePass = true;

    public boolean bIsInvitedBefore = true;

    public String getSInvitePass()
    {
        return sInvitePass;
    }

    public void  setSInvitePass(String sInvitePass)
    {
        this.sInvitePass = sInvitePass;
    }

    public boolean getBIsDtInvitePass()
    {
        return bIsDtInvitePass;
    }

    public void  setBIsDtInvitePass(boolean bIsDtInvitePass)
    {
        this.bIsDtInvitePass = bIsDtInvitePass;
    }

    public boolean getBIsInvitedBefore()
    {
        return bIsInvitedBefore;
    }

    public void  setBIsInvitedBefore(boolean bIsInvitedBefore)
    {
        this.bIsInvitedBefore = bIsInvitedBefore;
    }

    public GetInvitePassRsp()
    {
    }

    public GetInvitePassRsp(String sInvitePass, boolean bIsDtInvitePass, boolean bIsInvitedBefore)
    {
        this.sInvitePass = sInvitePass;
        this.bIsDtInvitePass = bIsDtInvitePass;
        this.bIsInvitedBefore = bIsInvitedBefore;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sInvitePass) {
            ostream.writeString(0, sInvitePass);
        }
        ostream.writeBoolean(1, bIsDtInvitePass);
        ostream.writeBoolean(2, bIsInvitedBefore);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sInvitePass = (String)istream.readString(0, false, this.sInvitePass);
        this.bIsDtInvitePass = (boolean)istream.readBoolean(1, false, this.bIsDtInvitePass);
        this.bIsInvitedBefore = (boolean)istream.readBoolean(2, false, this.bIsInvitedBefore);
    }

}

