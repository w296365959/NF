package BEC;

public final class GetAccuPointUserInviteRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sInviteCode = "";

    public int iInviteUserNum = 0;

    public int iInviteGetPoints = 0;

    public java.util.Map<Integer, Integer> mPrivi = null;

    public int iRetCode = 0;

    public long lCurrTimeStamp = 0;

    public String getSInviteCode()
    {
        return sInviteCode;
    }

    public void  setSInviteCode(String sInviteCode)
    {
        this.sInviteCode = sInviteCode;
    }

    public int getIInviteUserNum()
    {
        return iInviteUserNum;
    }

    public void  setIInviteUserNum(int iInviteUserNum)
    {
        this.iInviteUserNum = iInviteUserNum;
    }

    public int getIInviteGetPoints()
    {
        return iInviteGetPoints;
    }

    public void  setIInviteGetPoints(int iInviteGetPoints)
    {
        this.iInviteGetPoints = iInviteGetPoints;
    }

    public java.util.Map<Integer, Integer> getMPrivi()
    {
        return mPrivi;
    }

    public void  setMPrivi(java.util.Map<Integer, Integer> mPrivi)
    {
        this.mPrivi = mPrivi;
    }

    public int getIRetCode()
    {
        return iRetCode;
    }

    public void  setIRetCode(int iRetCode)
    {
        this.iRetCode = iRetCode;
    }

    public long getLCurrTimeStamp()
    {
        return lCurrTimeStamp;
    }

    public void  setLCurrTimeStamp(long lCurrTimeStamp)
    {
        this.lCurrTimeStamp = lCurrTimeStamp;
    }

    public GetAccuPointUserInviteRsp()
    {
    }

    public GetAccuPointUserInviteRsp(String sInviteCode, int iInviteUserNum, int iInviteGetPoints, java.util.Map<Integer, Integer> mPrivi, int iRetCode, long lCurrTimeStamp)
    {
        this.sInviteCode = sInviteCode;
        this.iInviteUserNum = iInviteUserNum;
        this.iInviteGetPoints = iInviteGetPoints;
        this.mPrivi = mPrivi;
        this.iRetCode = iRetCode;
        this.lCurrTimeStamp = lCurrTimeStamp;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sInviteCode) {
            ostream.writeString(0, sInviteCode);
        }
        ostream.writeInt32(1, iInviteUserNum);
        ostream.writeInt32(2, iInviteGetPoints);
        if (null != mPrivi) {
            ostream.writeMap(3, mPrivi);
        }
        ostream.writeInt32(4, iRetCode);
        ostream.writeInt64(5, lCurrTimeStamp);
    }

    static java.util.Map<Integer, Integer> VAR_TYPE_4_MPRIVI = new java.util.HashMap<Integer, Integer>();
    static {
        VAR_TYPE_4_MPRIVI.put(0, 0);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sInviteCode = (String)istream.readString(0, false, this.sInviteCode);
        this.iInviteUserNum = (int)istream.readInt32(1, false, this.iInviteUserNum);
        this.iInviteGetPoints = (int)istream.readInt32(2, false, this.iInviteGetPoints);
        this.mPrivi = (java.util.Map<Integer, Integer>)istream.readMap(3, false, VAR_TYPE_4_MPRIVI);
        this.iRetCode = (int)istream.readInt32(4, false, this.iRetCode);
        this.lCurrTimeStamp = (long)istream.readInt64(5, false, this.lCurrTimeStamp);
    }

}

