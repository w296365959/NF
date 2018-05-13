package BEC;

public final class VideoSchedule extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int eChannel = 0;

    public String sDate = "";

    public int eType = 0;

    public String sVideoKey = "";

    public java.util.ArrayList<BEC.VideoDetail> vtVideoDetail = null;

    public BEC.VideoChannelDesc stChannel = null;

    public int getEChannel()
    {
        return eChannel;
    }

    public void  setEChannel(int eChannel)
    {
        this.eChannel = eChannel;
    }

    public String getSDate()
    {
        return sDate;
    }

    public void  setSDate(String sDate)
    {
        this.sDate = sDate;
    }

    public int getEType()
    {
        return eType;
    }

    public void  setEType(int eType)
    {
        this.eType = eType;
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

    public BEC.VideoChannelDesc getStChannel()
    {
        return stChannel;
    }

    public void  setStChannel(BEC.VideoChannelDesc stChannel)
    {
        this.stChannel = stChannel;
    }

    public VideoSchedule()
    {
    }

    public VideoSchedule(int eChannel, String sDate, int eType, String sVideoKey, java.util.ArrayList<BEC.VideoDetail> vtVideoDetail, BEC.VideoChannelDesc stChannel)
    {
        this.eChannel = eChannel;
        this.sDate = sDate;
        this.eType = eType;
        this.sVideoKey = sVideoKey;
        this.vtVideoDetail = vtVideoDetail;
        this.stChannel = stChannel;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, eChannel);
        if (null != sDate) {
            ostream.writeString(1, sDate);
        }
        ostream.writeInt32(2, eType);
        if (null != sVideoKey) {
            ostream.writeString(3, sVideoKey);
        }
        if (null != vtVideoDetail) {
            ostream.writeList(4, vtVideoDetail);
        }
        if (null != stChannel) {
            ostream.writeMessage(5, stChannel);
        }
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

        this.eChannel = (int)istream.readInt32(0, false, this.eChannel);
        this.sDate = (String)istream.readString(1, false, this.sDate);
        this.eType = (int)istream.readInt32(2, false, this.eType);
        this.sVideoKey = (String)istream.readString(3, false, this.sVideoKey);
        this.vtVideoDetail = (java.util.ArrayList<BEC.VideoDetail>)istream.readList(4, false, VAR_TYPE_4_VTVIDEODETAIL);
        this.stChannel = (BEC.VideoChannelDesc)istream.readMessage(5, false, VAR_TYPE_4_STCHANNEL);
    }

}

