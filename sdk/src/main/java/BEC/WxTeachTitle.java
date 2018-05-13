package BEC;

public final class WxTeachTitle extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iID = 0;

    public int iTID = 0;

    public int iSourceID = 0;

    public String sTitle = "";

    public String sTitleImage = "";

    public String sInformationTime = "";

    public String sReadCount = "";

    public String sLaudCount = "";

    public int getIID()
    {
        return iID;
    }

    public void  setIID(int iID)
    {
        this.iID = iID;
    }

    public int getITID()
    {
        return iTID;
    }

    public void  setITID(int iTID)
    {
        this.iTID = iTID;
    }

    public int getISourceID()
    {
        return iSourceID;
    }

    public void  setISourceID(int iSourceID)
    {
        this.iSourceID = iSourceID;
    }

    public String getSTitle()
    {
        return sTitle;
    }

    public void  setSTitle(String sTitle)
    {
        this.sTitle = sTitle;
    }

    public String getSTitleImage()
    {
        return sTitleImage;
    }

    public void  setSTitleImage(String sTitleImage)
    {
        this.sTitleImage = sTitleImage;
    }

    public String getSInformationTime()
    {
        return sInformationTime;
    }

    public void  setSInformationTime(String sInformationTime)
    {
        this.sInformationTime = sInformationTime;
    }

    public String getSReadCount()
    {
        return sReadCount;
    }

    public void  setSReadCount(String sReadCount)
    {
        this.sReadCount = sReadCount;
    }

    public String getSLaudCount()
    {
        return sLaudCount;
    }

    public void  setSLaudCount(String sLaudCount)
    {
        this.sLaudCount = sLaudCount;
    }

    public WxTeachTitle()
    {
    }

    public WxTeachTitle(int iID, int iTID, int iSourceID, String sTitle, String sTitleImage, String sInformationTime, String sReadCount, String sLaudCount)
    {
        this.iID = iID;
        this.iTID = iTID;
        this.iSourceID = iSourceID;
        this.sTitle = sTitle;
        this.sTitleImage = sTitleImage;
        this.sInformationTime = sInformationTime;
        this.sReadCount = sReadCount;
        this.sLaudCount = sLaudCount;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iID);
        ostream.writeInt32(1, iTID);
        ostream.writeInt32(2, iSourceID);
        if (null != sTitle) {
            ostream.writeString(3, sTitle);
        }
        if (null != sTitleImage) {
            ostream.writeString(4, sTitleImage);
        }
        if (null != sInformationTime) {
            ostream.writeString(5, sInformationTime);
        }
        if (null != sReadCount) {
            ostream.writeString(6, sReadCount);
        }
        if (null != sLaudCount) {
            ostream.writeString(7, sLaudCount);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iID = (int)istream.readInt32(0, false, this.iID);
        this.iTID = (int)istream.readInt32(1, false, this.iTID);
        this.iSourceID = (int)istream.readInt32(2, false, this.iSourceID);
        this.sTitle = (String)istream.readString(3, false, this.sTitle);
        this.sTitleImage = (String)istream.readString(4, false, this.sTitleImage);
        this.sInformationTime = (String)istream.readString(5, false, this.sInformationTime);
        this.sReadCount = (String)istream.readString(6, false, this.sReadCount);
        this.sLaudCount = (String)istream.readString(7, false, this.sLaudCount);
    }

}

