package BEC;

public final class VideoInfo extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iID = 0;

    public int iTID = 0;

    public int iSourceID = 0;

    public String sExpertName = "";

    public String sExpertTitle = "";

    public String sExpertHeadImage = "";

    public String sUrl = "";

    public String sAbstracts = "";

    public String sColumnTitle = "";

    public String sVideoAttribute = "";

    public String sWatch = "";

    public String sNumberPeriod = "";

    public String sImage = "";

    public String sInformationTime = "";

    public String sReadCount = "";

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

    public String getSExpertName()
    {
        return sExpertName;
    }

    public void  setSExpertName(String sExpertName)
    {
        this.sExpertName = sExpertName;
    }

    public String getSExpertTitle()
    {
        return sExpertTitle;
    }

    public void  setSExpertTitle(String sExpertTitle)
    {
        this.sExpertTitle = sExpertTitle;
    }

    public String getSExpertHeadImage()
    {
        return sExpertHeadImage;
    }

    public void  setSExpertHeadImage(String sExpertHeadImage)
    {
        this.sExpertHeadImage = sExpertHeadImage;
    }

    public String getSUrl()
    {
        return sUrl;
    }

    public void  setSUrl(String sUrl)
    {
        this.sUrl = sUrl;
    }

    public String getSAbstracts()
    {
        return sAbstracts;
    }

    public void  setSAbstracts(String sAbstracts)
    {
        this.sAbstracts = sAbstracts;
    }

    public String getSColumnTitle()
    {
        return sColumnTitle;
    }

    public void  setSColumnTitle(String sColumnTitle)
    {
        this.sColumnTitle = sColumnTitle;
    }

    public String getSVideoAttribute()
    {
        return sVideoAttribute;
    }

    public void  setSVideoAttribute(String sVideoAttribute)
    {
        this.sVideoAttribute = sVideoAttribute;
    }

    public String getSWatch()
    {
        return sWatch;
    }

    public void  setSWatch(String sWatch)
    {
        this.sWatch = sWatch;
    }

    public String getSNumberPeriod()
    {
        return sNumberPeriod;
    }

    public void  setSNumberPeriod(String sNumberPeriod)
    {
        this.sNumberPeriod = sNumberPeriod;
    }

    public String getSImage()
    {
        return sImage;
    }

    public void  setSImage(String sImage)
    {
        this.sImage = sImage;
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

    public VideoInfo()
    {
    }

    public VideoInfo(int iID, int iTID, int iSourceID, String sExpertName, String sExpertTitle, String sExpertHeadImage, String sUrl, String sAbstracts, String sColumnTitle, String sVideoAttribute, String sWatch, String sNumberPeriod, String sImage, String sInformationTime, String sReadCount)
    {
        this.iID = iID;
        this.iTID = iTID;
        this.iSourceID = iSourceID;
        this.sExpertName = sExpertName;
        this.sExpertTitle = sExpertTitle;
        this.sExpertHeadImage = sExpertHeadImage;
        this.sUrl = sUrl;
        this.sAbstracts = sAbstracts;
        this.sColumnTitle = sColumnTitle;
        this.sVideoAttribute = sVideoAttribute;
        this.sWatch = sWatch;
        this.sNumberPeriod = sNumberPeriod;
        this.sImage = sImage;
        this.sInformationTime = sInformationTime;
        this.sReadCount = sReadCount;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iID);
        ostream.writeInt32(1, iTID);
        ostream.writeInt32(2, iSourceID);
        if (null != sExpertName) {
            ostream.writeString(3, sExpertName);
        }
        if (null != sExpertTitle) {
            ostream.writeString(4, sExpertTitle);
        }
        if (null != sExpertHeadImage) {
            ostream.writeString(5, sExpertHeadImage);
        }
        if (null != sUrl) {
            ostream.writeString(6, sUrl);
        }
        if (null != sAbstracts) {
            ostream.writeString(7, sAbstracts);
        }
        if (null != sColumnTitle) {
            ostream.writeString(8, sColumnTitle);
        }
        if (null != sVideoAttribute) {
            ostream.writeString(9, sVideoAttribute);
        }
        if (null != sWatch) {
            ostream.writeString(10, sWatch);
        }
        if (null != sNumberPeriod) {
            ostream.writeString(11, sNumberPeriod);
        }
        if (null != sImage) {
            ostream.writeString(12, sImage);
        }
        if (null != sInformationTime) {
            ostream.writeString(13, sInformationTime);
        }
        if (null != sReadCount) {
            ostream.writeString(14, sReadCount);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iID = (int)istream.readInt32(0, false, this.iID);
        this.iTID = (int)istream.readInt32(1, false, this.iTID);
        this.iSourceID = (int)istream.readInt32(2, false, this.iSourceID);
        this.sExpertName = (String)istream.readString(3, false, this.sExpertName);
        this.sExpertTitle = (String)istream.readString(4, false, this.sExpertTitle);
        this.sExpertHeadImage = (String)istream.readString(5, false, this.sExpertHeadImage);
        this.sUrl = (String)istream.readString(6, false, this.sUrl);
        this.sAbstracts = (String)istream.readString(7, false, this.sAbstracts);
        this.sColumnTitle = (String)istream.readString(8, false, this.sColumnTitle);
        this.sVideoAttribute = (String)istream.readString(9, false, this.sVideoAttribute);
        this.sWatch = (String)istream.readString(10, false, this.sWatch);
        this.sNumberPeriod = (String)istream.readString(11, false, this.sNumberPeriod);
        this.sImage = (String)istream.readString(12, false, this.sImage);
        this.sInformationTime = (String)istream.readString(13, false, this.sInformationTime);
        this.sReadCount = (String)istream.readString(14, false, this.sReadCount);
    }

}

