package BEC;

public final class ExpertInfo extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iID = 0;

    public int iTID = 0;

    public int iSourceID = 0;

    public String sExpertName = "";

    public String sExpertTitle = "";

    public String sExpertHeadImage = "";

    public String sExpertNumber = "";

    public String sExperIntroduction = "";

    public String sUpdateTime = "";

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

    public String getSExpertNumber()
    {
        return sExpertNumber;
    }

    public void  setSExpertNumber(String sExpertNumber)
    {
        this.sExpertNumber = sExpertNumber;
    }

    public String getSExperIntroduction()
    {
        return sExperIntroduction;
    }

    public void  setSExperIntroduction(String sExperIntroduction)
    {
        this.sExperIntroduction = sExperIntroduction;
    }

    public String getSUpdateTime()
    {
        return sUpdateTime;
    }

    public void  setSUpdateTime(String sUpdateTime)
    {
        this.sUpdateTime = sUpdateTime;
    }

    public ExpertInfo()
    {
    }

    public ExpertInfo(int iID, int iTID, int iSourceID, String sExpertName, String sExpertTitle, String sExpertHeadImage, String sExpertNumber, String sExperIntroduction, String sUpdateTime)
    {
        this.iID = iID;
        this.iTID = iTID;
        this.iSourceID = iSourceID;
        this.sExpertName = sExpertName;
        this.sExpertTitle = sExpertTitle;
        this.sExpertHeadImage = sExpertHeadImage;
        this.sExpertNumber = sExpertNumber;
        this.sExperIntroduction = sExperIntroduction;
        this.sUpdateTime = sUpdateTime;
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
        if (null != sExpertNumber) {
            ostream.writeString(6, sExpertNumber);
        }
        if (null != sExperIntroduction) {
            ostream.writeString(7, sExperIntroduction);
        }
        if (null != sUpdateTime) {
            ostream.writeString(8, sUpdateTime);
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
        this.sExpertNumber = (String)istream.readString(6, false, this.sExpertNumber);
        this.sExperIntroduction = (String)istream.readString(7, false, this.sExperIntroduction);
        this.sUpdateTime = (String)istream.readString(8, false, this.sUpdateTime);
    }

}

