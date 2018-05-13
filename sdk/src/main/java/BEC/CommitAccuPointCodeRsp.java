package BEC;

public final class CommitAccuPointCodeRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iGetPoints = 0;

    public int iGetPriviDays = 0;

    public int iRetCode = 0;

    public String sRetMsg = "";

    public int getIGetPoints()
    {
        return iGetPoints;
    }

    public void  setIGetPoints(int iGetPoints)
    {
        this.iGetPoints = iGetPoints;
    }

    public int getIGetPriviDays()
    {
        return iGetPriviDays;
    }

    public void  setIGetPriviDays(int iGetPriviDays)
    {
        this.iGetPriviDays = iGetPriviDays;
    }

    public int getIRetCode()
    {
        return iRetCode;
    }

    public void  setIRetCode(int iRetCode)
    {
        this.iRetCode = iRetCode;
    }

    public String getSRetMsg()
    {
        return sRetMsg;
    }

    public void  setSRetMsg(String sRetMsg)
    {
        this.sRetMsg = sRetMsg;
    }

    public CommitAccuPointCodeRsp()
    {
    }

    public CommitAccuPointCodeRsp(int iGetPoints, int iGetPriviDays, int iRetCode, String sRetMsg)
    {
        this.iGetPoints = iGetPoints;
        this.iGetPriviDays = iGetPriviDays;
        this.iRetCode = iRetCode;
        this.sRetMsg = sRetMsg;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iGetPoints);
        ostream.writeInt32(1, iGetPriviDays);
        ostream.writeInt32(2, iRetCode);
        if (null != sRetMsg) {
            ostream.writeString(3, sRetMsg);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iGetPoints = (int)istream.readInt32(0, false, this.iGetPoints);
        this.iGetPriviDays = (int)istream.readInt32(1, false, this.iGetPriviDays);
        this.iRetCode = (int)istream.readInt32(2, false, this.iRetCode);
        this.sRetMsg = (String)istream.readString(3, false, this.sRetMsg);
    }

}

