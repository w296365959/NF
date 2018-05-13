package BEC;

public final class FineVideoGroup extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sGroupKey = "";

    public int iOrder = 0;

    public int eChannel = 0;

    public String sGroupId = "";

    public int iCreateTime = 0;

    public String sTitle = "";

    public String sDesc = "";

    public java.util.ArrayList<BEC.Professor> vtProfessor = null;

    public String sImgUrl = "";

    public String sVideoKey = "";

    public java.util.ArrayList<BEC.VideoDetail> vtVideoDetail = null;

    public String sBuyDate = "";

    public String sExpiryDate = "";

    public float fPrice = 0;

    public int iBuyDays = 0;

    public String sSourceData = "";

    public int iFaverateStatus = 0;

    public int iFaverateTime = 0;

    public int iExpiryDays = 0;

    public BEC.VideoChannelDesc stChannel = null;

    public String getSGroupKey()
    {
        return sGroupKey;
    }

    public void  setSGroupKey(String sGroupKey)
    {
        this.sGroupKey = sGroupKey;
    }

    public int getIOrder()
    {
        return iOrder;
    }

    public void  setIOrder(int iOrder)
    {
        this.iOrder = iOrder;
    }

    public int getEChannel()
    {
        return eChannel;
    }

    public void  setEChannel(int eChannel)
    {
        this.eChannel = eChannel;
    }

    public String getSGroupId()
    {
        return sGroupId;
    }

    public void  setSGroupId(String sGroupId)
    {
        this.sGroupId = sGroupId;
    }

    public int getICreateTime()
    {
        return iCreateTime;
    }

    public void  setICreateTime(int iCreateTime)
    {
        this.iCreateTime = iCreateTime;
    }

    public String getSTitle()
    {
        return sTitle;
    }

    public void  setSTitle(String sTitle)
    {
        this.sTitle = sTitle;
    }

    public String getSDesc()
    {
        return sDesc;
    }

    public void  setSDesc(String sDesc)
    {
        this.sDesc = sDesc;
    }

    public java.util.ArrayList<BEC.Professor> getVtProfessor()
    {
        return vtProfessor;
    }

    public void  setVtProfessor(java.util.ArrayList<BEC.Professor> vtProfessor)
    {
        this.vtProfessor = vtProfessor;
    }

    public String getSImgUrl()
    {
        return sImgUrl;
    }

    public void  setSImgUrl(String sImgUrl)
    {
        this.sImgUrl = sImgUrl;
    }

    public String getSVideoKey()
    {
        return sVideoKey;
    }

    public void  setSVideoKey(String sVideoKey)
    {
        this.sVideoKey = sVideoKey;
    }

    public java.util.ArrayList<BEC.VideoDetail> getVtVideoDetail()
    {
        return vtVideoDetail;
    }

    public void  setVtVideoDetail(java.util.ArrayList<BEC.VideoDetail> vtVideoDetail)
    {
        this.vtVideoDetail = vtVideoDetail;
    }

    public String getSBuyDate()
    {
        return sBuyDate;
    }

    public void  setSBuyDate(String sBuyDate)
    {
        this.sBuyDate = sBuyDate;
    }

    public String getSExpiryDate()
    {
        return sExpiryDate;
    }

    public void  setSExpiryDate(String sExpiryDate)
    {
        this.sExpiryDate = sExpiryDate;
    }

    public float getFPrice()
    {
        return fPrice;
    }

    public void  setFPrice(float fPrice)
    {
        this.fPrice = fPrice;
    }

    public int getIBuyDays()
    {
        return iBuyDays;
    }

    public void  setIBuyDays(int iBuyDays)
    {
        this.iBuyDays = iBuyDays;
    }

    public String getSSourceData()
    {
        return sSourceData;
    }

    public void  setSSourceData(String sSourceData)
    {
        this.sSourceData = sSourceData;
    }

    public int getIFaverateStatus()
    {
        return iFaverateStatus;
    }

    public void  setIFaverateStatus(int iFaverateStatus)
    {
        this.iFaverateStatus = iFaverateStatus;
    }

    public int getIFaverateTime()
    {
        return iFaverateTime;
    }

    public void  setIFaverateTime(int iFaverateTime)
    {
        this.iFaverateTime = iFaverateTime;
    }

    public int getIExpiryDays()
    {
        return iExpiryDays;
    }

    public void  setIExpiryDays(int iExpiryDays)
    {
        this.iExpiryDays = iExpiryDays;
    }

    public BEC.VideoChannelDesc getStChannel()
    {
        return stChannel;
    }

    public void  setStChannel(BEC.VideoChannelDesc stChannel)
    {
        this.stChannel = stChannel;
    }

    public FineVideoGroup()
    {
    }

    public FineVideoGroup(String sGroupKey, int iOrder, int eChannel, String sGroupId, int iCreateTime, String sTitle, String sDesc, java.util.ArrayList<BEC.Professor> vtProfessor, String sImgUrl, String sVideoKey, java.util.ArrayList<BEC.VideoDetail> vtVideoDetail, String sBuyDate, String sExpiryDate, float fPrice, int iBuyDays, String sSourceData, int iFaverateStatus, int iFaverateTime, int iExpiryDays, BEC.VideoChannelDesc stChannel)
    {
        this.sGroupKey = sGroupKey;
        this.iOrder = iOrder;
        this.eChannel = eChannel;
        this.sGroupId = sGroupId;
        this.iCreateTime = iCreateTime;
        this.sTitle = sTitle;
        this.sDesc = sDesc;
        this.vtProfessor = vtProfessor;
        this.sImgUrl = sImgUrl;
        this.sVideoKey = sVideoKey;
        this.vtVideoDetail = vtVideoDetail;
        this.sBuyDate = sBuyDate;
        this.sExpiryDate = sExpiryDate;
        this.fPrice = fPrice;
        this.iBuyDays = iBuyDays;
        this.sSourceData = sSourceData;
        this.iFaverateStatus = iFaverateStatus;
        this.iFaverateTime = iFaverateTime;
        this.iExpiryDays = iExpiryDays;
        this.stChannel = stChannel;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sGroupKey) {
            ostream.writeString(0, sGroupKey);
        }
        ostream.writeInt32(1, iOrder);
        ostream.writeInt32(2, eChannel);
        if (null != sGroupId) {
            ostream.writeString(3, sGroupId);
        }
        ostream.writeInt32(4, iCreateTime);
        if (null != sTitle) {
            ostream.writeString(5, sTitle);
        }
        if (null != sDesc) {
            ostream.writeString(6, sDesc);
        }
        if (null != vtProfessor) {
            ostream.writeList(7, vtProfessor);
        }
        if (null != sImgUrl) {
            ostream.writeString(8, sImgUrl);
        }
        if (null != sVideoKey) {
            ostream.writeString(9, sVideoKey);
        }
        if (null != vtVideoDetail) {
            ostream.writeList(10, vtVideoDetail);
        }
        if (null != sBuyDate) {
            ostream.writeString(11, sBuyDate);
        }
        if (null != sExpiryDate) {
            ostream.writeString(12, sExpiryDate);
        }
        ostream.writeFloat(13, fPrice);
        ostream.writeInt32(14, iBuyDays);
        if (null != sSourceData) {
            ostream.writeString(15, sSourceData);
        }
        ostream.writeInt32(16, iFaverateStatus);
        ostream.writeInt32(17, iFaverateTime);
        ostream.writeInt32(18, iExpiryDays);
        if (null != stChannel) {
            ostream.writeMessage(19, stChannel);
        }
    }

    static java.util.ArrayList<BEC.Professor> VAR_TYPE_4_VTPROFESSOR = new java.util.ArrayList<BEC.Professor>();
    static {
        VAR_TYPE_4_VTPROFESSOR.add(new BEC.Professor());
    }

    static java.util.ArrayList<BEC.VideoDetail> VAR_TYPE_4_VTVIDEODETAIL = new java.util.ArrayList<BEC.VideoDetail>();
    static {
        VAR_TYPE_4_VTVIDEODETAIL.add(new BEC.VideoDetail());
    }

    static BEC.VideoChannelDesc VAR_TYPE_4_STCHANNEL = new BEC.VideoChannelDesc();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sGroupKey = (String)istream.readString(0, false, this.sGroupKey);
        this.iOrder = (int)istream.readInt32(1, false, this.iOrder);
        this.eChannel = (int)istream.readInt32(2, false, this.eChannel);
        this.sGroupId = (String)istream.readString(3, false, this.sGroupId);
        this.iCreateTime = (int)istream.readInt32(4, false, this.iCreateTime);
        this.sTitle = (String)istream.readString(5, false, this.sTitle);
        this.sDesc = (String)istream.readString(6, false, this.sDesc);
        this.vtProfessor = (java.util.ArrayList<BEC.Professor>)istream.readList(7, false, VAR_TYPE_4_VTPROFESSOR);
        this.sImgUrl = (String)istream.readString(8, false, this.sImgUrl);
        this.sVideoKey = (String)istream.readString(9, false, this.sVideoKey);
        this.vtVideoDetail = (java.util.ArrayList<BEC.VideoDetail>)istream.readList(10, false, VAR_TYPE_4_VTVIDEODETAIL);
        this.sBuyDate = (String)istream.readString(11, false, this.sBuyDate);
        this.sExpiryDate = (String)istream.readString(12, false, this.sExpiryDate);
        this.fPrice = (float)istream.readFloat(13, false, this.fPrice);
        this.iBuyDays = (int)istream.readInt32(14, false, this.iBuyDays);
        this.sSourceData = (String)istream.readString(15, false, this.sSourceData);
        this.iFaverateStatus = (int)istream.readInt32(16, false, this.iFaverateStatus);
        this.iFaverateTime = (int)istream.readInt32(17, false, this.iFaverateTime);
        this.iExpiryDays = (int)istream.readInt32(18, false, this.iExpiryDays);
        this.stChannel = (BEC.VideoChannelDesc)istream.readMessage(19, false, VAR_TYPE_4_STCHANNEL);
    }

}

