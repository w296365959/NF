package BEC;

public final class WxWalkRecord extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iID = 0;

    public int iTID = 0;

    public int iSourceID = 0;

    public String sTitle = "";

    public int iNumber = 0;

    public String sExpertName = "";

    public String sAbstracts = "";

    public int eType = 0;

    public String sUrl = "";

    public String sInformationTime = "";

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

    public int getINumber()
    {
        return iNumber;
    }

    public void  setINumber(int iNumber)
    {
        this.iNumber = iNumber;
    }

    public String getSExpertName()
    {
        return sExpertName;
    }

    public void  setSExpertName(String sExpertName)
    {
        this.sExpertName = sExpertName;
    }

    public String getSAbstracts()
    {
        return sAbstracts;
    }

    public void  setSAbstracts(String sAbstracts)
    {
        this.sAbstracts = sAbstracts;
    }

    public int getEType()
    {
        return eType;
    }

    public void  setEType(int eType)
    {
        this.eType = eType;
    }

    public String getSUrl()
    {
        return sUrl;
    }

    public void  setSUrl(String sUrl)
    {
        this.sUrl = sUrl;
    }

    public String getSInformationTime()
    {
        return sInformationTime;
    }

    public void  setSInformationTime(String sInformationTime)
    {
        this.sInformationTime = sInformationTime;
    }

    public WxWalkRecord()
    {
    }

    public WxWalkRecord(int iID, int iTID, int iSourceID, String sTitle, int iNumber, String sExpertName, String sAbstracts, int eType, String sUrl, String sInformationTime)
    {
        this.iID = iID;
        this.iTID = iTID;
        this.iSourceID = iSourceID;
        this.sTitle = sTitle;
        this.iNumber = iNumber;
        this.sExpertName = sExpertName;
        this.sAbstracts = sAbstracts;
        this.eType = eType;
        this.sUrl = sUrl;
        this.sInformationTime = sInformationTime;
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
        ostream.writeInt32(4, iNumber);
        if (null != sExpertName) {
            ostream.writeString(5, sExpertName);
        }
        if (null != sAbstracts) {
            ostream.writeString(6, sAbstracts);
        }
        ostream.writeInt32(7, eType);
        if (null != sUrl) {
            ostream.writeString(8, sUrl);
        }
        if (null != sInformationTime) {
            ostream.writeString(9, sInformationTime);
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
        this.iNumber = (int)istream.readInt32(4, false, this.iNumber);
        this.sExpertName = (String)istream.readString(5, false, this.sExpertName);
        this.sAbstracts = (String)istream.readString(6, false, this.sAbstracts);
        this.eType = (int)istream.readInt32(7, false, this.eType);
        this.sUrl = (String)istream.readString(8, false, this.sUrl);
        this.sInformationTime = (String)istream.readString(9, false, this.sInformationTime);
    }

}

