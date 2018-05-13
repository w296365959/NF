package BEC;

public final class CommitInvitePassRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iNeedInvite = 0;

    public int iCodeType = 0;

    public String sPriviEndDay = "";

    public int getINeedInvite()
    {
        return iNeedInvite;
    }

    public void  setINeedInvite(int iNeedInvite)
    {
        this.iNeedInvite = iNeedInvite;
    }

    public int getICodeType()
    {
        return iCodeType;
    }

    public void  setICodeType(int iCodeType)
    {
        this.iCodeType = iCodeType;
    }

    public String getSPriviEndDay()
    {
        return sPriviEndDay;
    }

    public void  setSPriviEndDay(String sPriviEndDay)
    {
        this.sPriviEndDay = sPriviEndDay;
    }

    public CommitInvitePassRsp()
    {
    }

    public CommitInvitePassRsp(int iNeedInvite, int iCodeType, String sPriviEndDay)
    {
        this.iNeedInvite = iNeedInvite;
        this.iCodeType = iCodeType;
        this.sPriviEndDay = sPriviEndDay;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iNeedInvite);
        ostream.writeInt32(1, iCodeType);
        if (null != sPriviEndDay) {
            ostream.writeString(2, sPriviEndDay);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iNeedInvite = (int)istream.readInt32(0, false, this.iNeedInvite);
        this.iCodeType = (int)istream.readInt32(1, false, this.iCodeType);
        this.sPriviEndDay = (String)istream.readString(2, false, this.sPriviEndDay);
    }

}

