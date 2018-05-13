package BEC;

public final class GetUserPointInfoRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sInviteCode = "";

    public int iAccuPoints = 0;

    public int iGetPointsDaily = 0;

    public int iLeftTaskNum = 0;

    public java.util.ArrayList<AccuPointPriviInfo> vPrivi = null;

    public java.util.Map<Integer, Integer> mPriviPoints = null;

    public String getSInviteCode()
    {
        return sInviteCode;
    }

    public void  setSInviteCode(String sInviteCode)
    {
        this.sInviteCode = sInviteCode;
    }

    public int getIAccuPoints()
    {
        return iAccuPoints;
    }

    public void  setIAccuPoints(int iAccuPoints)
    {
        this.iAccuPoints = iAccuPoints;
    }

    public int getIGetPointsDaily()
    {
        return iGetPointsDaily;
    }

    public void  setIGetPointsDaily(int iGetPointsDaily)
    {
        this.iGetPointsDaily = iGetPointsDaily;
    }

    public int getILeftTaskNum()
    {
        return iLeftTaskNum;
    }

    public void  setILeftTaskNum(int iLeftTaskNum)
    {
        this.iLeftTaskNum = iLeftTaskNum;
    }

    public java.util.ArrayList<AccuPointPriviInfo> getVPrivi()
    {
        return vPrivi;
    }

    public void  setVPrivi(java.util.ArrayList<AccuPointPriviInfo> vPrivi)
    {
        this.vPrivi = vPrivi;
    }

    public java.util.Map<Integer, Integer> getMPriviPoints()
    {
        return mPriviPoints;
    }

    public void  setMPriviPoints(java.util.Map<Integer, Integer> mPriviPoints)
    {
        this.mPriviPoints = mPriviPoints;
    }

    public GetUserPointInfoRsp()
    {
    }

    public GetUserPointInfoRsp(String sInviteCode, int iAccuPoints, int iGetPointsDaily, int iLeftTaskNum, java.util.ArrayList<AccuPointPriviInfo> vPrivi, java.util.Map<Integer, Integer> mPriviPoints)
    {
        this.sInviteCode = sInviteCode;
        this.iAccuPoints = iAccuPoints;
        this.iGetPointsDaily = iGetPointsDaily;
        this.iLeftTaskNum = iLeftTaskNum;
        this.vPrivi = vPrivi;
        this.mPriviPoints = mPriviPoints;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sInviteCode) {
            ostream.writeString(0, sInviteCode);
        }
        ostream.writeInt32(1, iAccuPoints);
        ostream.writeInt32(2, iGetPointsDaily);
        ostream.writeInt32(3, iLeftTaskNum);
        if (null != vPrivi) {
            ostream.writeList(4, vPrivi);
        }
        if (null != mPriviPoints) {
            ostream.writeMap(5, mPriviPoints);
        }
    }

    static java.util.ArrayList<AccuPointPriviInfo> VAR_TYPE_4_VPRIVI = new java.util.ArrayList<AccuPointPriviInfo>();
    static {
        VAR_TYPE_4_VPRIVI.add(new AccuPointPriviInfo());
    }

    static java.util.Map<Integer, Integer> VAR_TYPE_4_MPRIVIPOINTS = new java.util.HashMap<Integer, Integer>();
    static {
        VAR_TYPE_4_MPRIVIPOINTS.put(0, 0);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sInviteCode = (String)istream.readString(0, false, this.sInviteCode);
        this.iAccuPoints = (int)istream.readInt32(1, false, this.iAccuPoints);
        this.iGetPointsDaily = (int)istream.readInt32(2, false, this.iGetPointsDaily);
        this.iLeftTaskNum = (int)istream.readInt32(3, false, this.iLeftTaskNum);
        this.vPrivi = (java.util.ArrayList<AccuPointPriviInfo>)istream.readList(4, false, VAR_TYPE_4_VPRIVI);
        this.mPriviPoints = (java.util.Map<Integer, Integer>)istream.readMap(5, false, VAR_TYPE_4_MPRIVIPOINTS);
    }

}

