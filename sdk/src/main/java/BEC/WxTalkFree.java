package BEC;

public final class WxTalkFree extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iID = 0;

    public int iTID = 0;

    public int iSourceID = 0;

    public String sQuestionName = "";

    public String sQuestionContent = "";

    public String sQuestionDate = "";

    public String sExpertName = "";

    public String sExpertAnswer = "";

    public String sQuestionHeadImage = "";

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

    public String getSQuestionName()
    {
        return sQuestionName;
    }

    public void  setSQuestionName(String sQuestionName)
    {
        this.sQuestionName = sQuestionName;
    }

    public String getSQuestionContent()
    {
        return sQuestionContent;
    }

    public void  setSQuestionContent(String sQuestionContent)
    {
        this.sQuestionContent = sQuestionContent;
    }

    public String getSQuestionDate()
    {
        return sQuestionDate;
    }

    public void  setSQuestionDate(String sQuestionDate)
    {
        this.sQuestionDate = sQuestionDate;
    }

    public String getSExpertName()
    {
        return sExpertName;
    }

    public void  setSExpertName(String sExpertName)
    {
        this.sExpertName = sExpertName;
    }

    public String getSExpertAnswer()
    {
        return sExpertAnswer;
    }

    public void  setSExpertAnswer(String sExpertAnswer)
    {
        this.sExpertAnswer = sExpertAnswer;
    }

    public String getSQuestionHeadImage()
    {
        return sQuestionHeadImage;
    }

    public void  setSQuestionHeadImage(String sQuestionHeadImage)
    {
        this.sQuestionHeadImage = sQuestionHeadImage;
    }

    public WxTalkFree()
    {
    }

    public WxTalkFree(int iID, int iTID, int iSourceID, String sQuestionName, String sQuestionContent, String sQuestionDate, String sExpertName, String sExpertAnswer, String sQuestionHeadImage)
    {
        this.iID = iID;
        this.iTID = iTID;
        this.iSourceID = iSourceID;
        this.sQuestionName = sQuestionName;
        this.sQuestionContent = sQuestionContent;
        this.sQuestionDate = sQuestionDate;
        this.sExpertName = sExpertName;
        this.sExpertAnswer = sExpertAnswer;
        this.sQuestionHeadImage = sQuestionHeadImage;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iID);
        ostream.writeInt32(1, iTID);
        ostream.writeInt32(2, iSourceID);
        if (null != sQuestionName) {
            ostream.writeString(3, sQuestionName);
        }
        if (null != sQuestionContent) {
            ostream.writeString(4, sQuestionContent);
        }
        if (null != sQuestionDate) {
            ostream.writeString(5, sQuestionDate);
        }
        if (null != sExpertName) {
            ostream.writeString(6, sExpertName);
        }
        if (null != sExpertAnswer) {
            ostream.writeString(7, sExpertAnswer);
        }
        if (null != sQuestionHeadImage) {
            ostream.writeString(8, sQuestionHeadImage);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iID = (int)istream.readInt32(0, false, this.iID);
        this.iTID = (int)istream.readInt32(1, false, this.iTID);
        this.iSourceID = (int)istream.readInt32(2, false, this.iSourceID);
        this.sQuestionName = (String)istream.readString(3, false, this.sQuestionName);
        this.sQuestionContent = (String)istream.readString(4, false, this.sQuestionContent);
        this.sQuestionDate = (String)istream.readString(5, false, this.sQuestionDate);
        this.sExpertName = (String)istream.readString(6, false, this.sExpertName);
        this.sExpertAnswer = (String)istream.readString(7, false, this.sExpertAnswer);
        this.sQuestionHeadImage = (String)istream.readString(8, false, this.sQuestionHeadImage);
    }

}

