package BEC;

public final class CheckInvitePassRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public boolean bIsDtInvitePass = true;

    public boolean bIsInvitedBefore = true;

    public int iCodeType = 0;

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

    public int getICodeType()
    {
        return iCodeType;
    }

    public void  setICodeType(int iCodeType)
    {
        this.iCodeType = iCodeType;
    }

    public CheckInvitePassRsp()
    {
    }

    public CheckInvitePassRsp(boolean bIsDtInvitePass, boolean bIsInvitedBefore, int iCodeType)
    {
        this.bIsDtInvitePass = bIsDtInvitePass;
        this.bIsInvitedBefore = bIsInvitedBefore;
        this.iCodeType = iCodeType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeBoolean(1, bIsDtInvitePass);
        ostream.writeBoolean(2, bIsInvitedBefore);
        ostream.writeInt32(3, iCodeType);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.bIsDtInvitePass = (boolean)istream.readBoolean(1, false, this.bIsDtInvitePass);
        this.bIsInvitedBefore = (boolean)istream.readBoolean(2, false, this.bIsInvitedBefore);
        this.iCodeType = (int)istream.readInt32(3, false, this.iCodeType);
    }

}

