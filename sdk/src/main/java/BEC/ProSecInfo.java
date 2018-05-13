package BEC;

public final class ProSecInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public float fHighPrice = -1;

    public float fLowPrice = -1;

    public float fIncreasePer = -1;

    public float fDecreasesPer = -1;

    public boolean bRecvAnnounce = false;

    public boolean bRecvResearch = false;

    public boolean isDel = false;

    public int iCreateTime = -1;

    public int iUpdateTime = -1;

    public String sName = "";

    public boolean isHold = false;

    public BEC.CommentInfo stCommentInfo = null;

    public boolean isAiAlert = true;

    public boolean isDKAlert = false;

    public java.util.ArrayList<Integer> vBroadcastTime = null;

    public float fChipHighPrice = -1;

    public float fChipLowPrice = -1;

    public float fMainChipHighPrice = -1;

    public float fMainChipLowPrice = -1;

    public java.util.ArrayList<Integer> vStrategyId = null;

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public float getFHighPrice()
    {
        return fHighPrice;
    }

    public void  setFHighPrice(float fHighPrice)
    {
        this.fHighPrice = fHighPrice;
    }

    public float getFLowPrice()
    {
        return fLowPrice;
    }

    public void  setFLowPrice(float fLowPrice)
    {
        this.fLowPrice = fLowPrice;
    }

    public float getFIncreasePer()
    {
        return fIncreasePer;
    }

    public void  setFIncreasePer(float fIncreasePer)
    {
        this.fIncreasePer = fIncreasePer;
    }

    public float getFDecreasesPer()
    {
        return fDecreasesPer;
    }

    public void  setFDecreasesPer(float fDecreasesPer)
    {
        this.fDecreasesPer = fDecreasesPer;
    }

    public boolean getBRecvAnnounce()
    {
        return bRecvAnnounce;
    }

    public void  setBRecvAnnounce(boolean bRecvAnnounce)
    {
        this.bRecvAnnounce = bRecvAnnounce;
    }

    public boolean getBRecvResearch()
    {
        return bRecvResearch;
    }

    public void  setBRecvResearch(boolean bRecvResearch)
    {
        this.bRecvResearch = bRecvResearch;
    }

    public boolean getIsDel()
    {
        return isDel;
    }

    public void  setIsDel(boolean isDel)
    {
        this.isDel = isDel;
    }

    public int getICreateTime()
    {
        return iCreateTime;
    }

    public void  setICreateTime(int iCreateTime)
    {
        this.iCreateTime = iCreateTime;
    }

    public int getIUpdateTime()
    {
        return iUpdateTime;
    }

    public void  setIUpdateTime(int iUpdateTime)
    {
        this.iUpdateTime = iUpdateTime;
    }

    public String getSName()
    {
        return sName;
    }

    public void  setSName(String sName)
    {
        this.sName = sName;
    }

    public boolean getIsHold()
    {
        return isHold;
    }

    public void  setIsHold(boolean isHold)
    {
        this.isHold = isHold;
    }

    public BEC.CommentInfo getStCommentInfo()
    {
        return stCommentInfo;
    }

    public void  setStCommentInfo(BEC.CommentInfo stCommentInfo)
    {
        this.stCommentInfo = stCommentInfo;
    }

    public boolean getIsAiAlert()
    {
        return isAiAlert;
    }

    public void  setIsAiAlert(boolean isAiAlert)
    {
        this.isAiAlert = isAiAlert;
    }

    public boolean getIsDKAlert()
    {
        return isDKAlert;
    }

    public void  setIsDKAlert(boolean isDKAlert)
    {
        this.isDKAlert = isDKAlert;
    }

    public java.util.ArrayList<Integer> getVBroadcastTime()
    {
        return vBroadcastTime;
    }

    public void  setVBroadcastTime(java.util.ArrayList<Integer> vBroadcastTime)
    {
        this.vBroadcastTime = vBroadcastTime;
    }

    public float getFChipHighPrice()
    {
        return fChipHighPrice;
    }

    public void  setFChipHighPrice(float fChipHighPrice)
    {
        this.fChipHighPrice = fChipHighPrice;
    }

    public float getFChipLowPrice()
    {
        return fChipLowPrice;
    }

    public void  setFChipLowPrice(float fChipLowPrice)
    {
        this.fChipLowPrice = fChipLowPrice;
    }

    public float getFMainChipHighPrice()
    {
        return fMainChipHighPrice;
    }

    public void  setFMainChipHighPrice(float fMainChipHighPrice)
    {
        this.fMainChipHighPrice = fMainChipHighPrice;
    }

    public float getFMainChipLowPrice()
    {
        return fMainChipLowPrice;
    }

    public void  setFMainChipLowPrice(float fMainChipLowPrice)
    {
        this.fMainChipLowPrice = fMainChipLowPrice;
    }

    public java.util.ArrayList<Integer> getVStrategyId()
    {
        return vStrategyId;
    }

    public void  setVStrategyId(java.util.ArrayList<Integer> vStrategyId)
    {
        this.vStrategyId = vStrategyId;
    }

    public ProSecInfo()
    {
    }

    public ProSecInfo(String sDtSecCode, float fHighPrice, float fLowPrice, float fIncreasePer, float fDecreasesPer, boolean bRecvAnnounce, boolean bRecvResearch, boolean isDel, int iCreateTime, int iUpdateTime, String sName, boolean isHold, BEC.CommentInfo stCommentInfo, boolean isAiAlert, boolean isDKAlert, java.util.ArrayList<Integer> vBroadcastTime, float fChipHighPrice, float fChipLowPrice, float fMainChipHighPrice, float fMainChipLowPrice, java.util.ArrayList<Integer> vStrategyId)
    {
        this.sDtSecCode = sDtSecCode;
        this.fHighPrice = fHighPrice;
        this.fLowPrice = fLowPrice;
        this.fIncreasePer = fIncreasePer;
        this.fDecreasesPer = fDecreasesPer;
        this.bRecvAnnounce = bRecvAnnounce;
        this.bRecvResearch = bRecvResearch;
        this.isDel = isDel;
        this.iCreateTime = iCreateTime;
        this.iUpdateTime = iUpdateTime;
        this.sName = sName;
        this.isHold = isHold;
        this.stCommentInfo = stCommentInfo;
        this.isAiAlert = isAiAlert;
        this.isDKAlert = isDKAlert;
        this.vBroadcastTime = vBroadcastTime;
        this.fChipHighPrice = fChipHighPrice;
        this.fChipLowPrice = fChipLowPrice;
        this.fMainChipHighPrice = fMainChipHighPrice;
        this.fMainChipLowPrice = fMainChipLowPrice;
        this.vStrategyId = vStrategyId;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        ostream.writeFloat(1, fHighPrice);
        ostream.writeFloat(2, fLowPrice);
        ostream.writeFloat(3, fIncreasePer);
        ostream.writeFloat(4, fDecreasesPer);
        ostream.writeBoolean(5, bRecvAnnounce);
        ostream.writeBoolean(6, bRecvResearch);
        ostream.writeBoolean(7, isDel);
        ostream.writeInt32(8, iCreateTime);
        ostream.writeInt32(9, iUpdateTime);
        if (null != sName) {
            ostream.writeString(10, sName);
        }
        ostream.writeBoolean(11, isHold);
        if (null != stCommentInfo) {
            ostream.writeMessage(12, stCommentInfo);
        }
        ostream.writeBoolean(13, isAiAlert);
        ostream.writeBoolean(14, isDKAlert);
        if (null != vBroadcastTime) {
            ostream.writeList(15, vBroadcastTime);
        }
        ostream.writeFloat(16, fChipHighPrice);
        ostream.writeFloat(17, fChipLowPrice);
        ostream.writeFloat(18, fMainChipHighPrice);
        ostream.writeFloat(19, fMainChipLowPrice);
        if (null != vStrategyId) {
            ostream.writeList(20, vStrategyId);
        }
    }

    static BEC.CommentInfo VAR_TYPE_4_STCOMMENTINFO = new BEC.CommentInfo();

    static java.util.ArrayList<Integer> VAR_TYPE_4_VBROADCASTTIME = new java.util.ArrayList<Integer>();
    static {
        VAR_TYPE_4_VBROADCASTTIME.add(0);
    }

    static java.util.ArrayList<Integer> VAR_TYPE_4_VSTRATEGYID = new java.util.ArrayList<Integer>();
    static {
        VAR_TYPE_4_VSTRATEGYID.add(0);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.fHighPrice = (float)istream.readFloat(1, false, this.fHighPrice);
        this.fLowPrice = (float)istream.readFloat(2, false, this.fLowPrice);
        this.fIncreasePer = (float)istream.readFloat(3, false, this.fIncreasePer);
        this.fDecreasesPer = (float)istream.readFloat(4, false, this.fDecreasesPer);
        this.bRecvAnnounce = (boolean)istream.readBoolean(5, false, this.bRecvAnnounce);
        this.bRecvResearch = (boolean)istream.readBoolean(6, false, this.bRecvResearch);
        this.isDel = (boolean)istream.readBoolean(7, false, this.isDel);
        this.iCreateTime = (int)istream.readInt32(8, false, this.iCreateTime);
        this.iUpdateTime = (int)istream.readInt32(9, false, this.iUpdateTime);
        this.sName = (String)istream.readString(10, false, this.sName);
        this.isHold = (boolean)istream.readBoolean(11, false, this.isHold);
        this.stCommentInfo = (BEC.CommentInfo)istream.readMessage(12, false, VAR_TYPE_4_STCOMMENTINFO);
        this.isAiAlert = (boolean)istream.readBoolean(13, false, this.isAiAlert);
        this.isDKAlert = (boolean)istream.readBoolean(14, false, this.isDKAlert);
        this.vBroadcastTime = (java.util.ArrayList<Integer>)istream.readList(15, false, VAR_TYPE_4_VBROADCASTTIME);
        this.fChipHighPrice = (float)istream.readFloat(16, false, this.fChipHighPrice);
        this.fChipLowPrice = (float)istream.readFloat(17, false, this.fChipLowPrice);
        this.fMainChipHighPrice = (float)istream.readFloat(18, false, this.fMainChipHighPrice);
        this.fMainChipLowPrice = (float)istream.readFloat(19, false, this.fMainChipLowPrice);
        this.vStrategyId = (java.util.ArrayList<Integer>)istream.readList(20, false, VAR_TYPE_4_VSTRATEGYID);
    }

}

